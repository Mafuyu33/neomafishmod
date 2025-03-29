package com.mafuyu33.neomafishmod.datagen;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.datagen.item.tags.ModBlockTagsProvider;
import com.mafuyu33.neomafishmod.datagen.item.tags.ModEnchantmentTagsProvider;
import com.mafuyu33.neomafishmod.datagen.item.tags.ModItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = NeoMafishMod.MODID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Server event) {  // 注意这里是 GatherDataEvent.Server
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 使用 createDatapackRegistryObjects 来注册数据包条目
//        event.createDatapackRegistryObjects(ModDatapackBuiltinEntriesProvider.BUILDER);

        // 为数据生成器添加一个自定义的数据包内置条目提供者
        generator.addProvider(true, new ModDatapackBuiltinEntriesProvider(output, lookupProvider));

        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(output, lookupProvider, NeoMafishMod.MODID);
        generator.addProvider(true, blockTagsProvider);

        generator.addProvider(true, new ModItemTagsProvider(
                output,
                lookupProvider,
                blockTagsProvider.contentsGetter(),
                NeoMafishMod.MODID
        ));

        generator.addProvider(true, new ModEnchantmentTagsProvider(output, lookupProvider));
    }
}