package com.minecraftspain.firstmod.init;

import com.minecraftspain.firstmod.FirstMod;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FirstMod.MODID);

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final RegistryObject<Block> SMILE_BLOCK = BLOCKS.register("smile_block",
            () -> new Block(Block.Properties.of(Material.STONE).strength(4f, 1200f).requiresCorrectToolForDrops().lightLevel((state) -> 15)));

    public static final RegistryObject<Block> SAD_BLOCK = BLOCKS.register("sad_block",
            () -> new SadBlock(Block.Properties.copy(Blocks.DIRT)));




    @SubscribeEvent
    public static void onRegisterItems(final RegisterEvent event) {

        LOGGER.info("------------ SUSCRIBIENDO LOS BLOQUES -------------------------");
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
            BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
                Block block = blockRegistryObject.get();
                Item.Properties properties = new Item.Properties().tab(ItemInit.instance);
                Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);
                event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
            });
        }
    }

    public static class SadBlock extends Block {
        public SadBlock(Block.Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResult use(BlockState state, Level world, BlockPos pos,
                                     Player player, InteractionHand hand, BlockHitResult hit) {

            LOGGER.info("------- INTERACCIÓN --------");
            ItemStack held = player.getItemInHand(hand);

            if (!world.isClientSide() &&
                    (held.getItem() == Items.GUNPOWDER || held.getItem()==Items.IRON_PICKAXE)){
                LOGGER.info("----- ESTAMOS DENTRO DE LA EXPLOSION ---------s");
                world.explode(player, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true, Explosion.BlockInteraction.DESTROY);
                held.shrink(1);
                return InteractionResult.CONSUME;
            }

            return super.use(state, world, pos, player, hand, hit);
        }

        @Override
        public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
            LOGGER.info("-------- EXPLOTADO ----------");
            world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true, Explosion.BlockInteraction.DESTROY);
            super.wasExploded(world, pos, explosion);
        }

        @Override
        public boolean canSustainPlant(BlockState state, BlockGetter world,
                                       BlockPos pos, Direction facing, IPlantable plantable) {
            Block plant = plantable.getPlant(world, pos.relative(facing)).getBlock();

            if (plant == Blocks.CACTUS){
                return true;
            } else {
                return super.canSustainPlant(state, world, pos, facing, plantable);
            }
        }
    }
}
