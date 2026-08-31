package com.universalmodloader.runtime;

import java.nio.file.Path;

/**
 * Descreve um perfil de execução dentro do runtime híbrido.
 *
 * O objetivo é unificar a identidade do Fabric e do NeoForge em um mesmo ambiente,
 * permitindo que o carregador central gerencie perfis de runtime diferentes em uma mesma JVM.
 */
public record ModRuntimeDescriptor(
    String id,
    String name,
    String loaderType,
    Path rootPath
) {
    public ModRuntimeDescriptor {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID do runtime não pode ser vazio");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do runtime não pode ser vazio");
        }
        if (loaderType == null || loaderType.isBlank()) {
            throw new IllegalArgumentException("Tipo de loader não pode ser vazio");
        }
        if (rootPath == null) {
            throw new IllegalArgumentException("Path do runtime não pode ser nulo");
        }
    }
}
