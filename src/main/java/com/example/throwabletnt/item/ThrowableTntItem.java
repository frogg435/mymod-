package com.example.throwabletnt.item;

import com.example.throwabletnt.ThrowableTnt;
import com.example.throwabletnt.entity.ThrowableTntEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThrowableTntItem extends Item {
    public ThrowableTntItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            ThrowableTntEntity projectile = new ThrowableTntEntity(ThrowableTnt.THROWABLE_TNT_ENTITY.get(), level);
            projectile.setPos(player.getX(), player.getEyeY() - 0.1D, player.getZ());
            projectile.setItem(stack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL, 0.8F, 0.8F);
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
