package lib.multiblock.test.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

public class Util {
    public static Rotation DirectionToRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.CLOCKWISE_90;
            case EAST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    public static Map<Character, List<MultiBlockOffsetPos>> parseBlockPattern(List<String[]> aisles, Set<Character> allowedCharacters) {
        Collections.reverse(aisles);
        Map<Character, List<MultiBlockOffsetPos>> blockOffsets = new HashMap<>();

        int starX = -1;
        int starY = -1;
        int starZ = -1;

        for (int y = 0; y < aisles.size(); y++) {
            String[] aisle = aisles.get(y);

            for (int x=0; x < aisle.length; x++) {
                for (int z = 0; z < aisle[x].length(); z++) {
                    if (aisle[x].charAt(z) == '*') {
                        starX = x;
                        starY = y;
                        starZ = z;
                        break;
                    }
                }
            }
        }

        if (starX == -1 || starY == -1 || starZ == -1) {
            return blockOffsets;
        }

        for (int y = 0; y < aisles.size(); y++) {
            String[] aisle = aisles.get(y);

            for (int x = 0; x < aisle.length; x++) {
                for (int z =0; z < aisle[x].length(); z++) {
                    char currentChar = aisle[x].charAt(z);

                    if (allowedCharacters.contains(currentChar)) {
                        if (!blockOffsets.containsKey(currentChar)) {
                            blockOffsets.put(currentChar, new ArrayList<>());
                        }
                        int xOffset = x - starX;
                        int yOffset = y - starY;
                        int zOffset = z - starZ;

                        blockOffsets.get(currentChar).add(new MultiBlockOffsetPos(currentChar, new BlockPos(xOffset, yOffset, zOffset)));
                    }
                }
            }
        }
        return blockOffsets;
    }
}
