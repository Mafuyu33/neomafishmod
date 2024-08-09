package com.mafuyu33.neomafishmod.mixin.itemmixin;

import com.mafuyu33.neomafishmod.entity.DiamondProjectileEntity;
import com.mafuyu33.neomafishmod.event.KeyInputHandler;
import com.mafuyu33.neomafishmod.network.packet.C2S.ThrowPowerC2SPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ThrowableDiamondMixin {

    @Inject(at=@At("HEAD"),method = "use")
    public void init(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (itemStack.getItem() == Items.DIAMOND){

            if (level.isClientSide){
                sendC2S();
            }
            level.playSound(null,player.getX(),player.getYRot(),player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,0.5f,0.4f/(level.getRandom().nextFloat() * 0.4f + 0.8f));

            if (!level.isClientSide) {
                DiamondProjectileEntity diamondProjectileEntity = new DiamondProjectileEntity(player, level);
                diamondProjectileEntity.setItem(itemStack);
                diamondProjectileEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, ThrowPowerC2SPacket.throw_power, 1.0f);
                level.addFreshEntity(diamondProjectileEntity);
            }

            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            itemStack.consume(1, player);
        }
    }


    @Unique
    @OnlyIn(Dist.CLIENT)
    private void sendC2S(){
//        float throwPower = KeyInputHandler.getThrowPower();//获取当前的投掷力度
//        PacketByteBuf buf = PacketByteBufs.create();//传输到服务端
//        buf.writeFloat(throwPower);
//        ClientPlayNetworking.send(ModMessages.THROW_POWER_ID, buf);
        PacketDistributor.sendToServer(new ThrowPowerC2SPacket(KeyInputHandler.getThrowPower()));
    }
}
