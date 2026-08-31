package com.universalmodloader.api.forge;

import com.universalmodloader.api.IModAPIAdapter;

/**
 * Adaptador minimalista do Forge/NeoForge.
 *
 * Em um projeto real, este adaptador conectaria eventos do Forge e do NeoForge, como:
 *   - PlayerJoinEvent
 *   - LivingAttackEvent
 *   - ServerStartingEvent
 *
 * A lógica aqui demonstra como transformar um evento do mundo Forge em um evento do runtime
 * universal, sem que os mods de origem precisem depender diretamente de uma API específica.
 */
public class ForgeAPIAdapter implements IModAPIAdapter {

    @Override
    public String getName() {
        return "Forge/NeoForge";
    }

    @Override
    public void initialize() {
        // Inicialização do ambiente de carregamento do Forge/NeoForge.
    }

    @Override
    public void onEvent(String eventName, Object payload) {
        // Exemplo de tradução:
        //  Forge: PlayerJoinEvent(player)
        //  Universal: PlayerJoinedEvent(player)
        if ("PlayerJoinEvent".equals(eventName)) {
            System.out.println("[UniversalModLoader][Forge] PlayerJoinEvent traduzido para PlayerJoinedEvent");
        }
    }
}
