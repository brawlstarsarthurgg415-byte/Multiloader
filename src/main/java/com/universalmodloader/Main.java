package com.universalmodloader;

import com.universalmodloader.api.fabric.FabricAPIAdapter;
import com.universalmodloader.api.forge.ForgeAPIAdapter;
import com.universalmodloader.core.JavaAgent;
import com.universalmodloader.discovery.ModScanner;

import java.nio.file.Path;
import java.util.List;

/**
 * Ponto de entrada principal do carregador universal.
 *
 * Esta classe funciona como o núcleo principal de bootstrap do loader. É o lugar ideal para:
 *  - descobrir mods na pasta /mods
 *  - instanciar adapters de Fabric e Forge
 *  - registrar transformers e sincronizar o ambiente com a JVM
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("[UniversalModLoader] Inicializando carregador híbrido...");

        // Inicializa o Java Agent se necessário. Em uma execução tradicional fora do ambiente de JVM
        // instrumentada, o agente pode não estar registrado, por isso o código abaixo oferece logging
        // e inicialização local do contexto de instrumentação.
        if (JavaAgent.getInstrumentation() == null) {
            System.out.println("[UniversalModLoader] Instrumentation ainda não está ativa. O agente deve ser anexado via JVM.");
        }

        ModScanner scanner = new ModScanner(Path.of("mods"));
        try {
            List<ModScanner.ModDescriptor> mods = scanner.scan();
            for (ModScanner.ModDescriptor mod : mods) {
                System.out.println("[UniversalModLoader] Mod detectado: " + mod.getPath() + " | tipo: " + mod.getType());
            }
        } catch (Exception e) {
            System.err.println("[UniversalModLoader] Falha ao escanear mods: " + e.getMessage());
        }

        FabricAPIAdapter fabricAdapter = new FabricAPIAdapter();
        ForgeAPIAdapter forgeAdapter = new ForgeAPIAdapter();

        fabricAdapter.initialize();
        forgeAdapter.initialize();

        fabricAdapter.onEvent("ClientLifecycleEvents.CLIENT_STARTED", null);
        forgeAdapter.onEvent("PlayerJoinEvent", null);
    }
}
