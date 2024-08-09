package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.luoyangshovel;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.badluckofsea.EntityBlockAccessor;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends DiggerItem {

	public ShovelItemMixin(Tier tier, TagKey<Block> blocks, Properties properties) {
		super(tier, blocks, properties);
	}

	@Inject(at = @At("HEAD"), method = "useOn",cancellable = true)
	private void init(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		int k = InjectHelper.getEnchantmentLevel(context.getItemInHand(), Enchantments.POWER);
		if (k > 0) {
			mafishmod$generateFallingBlock(context.getClickedPos(),context.getLevel().getBlockState(context.getClickedPos()), context.getLevel(),k,context.getPlayer());
			cir.setReturnValue(InteractionResult.sidedSuccess(context.getLevel().isClientSide));
		}
	}

	@Unique
	private void mafishmod$generateFallingBlock(BlockPos targetPos , BlockState blockState, Level world , int power, Player user) {
		if(!world.isClientSide()) {
			BlockEntity blockEntity = world.getBlockEntity(targetPos);

			FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);

			fallingBlockEntity.blockState = blockState;
			fallingBlockEntity.time = 1;
			fallingBlockEntity.setNoGravity(false);
			fallingBlockEntity.blocksBuilding = true;
			fallingBlockEntity.setPos(targetPos.getX() + 0.5, targetPos.getY() + 1.2, targetPos.getZ() + 0.5);
			fallingBlockEntity.setDeltaMovement(Vec3.ZERO);
			fallingBlockEntity.xOld= targetPos.getX() + 0.5;
			fallingBlockEntity.yOld = targetPos.getY() + 1.2;
			fallingBlockEntity.zOld = targetPos.getZ() + 0.5;
			fallingBlockEntity.setStartPos(targetPos);
			//设置速度
			mafishmod$launchBlock(targetPos, power, user, fallingBlockEntity);
			//设置伤害
			if (!Objects.equals(BlockEnchantmentStorage.getEnchantmentsAtPosition(targetPos), new ListTag())) {//附魔海之嫌弃
//				TutorialMod.LOGGER.info(String.valueOf("附魔海之嫌弃"));
				fallingBlockEntity.setHurtsEntities(0, -1);
				BlockEnchantmentStorage.removeBlockEnchantment(targetPos.immutable());//删除信息
			} else {
//				TutorialMod.LOGGER.info(String.valueOf("没附魔海之嫌弃"));
				fallingBlockEntity.setHurtsEntities(50, power * 2);
			}
			// 如果方块有附加的 BlockEntity 数据，可以设置 blockEntityData 字段
			if (blockEntity != null) {
				CompoundTag blockEntityData = new CompoundTag();
				((EntityBlockAccessor)blockEntity).saveAdditional(blockEntityData, Objects.requireNonNull(blockEntity.getLevel()).registryAccess());
				fallingBlockEntity.blockData = blockEntityData;
			}

			world.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

			world.addFreshEntity(fallingBlockEntity);
		}

	}

	@Unique
	private static void mafishmod$launchBlock( BlockPos targetPos, int power, Player user, FallingBlockEntity fallingBlockEntity) {
		// 获取用户的位置
		Vec3 userPos = user.position();

		// 获取目标位置并转换为方块中心的浮点坐标
		Vec3 targetVec = new Vec3(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);

		// 计算指向目标位置的向量
		Vec3 direction = targetVec.subtract(userPos).normalize();

		// 将Y轴的分量稍微增加一点
		direction = new Vec3(direction.x, direction.y + 0.2, direction.z).normalize();

		// 根据power调整速度
		Vec3 velocity = direction.scale(power*0.5);

		// 设置方块实体的速度
		fallingBlockEntity.setDeltaMovement(velocity);
	}
}