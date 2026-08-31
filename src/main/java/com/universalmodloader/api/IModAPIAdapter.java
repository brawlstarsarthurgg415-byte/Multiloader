package com.universalmodloader.api;

/**
 * Contrato comum para o sistema híbrido.
 *
 * Ele representa a camada de abstração por onde cada plataforma de mod (Fabric, Forge/NeoForge)
 * será adaptada para um modelo de runtime único. Em um projeto real, seria expandido para incluir
 * carregamento de classes, eventos, serviços, registro de itens, etc.
 */
public interface IModAPIAdapter {

    /**
     * Nome do adaptador, útil para depuração e logging.
     */
    String getName();

    /**
     * Inicializa a integração da plataforma.
     */
    void initialize();

    /**
     * Descreve a estratégia de tradução de um evento da plataforma interna para o runtime universal.
     */
    default void onEvent(String eventName, Object payload) {
        // Implementação padrão vazia, permitindo que plataformas específicas insiram lógica.
    }
}
