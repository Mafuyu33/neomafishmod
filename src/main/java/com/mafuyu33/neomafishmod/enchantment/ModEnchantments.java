package com.mafuyu33.neomafishmod.enchantment;


import com.mafuyu33.neomafishmod.NeoMafishMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

// 自定义附魔类，用于定义和注册新的附魔
public class ModEnchantments {
    // 自定义附魔资源键
    public static final ResourceKey<Enchantment> ONE_STEP_TEN_LINE = key("z_one_step_ten_line");

    // *********************
    public static final ResourceKey<Enchantment> BAD_LUCK_OF_SEA = key("z_bad_luck_of_sea");
    public static final ResourceKey<Enchantment> EIGHT_GODS_PASS_SEA = key("z_eight_gods_pass_sea");
    public static final ResourceKey<Enchantment> KILL_CHICKEN_GET_EGG = key("z_kill_chicken_get_egg");
    public static final ResourceKey<Enchantment> GO_TO_SKY = key("z_go_to_sky");
    public static final ResourceKey<Enchantment> GONG_XI_FA_CAI = key("z_gong_xi_fa_cai");
    public static final ResourceKey<Enchantment> MERCY = key("z_mercy");
    public static final ResourceKey<Enchantment> KILL_MY_HORSE = key("z_kill_my_horse");
    public static final ResourceKey<Enchantment> KILL_MY_HORSE_PLUS = key("z_kill_my_horse_plus");
    public static final ResourceKey<Enchantment> HOT_POTATO = key("z_hot_potato");
    public static final ResourceKey<Enchantment> VERY_EASY = key("z_very_easy");
    public static final ResourceKey<Enchantment> REVERSE = key("z_reverse");
    public static final ResourceKey<Enchantment> PAY_TO_PLAY = key("z_pay_to_play");
    public static final ResourceKey<Enchantment> FEAR = key("z_fear");
    public static final ResourceKey<Enchantment> MUTE = key("z_mute");
    public static final ResourceKey<Enchantment> SLIPPERY = key("z_slippery");
    public static final ResourceKey<Enchantment> NEVER_GONNA = key("z_never_gonna");
    public static final ResourceKey<Enchantment> RESONANCE = key("z_resonance");
    public static final ResourceKey<Enchantment> NO_BLAST_PROTECTION = key("z_no_blast_protection");
    public static final ResourceKey<Enchantment> A_LEAF = key("z_a_leaf");
    public static final ResourceKey<Enchantment> BUTTERFLY = key("z_butterfly");
    public static final ResourceKey<Enchantment> FANGSHENG = key("z_fangsheng");
    public static final ResourceKey<Enchantment> NEVER_STOP = key("z_never_stop");
    public static final ResourceKey<Enchantment> BOW_LEFT = key("z_bow_left");
    public static final ResourceKey<Enchantment> SUPER_PROJECTILE_PROTECTION = key("z_super_projectile_protection");
    public static final ResourceKey<Enchantment> STICKY = key("z_sticky");
    public static final ResourceKey<Enchantment> ONE_WITH_SHADOWS = key("z_one_with_shadows");

    // *********************

    // 引导方法，用于初始化附魔注册
    public static <DamageType> void bootstrap(BootstrapContext<Enchantment> context)
    {
        // 获取各种注册表的持有者获取器
        HolderGetter<net.minecraft.world.damagesource.DamageType> holdergetter = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<Enchantment> holdergetter1 = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);
        HolderGetter<Block> holdergetter3 = context.lookup(Registries.BLOCK);


        register(
                context,
                ONE_WITH_SHADOWS,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        // 注册自定义附魔
        register(//一步十行
                context,
                ONE_STEP_TEN_LINE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.FEET
                        )
                )
        );


        // ******************
        register(
                context,
                BAD_LUCK_OF_SEA,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.FEET
                        )
                )
        );

        register(
                context,
                EIGHT_GODS_PASS_SEA,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.FEET
                        )
                )
        );



        register(
                context,
                KILL_CHICKEN_GET_EGG,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );


        register(
                context,
                GO_TO_SKY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                context,
                GONG_XI_FA_CAI,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );


        register(
                context,
                MERCY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );



        register(
                context,
                KILL_MY_HORSE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                KILL_MY_HORSE_PLUS,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                HOT_POTATO,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );


        register(
                context,
                VERY_EASY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                5,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );


        register(
                context,
                REVERSE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );


        register(
                context,
                PAY_TO_PLAY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                5,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        register(
                context,
                FEAR,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                2,
                                5,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                context,
                MUTE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.HEAD
                        )
                )
        );


        register(
                context,
                SLIPPERY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );


        register(
                context,
                NEVER_GONNA,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );



        register(
                context,
                NO_BLAST_PROTECTION,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                A_LEAF,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                BUTTERFLY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );
        register(
                context,
                FANGSHENG,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                NEVER_STOP,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                BOW_LEFT,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                SUPER_PROJECTILE_PROTECTION,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );

        register(
                context,
                STICKY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );


        register(
                context,
                RESONANCE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                holdergetter2.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(25),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
        );
        // ******************
    }

    // 注册附魔的方法
    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    // 创建附魔资源键的方法
    private static ResourceKey<Enchantment> key(String name)
    {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID,name));
    }
}
