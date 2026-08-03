package com.example.throwabletnt.item;

import com.example.throwabletnt.ThrowableTnt;
import com.example.throwabletnt.entity.HomingTntEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class HomingTntItem extends Item {
    private static final double PICK_RANGE = 64.0D;

    public HomingTntItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            HomingTntEntity projectile = new HomingTntEntity(ThrowableTnt.HOMING_TNT_ENTITY.get(), level);
            projectile.setPos(player.getX(), player.getEyeY() - 0.1D, player.getZ());
            projectile.setItem(stack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
            Entity target = pickTarget(level, player);
            if (target != null) {
                projectile.setTarget(target);
            }
            level.addFreshEntity(projectile);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL, 0.8F, 0.8F);
        }
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Nullable
    private static Entity pickTarget(Level level, Player player) {
        Vec3 from = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();
        Vec3 to = from.add(look.scale(PICK_RANGE));
        BlockHitResult blockHit = player.pick(PICK_RANGE, 1.0F, false);
        double maxDistSqr = blockHit.getType() == HitResult.Type.MISS ? PICK_RANGE * PICK_RANGE
                : from.distanceToSqr(blockHit.getLocation());
        Entity result = null;
        for (Entity entity : level.getEntities(player, player.getBoundingBox().inflate(PICK_RANGE),
                e -> e.isAlive() && e instanceof LivingEntity)) {
            Optional<Vec3> hit = entity.getBoundingBox().clip(from, to);
            if (hit.isPresent()) {
                double distSqr = from.distanceToSqr(hit.get());
                if (distSqr < maxDistSqr) {
                    maxDistSqr = distSqr;
                    result = entity;
                }
            }
        }
        return result;
    }
}
