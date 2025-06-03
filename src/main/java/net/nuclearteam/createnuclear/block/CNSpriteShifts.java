package net.nuclearteam.createnuclear.block;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

import net.nuclearteam.createnuclear.CreateNuclear;

@SuppressWarnings({"unused"})
public class CNSpriteShifts {
    public static final CTSpriteShiftEntry REACTOR_CASING = omni("reactor/casing/reactor_casing");
    public static final CTSpriteShiftEntry REACTOR_GLASS = omni("reinforced_glass");

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.RECTANGLE, name);

    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, CreateNuclear.asResource("block/" + blockTextureName),
                CreateNuclear.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}
