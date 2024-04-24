package net.ynov.createnuclear.block;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.ynov.createnuclear.CreateNuclear;

public class CNSpriteShifts {

    public static final CTSpriteShiftEntry REACTOR_CASING = omni("reactor_casing");

    public static final CTSpriteShiftEntry
            REACTOR = getCT(AllCTTypes.RECTANGLE, "reactor/casing/reactor_casing", "reactor/casing/reactor_casing"),
            REACTOR_TOP = getCT(AllCTTypes.RECTANGLE, "reactor/casing/reactor_casing_top", "reactor/casing/reactor_casing_top"),
            REACTOR_INNER = getCT(AllCTTypes.RECTANGLE, "reactor/casing/reactor_casing_inner", "reactor/casing/reactor_casing_inner")
                    ;

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, CreateNuclear.asResource("block/" + blockTextureName),
                CreateNuclear.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}