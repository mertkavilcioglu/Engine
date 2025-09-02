package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class PlayPausePanel extends VCSPanel{
    private Color initialButColor;
    public PlayPausePanel(VCSApp app) {
        super(app);

        this.setLayout(new GridBagLayout());
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        //playpanel.setSize(20,90);
        //playpanel.setBounds(20,100,20,90);
        JButton play = new JButton("p");
        play.setFocusable(false);
        play.setSize(10,20);
        JButton pause = new JButton("s");
        pause.setSize(10,20);
        pause.setFocusable(false);
        JButton reset = new JButton("r");
        reset.setSize(10,20);
        reset.setFocusable(false);
        buttonpanel.add(play);
        buttonpanel.add(pause);
        buttonpanel.add(reset);
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        this.add(buttonpanel);
        initialButColor = play.getBackground();
        reset.setEnabled(false);

        play.addActionListener(e -> {
            app.simTimer.start();
            play.setBackground(Color.GREEN);
            pause.setBackground(initialButColor);
        });

        pause.addActionListener(e -> {
            app.simTimer.stop();
            pause.setBackground(Color.YELLOW);
            play.setBackground(initialButColor);
        });

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
