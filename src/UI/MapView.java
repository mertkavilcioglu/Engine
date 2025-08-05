package UI;

import App.VCSapp;
import Sim.Entity;
import Sim.World;
import Vec.Vec2int;

import java.awt.*;

public class MapView extends VCSPanel {
    private World world;

    public MapView(VCSapp app) {
        super(app);
        this.world = app.world;
    }

    @Override
    public void selectedEntityChanged(Entity entity) {
        System.out.println("EditorView::selectedEntityChanged");
    }

    @Override
    public void paint(Graphics g) {
        //super.paint(g);

        g.setColor(Color.RED);
        //g.fillRect(0,0, this.getWidth(), this.getHeight());

        for (int i = 0; i < world.entities.size(); i++) {
            Entity e = world.entities.get(i);
            Vec2int pos = e.getPos();

            //drawEntity(e);
            g.setColor(Color.BLACK);
            g.drawOval(pos.x, pos.y, 20, 20);
        }
    }


}
