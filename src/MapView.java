import javax.swing.*;
import java.awt.*;

public class MapView extends JPanel {
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

            //drawEntity(e);
            g.setColor(Color.BLACK);
            g.drawOval(e.pos.x, e.pos.y, 20, 20);
        }
    }


}
