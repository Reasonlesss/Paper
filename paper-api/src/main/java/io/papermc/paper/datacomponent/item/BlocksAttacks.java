package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;

@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlocksAttacks {

    @Contract(value = "-> new", pure = true)
    static Builder blocksAttacks() {
        return ItemComponentTypesBridge.bridge().blocksAttacks();
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static DamageReduction damageReduction(float horizontalBlockingAngle, @Nullable RegistryKeySet<DamageType> type, float base, float factor) {
        return ItemComponentTypesBridge.bridge().damageReduction(horizontalBlockingAngle, type, base, factor);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static ItemDamageFunction itemDamageFunction(float threshold, float base, float factor) {
        return ItemComponentTypesBridge.bridge().itemDamageFunction(threshold, base, factor);
    }

    float blockDelaySeconds();

    float disableCooldownScale();

    @Unmodifiable List<DamageReduction> damageReductions();

    ItemDamageFunction itemDamage();

    @Nullable TagKey<DamageType> bypassedBy();

    @Nullable Key blockSound();

    @Nullable Key disableSound();

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface DamageReduction {

        @Contract(pure = true)
        float horizontalBlockingAngle();

        @Contract(pure = true)
        @Nullable RegistryKeySet<DamageType> type();

        @Contract(pure = true)
        float base();

        @Contract(pure = true)
        float factor();
    }

    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface ItemDamageFunction {

        @Contract(pure = true)
        float threshold();

        @Contract(pure = true)
        float base();

        @Contract(pure = true)
        float factor();
    }

    /**
     * Builder for {@link BlocksAttacks}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BlocksAttacks> {

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockDelaySeconds(float delay);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableCooldownScale(float scale);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addDamageReduction(DamageReduction reduction);

        @Contract(value = "_ -> this", mutates = "this")
        Builder addDamageReductions(List<DamageReduction> reductions);

        @Contract(value = "_ -> this", mutates = "this")
        Builder itemDamage(ItemDamageFunction function);

        @Contract(value = "_ -> this", mutates = "this")
        Builder bypassedBy(@Nullable TagKey<DamageType> bypassedBy);

        @Contract(value = "_ -> this", mutates = "this")
        Builder blockSound(@Nullable Key sound);

        @Contract(value = "_ -> this", mutates = "this")
        Builder disableSound(@Nullable Key sound);
    }
}
