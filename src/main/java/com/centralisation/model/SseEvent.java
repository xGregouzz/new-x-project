package com.centralisation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SseEvent {
    private final String username;
    private final Object data;
    private final String type;
    private final long id = System.currentTimeMillis();
}
