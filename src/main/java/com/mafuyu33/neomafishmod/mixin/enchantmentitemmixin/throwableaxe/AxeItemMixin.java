package com.mafuyu33.neomafishmod.mixin.enchantmentitemmixin.throwableaxe;

import com.mafuyu33.neomafishmod.Config;
import com.mafuyu33.neomafishmod.entity.custom.FuProjectileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public class AxeItemMixin extends DiggerItem {

    public AxeItemMixin(Tier tier, TagKey<Block> blocks, Properties properties) {
        super(tier, blocks, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        boolean CFG_isFuThrowable = Config.isFuThrowable();
        ItemStack itemStack = player.getItemInHand(usedHand);

        if (CFG_isFuThrowable){
            level.playSound(null,player.getX(),player.getY(),player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL,0.5f,0.4f/(level.getRandom().nextFloat() * 0.4f + 0.8f));
            player.awardStat(Stats.ITEM_USED.get(this));

            if (!level.isClientSide)
            {
                player.awardStat(Stats.ITEM_USED.get(this));
                if(!player.getAbilities().invulnerable){
                    itemStack.hurtAndBreak(1, (ServerLevel) level,player, p->{

                    });
                    player.getInventory().removeItem(itemStack);
                }

                FuProjectileEntity fuProjectileEntity = new FuProjectileEntity(player,level);
                fuProjectileEntity.setItem(itemStack);
                fuProjectileEntity.shootFromRotation(player,player.getXRot(),player.getYRot(),0.0f,1.5f,1.0f);
                level.addFreshEntity(fuProjectileEntity);
            }
            itemStack.consume(1, player);
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }
}
