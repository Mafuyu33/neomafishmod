package com.mafuyu33.neomafishmod.item.custom;

import com.mafuyu33.neomafishmod.effect.ModEffects;
import com.mafuyu33.neomafishmod.enchantment.ModEnchantments;
import com.mafuyu33.neomafishmod.item.component.ModDataComponents;
import com.mafuyu33.neomafishmod.mixinhelper.InjectHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.xml.crypto.Data;
import java.util.List;

public class RubyStuffItem extends Item {
    int timer = 0; //计时器
    private boolean startGoing=false;
    private boolean finishGoing=false;
    private BlockPos firstPos;
    private BlockPos finalPos;
    private GameType gameMode = GameType.SURVIVAL;

    public RubyStuffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack mainHandStack = user.getMainHandItem();
        ItemStack offHandStack = user.getOffhandItem();
        ItemStack itemStack;

        int k = mainHandStack.is(this) ?
                InjectHelper.getEnchantmentLevel(offHandStack, ModEnchantments.GO_TO_SKY) :
                offHandStack.is(this) ?
                        InjectHelper.getEnchantmentLevel(mainHandStack, ModEnchantments.GO_TO_SKY) :
                        0;//检测除了法杖的那只手的附魔
            System.out.println(k);//一直是0
            if ((offHandStack.is(Items.WRITTEN_BOOK)  || mainHandStack.is(Items.WRITTEN_BOOK))) {

                if (k > 0 && checkForSkywardPortal(user).found) {//通天术附魔
                    if (user instanceof ServerPlayer) {
                        firstPos = user.blockPosition();
                        gameMode = getPlayerGameMode(((ServerPlayer) user));
                        MinecraftServer server = user.getServer();
                        // 获取服务器命令调度程序
                        CommandDispatcher<CommandSourceStack> dispatcher = server.createCommandSourceStack().dispatcher();
                        try {
                            // 解析指令并获取命令源
                            ParseResults<CommandSourceStack> parseResults
                                    = dispatcher.parse("gamemode spectator @p", server.createCommandSourceStack());
                            // 执行指令
                            dispatcher.execute(parseResults);

                            // 在控制台输出提示信息
                        } catch (CommandSyntaxException e) {
                            // 指令语法异常处理
                            e.printStackTrace();
                        }

                        // 获取落脚方块的位置
                        finalPos = checkForSkywardPortal(user).landingPos;

                        // 计算玩家到落脚方块的距离
                        double distance = user.blockPosition().distToCenterSqr(Vec3.atCenterOf(finalPos));

                        startGoing = true;


                    }
                } else if (k > 0 && !checkForSkywardPortal(user).found) {
                    user.displayClientMessage(Component.literal((String.valueOf("没有合适的落脚方块释放通天术"))), true);
                }


                //更多的附魔。。。

            }

        if(mainHandStack.getItem()==this){
            itemStack = mainHandStack;
        }else {
            itemStack = offHandStack;
        }

        return InteractionResultHolder.success(itemStack);
    }





    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
        super.onCraftedBy(stack, world, player);
        if(!world.isClientSide()) {




            ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
//            bookStack.applyComponents(ModDataComponents.DATA_COMPONENTS);
//            Component nbt = stack.getOrCreateNbt();
            DataComponentMap.Builder builder = DataComponentMap.builder();


            // 添加书籍的内容
            addPageContent(builder, "§n你好陌生人，愿意和我签订契约，成为魔法少女吗？\n " +
                    "§r§kMafuyu33Mafuyu33\n" +
                    "§r§l使用方法：就是这样喵\n" +
                    "§c在第一页签下你的名字。\n" +
                    "§r§0给此书添加对应附魔，一手法杖一手魔法书心中默念咒语即可释放对应魔法\n" +
                    "\n" +
                    "现已收录的魔法：\n" +
                    "1-通天术"+
                    "2-时间停止（伪）\n" +
                    "通天术：\n" +
                    "似乎是海拉鲁大陆的失传魔法,如果玩家正上方的一定距离内有可穿透物体，就可以施展通天术。\n" +
                    "注意事项：施展通天术的时候请不要左右移动，否则后果自负。");

            DataComponentMap build = builder.build();
            bookStack.applyComponents(build);

            player.getInventory().add(bookStack);
        }
    }

    // 添加内容到书的页面中
    public static void addPageContent(DataComponentMap.Builder builder, String content) {
//        DataComponentMap build = DataComponentMap.builder().set(ModDataComponents.RUBY_STAFF_DES,List.of("123")).build();
        builder.set(ModDataComponents.RUBY_STAFF_DES, List.of(content));

//        // 获取书的NBT数据
//        for (Object o : stack.getOrDefault(ModDataComponents.RUBY_STAFF_DES, build)) {
//
//        }
//
//        // 获取书的页面列表
//        ListTag pagesList = nbt.getSiblings("pages", .STRING);
//
//        // 将内容添加到页面列表中
//        pagesList.add(NbtString.of(content));
//
//        // 更新书的NBT数据中的页面列表
//        nbt.put("pages", pagesList);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof ServerPlayer) {
            Player player = (Player) entity;
            if (startGoing) {
                //每个tick向上移动一段距离
                System.out.println("开始移动");
                player.displayClientMessage(Component.literal((String.valueOf("正在释放通天术"))),true);

                //检查是否到达落脚方块上方
                if (finalPos.getY()<player.blockPosition().getY()) {// 如果到了落脚方块，则停止
                    startGoing=false;
                    finishGoing=true;
                }
                if (player.blockPosition().getY()<firstPos.getY()-1.5){
                    player.teleportTo(firstPos.getX(),firstPos.getY(),firstPos.getZ());
                }
            }
        if (finishGoing) {
            System.out.println("停止移动");
            player.displayClientMessage(Component.literal((String.valueOf("通天术释放结束"))),true);
            MinecraftServer server = player.getServer();
            // 获取服务器命令调度程序
            CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
            if (gameMode == GameType.SURVIVAL) {
                try {
                    // 解析指令并获取命令源
                    ParseResults<CommandSourceStack> parseResults
                            = dispatcher.parse("gamemode survival @p", server.createCommandSourceStack());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            } else if (gameMode == GameType.CREATIVE) {
                try {
                    // 解析指令并获取命令源
                    ParseResults<CommandSourceStack> parseResults
                            = dispatcher.parse("gamemode creative @p", server.createCommandSourceStack());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            } else if (gameMode == GameType.ADVENTURE) {
                try {
                    // 解析指令并获取命令源
                    ParseResults<CommandSourceStack> parseResults
                            = dispatcher.parse("gamemode adventure @p", server.createCommandSourceStack());
                    // 执行指令
                    dispatcher.execute(parseResults);

                    // 在控制台输出提示信息
                } catch (CommandSyntaxException e) {
                    // 指令语法异常处理
                    e.printStackTrace();
                }
            }
                finishGoing=false;
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player playerEntity = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Level world = context.getLevel();
        int k = InjectHelper.getEnchantmentLevel(itemStack, Enchantments.CHANNELING);
        if (k > 0) {//引雷
            LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(context.getLevel());
            if (lightningEntity != null) {
                lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
                lightningEntity.setCause(playerEntity instanceof ServerPlayer ? (ServerPlayer) playerEntity : null);
                context.getLevel().addFreshEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.TRIDENT_THUNDER.value();
                if(playerEntity !=null) {
                    playerEntity.playSound(soundEvent, 5, 1.0F);
                }
            }
        }


        if(playerEntity!=null && playerEntity.hasEffect(ModEffects.FLOWER_EFFECT)) {
            int j = (playerEntity.getEffect(ModEffects.FLOWER_EFFECT)).getAmplifier()+1;
            int radius = 2; // 设置半径，可以根据需要调整
            BlockState flowerState = Blocks.BLUE_ORCHID.defaultBlockState();

            for (int xOffset = -radius; xOffset <= radius; xOffset++) {
                for (int zOffset = -radius; zOffset <= radius; zOffset++) {
                    BlockPos flowerPos = blockPos.offset(xOffset, 1, zOffset); // 在Y轴加1
                    world.setBlock(flowerPos, flowerState, 3); // 在flowerPos处生成花
                }
            }
            if(j>0) {
                clearStatusEffect(playerEntity, ModEffects.FLOWER_EFFECT);
                playerEntity.addEffect(new MobEffectInstance(ModEffects.FLOWER_EFFECT, 3600, j-2));
            }else {
                clearStatusEffect(playerEntity, ModEffects.FLOWER_EFFECT);
            }
        }
        return super.useOn(context);
    }

//    @Override
//    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
//        tooltip.add(Text.translatable("tooltip.mafishmod.ruby_stuff.tooltip"));
//        super.appendTooltip(stack, world, tooltip, context);
//    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.mafishmod.ruby_stuff.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    // 清除特定的药水状态
    public void clearStatusEffect(Player player, Holder<MobEffect> statusEffect) {
            player.removeEffect(statusEffect);
    }

    public static GameType getPlayerGameMode(ServerPlayer player) {
            return player.gameMode.getGameModeForPlayer();
    }


    public static class CheckResult {
        private boolean found; // 是否找到符合条件的方块
        private BlockPos landingPos; // 落脚方块的位置

        // 构造函数
        public CheckResult(boolean found, BlockPos landingPos) {
            this.found = found;
            this.landingPos = landingPos;
        }
    }


    // 检测是否有连续空气方块和落脚方块的方法
    public static CheckResult checkForSkywardPortal(Player player) {
        Level world = player.level();
        BlockPos playerPos = player.blockPosition();

        // 检查头顶 30 格内的方块
        for (int offsetY = 3; offsetY <= 50; offsetY++) {
            BlockPos checkPos = playerPos.above(offsetY);
            BlockPos checkPosAbove = checkPos.above();

            // 检查当前方块和上方方块是否符合条件
            if (world.isEmptyBlock(checkPos) && world.isEmptyBlock(checkPosAbove)
                    &&(!world.isEmptyBlock(checkPos.below()))) {
                System.out.println("找到符合条件的方块");
                return new CheckResult(true, checkPos.below()); // 找到符合条件的方块，返回 true
            }
        }
        System.out.println("未找到符合条件的方块");
        return new CheckResult(false, null); // 未找到符合条件的方块，返回 false
    }
    
}
