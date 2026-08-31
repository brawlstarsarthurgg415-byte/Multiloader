package com.universalmodloader;

import com.universalmodloader.api.fabric.FabricAPIAdapter;
import com.universalmodloader.api.forge.ForgeAPIAdapter;
import com.universalmodloader.core.JavaAgent;
import com.universalmodloader.discovery.ModScanner;
import com.universalmodloader.installer.InstallerFrame;

import javax.swing.SwingUtilities;
import java.nio.file.Path;
import java.util.List;

/**
 * Ponto de entrada principal do carregador universal.
 *
 * Este launcher funciona como um instalador/bootstraper do loader. Ao abrir o JAR, ele exibe uma
 * interface visual simples e prepara a estrutura de diretórios do ambiente do loader.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InstallerFrame frame = new InstallerFrame();
            frame.setVisible(true);
        });

        System.out.println("[UniversalModLoader] Inicializando carregador híbrido...");

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
