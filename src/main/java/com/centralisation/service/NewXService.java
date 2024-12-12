package com.centralisation.service;

import com.centralisation.model.SseEvent;
import com.centralisation.model.dto.MessageDTO;
import com.centralisation.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewXService {
    private List<UserDTO> cachedUsers = new ArrayList<>();
    private List<MessageDTO> cachedMessages = new ArrayList<>();

    public void sendSseEvent(String username, Object data, String eventType, Sinks.Many<SseEvent> sink) {
        SseEvent event = new SseEvent(username, data, eventType);
        sink.tryEmitNext(event);
    }

    public UserDTO signup(UserDTO userDTO) {
        if (!cachedUsers.contains(userDTO)) {
            cachedUsers.add(userDTO);
        }
        return userDTO;
    }

    public void sendMessage(MessageDTO messageDTO, Sinks.Many<SseEvent> sink) {
        if (!cachedUsers.contains(cachedUsers.stream().filter(user -> user.getUsername().equals(messageDTO.getUsername())).findFirst().orElse(null))) {
            throw new RuntimeException("User not found");
        }
        cachedMessages.add(messageDTO);
        sendSseEvent(messageDTO.getUsername(), messageDTO, "message", sink);
    }

    public List<MessageDTO> getMessages() {
        return cachedMessages;
    }
}
