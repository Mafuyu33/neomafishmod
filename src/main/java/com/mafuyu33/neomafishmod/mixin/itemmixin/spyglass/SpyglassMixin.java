package com.mafuyu33.neomafishmod.mixin.itemmixin.spyglass;

import com.ibm.icu.impl.coll.UVector32;
import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.mixinhelper.BowDashMixinHelper;
import com.mafuyu33.neomafishmod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class SpyglassMixin extends LivingEntity {
	protected SpyglassMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract boolean isScoping();

	@Shadow public abstract Iterable<ItemStack> getHandSlots();

	@Unique
	BlockPos lastHitBlockPos;

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo ci) {
		boolean isSpyglassCanPin = Config.isSpyglassCanPin();
		if (isSpyglassCanPin) {
			if (this.isScoping() && BowDashMixinHelper.isAttackKeyPressed()) {//标记地点
				// 获取玩家的位置和视线方向
				Vec3 playerPos = this.getEyePosition(1.0F);
				Vec3 playerLook = this.getViewVector(1.0F);

				// 设置射线起点和方向
				Vec3 rayEnd = playerPos.add(playerLook.scale(25565)); // 假设射线长度为 10

				// 进行射线投射
				BlockHitResult blockHitResult = level().clip(new ClipContext(playerPos, rayEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));

				// 检查是否击中方块
				if (blockHitResult.getType() == HitResult.Type.BLOCK) {
					// 获取方块坐标
					BlockPos currentHitBlockPos = blockHitResult.getBlockPos();
					// 如果当前坐标和上一次点击的坐标在 y 轴上相同，则清除存储的坐标值
					if (lastHitBlockPos != null && currentHitBlockPos.getY() == lastHitBlockPos.getY()) {
						lastHitBlockPos = null;
					} else {
						// 否则，存储当前坐标值
						lastHitBlockPos = currentHitBlockPos;
						if (level().isClientSide) {
							level().playSound((Player) (Object) this, this.blockPosition(), ModSounds.PIN.value(), SoundSource.PLAYERS);
							System.out.println("击中方块坐标：" + currentHitBlockPos.getX() + ", " + currentHitBlockPos.getY() + ", " + currentHitBlockPos.getZ());
						}
					}

				}
			}
			if (lastHitBlockPos != null) {
				// 在hitBlockPos为原点，竖直向上延伸20格生成末地烛粒子
				for (float yOffset = 0; yOffset < 20; yOffset += 0.1f) {
					double particleX = lastHitBlockPos.getX() + 0.5 + (random.nextFloat() - 0.5) * 0.1; // 加入随机性模拟波动
					double particleY = lastHitBlockPos.getY() + yOffset + 0.5;
					double particleZ = lastHitBlockPos.getZ() + 0.5 + (random.nextFloat() - 0.5) * 0.1; // 加入随机性模拟波动

					// 计算粒子密度，可以根据需要调整指数底数和系数
					double density = Math.exp(-yOffset / 20); // 使用指数衰减模拟密度减少

					// 根据粒子密度生成粒子
					if (level().isClientSide && random.nextFloat() < density) {
						level().addParticle(ParticleTypes.COMPOSTER, true, particleX, particleY, particleZ, 0, 0.05, 0);
					}
				}
			}

			if (this.isHolding(Items.GOAT_HORN) && this.isUsingItem() && lastHitBlockPos != null) {//使用山羊角
				if (!level().isClientSide) {
					for (int i = 0; i < 20; i++) {
						double xOffset = random.nextDouble() * 16 - 8; // 在-5到5之间生成随机偏移量
						double zOffset = random.nextDouble() * 16 - 8;
						double x = lastHitBlockPos.getX() + 0.5 + xOffset;
						double y = lastHitBlockPos.getY() + 100 + random.nextDouble() * 4 - 2; // 在lastHitBlockPos上空20格附近随机生成Y坐标
						double z = lastHitBlockPos.getZ() + 0.5 + zOffset;

						PrimedTnt tnt = new PrimedTnt(EntityType.TNT, level());
						tnt.setPos(x, y, z);

						// 设置不同的向下速度
						double downwardVelocity = random.nextDouble() * 1 - 0.5; // 随机生成一个向下速度，范围在-0.5到0.5之间
						tnt.setDeltaMovement(0, downwardVelocity, 0);

						// 根据向下速度的大小确定引爆时间
						int fuse = (int) (downwardVelocity * -50) + 120; // 根据向下速度计算引爆时间，速度越小，引爆时间越长
						tnt.setFuse(fuse); // 设置TNT的引爆时间

						level().addFreshEntity(tnt);
					}
				}
				lastHitBlockPos = null;
			}
		}
	}
}