//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.nuclearteam.createnuclear.foundation.utility;

import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public class Maths {
    public static final Direction[] HORIZONTAL_DIRECTIONS;
    public static final Direction[] NOT_UP_DIRECTIONS;
    public static final float HALF_SQRT_3;
    public static final float QUARTER_PI = 0.7853982F;

    public Maths() {
    }

    public static float smin(float a, float b, float k) {
        float h = Math.max(k - Math.abs(a - b), 0.0F) / k;
        return Math.min(a, b) - h * h * k * 0.25F;
    }

    public static float sampleNoise2D(int x, int z, float simplexSampleRate) {
        return (float) SimplexNoise.noise((double)(((float)x + simplexSampleRate) / simplexSampleRate), (double)(((float)z + simplexSampleRate) / simplexSampleRate));
    }

    public static float sampleNoise3D(int x, int y, int z, float simplexSampleRate) {
        return (float) SimplexNoise.noise((double)(((float)x + simplexSampleRate) / simplexSampleRate), (double)(((float)y + simplexSampleRate) / simplexSampleRate), (double)(((float)z + simplexSampleRate) / simplexSampleRate));
    }

    public static float sampleNoise3D(float x, float y, float z, float simplexSampleRate) {
        return (float) SimplexNoise.noise((double)((x + simplexSampleRate) / simplexSampleRate), (double)((y + simplexSampleRate) / simplexSampleRate), (double)((z + simplexSampleRate) / simplexSampleRate));
    }

    public static VoxelShape buildShape(VoxelShape... from) {
        return (VoxelShape)Stream.of(from).reduce((v1, v2) -> {
            return Shapes.combineAndSimplify(v1, v2, BooleanOp.OR);
        }).get();
    }

    public static float walkValue(float limbSwing, float limbSwingAmount, float speed, float offset, float degree, boolean inverse) {
        return (float)(Math.cos((double)(limbSwing * speed + offset)) * (double)degree * (double)limbSwingAmount * (double)(inverse ? -1 : 1));
    }

    public static float approachRotation(float current, float target, float max) {
        float f = Mth.wrapDegrees(target - current);
        if (f > max) {
            f = max;
        }

        if (f < -max) {
            f = -max;
        }

        return Mth.wrapDegrees(current + f);
    }

    public static Vec3 getGroundBelowPosition(BlockGetter level, Vec3 in) {
        BlockPos pos;
        for(pos = BlockPos.ofFloored(in); pos.getY() > level.getBottomY() && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty(); pos = pos.down()) {
        }

        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(level, pos);
        float top;
        if (shape.isEmpty()) {
            top = 0.0F;
        } else {
            Vec3 modIn = new Vec3(in.x % 1.0, 1.0, in.z % 1.0);
            Optional<Vec3> closest = shape.getClosestPointTo(modIn);
            top = closest.isPresent() ? (float)((Vec3)closest.get()).y : 0.0F;
        }

        return Vec3.ofCenter(pos, (double)top);
    }

    public static Vec3 readVec3(FriendlyByteBuf buf) {
        return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static FriendlyByteBuf writeVec3(FriendlyByteBuf buf, Vec3 vec3) {
        buf.writeDouble(vec3.getX());
        buf.writeDouble(vec3.getY());
        buf.writeDouble(vec3.getZ());
        return buf;
    }

    public static float approachDegreesNoWrap(float from, float to, float by) {
        float f = (to - from) % 360.0F;
        return Mth.stepTowards(from, from + f, by);
    }

    public static float canyonStep(float heightScale, int scaleTo) {
        int clampTo100 = (int)(heightScale * (float)scaleTo * (float)scaleTo);
        return Mth.clamp((float)Math.round((float)clampTo100 / (float)scaleTo) / (float)scaleTo, 0.0F, 1.0F);
    }

    public static Set<Holder<Biome>> getBiomesWithinAtY(BiomeSource biomeSource, int x, int y, int z, int xzDist, Climate.Sampler sampler) {
        int i = QuartPos.fromBlock(x - xzDist);
        int j = QuartPos.fromBlock(y);
        int k = QuartPos.fromBlock(z - xzDist);
        int l = QuartPos.fromBlock(x + xzDist);
        int j1 = QuartPos.fromBlock(z + xzDist);
        int k1 = l - i + 1;
        int i2 = j1 - k + 1;
        Set<Holder<Biome>> set = Sets.newHashSet();

        for(int j2 = 0; j2 < i2; ++j2) {
            for(int k2 = 0; k2 < k1; ++k2) {
                int i3 = i + k2;
                int k3 = k + j2;
                set.add(biomeSource.getBiome(i3, j, k3, sampler));
            }
        }

        return set;
    }

    static {
        HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        NOT_UP_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN};
        HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);
    }
}

