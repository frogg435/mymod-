package com.example.throwabletnt.entity;

import com.example.throwabletnt.ThrowableTnt;
import net.minecraft.core.particles.DustParticleOptions;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class HomingTntEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(HomingTntEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TARGET = SynchedEntityData.defineId(HomingTntEntity.class, EntityDataSerializers.INT);
    private static final String TAG_FUSE = "Fuse";
    private static final String TAG_TARGET = "Target";
    public static final int DEFAULT_FUSE_TIME = 240;
    public static final double HOMING_SPEED = 1.6D;
    public static final double HIT_DISTANCE_SQR = 2.25D;

    public HomingTntEntity(EntityType<? extends HomingTntEntity> type, Level level) {
        super(type, level);
    }

    public HomingTntEntity(Level level, double x, double y, double z) {
        super(ThrowableTnt.HOMING_TNT_ENTITY.get(), x, y, z, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FUSE, DEFAULT_FUSE_TIME);
        builder.define(DATA_TARGET, -1);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setFuse(int fuse) {
        this.entityData.set(DATA_FUSE, fuse);
    }

    public void setTarget(Entity target) {
        this.entityData.set(DATA_TARGET, target == null ? -1 : target.getId());
    }

    public Entity getTarget() {
        int id = this.entityData.get(DATA_TARGET);
        return id == -1 ? null : this.level().getEntity(id);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            Entity target = this.getTarget();
            if (target != null && target.isAlive()) {
                Vec3 to = target.getBoundingBox().getCenter().subtract(this.position());
                if (to.lengthSqr() < HIT_DISTANCE_SQR) {
                    this.discard();
                    this.explode();
                    return;
                }
                this.setDeltaMovement(to.normalize().scale(HOMING_SPEED));
            }
            int fuse = this.getFuse() - 1;
            this.setFuse(fuse);
            if (fuse <= 0) {
                this.discard();
                this.explode();
            }
        }
        Vec3 pos = this.position();
        for (int i = 0; i < 3; i++) {
            this.level().addParticle(new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F),
                    pos.x + (this.random.nextDouble() - 0.5D) * 0.3D,
                    pos.y + (this.random.nextDouble() - 0.5D) * 0.3D,
                    pos.z + (this.random.nextDouble() - 0.5D) * 0.3D,
                    0.0D, 0.0D, 0.0D);
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
            float radius = Math.max(0.0F, Math.min(100.0F, ThrowableTntEntity.power));
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), radius, false, Level.ExplosionInteraction.TNT);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ThrowableTnt.HOMING_TNT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_FUSE, this.getFuse());
        tag.putInt(TAG_TARGET, this.entityData.get(DATA_TARGET));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(TAG_FUSE, 99)) {
            this.setFuse(tag.getInt(TAG_FUSE));
        }
        if (tag.contains(TAG_TARGET, 99)) {
            this.entityData.set(DATA_TARGET, tag.getInt(TAG_TARGET));
        }
    }
}
