package com.universalmodloader.runtime;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassLoader híbrido para unificar o carregamento de classes de diferentes loaders.
 *
 * A ideia central é permitir que classes originárias do Fabric e do NeoForge sejam carregadas por
 * uma JVM comum, com traduções de nomes e redirecionamento de classpath para o runtime central.
 */
public class HybridClassLoader extends URLClassLoader {

    private final Map<String, String> classMappings = new HashMap<>();

    public HybridClassLoader() {
        super(new URL[0], HybridClassLoader.class.getClassLoader());
    }

    public void addModPath(Path jarPath) throws IOException {
        if (jarPath == null || !jarPath.toFile().exists()) {
            throw new IllegalArgumentException("Caminho de mod inválido: " + jarPath);
        }
        if (!jarPath.getFileName().toString().endsWith(".jar")) {
            throw new IllegalArgumentException("O caminho deve apontar para um arquivo .jar: " + jarPath);
        }
        addURL(jarPath.toUri().toURL());
    }

    /**
     * Registra um mapeamento de classe para permitir que uma classe da API de um loader seja
     * redirecionada para outra no ambiente unificado.
     */
    public void registerClassMapping(String originalName, String mappedName) {
        if (originalName == null || mappedName == null) {
            throw new IllegalArgumentException("Mapeamento de classe inválido");
        }
        classMappings.put(originalName, mappedName);
    }

    public String resolveMappedName(String originalName) {
        return classMappings.getOrDefault(originalName, originalName);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        String mappedName = resolveMappedName(name);
        return super.loadClass(mappedName, resolve);
    }
}
