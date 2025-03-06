//package com.mafuyu33.neomafishmod.event.rotate_screen_180;
//
//import com.mafuyu33.neomafishmod.Config;
//import com.mafuyu33.neomafishmod.NeoMafishMod;
//import com.mafuyu33.neomafishmod.effect.ModEffects;
//import com.mafuyu33.neomafishmod.item.ModItems;
//import net.minecraft.client.Minecraft;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.animal.IronGolem;
//import net.minecraft.world.entity.animal.horse.Llama;
//import net.minecraft.world.entity.npc.Villager;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
//
//
///**
// * @author Mafuyu33
// */
//@EventBusSubscriber
//public class ApplyEffectHandler {
//
//
//    @SubscribeEvent
//    public static void onMobEffectAdded(MobEffectEvent.Added event) {
//        LivingEntity entity = event.getEntity();
//        if (entity instanceof Player && event.getEffectInstance().getEffect().equals(ModEffects.ROTATE_SCREEN_180_EFFECT)) {
//            applyShader();
//        }
//    }
//
//    @SubscribeEvent
//    public static void onMobEffectRemoved(MobEffectEvent.Remove event) {
//        LivingEntity entity = event.getEntity();
//        if (entity instanceof Player && event.getEffectInstance().getEffect().equals(ModEffects.ROTATE_SCREEN_180_EFFECT)) {
//            removeShader();
//        }
//    }
//
//    private static void applyShader() {
//        if (Minecraft.getInstance().level != null) {
//            Minecraft mc = Minecraft.getInstance();
//            mc.gameRenderer.loadEffect(ResourceLocation.fromNamespaceAndPath(NeoMafishMod.MODID, "shaders/post/rotate_screen_180.json"));
//        }
//    }
//
//    private static void removeShader() {
//        if (Minecraft.getInstance().level != null) {
//            Minecraft.getInstance().gameRenderer.shutdownEffect();
//        }
//    }
//}
//尝试用事件触发但是好麻烦mixin还是好用
