package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.elytra;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.mixinhelper.ElytraJumpMixinHelper;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot1);

	// 假设这些变量是在类中定义的，以便跨多个ticks记住上一次的位置
	@Unique
	private double lastMainPosY = 0;
	@Unique
	private double lastOffPosY = 0;
	@Unique
	private final double THRESHOLD = 0.01; // 可以调整的阈值

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo ci) {
		ItemStack itemStack = this.getItemBySlot(EquipmentSlot.CHEST);
		if(this.isFallFlying() && InjectHelper.getEnchantmentLevel(itemStack, ModEnchantments.BUTTERFLY) >0){
			Player player =(Player) (Object)this;
//			if (VRPlugin.canRetrieveData(player)) {// todo VR
			if (false){
				// 获取当前的位置
//				float yaw = player.getYaw();
//				float pitch = player.getPitch();
				Vec3 pos = player.position();
//				Vec3 mainPos = VRDataHandler.getMainhandControllerPosition(player); // todo VR
//				Vec3 offPos = VRDataHandler.getOffhandControllerPosition(player);
//				player.sendMessage(Text.literal(("a"+ String.format("%.2f", pos.y))
//						+Text.literal(("b"+String.format("%.2f", mainPos.y))
//						+Text.literal("c"+String.format("%.2f", lastMainPosY))
//						+Text.literal("d"+String.format("%.2f", (pos.y - mainPos.y - lastMainPosY))))),true);
//				if(lastMainPosY!=0 && lastOffPosY!=0) { // todo VR
//					// 检查主手位置变化是否小于阈值
//					if (pos.y - mainPos.y - lastMainPosY > THRESHOLD) {
//						// 添加速度逻辑，向左上添加速度
//						player.addVelocity(0, 0.03, 0); // 以x轴正方向为例
//						System.out.println("addVelocity r");
//					}
//
//					// 检查副手位置变化是否小于阈值
//					if (pos.y - offPos.y - lastOffPosY > THRESHOLD) {
//						// 添加速度逻辑，向右上添加速度，尝试和主手速度合成
//						player.addVelocity(0, 0.03, 0); // 以x轴负方向为例
//						System.out.println("addVelocity l");
//					}
//				}
				// 更新位置记录以用于下一次比较
//				lastMainPosY = pos.y - mainPos.y; // todo VR
//				lastOffPosY = pos.y - offPos.y;

			}else if(ElytraJumpMixinHelper.isJumpKeyPressed()){
				this.setPose(Pose.STANDING);
				this.push(0, 0.06, 0);
			}
		}
	}

}