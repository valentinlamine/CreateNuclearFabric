package net.nuclearteam.createnuclear.menu;

import com.tterrag.registrate.util.entry.MenuEntry;

import net.nuclearteam.createnuclear.CreateNuclear;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerMenu;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerScreen;

public class CNMenus {
    public static final MenuEntry<ReactorControllerMenu> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.menu("reactor_controller", ReactorControllerMenu::new, () -> ReactorControllerScreen::new).register();


    public static void register() {}
}
