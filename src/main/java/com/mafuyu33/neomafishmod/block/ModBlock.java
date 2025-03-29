package com.mafuyu33.neomafishmod.block;

import com.mafuyu33.neomafishmod.NeoMafishMod;
import com.mafuyu33.neomafishmod.block.custom.PotatoTNTBlock;
import com.mafuyu33.neomafishmod.block.custom.PotatoTNTPrepareBlock;
import com.mafuyu33.neomafishmod.block.custom.SoundBlock;
import com.mafuyu33.neomafishmod.item.ModItems;
import com.mafuyu33.neomafishmod.sound.ModSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlock {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NeoMafishMod.MODID);

    public static final DeferredBlock<Block> GOLD_MELON = registerSimpleBlock("gold_melon",
            blockProps("gold_melon").mapColor(MapColor.GOLD));

    public static final DeferredBlock<Block> POTATO_TNT = registerBlock("potato_tnt",
            ()-> new PotatoTNTBlock(blockProps("potato_tnt").mapColor(MapColor.COLOR_BROWN)
                    .instrument(NoteBlockInstrument.BASS).strength(5f,1f).sound(SoundType.STONE)));

//    public static final DeferredBlock<Block> POTATO_TNT_PREPARE = registerBlock("potato_tnt_prepare",
//            ()-> new PotatoTNTPrepareBlock(MobEffects.FIRE_RESISTANCE,10,
//                    blockProps("potato_tnt_prepare").ofFullCopy(Blocks.ALLIUM).noOcclusion().noCollission()));

    public static final DeferredBlock<Block> WHATE_CAT_BLOCK = registerBlock("white_cat_block",
            ()-> new SoundBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .setId(ResourceKey.create(Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "white_cat_block")))
                    .sound(ModSounds.SOUND_BLOCK_SOUNDS)));

    private static DeferredBlock<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        DeferredBlock<Block> register = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name,()-> new BlockItem(register.get(),
                ModItems.itemProps(name))); // 使用ModItems中的方法设置Item ID
        return register;
    }

    public static DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties props) {
        DeferredBlock<Block> deferredBlock = BLOCKS.registerSimpleBlock(name,props);
        ModItems.ITEMS.register(name,()-> new BlockItem(deferredBlock.get(),
                ModItems.itemProps(name))); // 使用ModItems中的方法设置Item ID
        return deferredBlock;
    }

    public static DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties props, Item.Properties properties) {
        DeferredBlock<Block> deferredBlock = BLOCKS.registerSimpleBlock(name,props);
        ModItems.ITEMS.register(name,()-> new BlockItem(deferredBlock.get(), properties));
        return deferredBlock;
    }

    // 添加这个辅助方法来设置Block ID
    private static BlockBehaviour.Properties blockProps(String name) {
        return BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, name)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}