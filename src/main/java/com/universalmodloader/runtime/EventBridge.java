package com.universalmodloader.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ponte de eventos do runtime híbrido.
 *
 * Centraliza a retransmissão de eventos entre o núcleo do loader e os adapters de Fabric/NeoForge.
 * Em um sistema de runtime real, esta classe seria o hub que recebe ticks, eventos de player,
 * registro de blocos e outros gatilhos do jogo e retransmite para todos os módulos ativos.
 */
public class EventBridge {

    private final Map<String, List<EventHandler>> handlers = new ConcurrentHashMap<>();

    public void register(String eventId, EventHandler handler) {
        if (eventId == null || handler == null) {
            throw new IllegalArgumentException("Evento/handler inválido");
        }
        handlers.computeIfAbsent(eventId, ignored -> new ArrayList<>()).add(handler);
    }

    public void publish(String eventId, Object payload) {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("ID do evento inválido");
        }

        List<EventHandler> list = handlers.getOrDefault(eventId, List.of());
        for (EventHandler handler : list) {
            handler.handle(eventId, payload);
        }
    }

    @FunctionalInterface
    public interface EventHandler {
        void handle(String eventId, Object payload);
    }
}
