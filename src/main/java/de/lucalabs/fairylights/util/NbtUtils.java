package de.lucalabs.fairylights.util;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.util.math.BlockPos;

public final class NbtUtils {
    private NbtUtils() {}

    public static BlockPos toBlockPos(NbtElement e) {
       if (e instanceof NbtIntArray eArr)  {
           int[] is = eArr.getIntArray();
           return is.length == 3 ? new BlockPos(is[0], is[1], is[2]) : null;
       }

       return null;
    }
}
