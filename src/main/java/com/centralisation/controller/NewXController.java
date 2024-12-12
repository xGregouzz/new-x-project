package com.centralisation.controller;

import com.centralisation.model.SseEvent;
import com.centralisation.model.dto.MessageDTO;
import com.centralisation.model.dto.UserDTO;
import com.centralisation.service.NewXService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NewXController {

    private final NewXService newXService;
    private final Sinks.Many<SseEvent> sink = Sinks.many().multicast().onBackpressureBuffer();

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(newXService.signup(userDTO));
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody MessageDTO messageDTO) {
        newXService.sendMessage(messageDTO, sink);
    }

    @GetMapping("/getMessages")
    public ResponseEntity<List<MessageDTO>> getMessages() {
        return ResponseEntity.ok(newXService.getMessages());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> streamData(@RequestHeader(value = "USERNAME") String username) {
        return sink.asFlux()
                .doOnSubscribe(subscription -> log.info("User connecté : {}", username))
                .doOnCancel(() -> log.info("User déconnecté : {}", username))
                .switchOnFirst((signal, flux) -> {
                    if (signal.hasValue()) {
                        return Flux.concat(
                                Flux.just(Objects.requireNonNull(signal.get())),
                                flux.skip(1).delayElements(Duration.ofSeconds(30))
                        );
                    }
                    return flux;
                })
                .map(event -> ServerSentEvent.builder()
                        .id(String.valueOf(event.getId()))
                        .data(event.getData())
                        .event(event.getType())
                        .build());
    }
}
