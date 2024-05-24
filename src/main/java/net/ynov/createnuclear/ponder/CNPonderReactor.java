package net.ynov.createnuclear.ponder;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.ynov.createnuclear.CreateNuclear;
import net.ynov.createnuclear.item.CNItems;
import net.ynov.createnuclear.multiblock.controller.ReactorControllerBlock;

public class CNPonderReactor {

    private static final BlockPos CONTROLLER = new BlockPos(3,4,5);
    public static void init(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("reactor", "Setup from Reactor");
        scene.configureBasePlate(0,0,7);
        scene.showBasePlate();


        for (int y = 1; y < 8; y++) {
            scene.overlay.showText(10)
                    .text("etage " + y)
                    .attachKeyFrame()
                    .placeNearTarget();

            for (int x = 1; x < 7; x++) {
                for (int z = 1; z < 7; z++) {
                    scene.world.showSection(util.select.position(x,y,z), Direction.NORTH);
                    CreateNuclear.LOGGER.warn(x + " | " + y + " | " + z);
                    scene.idle(5);
                    if (x == 5 && z == 5 && y == 1) {
                        scene.rotateCameraY(180f);
                    }
                    if (x == CONTROLLER.getX() && y == CONTROLLER.getY() && z == CONTROLLER.getZ()){
                        scene.overlay.showText(13)
                            .text("Controller")
                            .pointAt(util.vector.blockSurface(CONTROLLER, Direction.DOWN))
                            .attachKeyFrame()
                            .placeNearTarget();
                    }
                }
            }

        }
    }

    public static void enable(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("reactor", "Enable controller");
        scene.configureBasePlate(0,0,7);
        scene.showBasePlate();

        scene.rotateCameraY(-140f);
        for (int y = 1; y < 8; y++) {
            for (int x = 1; x < 7; x++) {
                for (int z = 1; z < 7; z++) {
                    scene.world.showSection(util.select.position(x,y,z), Direction.NORTH);
                }
            }
        }
        scene.idle(30);

        scene.world.modifyBlock(CONTROLLER, s -> s.setValue(ReactorControllerBlock.ASSEMBLED, true), true);
    }
}
