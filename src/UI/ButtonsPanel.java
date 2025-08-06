package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class ButtonsPanel extends VCSPanel{
    public ButtonsPanel(VCSapp app) {
        super(app);

        this.setLayout(new GridBagLayout());
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        //playpanel.setSize(20,90);
        //playpanel.setBounds(20,100,20,90);
        JButton play = new JButton("p");
        play.setSize(10,20);
        JButton pause = new JButton("s");
        pause.setSize(10,20);
        JButton reset = new JButton("r");
        reset.setSize(10,20);
        buttonpanel.add(play);
        buttonpanel.add(pause);
        buttonpanel.add(reset);
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        this.add(buttonpanel);
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
