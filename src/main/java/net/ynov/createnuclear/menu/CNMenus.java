package net.ynov.createnuclear.menu;

import com.simibubi.create.Create;
import com.simibubi.create.content.schematics.table.SchematicTableMenu;
import com.simibubi.create.content.schematics.table.SchematicTableScreen;
import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.content.reactor.controller.ReactorControllerMenu;
import net.ynov.createnuclear.content.reactor.controller.ReactorControllerScreen;

public class CNMenus {
    public static final MenuEntry<ReactorControllerMenu> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.menu("reactor_controller", ReactorControllerMenu::new, () -> ReactorControllerScreen::new).register();


    public static void register() {}
}
