package com.minecraftspain.firstmod.init;

import com.minecraftspain.firstmod.FirstMod;
import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {


    public static final ModCreativeTab instance = new ModCreativeTab(0, "tabcreaciones");
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FirstMod.MODID);

    public static final RegistryObject<Item> SMILE = ITEMS.register("smile",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));


    // Teleport
    public static final RegistryObject<Item> TELEPORT_STAFF = ITEMS.register("teletransportador",
            () -> new TeleportStaff(new Item.Properties().tab(instance)));




    public static class TeleportStaff extends Item {
        public TeleportStaff(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
            System.out.println("Estamos pulsando el boton derecho");
            BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
            BlockPos lookPos = ray.getBlockPos().relative(ray.getDirection());
            player.setPos(lookPos.getX(), lookPos.getY(), lookPos.getZ());

            player.getCooldowns().addCooldown(this, 60);

            player.fallDistance = 0F;
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);



            return super.use(world, player, hand);
        }

        protected static BlockHitResult getPlayerPOVHitResult(Level world, Player player, ClipContext.Fluid fluidMode) {
            double range = 15;

            float f = player.getXRot();
            float f1 = player.getYRot();
            Vec3 vector3d = player.getEyePosition(1.0F);
            float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
            float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
            float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
            float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
            float f6 = f3 * f4;
            float f7 = f2 * f4;
            Vec3 vector3d1 = vector3d.add((double)f6 * range, (double)f5 * range, (double)f7 * range);
            return world.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, fluidMode, player));
        }

    }



    public static class ModCreativeTab extends CreativeModeTab {
        private ModCreativeTab(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SMILE.get());
        }
    }

}
