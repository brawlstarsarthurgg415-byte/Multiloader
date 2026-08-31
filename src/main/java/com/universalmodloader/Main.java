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
 * Ponto de entrada principal do launcher/loader híbrido.
 *
 * A interface aqui é a de um launcher de Minecraft real: configuração de versão, loader,
 * diretórios de mods/config e o botão que tenta iniciar o cliente oficial do Minecraft.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InstallerFrame frame = new InstallerFrame();
            frame.setVisible(true);
        });

        System.out.println("[UniversalModLoader] Inicializando ambiente de launcher...");

        if (JavaAgent.getInstrumentation() == null) {
            System.out.println("[UniversalModLoader] Instrumentation não está anexada, como esperado em um launcher simples.");
        }

        ModScanner scanner = new ModScanner(Path.of(System.getProperty("user.home"), "UniversalModLoader", "UniversalModLoader", "mods"));
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
