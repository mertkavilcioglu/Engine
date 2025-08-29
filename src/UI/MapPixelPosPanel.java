package UI;

import App.VCSApp;
import Sim.Entity;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;

public class MapPixelPosPanel extends VCSPanel{
    Vec2int pixelPos;
    JLabel labelX;
    JLabel labelY;

    public MapPixelPosPanel(VCSApp app) {
        super(app);

        this.setLayout(new GridLayout(1,2));
        labelX = new JLabel();
        labelY = new JLabel();

        if (pixelPos == null) {
            labelX.setText(" x:    ");
            labelY.setText(" y:    ");
        }
        this.add(labelX);
        this.add(labelY);
        this.setPreferredSize(new Dimension(80,20));
    }

    public void showPixelPosOfCursor(Vec2int p){
        this.pixelPos = p;
        if (pixelPos == null){
            labelX.setText("x: ");
            labelY.setText("y: ");
        }else {
            labelX.setText(" x: " + pixelPos.x);
            labelY.setText(" y: " + pixelPos.y);
        }

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
