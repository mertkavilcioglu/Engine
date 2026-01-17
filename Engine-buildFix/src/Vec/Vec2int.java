package Vec;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.sqrt;

public class Vec2int {
    public int x;
    public int y;
    public double length;

    public Vec2int() {
    }

    public Vec2int(int x, int y){
        this.x = x;
        this.y = y;
        length = sqrt((x*x) + (y*y));
    }

    public double distance(Vec2int other) {
      return sqrt((other.x - this.x)*(other.x - this.x) + (other.y - this.y)*(other.y - this.y));
    }

    public Vec2int vectorDiff(Vec2int other) {
        return new Vec2int((other.x - this.x),(other.y - this.y));
    }

    public Vec2int normalize(int targetLength){
        //Vec2int normalized = input;
        double vMagnitude = this.length;
        double vecX = targetLength * this.x / vMagnitude;
        double vecY = -targetLength * this.y / vMagnitude;
        this.x = (int) (vecX + 0.5);
        this.y = (int) (vecY + 0.5);
        return this;
    }

    public int getMagnitudeAsInt() {
        double h = sqrt((this.x * this.x) + (this.y * this.y));
        return (int) (h + 0.5);
    }

    public static Vec2int getRandom(int maxX, int maxY) {
        Vec2int res = new Vec2int();
        res.x = ThreadLocalRandom.current().nextInt(maxX);
        res.y = ThreadLocalRandom.current().nextInt(maxY);
        return res;
    }

    public static Vec2int getRandom(int minX, int maxX, int minY, int maxY) {
        Vec2int res = new Vec2int();
        res.x = ThreadLocalRandom.current().nextInt(minX, maxX);
        res.y = ThreadLocalRandom.current().nextInt(minY, maxY);
        return res;
    }
    @Override
    public String toString() {
        return String.format("[%d,%d]",x,y);
    }
}
