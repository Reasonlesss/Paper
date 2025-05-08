package io.papermc.paper.datacomponent.item;

import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.bukkit.damage.DamageType;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Holds data for how an item blocks attacks
 * @see io.papermc.paper.datacomponent.DataComponentTypes#BLOCKS_ATTACKS
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlocksAttacks {

    /**
     * Creates a new {@link BlocksAttacks.Builder} instance.
     *
     * @return a new builder
     */
    @Contract(value = "-> new", pure = true)
    static Builder blocksAttacks() {
        return ItemComponentTypesBridge.bridge().blocksAttacks();
    }

    /**
     * Creates a new {@link DamageReduction} instance.
     *
     * @param horizontalBlockingAngle maximum horizontal angle of the incoming attack for damage to be reduced
     * @param type the types of damage to be reduced
     * @param base the base amount of damage to be reduced
     * @param factor the multiplier
     * @return a new DamageReduction
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static DamageReduction damageReduction(@Positive float horizontalBlockingAngle, @Nullable RegistryKeySet<DamageType> type, float base, float factor) {
        return ItemComponentTypesBridge.bridge().damageReduction(horizontalBlockingAngle, type, base, factor);
    }

    /**
     * Creates a new {@link ItemDamageFunction} instance.
     *
     * @param threshold the minimum amount of damage that must be blocked before being transferred to the item
     * @param base the base amount of item damage
     * @param factor the multiplier of incoming damage to be applied as item damage
     * @return a new ItemDamageFunction
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static ItemDamageFunction itemDamageFunction(float threshold, float base, float factor) {
        return ItemComponentTypesBridge.bridge().itemDamageFunction(threshold, base, factor);
    }

    /**
     * Gets the amount of time a player must hold the item for before it can block damage.
     *
     * @return the block delay in seconds
     */
    float blockDelaySeconds();

    /**
     * Gets the multiplier applied to the cooldown from disabling attacks.
     *
     * @return the disable cooldown scale
     * @see Weapon#disableBlockingForSeconds()
     */
    float disableCooldownScale();

    /**
     * Gets the damage reduction rules.
     *
     * @return unmodifiable list of damage reduction rules
     */
    @Unmodifiable List<DamageReduction> damageReductions();

    /**
     * Gets the item damage function.
     *
     * @return the item damage function
     */
    ItemDamageFunction itemDamage();

    /**
     * Gets the damage types that bypass the blocking.
     *
     * @return the key of the tag holding the respective damage types.
     */
    @Nullable TagKey<DamageType> bypassedBy();

    /**
     * Gets the block sound key.
     *
     * @return the block sound key or null
     */
    @Nullable Key blockSound();

    /**
     * Gets the disable sound key.
     *
     * @return the disable sound key or null
     */
    @Nullable Key disableSound();

    /**
     * Controls how much damage should be reduced during an attack.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface DamageReduction {

        /**
         * Gets the maximum horizontal angle of the incoming attack for damage to be reduced.
         *
         * @return maximum horizontal blocking angle
         */
        @Contract(pure = true)
        float horizontalBlockingAngle();

        /**
         * Gets the types of damage to be reduced.
         *
         * @return types of damage to be reduced or null
         */
        @Contract(pure = true)
        @Nullable RegistryKeySet<DamageType> type();

        /**
         * Gets the base amount of damage to be reduced.
         *
         * @return base damage reduction
         */
        @Contract(pure = true)
        float base();

        /**
         * Gets the multiplier of incoming damage to be applied as item damage.
         *
         * @return damage factor
         */
        @Contract(pure = true)
        float factor();
    }

    /**
     * Controls how much item damage should be applied during an attack.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface ItemDamageFunction {

        /**
         * Gets the minimum amount of damage that must be blocked
         * before damage is applied to the item.
         *
         * @return damage threshold
         */
        @Contract(pure = true)
        @NonNegative float threshold();

        /**
         * Gets the base amount of item damage after the threshold is met.
         *
         * @return base item damage
         */
        @Contract(pure = true)
        float base();

        /**
         * Gets the multiplier applied to incoming damage after the threshold is met.
         *
         * @return damage multiplier
         */
        @Contract(pure = true)
        float factor();
    }

    /**
     * Builder for {@link BlocksAttacks}.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends DataComponentBuilder<BlocksAttacks> {

        /**
         * Sets the amount of time a player must hold this item for before it can block damage.
         *
         * @param delay the block delay in seconds
         * @return the builder for chaining
         * @see #blockDelaySeconds()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder blockDelaySeconds(float delay);

        /**
         * Sets the multiplier applied to the cooldown from disabling attacks.
         *
         * @param scale the cooldown scale
         * @return the builder for chaining
         * @see #disableCooldownScale()
         * @see Weapon.Builder#disableBlockingForSeconds(float)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder disableCooldownScale(float scale);

        /**
         * Adds a damage reduction rule.
         *
         * @param reduction damage reduction rule to add
         * @return the builder for chaining
         * @see #damageReductions()
         * @see #damageReduction(float, RegistryKeySet, float, float)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addDamageReduction(DamageReduction reduction);

        /**
         * Adds damage reduction rules.
         *
         * @param reductions damage reduction rules to add
         * @return the builder for chaining
         * @see #damageReductions()
         * @see #damageReduction(float, RegistryKeySet, float, float)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder addDamageReductions(List<DamageReduction> reductions);

        /**
         * Sets the item damage function.
         *
         * @param function the item damage function
         * @return the builder for chaining
         * @see #itemDamage()
         * @see #itemDamageFunction(float, float, float)
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder itemDamage(ItemDamageFunction function);

        /**
         * Sets the damage types that bypass the blocking.
         *
         * @param bypassedBy the key of the tag holding the respective damage types
         * @return the builder for chaining
         * @see #bypassedBy()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder bypassedBy(@Nullable TagKey<DamageType> bypassedBy);

        /**
         * Sets the block sound key.
         *
         * @param sound the block sound key or null
         * @return the builder for chaining
         * @see #blockSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder blockSound(@Nullable Key sound);

        /**
         * Sets the disable sound key.
         *
         * @param sound the disable sound key or null
         * @return the builder for chaining
         * @see #disableSound()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder disableSound(@Nullable Key sound);
    }
}
