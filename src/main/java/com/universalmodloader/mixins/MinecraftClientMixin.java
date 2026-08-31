package com.universalmodloader.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin exemplo para o Minecraft Client.
 *
 * Essa classe demonstra o padrão mais comum em loaders baseados em Mixin: injetar lógica em um
 * método de classe do jogo sem modificar o código original diretamente. Em um runtime híbrido
 * de Fabric + Forge, isso permite que o carregador central insira hooks e observadores para eventos
 * do mundo e do cliente de forma modular.
 */
@Mixin(value = Object.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "run", at = @At("HEAD"), remap = false)
    private void uml$onClientLaunch(CallbackInfo info) {
        System.out.println("[UniversalModLoader] MinecraftClient iniciado via Mixin");
    }
}
