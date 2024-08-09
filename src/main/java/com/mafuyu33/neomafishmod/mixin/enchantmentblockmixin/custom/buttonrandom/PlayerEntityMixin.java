package com.mafuyu33.neomafishmod.mixin.enchantmentblockmixin.custom.buttonrandom;

import com.mafuyu33.neomafishmod.enchantmentblock.BlockEnchantmentStorage;
import com.mafuyu33.neomafishmod.event.PlayerServerEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.stream.StreamSupport;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
    @Shadow
    public abstract void displayClientMessage(Component chatComponent, boolean actionBar);

    @Shadow public abstract Inventory getInventory();

    @Shadow public abstract double entityInteractionRange();

    @Shadow public abstract ItemStack eat(Level level, ItemStack food, FoodProperties foodProperties);

    @Shadow public abstract boolean addItem(ItemStack stack);

    @Unique
    Holder<Enchantment> randomPositiveEnchantment;
    @Unique
    Holder<Enchantment> randomNegativeEnchantment;

    @Unique
    Item randomItem;
    @Unique
    ItemStack randomItemStack;
    @Unique
    Holder<MobEffect> randomPositiveEffect;
    @Unique
    Holder<MobEffect> randomNegativeEffect;
    @Unique
    int flag = -1;
    @Unique
    int buttonCount = 0;
    @Unique
    int randomNumber = (random.nextInt(10) + 1);
    @Unique
    String goodEvent;
    @Unique
    String badEvent;


    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info) {
        if (!level().isClientSide) {
            if(goodEvent==null||badEvent==null){//初始化
                goodEvent = getRandomGoodEvent();
                badEvent = getRandomBadEvent();
            }
            // 获取玩家的位置和视线方向
            Vec3 playerPos = this.getEyePosition(1.0F);
            Vec3 playerLook = this.getViewVector(1.0F);
            // 设置射线起点和方向
            Vec3 rayEnd = playerPos.add(playerLook.scale(10)); // 假设射线长度为 10

            // 进行射线投射
            BlockHitResult blockHitResult = level().clip(new ClipContext(playerPos, rayEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));

            // 检查是否击中按钮方块
            if (level().getBlockState(blockHitResult.getBlockPos()).is(BlockTags.BUTTONS)
                    && !PlayerServerEvent.isButtonUsed && BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,blockHitResult.getBlockPos())>0) {
                //如果击中，持续向玩家输送文字
                this.displayClientMessage(
                        Component.literal("获得 ")
                                .append(Component.literal(goodEvent))
                                .append(" ")
                                .append(Component.literal(toRoman(randomNumber)))
                                .append(", 但代价是 ")
                                .append(Component.literal(badEvent).withStyle(ChatFormatting.RED)),
                        true
                );
            }
            if (PlayerServerEvent.isButtonUsed && BlockEnchantmentStorage.getLevel(Enchantments.INFINITY,blockHitResult.getBlockPos())>0) {
                this.displayClientMessage(Component.literal("实现了..."),true);
                if(buttonCount==0) {//只有第一次会触发
                    // 触发好事件
                    executeEvent(randomNumber);
                    //重新获得新的好事件和坏事件
                    goodEvent = getRandomGoodEvent();
                    badEvent = getRandomBadEvent();
                }
                buttonCount++;
            }
            if (buttonCount >= 30) {
                buttonCount = 0;
                PlayerServerEvent.isButtonUsed = false;
                randomNumber = (random.nextInt(10) + 1);
            }
        }
    }
    @Unique
    private void executeEvent(int randomNumber) {
        switch (flag) {
            case 0:
                //applyRandomPositiveEffect
                this.addEffect(new MobEffectInstance(randomPositiveEffect, 100*randomNumber, randomNumber-1)); // 持续时间和等级可以根据需要调整
                this.addEffect(new MobEffectInstance(randomNegativeEffect, 100*randomNumber, randomNumber-1)); // 持续时间和等级可以根据需要调整
                break;
            case 1:
                // grantRandomEnchantment
                Player player = (Player) (Object) this; // 获取当前玩家实体
                ItemStack heldItem = player.getMainHandItem(); // 获取玩家手上的物品
                heldItem.enchant(randomPositiveEnchantment, randomNumber);
                heldItem.enchant(randomNegativeEnchantment, randomNumber);
                break;
            case 2:
                // grantRandomItem
                randomItemStack.setCount(randomNumber);
                if (!this.getInventory().add(randomItemStack)) {
                    this.spawnAtLocation(randomItem);
                }
                clearRandomItems(randomNumber,randomItemStack);

                // grantRandomItem
                break;
            case -1:
                break;
            default:
                // 处理未知事件
                break;
        }
    }
    @Unique
    private String getRandomGoodEvent() {
//        List<MobEffect> POSITIVE_EFFECTS = StreamSupport.stream(BuiltInRegistries.MOB_EFFECT.spliterator(), false)
//                .filter(effect -> effect.getCategory() == MobEffectCategory.BENEFICIAL || effect.getCategory() == MobEffectCategory.NEUTRAL)
//                .toList();
        Optional<HolderLookup.RegistryLookup<MobEffect>> mobEffectRegistryLookup = level().registryAccess().lookup(Registries.MOB_EFFECT);
        HolderLookup.RegistryLookup<MobEffect> mobEffectRegistryLookup1 = mobEffectRegistryLookup.get();
        List<Holder.Reference<MobEffect>> POSITIVE_EFFECTS = mobEffectRegistryLookup1.listElements().filter(mobEffectReference -> mobEffectReference.value().getCategory() == MobEffectCategory.BENEFICIAL || mobEffectReference.value().getCategory() == MobEffectCategory.NEUTRAL).toList();


//        List<Enchantment> POSITIVE_ENCHANTMENTS = StreamSupport.stream(BuiltInRegistries.ENCHANTMENT.spliterator(), false)
//                .filter(enchantment -> !enchantment.isCursed())
//                .toList();

        Optional<HolderLookup.RegistryLookup<Enchantment>> lookup = level().registryAccess().lookup(Registries.ENCHANTMENT);
        HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lookup.get();
        List<Holder.Reference<Enchantment>> POSITIVE_ENCHANTMENTS = enchantmentRegistryLookup.listElements().filter(enchantmentReference -> !enchantmentReference.is(EnchantmentTags.CURSE)).toList();


        List<Item> RANDOM_ITEMS = StreamSupport.stream(BuiltInRegistries.ITEM.spliterator(), false)
                .filter(item -> item.getFoodProperties(item.getDefaultInstance(), null) == null)
                .toList();

        int randomChoice = random.nextInt(4);
        if (randomChoice == 0) {
            flag = 0;
            return applyRandomPositiveEffect(POSITIVE_EFFECTS);
        } else if (randomChoice == 1) {
            flag = 1;
            return applyRandomPositiveEnchantment(POSITIVE_ENCHANTMENTS);
        } else {
            flag = 2;
            return grantRandomItem(RANDOM_ITEMS);
        }
    }
    @Unique
    private String getRandomBadEvent() {
//        List<MobEffect> NEGATIVE_EFFECTS = StreamSupport.stream(BuiltInRegistries.MOB_EFFECT.spliterator(), false)
//                .filter(effect -> effect.getCategory() == MobEffectCategory.HARMFUL)
//                .toList();

        Optional<HolderLookup.RegistryLookup<MobEffect>> mobEffectRegistryLookup = level().registryAccess().lookup(Registries.MOB_EFFECT);
        HolderLookup.RegistryLookup<MobEffect> mobEffectRegistryLookup1 = mobEffectRegistryLookup.get();
        List<Holder.Reference<MobEffect>> NEGATIVE_EFFECTS = mobEffectRegistryLookup1.listElements().filter(mobEffectReference -> mobEffectReference.value().getCategory() == MobEffectCategory.HARMFUL).toList();



//        List<Enchantment> NEGATIVE_ENCHANTMENTS = StreamSupport.stream(Registries.ENCHANTMENT.spliterator(), false)
//                .filter(Enchantment::isCursed)
//                .toList();
        Optional<HolderLookup.RegistryLookup<Enchantment>> lookup = level().registryAccess().lookup(Registries.ENCHANTMENT);
        HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = lookup.get();
        List<Holder.Reference<Enchantment>> NEGATIVE_ENCHANTMENTS = enchantmentRegistryLookup.listElements().filter(enchantmentReference -> enchantmentReference.is(EnchantmentTags.CURSE)).toList();


        if (flag == 0) {
            return "获得"+applyRandomNegativeEffect(NEGATIVE_EFFECTS);
        } else if (flag == 1) {
            return "获得"+applyRandomNegativeEnchantment(NEGATIVE_ENCHANTMENTS);
        } else if(flag == 2){
            return "献祭相同数量的物品";
        }else {
            return "null";
        }
    }
    @Unique
    private String applyRandomPositiveEnchantment(List<Holder.Reference<Enchantment>> POSITIVE_ENCHANTMENTS) {
        if (!POSITIVE_ENCHANTMENTS.isEmpty()) {
            randomPositiveEnchantment = POSITIVE_ENCHANTMENTS.get(random.nextInt(POSITIVE_ENCHANTMENTS.size()));
            // 创建不包含等级的附魔名称
            // todo maybe not the translate key
            MutableComponent enchantmentName = Component.translatable(randomPositiveEnchantment.getKey().toString());
            return enchantmentName.getString();
        }
        return "一个正面附魔";
    }

    @Unique
    private String applyRandomNegativeEnchantment(List<Holder.Reference<Enchantment>> NEGATIVE_ENCHANTMENTS) {
        if (!NEGATIVE_ENCHANTMENTS.isEmpty()) {
            randomNegativeEnchantment = NEGATIVE_ENCHANTMENTS.get(random.nextInt(NEGATIVE_ENCHANTMENTS.size()));
            // 创建不包含等级的附魔名称
            MutableComponent enchantmentName = Component.translatable(randomNegativeEnchantment.getKey().toString());
            return enchantmentName.getString();
        }
        return "一个反面附魔";
    }
    @Unique
    private String applyRandomPositiveEffect(List<Holder.Reference<MobEffect>> POSITIVE_EFFECTS ) {
        if (!POSITIVE_EFFECTS.isEmpty()) {
            randomPositiveEffect = POSITIVE_EFFECTS.get(random.nextInt(POSITIVE_EFFECTS.size()));
            return randomPositiveEffect.value().getDisplayName().getString();
        }
        return "一个正面效果";
    }
    @Unique
    private String applyRandomNegativeEffect(List<Holder.Reference<MobEffect>> NEGATIVE_EFFECTS ) {
        if (!NEGATIVE_EFFECTS.isEmpty()) {
            randomNegativeEffect = NEGATIVE_EFFECTS.get(random.nextInt(NEGATIVE_EFFECTS.size()));
            return randomNegativeEffect.value().getDisplayName().getString();
        }
        return "一个负面效果";
    }
    @Unique
    private void clearRandomItems(int itemCount, ItemStack priorityItem) {
        Player player = (Player) (Object) this; // 获取当前玩家实体
        List<ItemStack> inventory = player.getInventory().items; // 获取玩家物品栏

        Random random = new Random();
        int itemsCleared = 0;

        // 清除其他随机物品
        while (itemsCleared < itemCount) {
            int slot = random.nextInt(inventory.size());
            ItemStack stack = inventory.get(slot);

            if (!stack.isEmpty() && !stack.is(priorityItem.getItem())) {
                int removeCount = Math.min(stack.getCount(), itemCount - itemsCleared);
                stack.shrink(removeCount);
                itemsCleared += removeCount;

                if (stack.isEmpty()) {
                    inventory.set(slot, ItemStack.EMPTY); // 将堆栈设置为空，而不是移除
                }
            }

            // 如果所有非优先物品已检查完，跳出循环
            if (itemsCleared < itemCount) {
                boolean hasNonPriorityItems = false;
                for (ItemStack itemStack : inventory) {
                    if (!itemStack.isEmpty() && !itemStack.is(priorityItem.getItem())) {
                        hasNonPriorityItems = true;
                        break;
                    }
                }
                if (!hasNonPriorityItems) {
                    break;
                }
            }
        }

        // 如果还有剩余的数量需要清除，最后清除指定的物品
        if (itemsCleared < itemCount) {
            for (int i = 0; i < inventory.size() && itemsCleared < itemCount; i++) {
                ItemStack stack = inventory.get(i);

                if (stack.is(priorityItem.getItem())) {
                    int removeCount = Math.min(stack.getCount(), itemCount - itemsCleared);
                    stack.shrink(removeCount);
                    itemsCleared += removeCount;

                    if (stack.isEmpty()) {
                        inventory.set(i, ItemStack.EMPTY); // 将堆栈设置为空，而不是移除
                    }
                }
            }
        }
    }

    @Unique
    private String grantRandomItem( List<Item> RANDOM_ITEMS) {
        if (!RANDOM_ITEMS.isEmpty()) {
            randomItem = RANDOM_ITEMS.get(random.nextInt(RANDOM_ITEMS.size()));
            randomItemStack = new ItemStack(randomItem);
            return randomItem.getDescription().getString();
        }
        return "一个随机物品";
    }

    @Unique
    private static final Map<Integer, String> romanMap = new HashMap<>();

    static {
        romanMap.put(1, "I");
        romanMap.put(2, "II");
        romanMap.put(3, "III");
        romanMap.put(4, "IV");
        romanMap.put(5, "V");
        romanMap.put(6, "VI");
        romanMap.put(7, "VII");
        romanMap.put(8, "VIII");
        romanMap.put(9, "IX");
        romanMap.put(10, "X");
    }

    @Unique
    private static String toRoman(int number) {
        return romanMap.getOrDefault(number, "");
    }
}
