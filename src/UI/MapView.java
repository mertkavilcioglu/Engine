package UI;

import Sim.Entity;
import Sim.World;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;

public class MapView extends VCSpanel {
    private World world;

    public MapView(World world) {
        this.world = world;
    }
    @Override
    public void paint(Graphics g) {
        //super.paint(g);

        g.setColor(Color.RED);
        //g.fillRect(0,0, this.getWidth(), this.getHeight());

        for (int i = 0; i < World.entities.size(); i++) {
            Entity e = World.entities.get(i);
            Vec2int pos = e.getPos();

            //drawEntity(e);
            g.setColor(Color.BLACK);
            g.drawOval(pos.x, pos.y, 20, 20);
        }
    }


}
