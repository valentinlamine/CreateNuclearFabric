package net.nuclearteam.createnuclear.foundation.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.level.LevelAccessor;
import net.nuclearteam.createnuclear.CreateNuclear;
import java.util.concurrent.Executor;
import com.simibubi.create.AllPackets;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.content.multiblock.itemRods.ItemRodTypeManager;

public class CommonEvents {
    public static void onDatapackSync(ServerPlayer player, boolean joined) {
        ItemRodTypeManager.syncTo(player);
    }

    public static void addReloadListener() {
        CreateNuclear.LOGGER.warn("Adding ReloadListener");
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(ItemRodTypeManager.ReloadListener.INSTANCE);
    }
  
  

  public static void onLoadWorld(Executor executor, LevelAccessor world) {

        CreateNuclear.TEST_PROPA.onLoadWorld(world);

    }



    public static void onUnloadWorld(Executor executor, LevelAccessor world) {

        CreateNuclear.TEST_PROPA.onUnloadWorld(world);

    }

    public static void register() {
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(CommonEvents::onDatapackSync);
      

        ServerWorldEvents.LOAD.register(CommonEvents::onLoadWorld);

        ServerWorldEvents.UNLOAD.register(CommonEvents::onUnloadWorld);

        CommonEvents.addReloadListener();
    }
}
