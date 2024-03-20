package net.ynov.createnuclear.gui;

import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.gui.widget.IconButton;

public class CNIconButton extends IconButton {
    public CNIconButton(int x, int y, ScreenElement icon) {
        super(x, y, icon);
    }

    public ScreenElement getIcon() {
        return icon;
    }
}
