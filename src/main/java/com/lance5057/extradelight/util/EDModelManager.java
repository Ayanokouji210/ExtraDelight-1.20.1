package com.lance5057.extradelight.util;

import com.lance5057.extradelight.ExtraDelight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = ExtraDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class EDModelManager {
    private static final Map<ResourceLocation, BakedModel> models=new HashMap<>();
    private static final ResourceLocation TOAST_FOLDER= ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, "models/extra/dynamics/toast");

    @SubscribeEvent
    public static void getBakedModels(ModelEvent.BakingCompleted event) {
        ModelManager modelManager = event.getModelManager();
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        Map<ResourceLocation, Resource> toastResource = resourceManager.listResources(TOAST_FOLDER.getPath(),
                path -> path.getPath().endsWith(".json"));

        for(ResourceLocation location:toastResource.keySet()) {
            String path=location.getPath().substring(7,location.getPath().length()-5);
            String modelName=path.substring(TOAST_FOLDER.getPath().length()-6);

            ResourceLocation modelLocation = ResourceLocation.fromNamespaceAndPath(ExtraDelight.MOD_ID, path);
            BakedModel model = modelManager.getModel(modelLocation);

            if(model!=modelManager.getMissingModel()) {
                models.put(modelLocation, model);
            }else{
                ExtraDelight.logger.warn("Model {} not found,using missing model instead", modelLocation);
            }
        }
    }

    public static Optional<BakedModel> getBakedModel(ResourceLocation location) {
        return Optional.ofNullable(models.get(location));
    }

    public static Map<ResourceLocation, BakedModel> getAllModels() {
        return Collections.unmodifiableMap(models);
    }

}
