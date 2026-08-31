package com.universalmodloader.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Scanner simples de mods no diretório /mods.
 *
 * Ele verifica se o JAR contém um dos metadados primários dos loaders híbridos:
 *  - fabric.mod.json para mods de Fabric
 *  - mods.toml para mods de Forge/NeoForge
 *
 * Esse tipo de análise é o primeiro passo para decidir o pipeline correto de carga e de
 * adaptação entre API de Fabric e API de Forge.
 */
public final class ModScanner {

    private final Path modsDirectory;

    public ModScanner(Path modsDirectory) {
        this.modsDirectory = modsDirectory;
    }

    public List<ModDescriptor> scan() throws IOException {
        List<ModDescriptor> descriptors = new ArrayList<>();

        if (!Files.exists(modsDirectory)) {
            return descriptors;
        }

        try (var paths = Files.list(modsDirectory)) {
            for (Path file : paths.filter(path -> path.getFileName().toString().endsWith(".jar")).toList()) {
                descriptors.add(readModDescriptor(file));
            }
        }

        return descriptors;
    }

    public ModDescriptor readModDescriptor(Path jarPath) throws IOException {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            ZipEntry fabricEntry = jarFile.getEntry("fabric.mod.json");
            ZipEntry forgeEntry = jarFile.getEntry("META-INF/mods.toml");

            if (fabricEntry != null) {
                String content = readText(jarFile, fabricEntry);
                return new ModDescriptor(jarPath, ModType.FABRIC, content);
            }

            if (forgeEntry != null) {
                String content = readText(jarFile, forgeEntry);
                return new ModDescriptor(jarPath, ModType.FORGE, content);
            }

            return new ModDescriptor(jarPath, ModType.UNKNOWN, "");
        }
    }

    private String readText(JarFile jarFile, ZipEntry entry) throws IOException {
        try (InputStream inputStream = jarFile.getInputStream(entry)) {
            return new String(inputStream.readAllBytes());
        }
    }

    public enum ModType {
        FABRIC,
        FORGE,
        UNKNOWN
    }

    public static final class ModDescriptor {
        private final Path path;
        private final ModType type;
        private final String metadataRaw;

        public ModDescriptor(Path path, ModType type, String metadataRaw) {
            this.path = path;
            this.type = type;
            this.metadataRaw = metadataRaw;
        }

        public Path getPath() {
            return path;
        }

        public ModType getType() {
            return type;
        }

        public String getMetadataRaw() {
            return metadataRaw;
        }

        @Override
        public String toString() {
            return "ModDescriptor{" +
                "path=" + path +
                ", type=" + type +
                '}';
        }
    }
}
