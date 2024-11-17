package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantmentHelper;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// TODO add enchantment
@Mixin(LivingEntity.class)
public abstract class ArmorEnchantmentMixin extends Entity implements Attackable,net.neoforged.neoforge.common.extensions.ILivingEntityExtension {


	public ArmorEnchantmentMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow public abstract Iterable<ItemStack> getArmorSlots();

	@Shadow
	public abstract boolean isSensitiveToWater();

	@Shadow public abstract void handleDamageEvent(DamageSource damageSource);

	@Shadow @Nullable
	public abstract DamageSource getLastDamageSource();

	@Shadow public abstract void kill();

	@Shadow public abstract boolean hurt(DamageSource source, float amount);

	@Shadow public abstract void push(Entity entity);

	@Shadow protected abstract void pushEntities();

	@Shadow public abstract Iterable<ItemStack> getArmorAndBodyArmorSlots();

	@Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

	@Shadow @Nullable public abstract LivingEntity getKillCredit();

	@Unique
	private static Vec3 lastPos= new Vec3(0, 0, 0);


	@Unique
	public RandomSource getRandom() {
		return this.random;
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z",ordinal = 2), method = "hurt",cancellable = true)
	private void init2(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		Iterable<ItemStack> armorItems = Collections.singleton(this.getItemBySlot(EquipmentSlot.BODY));
		for (ItemStack armorItem : armorItems) {
			if (armorItem.getItem() instanceof ArmorItem) {
				int p = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.SUPER_PROJECTILE_PROTECTION);//超级投射物保护
				if(p > 0 && source.is(DamageTypeTags.IS_PROJECTILE)){
					Entity entity = source.getEntity();
					Entity projectile = source.getDirectEntity();
					if(projectile!=null && !level().isClientSide) {
						if (entity instanceof LivingEntity livingEntity) {
							// 创建物品实体并设置位置
							double d = this.getX() - livingEntity.getX();
							double e = this.getY() - livingEntity.getY();
							double f = this.getZ() - livingEntity.getZ();
							Projectile newProjectileEntity = new Arrow(this.level(),(LivingEntity) (Object)this, new ItemStack(Items.ARROW),null);
							newProjectileEntity.setPos(this.getX(), this.getY() + 1, this.getZ());
							newProjectileEntity.setDeltaMovement(-d * 0.1 * 1.3, -e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08 * 1.3, -f * 0.1 * 1.3);
							// 将物品实体添加到世界中
							projectile.discard();
							this.level().addFreshEntity(newProjectileEntity);
							cir.setReturnValue(false);
							break;
						} else {
							double offset = 0.0; // 调整的偏移量，可以根据需要调整
							Vec3 lookVec = this.getViewVector(1.0F);// 获取玩家朝向向量
							double speedMultiplier = 2.0;
							Projectile newProjectileEntity = new Arrow(this.level(), (LivingEntity) (Object)this, new ItemStack(Items.ARROW),null);
							newProjectileEntity.setPos(this.getX()+ lookVec.x * offset, this.getY() + 1.65, this.getZ() + lookVec.z * offset);
							// 将物品实体速度设置为玩家朝向方向
							newProjectileEntity.setDeltaMovement(
									lookVec.x * speedMultiplier,
									lookVec.y * speedMultiplier,
									lookVec.z * speedMultiplier
							);
							projectile.discard();
							// 将物品实体添加到世界中
							this.level().addFreshEntity(newProjectileEntity);
							cir.setReturnValue(false);
							break;
						}
					}
				}
			}
		}
	}


	@Inject(at = @At("HEAD"), method = "die")
	private void init(CallbackInfo ci) {
		BlockPos blockPos =this.blockPosition();
		Level world =this.level();
		if (this.getKillCredit() != null) {
			if (this.getType() == EntityType.HORSE ){
				Iterable<ItemStack> armorItems = Collections.singleton(this.getItemBySlot(EquipmentSlot.BODY));
				for (ItemStack armorItem : armorItems) {
					int j = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.KILL_MY_HORSE);//敢杀我的马！
					int k = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.KILL_MY_HORSE_PLUS);//敢杀我的马！plus
					if (j>0) {
						EntityType.WARDEN.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
					}
					if (k>0) {
						EntityType.WARDEN.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.BLAZE.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.CREEPER.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.EVOKER.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.GHAST.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.HOGLIN.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.HUSK.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.MAGMA_CUBE.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.PHANTOM.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.PIGLIN.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.RAVAGER.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.SHULKER.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.SILVERFISH.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.SKELETON.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.SLIME.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.STRAY.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.VEX.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.VINDICATOR.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.WITCH.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.WITHER_SKELETON.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.ZOGLIN.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.ZOMBIE.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
						EntityType.ZOMBIE_VILLAGER.spawn(((ServerLevel) world), blockPos, MobSpawnType.TRIGGERED);
					}
				}
			}
		}
	}
//
	@Inject(at = @At("HEAD"), method = "tick")
	private void init1(CallbackInfo info) {
		Iterable<ItemStack> armorItems = this.getArmorAndBodyArmorSlots();

		if (this.getType() == EntityType.HORSE){
			for (ItemStack armorItem : armorItems) {//检测马铠
				Iterable<ItemStack> armorAndBodyArmorSlots = this.getArmorAndBodyArmorSlots();
				int k = InjectHelper.getEnchantmentLevel(armorItem, Enchantments.FROST_WALKER);
				int j = InjectHelper.getEnchantmentLevel(armorItem, Enchantments.FIRE_ASPECT);
				int i = InjectHelper.getEnchantmentLevel(armorItem, Enchantments.CHANNELING);
				if (k>0) {//冰霜行者
					nEOFORGE1_21$freezeWater((LivingEntity) (Object)this, level(), this.blockPosition(), k+1);
				}
				if (j>0) {//火焰附加
					Level world = this.level();
					BlockPos blockPos = this.blockPosition();
					world.setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 3);
				}
				if (i>0) {//引雷
					BlockPos blockPos = this.blockPosition();
					LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(this.level());
					if (lightningEntity != null) {
						lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
						this.level().addFreshEntity(lightningEntity);
						SoundEvent soundEvent = SoundEvents.TRIDENT_THUNDER.value();
						this.playSound(soundEvent, 5, 1.0F);
					}
				}
			}
		}


		for (ItemStack armorItem : armorItems) {
			if (armorItem.getItem() instanceof ArmorItem && ((ArmorItem) armorItem.getItem()).getType() == ArmorItem.Type.BOOTS) {//鞋子
				int k = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.BAD_LUCK_OF_SEA);//海之嫌弃
				if (k > 0) {
					Level world = this.level();
					BlockPos blockPos = this.blockPosition();
					FluidState fluidState = world.getFluidState(blockPos);
					if (fluidState.is(FluidTags.WATER)) {

						BlockPos closestNonLiquidBlockPos = null;
						double closestDistanceSq = Double.MAX_VALUE; // 初始设置为最大值

						for (int xOffset = -20; xOffset <= 19; xOffset++) {
							for (int zOffset = -20; zOffset <= 19; zOffset++) {
								BlockPos currentPos = blockPos.offset(xOffset, 0, zOffset);
								FluidState fluidState1 = world.getFluidState(currentPos);

								// 检查当前方块是否不是液体方块
								if (!fluidState1.is(FluidTags.WATER)) {
									// 计算当前方块与玩家的距离的平方
									double distanceSq = this.distanceToSqr(Vec3.atCenterOf(currentPos));

									// 如果当前方块更近，则更新最近的非液体方块信息
									if (distanceSq < closestDistanceSq) {
										closestDistanceSq = distanceSq;
										closestNonLiquidBlockPos = currentPos;
									}
								}
							}
						}

						if (closestNonLiquidBlockPos != null) {
							// 发送信息给玩家
//							this.sendMessage(Text.literal("检测到最近的固体方块可弹射"));

							// 计算方向向量
							Vec3 direction = Vec3.atCenterOf(closestNonLiquidBlockPos).subtract(this.position()).normalize();

							double speed = 1; // 设定速度大小（可以根据需要调整）

							// 计算最终的速度向量
							Vec3 velocity = direction.scale(speed);

							this.push(0, 1, 0);
							this.addDeltaMovement(velocity); // 应用速度
						}
						else {
							this.push(0, 1, 0);
						}
					}
				}


				int j = InjectHelper.getEnchantmentLevel(armorItem, Enchantments.FIRE_ASPECT);//火焰附加
				if (j > 0) {
					Level world = this.level();
					BlockPos blockPos = this.blockPosition();
					world.setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 3);
				}

				int i = InjectHelper.getEnchantmentLevel(armorItem, Enchantments.CHANNELING);//引雷
				if (i > 0) {
					BlockPos blockPos = this.blockPosition();
					LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(this.level());
					if (lightningEntity != null) {
						lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
						this.level().addFreshEntity(lightningEntity);
						SoundEvent soundEvent = SoundEvents.TRIDENT_THUNDER.value();
						this.playSound(soundEvent, 5, 1.0F);
					}
				}

				int m = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.EIGHT_GODS_PASS_SEA);//八仙过海
				if (m > 0) {
					Level world = this.level();
					BlockPos blockPos = this.blockPosition();
					nEOFORGE1_21$checkAndReplaceWaterBlocks(world, blockPos);
				}
				int n = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.NEVER_STOP);//不要停下来啊
				if (n > 0) {
					if (!this.level().isClientSide) {
						Vec3 currentPos = this.position();
						double distance = currentPos.distanceTo(lastPos); // 计算当前位置和上一个位置之间的距离
						boolean isMoving = distance > 0.0784000015258790; // 设置一个小于的阈值，比如0.1
						lastPos = currentPos;
						if (!isMoving) {
							this.hurt(damageSources().magic(), 1f);
						}
					}
				}

			}
			if (level().isClientSide && armorItem.getItem() instanceof ArmorItem
					&& ((ArmorItem) armorItem.getItem()).getType() == ArmorItem.Type.HELMET) {//帽子
				int o = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.MUTE);//静音
				if (o > 0 && this.isAlwaysTicking()) {
					nEOFORGE1_21$mute();
				}
			}
			if (armorItem.getItem() instanceof ArmorItem) {//随便什么装甲
				int p = InjectHelper.getEnchantmentLevel(armorItem, ModEnchantments.NO_BLAST_PROTECTION);//爆炸不保护
				if (p > 0 && this.getLastDamageSource()!= null){
					// todo
					if(this.getLastDamageSource().typeHolder().getKey().toString().contains("explosion")) {
						this.kill();
					}
				}
			}
		}
	}
	@Unique
	@OnlyIn(Dist.CLIENT)
	private void nEOFORGE1_21$mute(){
		Options gameOptions = Minecraft.getInstance().options;
		gameOptions.getSoundSourceOptionInstance(SoundSource.MASTER).set((double) 0);
	}
	@Unique
	private static void nEOFORGE1_21$freezeWater(LivingEntity entity, Level world, BlockPos blockPos, int level) {
		if (entity.onGround()) {
			BlockState blockState = Blocks.FROSTED_ICE.defaultBlockState();
			int i = Math.min(16, 2 + level);
			BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
			Iterator var7 = BlockPos.betweenClosed(blockPos.offset(-i, -1, -i), blockPos.offset(i, -1, i)).iterator();

			while(var7.hasNext()) {
				BlockPos blockPos2 = (BlockPos)var7.next();
				if (blockPos2.closerThan(entity.blockPosition(), (double)i)) {
					mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
					BlockState blockState2 = world.getBlockState(mutable);
					if (blockState2.isAir()) {
						BlockState blockState3 = world.getBlockState(blockPos2);
//						if (blockState3 == FrostedIceBlock.meltsInto() && blockState.canSurvive(world, blockPos2) && world.canPlace(blockState, blockPos2, CollisionContext.empty())) {
						if (blockState3 == FrostedIceBlock.meltsInto() && blockState.canSurvive(world, blockPos2)) {
							world.setBlockAndUpdate(blockPos2, blockState);
							world.scheduleTick(blockPos2, Blocks.FROSTED_ICE, Mth.nextInt(entity.getRandom(), 60, 120));
						}
					}
				}
			}
		}
	}
	@Unique
	private List<BlockPos> nEOFORGE1_21$replacedWaterBlocks = new ArrayList<>();
//
	@Unique
	public void nEOFORGE1_21$restoreReplacedWaterBlocks(Level world) {
		for (BlockPos pos : nEOFORGE1_21$replacedWaterBlocks) {
			world.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
		}
		// 清空替换过的水方块列表
		nEOFORGE1_21$replacedWaterBlocks.clear();
	}
//
	@Unique
	public void nEOFORGE1_21$checkAndReplaceWaterBlocks(Level world, BlockPos playerPos) {
		int radius = 4; // 3×3范围检索

		for (int yOffset = -3; yOffset <= 30; yOffset++) {
			for (int xOffset = -radius; xOffset <= radius; xOffset++) {
				for (int zOffset = -radius; zOffset <= radius; zOffset++) {
					BlockPos targetPos = playerPos.offset(xOffset, yOffset, zOffset);

					if (nEOFORGE1_21$isWithin3x3(playerPos, targetPos) && world.getBlockState(targetPos).getBlock() == Blocks.WATER) {
						// 如果方块在2x2范围内且是水方块，替换为空气方块
						nEOFORGE1_21$replacedWaterBlocks.add(targetPos);
						world.setBlock(targetPos, Blocks.STRUCTURE_VOID.defaultBlockState(), 3);
					}

					if (!nEOFORGE1_21$isWithin3x3(playerPos, targetPos) & nEOFORGE1_21$replacedWaterBlocks.contains(targetPos)) {
						nEOFORGE1_21$restoreReplacedWaterBlocks(world);
					}
				}
			}
		}
	}
//

	@Unique
	private boolean nEOFORGE1_21$isWithin3x3(BlockPos playerPos, BlockPos targetPos) {
		int deltaX = Math.abs(playerPos.getX() - targetPos.getX());
		int deltaY = Math.abs(playerPos.getY() - targetPos.getY());
		int deltaZ = Math.abs(playerPos.getZ() - targetPos.getZ());

		return deltaX <= 3 && deltaY <= 30 && deltaZ <= 3;
	}
}