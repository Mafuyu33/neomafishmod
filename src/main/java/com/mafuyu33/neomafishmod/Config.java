package com.mafuyu33.neomafishmod;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    /// ***************

    public static ModConfigSpec.BooleanValue isFuThrowable = BUILDER.comment("Whether enable axe can throwable").define("isFuThrowable", true);

    public static ModConfigSpec.BooleanValue isAlwaysEnchantable = BUILDER.comment("Whether can enchanted any item ").define("isAlwaysEnchantable", true);
    ;

    public static ModConfigSpec.BooleanValue isFireworkCanUseOnEntity = BUILDER.comment("Whether fireworks rockets can be used entity").define("isFireworkCanUseOnEntity", true);


    public static ModConfigSpec.BooleanValue isFireworkCanHitOnEntity = BUILDER.comment("Whether fireworks rockets can be hit entity").define("isFireworkCanHitOnEntity", true);

    public static ModConfigSpec.BooleanValue isShieldDashable = BUILDER.comment("Whether enable Shield Dash").define("isShieldDashable", true);

    public static ModConfigSpec.BooleanValue isLeadCanLinkTogether = BUILDER.comment("Whether enable Lead Can Link Together").define("isLeadCanLinkTogether", true);

    public static ModConfigSpec.BooleanValue isLeadCanLinkEveryMob = BUILDER.comment("Whether enable Lead Can Link Every Mob").define("isLeadCanLinkEveryMob", true);

    public static ModConfigSpec.BooleanValue isSpyglassCanPin = BUILDER.comment("Whether enable Spyglass Can Pin").define("isSpyglassCanPin", true);

    public static ModConfigSpec.BooleanValue isSwimTripwire = BUILDER.comment("Whether enable Swim Tripwire").define("isSwimTripwire", true);

    public static ModConfigSpec.BooleanValue isBowDashable = BUILDER.comment("Whether enable Bow Dash").define("isBowDashable", true);

    public static ModConfigSpec.BooleanValue isNestedBoxInfinite = BUILDER.comment("Whether enable Nested Box Infinite").define("isNestedBoxInfinite", true);

    public static ModConfigSpec.BooleanValue isGoatDashForever = BUILDER.comment("Whether enable Goat Dash Forever").define("isGoatDashForever", true);

    public static ModConfigSpec.BooleanValue isGoatDashTogether = BUILDER.comment("Whether enable Goat Dash Together").define("isGoatDashTogether", true);

    public static ModConfigSpec.BooleanValue isBeeRideable = BUILDER.comment("Whether enable Bee Rideable").define("isBeeRideable", true);

    public static ModConfigSpec.BooleanValue isQinNa = BUILDER.comment("Whether enable Qin Na").define("isQinNa", true);

    public static ModConfigSpec.BooleanValue isLlamaSpitForever = BUILDER.comment("Whether enable Llama Spit Forever").define("isLlamaSpitForever", true);

    public static ModConfigSpec.ConfigValue<Float> breakDistance = BUILDER.comment("How far can you break").define("breakDistance", 10f);


    public static boolean isFuThrowable() {
        return isFuThrowable.get();

    }

    public static boolean isFireworkCanUseOnEntity() {
        return isFireworkCanUseOnEntity.get();
    }

    public static boolean isFireworkCanHitOnEntity() {
        return isFireworkCanHitOnEntity.get();
    }

    public static boolean isShieldDashable() {
        return isShieldDashable.get();
    }

    public static boolean isAlwaysEnchantable() {
        return isAlwaysEnchantable.get();
    }

    public static boolean isLeadCanLinkTogether() {
        return isLeadCanLinkTogether.get();
    }

    public static Float breakDistance() {
        return breakDistance.get();
    }

    public static boolean isLeadCanLinkEveryMob() {
        return isLeadCanLinkEveryMob.get();
    }

    public static boolean isSpyglassCanPin() {
        return isSpyglassCanPin.get();
    }

    public static boolean isSwimTripwire() {
        return isSwimTripwire.get();
    }

    public static boolean isBowDashable() {
        return isBowDashable.get();
    }

    public static boolean isNestedBoxInfinite() {
        return isNestedBoxInfinite.get();
    }

    public static boolean isGoatDashForever() {
        return isGoatDashForever.get();
    }

    public static boolean isGoatDashTogether() {
        return isGoatDashTogether.get();
    }

    public static boolean isLlamaSpitForever() {
        return isLlamaSpitForever.get();
    }

    public static boolean isBeeRideable() {
        return isBeeRideable.get();
    }

    public static boolean isQinNa() {
        return isQinNa.get();
    }

    static final ModConfigSpec SPEC = BUILDER.build();

}
