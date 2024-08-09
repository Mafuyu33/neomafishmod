package com.mafuyu33.neomafishmod.mixin.itemmixin.lead;

import com.mafuyu33.neomafishmod.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LeadItem.class)
public class LeadItemMixin extends Item {
	public LeadItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		boolean isLeadCanLinkTogether = Config.isLeadCanLinkTogether();
		if(isLeadCanLinkTogether){
			if (entity instanceof Mob) {
				Mob target = (Mob) entity;
				return attachHeldMobsToMob(user, target);
			}
		}
		return InteractionResult.PASS;
	}

	@Unique
	private static InteractionResult attachHeldMobsToMob(Player player, Mob target) {
		Level world = target.level();
		BlockPos targetPos = target.blockPosition();
		LeashFenceKnotEntity leashKnotEntity = LeashFenceKnotEntity.getOrCreateKnot(world, targetPos);
		leashKnotEntity.playPlacementSound();

		target.setLeashedTo(leashKnotEntity, true);

		world.gameEvent(GameEvent.ENTITY_PLACE, targetPos, GameEvent.Context.of(player));

		return InteractionResult.SUCCESS;
	}
}