package Sim;

import static java.lang.Math.sqrt;

public class Position {
    int x,y;

    public double distance(Position p) {
        //sqrt((entities.get(i).pos.x - selfPosition.x)*(entities.get(i).pos.x - selfPosition.x) + (entities.get(i).pos.y - selfPosition.y)*(entities.get(i).pos.y - selfPosition.y)) <= range
        //        && (entities.get(i).pos.x != selfPosition.x || entities.get(i).pos.y != selfPosition.y))
        //TODO
        return sqrt((p.x - this.x)*(p.x - this.x) + (p.y - this.y)*(p.y - this.y));
    }
}
