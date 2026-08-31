package com.universalmodloader.installer;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plano de instalação de um carregador híbrido.
 *
 * Em vez de ser um instalador que só empacota JARs, ele cria uma estrutura de diretórios
 * semelhante ao de um launcher/mod loader de Minecraft: pasta de instalação, pasta de mods,
 * pasta de cache e arquivo de configuração.
 */
public final class InstallationPlan {

    private final String name;
    private final Path installDir;
    private final Path modsDir;
    private final Path configDir;

    private InstallationPlan(String name, Path installDir, Path modsDir, Path configDir) {
        this.name = name;
        this.installDir = installDir;
        this.modsDir = modsDir;
        this.configDir = configDir;
    }

    public static InstallationPlan create(Path baseDir, String name) throws Exception {
        Path installDir = baseDir.resolve(name);
        Path modsDir = installDir.resolve("mods");
        Path configDir = installDir.resolve("config");

        Files.createDirectories(modsDir);
        Files.createDirectories(configDir);

        return new InstallationPlan(name, installDir, modsDir, configDir);
    }

    public String getName() {
        return name;
    }

    public Path getInstallDir() {
        return installDir;
    }

    public Path getModsDir() {
        return modsDir;
    }

    public Path getConfigDir() {
        return configDir;
    }
}
