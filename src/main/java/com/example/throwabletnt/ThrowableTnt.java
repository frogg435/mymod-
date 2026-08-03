package com.example.throwabletnt;

import com.example.throwabletnt.entity.HomingTntEntity;
import com.example.throwabletnt.entity.ThrowableTntEntity;
import com.example.throwabletnt.item.HomingTntItem;
import com.example.throwabletnt.item.ThrowableTntItem;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(ThrowableTnt.MODID)
public class ThrowableTnt {
    public static final String MODID = "throwabletnt";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

    public static final DeferredHolder<Item, ThrowableTntItem> THROWABLE_TNT = ITEMS.register("throwable_tnt",
            () -> new ThrowableTntItem(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, HomingTntItem> HOMING_TNT = ITEMS.register("homing_tnt",
            () -> new HomingTntItem(new Item.Properties().stacksTo(16)));

    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableTntEntity>> THROWABLE_TNT_ENTITY =
            ENTITY_TYPES.register("throwable_tnt",
                    () -> EntityType.Builder.of(ThrowableTntEntity::new, MobCategory.MISC)
                            .sized(0.6F, 0.6F).clientTrackingRange(8).updateInterval(2).build("throwable_tnt"));
    public static final DeferredHolder<EntityType<?>, EntityType<HomingTntEntity>> HOMING_TNT_ENTITY =
            ENTITY_TYPES.register("homing_tnt",
                    () -> EntityType.Builder.of(HomingTntEntity::new, MobCategory.MISC)
                            .sized(0.6F, 0.6F).clientTrackingRange(8).updateInterval(2).build("homing_tnt"));

    public ThrowableTnt(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(this::buildCreativeTabContents);
        LOGGER.info("[throwabletnt] 模组加载完成 (v1.1.0)");
    }

    private void buildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(THROWABLE_TNT.get());
            event.accept(HOMING_TNT.get());
        }
    }
}
