package com.mafuyu33.neomafishmod;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    /// ***************

    public static ModConfigSpec.BooleanValue isFuThrowable = BUILDER.define("isFuThrowable", false);

    public static ModConfigSpec.BooleanValue isAlwaysEnchantable = BUILDER.define("isAlwaysEnchantable", false);

    public static ModConfigSpec.BooleanValue isFireworkCanUseOnEntity = BUILDER.define("isFireworkCanUseOnEntity", false);

    public static ModConfigSpec.BooleanValue isFireworkCanHitOnEntity = BUILDER.define("isFireworkCanHitOnEntity", false);

    public static ModConfigSpec.BooleanValue isShieldDashable = BUILDER.define("isShieldDashable", false);

    public static ModConfigSpec.BooleanValue isLeadCanLinkTogether = BUILDER.define("isLeadCanLinkTogether", false);

    public static ModConfigSpec.BooleanValue isLeadCanLinkEveryMob = BUILDER.define("isLeadCanLinkEveryMob", false);

    public static ModConfigSpec.BooleanValue isSpyglassCanPin = BUILDER.define("isSpyglassCanPin", false);

    public static ModConfigSpec.BooleanValue isSwimTripwire = BUILDER.define("isSwimTripwire", false);

    public static ModConfigSpec.BooleanValue isBowDashable = BUILDER.define("isBowDashable", false);

    public static ModConfigSpec.BooleanValue isNestedBoxInfinite = BUILDER.define("isNestedBoxInfinite", false);

    public static ModConfigSpec.BooleanValue isGoatDashForever = BUILDER.define("isGoatDashForever", false);

    public static ModConfigSpec.BooleanValue isGoatDashTogether = BUILDER.define("isGoatDashTogether", false);

    public static ModConfigSpec.BooleanValue isBeeRideable = BUILDER.define("isBeeRideable", false);

    public static ModConfigSpec.BooleanValue isQinNa = BUILDER.define("isQinNa", false);

    public static ModConfigSpec.BooleanValue isLlamaSpitForever = BUILDER.define("isLlamaSpitForever", false);

    public static ModConfigSpec.BooleanValue isTridentPiercing = BUILDER.define("isTridentPiercing", false);

    public static ModConfigSpec.BooleanValue isHurtCoolDownCanceled = BUILDER.define("isHurtCoolDownCanceled", false);

//    public static ModConfigSpec.ConfigValue<Float> breakDistance = BUILDER.comment("How far can you break").define("breakDistance", 10f);


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

//    public static Float breakDistance() {
//        return breakDistance.get();
//    }

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

    public static boolean isTridentPiercing() {
        return isTridentPiercing.get();
    }

    public static boolean isHurtCoolDownCanceled() {
        return isHurtCoolDownCanceled.get();
    }

    static final ModConfigSpec SPEC = BUILDER.build();

}
