package Util;

import java.util.Random;

public class RandomGen extends Random {

    private static RandomGen inst = null;
    private long seed;

    private RandomGen() {
        super();
        seed = SeedGenerator.getInst().getSeed();
        this.setSeed(seed);
    }

    public static RandomGen getInst() {
        if (inst == null) {
            inst = new RandomGen();
        }
        return inst;

    }

    public long getSeed() {
        return seed;
    }

    @Override
    public void setSeed(long nseed) {
        seed = nseed;
        super.setSeed(nseed);
    }

    public int nextUInt() {
        return Math.abs(super.nextInt());
    }
}
