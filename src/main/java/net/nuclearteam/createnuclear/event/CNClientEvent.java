package net.nuclearteam.createnuclear.event;

import com.simibubi.create.foundation.events.ClientEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents.ColorData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FluidState;
import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.fluid.CNFluids;
import net.minecraft.world.level.material.Fluid;
import net.nuclearteam.createnuclear.tags.CNTag;
import org.jetbrains.annotations.Nullable;

public class CNClientEvent {

    private static final HudRenderer HUD_RENDERER = new HudRenderer();

    public static void register() {
        ClientEvents.ModBusEvents.registerClientReloadListeners();

        FogEvents.SET_COLOR.register(CNClientEvent::getForColor);
        HudRenderCallback.EVENT.register(HUD_RENDERER::onHudRender);
    }

    private static void getForColor(ColorData event, float partialTicks) {
        Camera info = event.getCamera();
        Level level = Minecraft.getInstance().level;
        BlockPos blockPos = info.getBlockPosition();
        FluidState fluidState = level.getFluidState(blockPos);
        if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

        Fluid fluid = fluidState.getType();

        if (CNFluids.URANIUM.get().isSame(fluid)){
            event.setRed(56 / 255F);
            event.setGreen(255 / 255F);
            event.setBlue(8 / 255F);
        }
    }
}
