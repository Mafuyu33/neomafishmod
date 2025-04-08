package com.mafuyu33.neomafishmod.command;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * @author Mafuyu33
 */
public class ModCommands {

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("clearAllBlockEnchantments")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            int cleared = BlockEnchantmentStorage.clearAll();
                            context.getSource().sendSuccess(() -> Component.literal(
                                    "Cleared " + cleared + " enchanted blocks."), true);
                            return cleared;
                        })
        );
    }
}
