package net.nuclearteam.createnuclear;

import com.tterrag.registrate.builders.MenuBuilder;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintItemScreen;
import net.nuclearteam.createnuclear.content.multiblock.bluePrintItem.ReactorBluePrintMenu;
import net.nuclearteam.createnuclear.content.multiblock.input.item.ReactorRodInputMenu;
import net.nuclearteam.createnuclear.content.multiblock.input.item.ReactorRodInputScreen;

public class CNMenus {
    public static final MenuEntry<ReactorBluePrintMenu> REACTOR_BLUEPRINT_MENU = menu("reactor_blueprint_menu", ReactorBluePrintMenu::new, () -> ReactorBluePrintItemScreen::new);
    public static final MenuEntry<ReactorRodInputMenu> SLOT_ITEM_STORAGE = menu("slot_item_menu", ReactorRodInputMenu::new, () -> ReactorRodInputScreen::new);

    private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> menu(String name, MenuBuilder.ForgeMenuFactory<C> factory, NonNullSupplier<MenuBuilder.ScreenFactory<C, S>> screenFactory) {
        return CreateNuclear.REGISTRATE.menu(name, factory, screenFactory).register();
    }

    public static void register() {}
}
