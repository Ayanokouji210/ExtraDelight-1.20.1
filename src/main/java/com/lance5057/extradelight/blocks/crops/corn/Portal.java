package com.lance5057.extradelight.blocks.crops.corn;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public interface Portal {
    default int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return 0;
    }

    @Nullable
    CornTop.DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos);

    default Portal.Transition getLocalTransition() {
        return Portal.Transition.NONE;
    }

    public static enum Transition {
        CONFUSION,
        NONE;
    }
}
