package net.nuclearteam.createnuclear.ponder;

import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.nuclearteam.createnuclear.item.CNItems;
import net.nuclearteam.createnuclear.multiblock.controller.ReactorControllerBlock;

public class CNPonderReactor {

    private static final BlockPos CONTROLLER = new BlockPos(6, 4, 4);
    private static final BlockPos OUTPUT = new BlockPos(6, 1, 4);
    private static final BlockPos INPUT = new BlockPos(2, 4, 4);
    public static void init(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("reactor", "Setup from Reactor");
        scene.configureBasePlate(0,0,9);
        scene.showBasePlate();


        for (int y = 1; y < 8; y++) {
            scene.overlay.showText(10)
                    .text("floor " + y)
                    .attachKeyFrame()
                    .placeNearTarget();

            for (int x = 1; x < 7; x++) {
                for (int z = 1; z < 7; z++) {
                    scene.world.showSection(util.select.position(x,y,z), Direction.NORTH);
                    //CreateNuclear.LOGGER.warn("x: {} | y: {} | z: {}, block: {}", x, y, z, new BlockPos(x,y,z));
                    scene.idle(5);
                    if (x == 5 && z == 5 && y == 1) {
                        scene.rotateCameraY(180f);
                    }
                    if (x == INPUT.getX() && y == INPUT.getY() && z == INPUT.getZ()) {
                        scene.rotateCameraY(180f);
                        scene.overlay.showText(30)
                                .text("Reactor Input: A block that stores uranium rods and graphite rods to operate the reactor.")
                                .pointAt(util.vector.blockSurface(INPUT, Direction.UP))
                                .attachKeyFrame()
                                .placeNearTarget();
                        scene.idle(40);
                        scene.rotateCameraY(180f);
                    }
                    if (x == OUTPUT.getX() && y == OUTPUT.getY() && z == OUTPUT.getZ()) {
                        AABB bb = new AABB(OUTPUT).inflate(1/16f, 0, 0);
                        scene.overlay.chaseBoundingBoxOutline(PonderPalette.MEDIUM, new Object(), bb.move(0, 1, 0)
                                .contract(0, .75, 0), 80);
                        scene.overlay.showText(30)
                                .text("Reactor Output: This is where the block outputs the energy (SU) generated from production.")
                                .pointAt(util.vector.blockSurface(OUTPUT, Direction.UP))
                                .attachKeyFrame()
                                .placeNearTarget();
                        scene.idle(5);
                    }
                    if (x == CONTROLLER.getX() && y == CONTROLLER.getY() && z == CONTROLLER.getZ()){
                        scene.overlay.showText(13)
                            .text("Reactor Controller: The most important block for operating the reactor; it handles all the calculations needed to generate energy.")
                            .pointAt(util.vector.blockSurface(CONTROLLER, Direction.DOWN))
                            .attachKeyFrame()
                            .placeNearTarget();
                        scene.idle(5);
                    }
                }
            }

        }
    }

    public static void enable(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("reactor", "Enable controller");
        scene.configureBasePlate(0,0,9);
        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.showBasePlate();

        scene.rotateCameraY(135f);
        for (int y = 1; y < 8; y++) {
            for (int x = 1; x < 7; x++) {
                for (int z = 1; z < 7; z++) {
                    scene.world.showSection(util.select.position(x,y,z), Direction.NORTH);
                }
            }
        }
        scene.idle(30);
        scene.overlay
                .showControls(
                new InputWindowElement(util.vector.blockSurface(CONTROLLER, Direction.NORTH), Pointing.UP)
                        .withItem(CNItems.REACTOR_BLUEPRINT.asStack())
                        .rightClick(),
                40);
        scene.overlay
                .showText(23)
                .text("Reactor Blueprint: The most important item; it allows you to configure the reactor according to specific patterns. For more details, refer to the item's tooltip.");


        scene.world.modifyBlock(CONTROLLER, s -> s.setValue(ReactorControllerBlock.ASSEMBLED, true), true);
    }
}
