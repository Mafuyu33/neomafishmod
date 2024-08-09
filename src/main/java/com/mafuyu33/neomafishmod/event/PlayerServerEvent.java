package com.mafuyu33.neomafishmod.event;

import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.item.ModItems;
import com.mafuyu33.neomafishmod.network.packet.S2C.NeverGonnaS2CPacket;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

@EventBusSubscriber
public class PlayerServerEvent {


    @SubscribeEvent
    public static void onEntityAttack(AttackEntityEvent event){
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level world = player.level();
        if(player.isHolding(ModItems.LIGHTNING_ITEM.get()) && !world.isClientSide){
            BlockPos blockPos = target.blockPosition();
            LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(target.level());
            if (lightningEntity != null) {
                lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
                lightningEntity.setCause(player instanceof ServerPlayer ? (ServerPlayer) player : null );
                target.level().addFreshEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.LIGHTNING_BOLT_IMPACT;
                player.playSound(soundEvent, 5, 1.0F);
            }
        }

        if (target instanceof Chicken && !world.isClientSide){
            player.sendSystemMessage(Component.literal("哎呦你干嘛"));
        }

        //TODO VR
//        if(!world.isClient && VrMagicItem.isUsingMagic){
//            BlockPos blockPos = entity.getBlockPos();
//            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(entity.getWorld());
//            if (lightningEntity != null) {
//                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
//                lightningEntity.setChanneler(player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player : null);
//                entity.getWorld().spawnEntity(lightningEntity);
//                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
//                entity.playSound(soundEvent, 5, 1.0F);
//            }
//            entity.kill();
//        }
    }



    @SubscribeEvent
    public static void onPlayerAttackBlock(BlockEvent.BreakEvent event){
        Player player = event.getPlayer();
        BlockPos useBlock = event.getPos();
        BlockState state = player.level().getBlockState(useBlock);

        InteractionHand hand = player.getUsedItemHand();
        ItemStack itemStack = player.getItemInHand(hand);
        int i = getEnchantmentLevel(itemStack, ModEnchantments.NEVER_GONNA);//如果手上的物品有被附魔 “你被骗了”

        if(i>0 && (state.is(BlockTags.DIAMOND_ORES)) && !player.level().isClientSide){
//            PacketByteBuf buf = PacketByteBufs.create();//传输到client端
//            ServerPlayNetworking.send((ServerPlayerEntity) player, ModMessages.NEVER_GONNA_ID, buf);
            PacketDistributor.sendToAllPlayers(new NeverGonnaS2CPacket());
//            //检测pos边上是不是有钻石掉落物，有的话替换成煤炭
            startDelayedOperation(player.level(),player,useBlock);
        }

    }

    private static int getEnchantmentLevel(ItemStack item, ResourceKey<Enchantment> enchantmentResourceKey){
        ItemEnchantments itemEnchantments = item.get(DataComponents.ENCHANTMENTS);
        if (itemEnchantments == null) {
            return -1;
        }
        Optional<Object2IntMap.Entry<Holder<Enchantment>>> levelOptional = itemEnchantments.entrySet().stream().filter(int2Enchatment -> int2Enchatment.getKey().is(enchantmentResourceKey)).findFirst();
        return levelOptional.map(Object2IntMap.Entry::getIntValue).orElse(-1);
    }

    public static void replaceDiamondsWithCoal(Level world, BlockPos pos) {
        // 创建一个立方体区域，以给定坐标为中心，半径为1
        AABB box = new AABB(pos).inflate(1.0);

        // 获取指定区域内的所有 ItemEntity
        List<ItemEntity> itemEntities = world.getEntities(EntityType.ITEM, box, itemEntity -> {
            // 检查实体持有的物品是否为钻石
            return itemEntity.getItem().getItem() == Items.DIAMOND;
        });

        // 遍历所有 ItemEntity
        for (ItemEntity itemEntity : itemEntities) {
            // 替换成煤炭
            itemEntity.setItem(new ItemStack(Items.COAL));
        }
    }
    private static void startDelayedOperation(Level world, Player player, BlockPos pos) {
        if (world.getServer() != null) {
            world.getServer().execute(() -> {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        replaceDiamondsWithCoal(world,pos);
                    }
                }, 200); // 延迟0.2秒执行，单位为毫秒
            });
        }
    }


    @SubscribeEvent
    public static void onPlayerAttack(PlayerInteractEvent.LeftClickBlock event){
        BlockPos startPos = null;

        Player player = event.getEntity();
        Level world  = event.getLevel();
        InteractionHand hand = event.getHand();
        BlockPos pos = event.getPos();
        if(!world.isClientSide) {
            Iterable<ItemStack> handItemStacks = player.getAllSlots();
            for (ItemStack itemstack : handItemStacks) {
                if (itemstack.is(Items.BRUSH)) {
                    if (itemstack.isEnchanted()) {//有附魔，全图获取
                        if (startPos == null) {
                            startPos = pos ;
                        } else {
                            brushAllBlocks(world,startPos, pos, itemstack);
                            startPos = null;
                        }
                    } else {//没附魔，清除附魔方块
                        if (startPos == null) {
                            startPos = pos;
                        } else {
                            clearAllBlocks(world,startPos, pos);
                            startPos = null;
                        }
                    }
//                    return InteractionResult.SUCCESS;
                }
            }
        }
//        return ActionResult.PASS;
    }


    private static void brushAllBlocks(Level world, BlockPos startPos, BlockPos pos, ItemStack itemStack) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), pos.getX());
        int minY = Math.min(startPos.getY(), pos.getY());
        int minZ = Math.min(startPos.getZ(), pos.getZ());
        int maxX = Math.max(startPos.getX(), pos.getX());
        int maxY = Math.max(startPos.getY(), pos.getY());
        int maxZ = Math.max(startPos.getZ(), pos.getZ());

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);

                    // 排除空气、水、岩浆等特定方块
                    if (blockState.is(Blocks.AIR) ||
                            blockState.is(Blocks.WATER) ||
                            blockState.is(Blocks.LAVA)) {
                        continue;
                    }

                    // 在这里对满足条件的方块进行处理
                    ItemEnchantments itemEnchantments = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

                    ListTag enchantmentNbtList = new ListTag();
                    Set<Object2IntMap.Entry<Holder<Enchantment>>> entries = itemEnchantments.entrySet();
                    for (Object2IntMap.Entry<Holder<Enchantment>> entry : entries) {
                        Holder<Enchantment> key = entry.getKey();
                        int intValue = entry.getIntValue();


                        CompoundTag enchantmentNbt = new CompoundTag();
                        enchantmentNbt.putString("id",String.valueOf(key.getKey()));
                        enchantmentNbt.putInt("lvl",intValue);
                        enchantmentNbtList.add(enchantmentNbt);

                    }
                    BlockEnchantmentStorage.addBlockEnchantment(currentPos, enchantmentNbtList);
                }
            }
        }
    }

    private static void clearAllBlocks(Level world,BlockPos startPos, BlockPos pos) {
        // 获取立方体对角方块的坐标
        int minX = Math.min(startPos.getX(), pos.getX());
        int minY = Math.min(startPos.getY(), pos.getY());
        int minZ = Math.min(startPos.getZ(), pos.getZ());
        int maxX = Math.max(startPos.getX(), pos.getX());
        int maxY = Math.max(startPos.getY(), pos.getY());
        int maxZ = Math.max(startPos.getZ(), pos.getZ());

        // 遍历立方体内的所有方块
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = world.getBlockState(currentPos);

                    // 在这里对满足条件的方块进行处理
                    BlockEnchantmentStorage.removeBlockEnchantment(currentPos);
//                    ExampleMod.LOGGER.info("Found block: " + blockState.getBlock().getTranslationKey() + " at " + currentPos);
                }
            }
        }
    }


    /**
     *  UseBlockHandler
     */
    public static boolean isButtonUsed = false;
    @SubscribeEvent
    public static void UseBlockHandler(UseItemOnBlockEvent event){
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!level.isClientSide)
        {
            if(state.is(BlockTags.BUTTONS)){
                isButtonUsed = true;
            }
        }
    }
}
