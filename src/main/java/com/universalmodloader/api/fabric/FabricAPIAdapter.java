package com.universalmodloader.api.fabric;

import com.universalmodloader.api.IModAPIAdapter;

/**
 * Adaptador minimalista do Fabric.
 *
 * O Fabric utiliza um pipeline de carregamento diferente do Forge. O objetivo deste esqueleto é
 * demonstrar como a camada de interface comum pode ser implementada para expor eventos e hooks de
 * way mais simples para o runtime do loader universal.
 */
public class FabricAPIAdapter implements IModAPIAdapter {

    @Override
    public String getName() {
        return "Fabric";
    }

    @Override
    public void initialize() {
        // Inicialização da API de Fabric e do runtime de mixins.
    }

    @Override
    public void onEvent(String eventName, Object payload) {
        // Tradução de eventos Fabric para o modelo do loader universal.
        // Exemplo: um evento Fabric de "ClientLifecycleEvents.CLIENT_STARTED" pode ser mapeado
        // para um evento interno do loader, como "GameStarted".
        if ("ClientLifecycleEvents.CLIENT_STARTED".equals(eventName)) {
            System.out.println("[UniversalModLoader][Fabric] Evento traduzido para GameStarted");
        }
    }
}
