package com.universalmodloader.api;

/**
 * Adaptador genérico de eventos usado como esqueleto para a camada híbrida.
 *
 * O papel desta classe é explicitar o conceito de ponte entre dois modelos de eventos:
 *  - o modelo original do mod (Fabric ou Forge)
 *  - o modelo interno do UniversalModLoader
 *
 * A tradução é importante porque, apesar de ambos os loaders trocarem eventos em momentos
 * semelhantes da execução, os nomes e contratos dos eventos não são equivalentes.
 */
public interface EventAdapter {

    /**
     * Traduz um evento do Forge para um evento interno do loader.
     *
     * Exemplo:
     *  Forge: PlayerJoinEvent(player)
     *  Universal: PlayerJoinedEvent(player)
     */
    default Object translateForgeEvent(String eventName, Object eventPayload) {
        return eventPayload;
    }

    /**
     * Traduz um evento do Fabric para um evento interno do loader.
     *
     * Exemplo:
     *  Fabric: ClientLifecycleEvents.CLIENT_STARTED
     *  Universal: GameStarted
     */
    default Object translateFabricEvent(String eventName, Object eventPayload) {
        return eventPayload;
    }
}
