package com.example.throwabletnt.entity;

import com.example.throwabletnt.ThrowableTnt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrowableTntEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(ThrowableTntEntity.class, EntityDataSerializers.INT);
    private static final String TAG_FUSE = "Fuse";
    public static final int DEFAULT_FUSE_TIME = 100;
    public static volatile float power = 5.0F;

    public ThrowableTntEntity(EntityType<? extends ThrowableTntEntity> type, Level level) {
        super(type, level);
    }

    public ThrowableTntEntity(Level level, double x, double y, double z) {
        super(ThrowableTnt.THROWABLE_TNT_ENTITY.get(), x, y, z, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FUSE, DEFAULT_FUSE_TIME);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setFuse(int fuse) {
        this.entityData.set(DATA_FUSE, fuse);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            int fuse = this.getFuse() - 1;
            this.setFuse(fuse);
            if (fuse <= 0) {
                this.discard();
                this.explode();
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            this.discard();
            this.explode();
        }
    }

    private void explode() {
        if (!this.level().isClientSide) {
            float radius = Math.max(0.0F, Math.min(100.0F, power));
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), radius, false, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ThrowableTnt.THROWABLE_TNT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_FUSE, this.getFuse());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(TAG_FUSE, 99)) {
            this.setFuse(tag.getInt(TAG_FUSE));
        }
    }
}
