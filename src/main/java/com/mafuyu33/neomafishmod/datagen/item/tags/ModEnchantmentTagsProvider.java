package com.mafuyu33.neomafishmod.datagen.item.tags;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public ModEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NeoMafishMod.MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(EnchantmentTags.TREASURE)
                .add(ModEnchantments.GO_TO_SKY)
                .add(ModEnchantments.KILL_MY_HORSE)
                .add(ModEnchantments.KILL_MY_HORSE_PLUS)
                .add(ModEnchantments.SLIPPERY)
                .add(ModEnchantments.RESONANCE)
                .add(ModEnchantments.BUTTERFLY);

        this.tag(EnchantmentTags.CURSE)
                .add(ModEnchantments.BAD_LUCK_OF_SEA)
                .add(ModEnchantments.HOT_POTATO)
                .add(ModEnchantments.VERY_EASY)
                .add(ModEnchantments.REVERSE)
                .add(ModEnchantments.MUTE)
                .add(ModEnchantments.SLIPPERY)
                .add(ModEnchantments.NEVER_GONNA)
                .add(ModEnchantments.NO_BLAST_PROTECTION)
                .add(ModEnchantments.A_LEAF)
                .add(ModEnchantments.NEVER_STOP)
                .add(ModEnchantments.BOW_LEFT);
    }
}
