package net.ynov.createnuclear.menu;

import com.tterrag.registrate.util.entry.MenuEntry;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.multiblock.configuredItem.ConfiguredReactorItemMenu;
import net.ynov.createnuclear.multiblock.configuredItem.ConfiguredReactorItemScreen;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerMenu;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerScreen;

public class CNMenus {
    public static final MenuEntry<ReactorControllerMenu> REACTOR_CONTROLLER =
            CreateNuclear.REGISTRATE.menu("reactor_controller", ReactorControllerMenu::new, () -> ReactorControllerScreen::new).register();
    public static final MenuEntry<ConfiguredReactorItemMenu> TEST = CreateNuclear.REGISTRATE.menu("test", ConfiguredReactorItemMenu::new, () -> ConfiguredReactorItemScreen::new).register();


    public static void register() {}
}
