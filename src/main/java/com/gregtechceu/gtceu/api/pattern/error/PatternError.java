package com.gregtechceu.gtceu.api.pattern.error;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class PatternError {

    protected MultiblockState worldState;

    public void setWorldState(MultiblockState worldState) {
        this.worldState = worldState;
    }

    public Level getWorld() {
        return worldState.getWorld();
    }

    public BlockPos getPos() {
        return worldState.getPos();
    }

    public List<List<ItemStack>> getCandidates() {
        TraceabilityPredicate predicate = worldState.predicate;
        List<List<ItemStack>> candidates = new ArrayList<>();
        for (SimplePredicate common : predicate.common) {
            candidates.add(common.getCandidates());
        }
        for (SimplePredicate limited : predicate.limited) {
            candidates.add(limited.getCandidates());
        }
        return candidates;
    }

    public Component getErrorInfo() {
        List<List<ItemStack>> candidates = getCandidates();
        StringBuilder builder = new StringBuilder();
        var empty = Component.empty();
        for (List<ItemStack> candidate : candidates) {
            if (!candidate.isEmpty()) {
                var items = candidate.stream()
                    .map(ItemStack::getDisplayName)
                    .reduce((a, c) -> Component.empty().append(a).append(c))
                    .orElseGet(Component::empty);
                empty.append(items);
            }
        }
        builder.append("...");
        return Component.translatable("gtceu.multiblock.pattern.error", builder, worldState.getPos());
    }
}
