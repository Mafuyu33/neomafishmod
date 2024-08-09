package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.bellknockbackandhurt;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.network.packet.C2S.GameOptionsC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BellBlockEntity.class)
public abstract class BellBlockEntityMixin extends BlockEntity {
	public BellBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Shadow private List<LivingEntity> nearbyEntities;

	@Inject(at = @At("TAIL"), method = "updateEntities")
	private void init1(CallbackInfo ci) {
		int k = BlockEnchantmentStorage.getLevel(Enchantments.IMPALING,worldPosition);
		int j = BlockEnchantmentStorage.getLevel(Enchantments.KNOCKBACK,worldPosition);
		if(level!=null && this.level.isClientSide) {
			sendC2S();
		}

		if (!this.level.isClientSide && k > 0) {//穿刺
			for (LivingEntity livingEntity : this.nearbyEntities) {
				if(!livingEntity.isAlwaysTicking()) {
						if (livingEntity.isAlive() && !livingEntity.isRemoved() && worldPosition.closerThan(livingEntity.blockPosition(), 32.0)) {
						livingEntity.hurt(livingEntity.damageSources().magic(), k);
					}
				}else if(GameOptionsC2SPacket.master > 0.0f && GameOptionsC2SPacket.blocks > 0.0f && livingEntity.isAlive()
						&& !livingEntity.isRemoved() && worldPosition.closerThan(livingEntity.blockPosition(), 32.0)){
					livingEntity.hurt(livingEntity.damageSources().magic(), k);
				}
			}
		}
		if ( j > 0) {//击退
			for (LivingEntity livingEntity : this.nearbyEntities) {
				if(!livingEntity.isAlwaysTicking() && !this.level.isClientSide) {
					if (livingEntity.isAlive() && !livingEntity.isRemoved() && worldPosition.closerThan(livingEntity.blockPosition(), 32.0)) {
						// 获取 pos 指向 livingEntity 的方向向量
						Vec3 direction = livingEntity.position().subtract(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).normalize();
						// 施加一个击退效果
						livingEntity.push(direction.x * j, 0.5, direction.z * j);
					}
				}else if(GameOptionsC2SPacket.master > 0.0f && GameOptionsC2SPacket.blocks > 0.0f && livingEntity.isAlive()
						&& !livingEntity.isRemoved() && worldPosition.closerThan(livingEntity.blockPosition(), 32.0)){
					// 获取 pos 指向 livingEntity 的方向向量
					Vec3 direction = livingEntity.position().subtract(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).normalize();
					// 施加一个击退效果
					livingEntity.push(direction.x * j, 0.5, direction.z * j);
				}
			}
		}
	}

	@Unique
	@OnlyIn(Dist.CLIENT)
	private void sendC2S(){
		Options options = Minecraft.getInstance().options;
		float blocks = options.getSoundSourceVolume(SoundSource.BLOCKS);
		float master = options.getSoundSourceVolume(SoundSource.MASTER);
//		FriendlyByteBuf buf = new FriendlyByteBuf();//传输到服务端
//
//		buf.writeFloat(blocks);
//		buf.writeFloat(master);
//		ClientPlayNetworking.send(ModMessages.GAME_OPTIONS_ID, buf);
		PacketDistributor.sendToServer(new GameOptionsC2SPacket(blocks,master));
	}

}