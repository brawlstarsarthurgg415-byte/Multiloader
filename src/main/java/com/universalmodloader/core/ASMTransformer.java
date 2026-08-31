package com.universalmodloader.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Transformador ASM base do loader.
 *
 * A ideia aqui é demonstrar como um ClassFileTransformer pode alterar o bytecode em memória
 * antes da classe ser definida pelo JVM. No contexto do Minecraft, isso permite:
 *  - injecao de logs em pontos críticos do jogo;
 *  - adaptação de métodos de mods externos;
 *  - criação de ponteiros para novos eventos do loader híbrido;
 *  - remapeamento de nomes em runtime.
 */
public class ASMTransformer implements ClassFileTransformer {

    private static final Logger LOGGER = Logger.getLogger(ASMTransformer.class.getName());

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer
    ) {
        if (className == null) {
            return classfileBuffer;
        }

        // Exemplo simples: interceptar a classe do Minecraft a partir do nome binário.
        // Em runtime real, a verificação pode ser mais robusta, usando nomes de classe oficiais
        // dos mappings ou um conjunto de classes alvo.
        if (!className.equals("net/minecraft/client/Minecraft")) {
            return classfileBuffer;
        }

        try {
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);

            boolean transformed = false;

            for (MethodNode method : classNode.methods) {
                // Exemplo de patch: adicionar um log no método principal de inicialização.
                // Para demonstrar funcionalidade, buscamos um método que provavelmente existe
                // em versões do Minecraft, como "run" ou "start".
                if (method.name.equals("run") || method.name.equals("start")) {
                    injectLog(method);
                    transformed = true;
                }
            }

            if (!transformed) {
                return classfileBuffer;
            }

            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            byte[] patched = writer.toByteArray();

            LOGGER.info("[UniversalModLoader] Bytecode transformado para classe: " + className);
            return patched;
        } catch (Throwable t) {
            LOGGER.warning("[UniversalModLoader] Falha ao transformar classe " + className + ": " + t.getMessage());
            return classfileBuffer;
        }
    }

    /**
     * Injeta uma chamada de log no início do método.
     *
     * O objetivo é apenas demonstrar como ASM pode inserir instruções na JVM. Em um loader real,
     * isso seria usado para injetar chamadas a um sistema de eventos, patchar métodos de terceiros,
     * ou adaptar uma API de jogo para uma camada uniforme.
     */
    private void injectLog(MethodNode method) {
        InsnList instructions = method.instructions;

        // Carrega a classe java.lang.System
        instructions.insertBefore(
            instructions.getFirst(),
            new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "java/lang/System",
                "getLogger",
                "()Ljava/util/logging/Logger;",
                false
            )
        );

        // Obtém o logger e imprime uma mensagem
        instructions.insertBefore(
            instructions.getFirst(),
            new LdcInsnNode("[UniversalModLoader] Minecraft inicializando via ASM Transformer")
        );

        instructions.insertBefore(
            instructions.getFirst(),
            new MethodInsnNode(
                Opcodes.INVOKEVIRTUAL,
                "java/util/logging/Logger",
                "info",
                "(Ljava/lang/String;)V",
                false
            )
        );
    }
}
