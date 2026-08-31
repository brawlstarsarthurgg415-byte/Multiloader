package com.universalmodloader.core;

import java.lang.instrument.Instrumentation;
import java.util.logging.Logger;

/**
 * Java Agent do carregador universal.
 *
 * O Java Agent entra na JVM antes da execução do programa principal e é o ponto de
 * entrada para instrumentação em tempo de execução. Ele registra um ClassFileTransformer,
 * que permite alterar o bytecode de classes do Minecraft e dos mods antes que elas sejam
 * carregadas por um ClassLoader.
 *
 * Em um carregador híbrido para Minecraft 1.21.1, esse tipo de agente é importante porque
 * permite interceptar classes que já estão em execução, mesmo antes de o jogo terminar de
 * inicializar. Isso dá espaço para adaptação de eventos, patching de métodos e mapeamento
 * de classes entre Fabric e Forge/NeoForge.
 */
public final class JavaAgent {

    private static final Logger LOGGER = Logger.getLogger(JavaAgent.class.getName());
    private static volatile Instrumentation instrumentation;

    private JavaAgent() {
    }

    /**
     * Método invocado pela JVM quando o agente é carregado via linha de comando.
     * Exemplo de uso:
     *   java -javaagent:UniversalModLoader.jar -jar minecraft.jar
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        LOGGER.info("[UniversalModLoader] Agente JVM carregado via premain");
        install(inst);
    }

    /**
     * Método invocado quando o agente é anexado em runtime via Attach API.
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        LOGGER.info("[UniversalModLoader] Agente JVM carregado via agentmain");
        install(inst);
    }

    private static void install(Instrumentation inst) {
        if (inst == null) {
            throw new IllegalArgumentException("Instrumentation não pode ser nulo");
        }

        if (instrumentation == null) {
            instrumentation = inst;
        }

        // Registro do transformer principal.
        // Colocamos na lista global para que qualquer classe carregada por qualquer classloader
        // passe pela transformação antes de ser definida.
        inst.addTransformer(new ASMTransformer(), true);

        LOGGER.info("[UniversalModLoader] ClassFileTransformer registrado com sucesso");
    }

    /**
     * Retorna a instância de Instrumentation ativa.
     * Permite que outros módulos do loader, como adaptadores de API e plataformas, possam
     * consultar o contexto JVM global e interagir com classes em runtime.
     */
    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
