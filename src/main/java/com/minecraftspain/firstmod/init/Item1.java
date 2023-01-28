package com.minecraftspain.firstmod.init;

import com.minecraftspain.firstmod.util.KeyboardHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class Item1 {


    public static class Sustitute extends Item {
        public Sustitute(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
            System.out.println("Boton derecho sustituto");
            BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);

            BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
            world.setBlock(lookPos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 0);
            return super.use(world, player, hand);
        }
    }
        public static class Veneno extends Item {
            public Veneno(Properties properties) {
                super(properties);
            }

            @Override
            public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
                System.out.println("Boton derecho veneno");

                float x = player.getXRot();
                float y = player.getYRot();
                double z = player.getZ();

                player.setDeltaMovement(x, y, z);
                return super.use(world, player, hand);
            }



        }

}
