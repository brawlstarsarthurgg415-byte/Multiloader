package com.universalmodloader.runtime;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Bootstrap unificado do runtime híbrido.
 *
 * Este ponto central é o coração da arquitetura proposta para o Super Mod Loader: ele mantém a JVM
 * em um estado unificado, registra múltiplos perfis de runtime e conecta os adaptadores de eventos.
 */
public final class UnifiedRuntimeBootstrap {

    private static final List<ModRuntimeDescriptor> RUNTIMES = new ArrayList<>();
    private static final EventBridge EVENT_BRIDGE = new EventBridge();
    private static final HybridClassLoader HYBRID_CLASS_LOADER = new HybridClassLoader();

    private UnifiedRuntimeBootstrap() {
    }

    public static void initialize() {
        registerRuntime(new ModRuntimeDescriptor("fabric", "Fabric", "FABRIC", Path.of(System.getProperty("user.home"), "UniversalModLoader", "fabric")));
        registerRuntime(new ModRuntimeDescriptor("neoforge", "NeoForge", "NEOFORGE", Path.of(System.getProperty("user.home"), "UniversalModLoader", "neoforge")));

        HYBRID_CLASS_LOADER.registerClassMapping("net.minecraft.client.MinecraftClient", "net.minecraft.client.Minecraft");
        HYBRID_CLASS_LOADER.registerClassMapping("net.fabricmc.loader.api.FabricLoader", "net.fabricmc.loader.api.FabricLoader");
        HYBRID_CLASS_LOADER.registerClassMapping("net.neoforged.fml.ModContainer", "net.neoforged.fml.ModContainer");

        System.out.println("[UnifiedRuntimeBootstrap] Runtime híbrido inicializado com sucesso");
    }

    public static void registerRuntime(ModRuntimeDescriptor descriptor) {
        if (descriptor == null) {
            throw new IllegalArgumentException("Runtime descriptor não pode ser nulo");
        }
        RUNTIMES.add(descriptor);
    }

    public static List<ModRuntimeDescriptor> getRuntimes() {
        return List.copyOf(RUNTIMES);
    }

    public static EventBridge getEventBridge() {
        return EVENT_BRIDGE;
    }

    public static HybridClassLoader getHybridClassLoader() {
        return HYBRID_CLASS_LOADER;
    }

    public static void bridgeEvent(String eventId, Object payload) {
        EVENT_BRIDGE.publish(eventId, payload);
    }
}
