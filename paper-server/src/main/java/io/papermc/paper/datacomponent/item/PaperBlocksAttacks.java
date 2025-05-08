package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.papermc.paper.util.MCUtil;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageType;
import org.jspecify.annotations.Nullable;

public record PaperBlocksAttacks(
    net.minecraft.world.item.component.BlocksAttacks impl
) implements BlocksAttacks, Handleable<net.minecraft.world.item.component.BlocksAttacks> {

    @Override
    public net.minecraft.world.item.component.BlocksAttacks getHandle() {
        return this.impl;
    }

    @Override
    public float blockDelaySeconds() {
        return this.impl.blockDelaySeconds();
    }

    @Override
    public float disableCooldownScale() {
        return this.impl.disableCooldownScale();
    }

    @Override
    public List<DamageReduction> damageReductions() {
        return MCUtil.transformUnmodifiable(this.impl.damageReductions(), DamageReductionImpl::new);
    }

    @Override
    public ItemDamageFunction itemDamage() {
        return new ItemDamageFunctionImpl(this.impl.itemDamage());
    }

    @Override
    public @Nullable TagKey<DamageType> bypassedBy() {
        final Optional<TagKey<DamageType>> tagKey = this.impl.bypassedBy().map(PaperRegistries::fromNms);
        return tagKey.orElse(null);
    }

    @Override
    public @Nullable Key blockSound() {
        return this.impl.blockSound().map(holder -> PaperAdventure.asAdventure(holder.value().location())).orElse(null);
    }

    @Override
    public @Nullable Key disableSound() {
        return this.impl.disableSound().map(holder -> PaperAdventure.asAdventure(holder.value().location())).orElse(null);
    }

    public record DamageReductionImpl(
        net.minecraft.world.item.component.BlocksAttacks.DamageReduction impl
    ) implements BlocksAttacks.DamageReduction, Handleable<net.minecraft.world.item.component.BlocksAttacks.DamageReduction> {

        @Override
        public float horizontalBlockingAngle() {
            return this.impl.horizontalBlockingAngle();
        }

        @Override
        public @Nullable RegistryKeySet<DamageType> type() {
            return this.impl.type()
                .map(set -> PaperRegistrySets.convertToApi(RegistryKey.DAMAGE_TYPE, set))
                .orElse(null);
        }

        @Override
        public float base() {
            return this.impl.base();
        }

        @Override
        public float factor() {
            return this.impl.factor();
        }

        @Override
        public net.minecraft.world.item.component.BlocksAttacks.DamageReduction getHandle() {
            return this.impl;
        }

        public static net.minecraft.world.item.component.BlocksAttacks.DamageReduction toNms(DamageReduction reduction) {
            if (reduction instanceof DamageReductionImpl damageReduction) {
                return damageReduction.impl;
            }
            throw new UnsupportedOperationException();
        }
    }

    public record ItemDamageFunctionImpl(
        net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction impl
    ) implements BlocksAttacks.ItemDamageFunction, Handleable<net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction> {

        @Override
        public float threshold() {
            return this.impl.threshold();
        }

        @Override
        public float base() {
            return this.impl.base();
        }

        @Override
        public float factor() {
            return this.impl.factor();
        }

        @Override
        public net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction getHandle() {
            return this.impl;
        }

        public static net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction toNms(ItemDamageFunction function) {
            if (function instanceof ItemDamageFunctionImpl damageFunction) {
                return damageFunction.impl;
            }
            throw new UnsupportedOperationException();
        }
    }

    static final class BuilderImpl implements Builder {

        private float blockDelaySeconds;
        private float disableCooldownScale = 1.0F;
        private final List<net.minecraft.world.item.component.BlocksAttacks.DamageReduction> damageReductions = new ArrayList<>();
        private net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction damageFunction = net.minecraft.world.item.component.BlocksAttacks.ItemDamageFunction.DEFAULT;
        private Optional<net.minecraft.tags.TagKey<net.minecraft.world.damagesource.DamageType>> bypassedBy = Optional.empty();
        private Optional<Holder<SoundEvent>> blockSound = Optional.empty();
        private Optional<Holder<SoundEvent>> disableSound = Optional.empty();

        @Override
        public Builder blockDelaySeconds(final float delay) {
            Preconditions.checkArgument(delay >= 0, "delay must be non-negative, was %s", delay);
            this.blockDelaySeconds = delay;
            return this;
        }

        @Override
        public Builder disableCooldownScale(final float scale) {
            Preconditions.checkArgument(scale >= 0, "scale must be non-negative, was %s", scale);
            this.disableCooldownScale = scale;
            return this;
        }

        @Override
        public Builder addDamageReduction(final DamageReduction reduction) {
            this.damageReductions.add(DamageReductionImpl.toNms(reduction));
            return this;
        }

        @Override
        public Builder addDamageReductions(final List<DamageReduction> reductions) {
            for (final DamageReduction reduction : reductions) {
                this.damageReductions.add(DamageReductionImpl.toNms(reduction));
            }
            return this;
        }

        @Override
        public Builder itemDamage(final ItemDamageFunction function) {
            this.damageFunction = ItemDamageFunctionImpl.toNms(function);
            return this;
        }

        @Override
        public Builder bypassedBy(@Nullable final TagKey<DamageType> bypassedBy) {
            this.bypassedBy = Optional.ofNullable(bypassedBy).map(PaperRegistries::toNms);
            return this;
        }

        @Override
        public Builder blockSound(@Nullable final Key sound) {
            this.blockSound = Optional.ofNullable(sound).map(PaperAdventure::resolveSound);
            return this;
        }

        @Override
        public Builder disableSound(@Nullable final Key sound) {
            this.disableSound = Optional.ofNullable(sound).map(PaperAdventure::resolveSound);
            return this;
        }

        @Override
        public BlocksAttacks build() {
            return new PaperBlocksAttacks(new net.minecraft.world.item.component.BlocksAttacks(
                this.blockDelaySeconds,
                this.disableCooldownScale,
                this.damageReductions,
                this.damageFunction,
                this.bypassedBy,
                this.blockSound,
                this.disableSound
            ));
        }
    }
}
