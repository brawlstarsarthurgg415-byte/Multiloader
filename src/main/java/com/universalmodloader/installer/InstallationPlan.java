package com.universalmodloader.installer;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plano de instalação de um launcher/loader híbrido de Minecraft.
 *
 * Este objeto cria a estrutura de diretórios que um cliente real de Minecraft costuma ter:
 *  - pasta de instalação
 *  - pasta de mods
 *  - pasta de configuração
 *  - pasta para versões e runtime
 */
public final class InstallationPlan {

    private final String name;
    private final String version;
    private final String loader;
    private final Path installDir;
    private final Path modsDir;
    private final Path configDir;
    private final Path runtimeDir;

    private InstallationPlan(String name, String version, String loader, Path installDir, Path modsDir,
                            Path configDir, Path runtimeDir) {
        this.name = name;
        this.version = version;
        this.loader = loader;
        this.installDir = installDir;
        this.modsDir = modsDir;
        this.configDir = configDir;
        this.runtimeDir = runtimeDir;
    }

    public static InstallationPlan create(Path baseDir, String name) throws Exception {
        return create(baseDir, name, "1.21.1", "Fabric");
    }

    public static InstallationPlan create(Path baseDir, String name, String version, String loader) throws Exception {
        Path installDir = baseDir.resolve(name);
        Path modsDir = installDir.resolve("mods");
        Path configDir = installDir.resolve("config");
        Path runtimeDir = installDir.resolve("runtime");

        Files.createDirectories(modsDir);
        Files.createDirectories(configDir);
        Files.createDirectories(runtimeDir);

        return new InstallationPlan(name, version, loader, installDir, modsDir, configDir, runtimeDir);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getLoader() {
        return loader;
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

    public Path getRuntimeDir() {
        return runtimeDir;
    }

    public Path getMinecraftJarPath() {
        return installDir.resolve("minecraft-" + version + ".jar");
    }
}
