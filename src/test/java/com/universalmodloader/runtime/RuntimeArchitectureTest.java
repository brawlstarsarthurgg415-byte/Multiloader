package com.universalmodloader.runtime;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RuntimeArchitectureTest {

    @Test
    void shouldRegisterRuntimeAndBridgeEvent() {
        UnifiedRuntimeBootstrap.initialize();

        assertFalse(UnifiedRuntimeBootstrap.getRuntimes().isEmpty());

        final String[] captured = new String[1];
        UnifiedRuntimeBootstrap.getEventBridge().register("player.join", (eventId, payload) -> captured[0] = eventId + ":" + payload);
        UnifiedRuntimeBootstrap.bridgeEvent("player.join", "Alice");

        assertEquals("player.join:Alice", captured[0]);
    }

    @Test
    void shouldLoadClassMappingAndAddJarPath() throws Exception {
        HybridClassLoader loader = UnifiedRuntimeBootstrap.getHybridClassLoader();
        Path tempDir = Files.createTempDirectory("uml-runtime-test");
        Path invalidPath = tempDir.resolve("sample.txt");
        Files.createDirectories(invalidPath.getParent());
        Files.writeString(invalidPath, "not-a-jar");

        assertThrows(IllegalArgumentException.class, () -> loader.addModPath(invalidPath));

        loader.registerClassMapping("demo.SampleMod", "demo.SampleMod");
        assertEquals("demo.SampleMod", loader.resolveMappedName("demo.SampleMod"));
    }
}
