package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class PlayPausePanel extends VCSPanel{
    private Color initialButColor;
    private Color panelBgColor;
    private ImageIcon playIcon = new ImageIcon("src/Assets/Icons/play_icon.png");
    private ImageIcon pauseIcon = new ImageIcon("src/Assets/Icons/pause_icon.png");
    private ImageIcon stopIcon = new ImageIcon("src/Assets/Icons/stop_icon.png");

    public PlayPausePanel(VCSApp app) {
        super(app);
        panelBgColor = app.uiColorManager.TOP_BAR_COLOR;
        this.setLayout(new GridBagLayout());
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        buttonpanel.setBackground(app.uiColorManager.TOP_BAR_COLOR);
        //playpanel.setSize(20,90);
        //playpanel.setBounds(20,100,20,90);
        JButton play = new JButton();
        ImageIcon rescaledPlay = new ImageIcon(
                playIcon.getImage().getScaledInstance(buttonpanel.getHeight(),buttonpanel.getHeight(),Image.SCALE_SMOOTH));
        play.setIcon(rescaledPlay);
        play.setFocusable(false);
        play.setSize(10,20);
        play.setBackground(app.uiColorManager.BUTTON_COLOR);
        JButton pause = new JButton("s");
        pause.setSize(10,20);
        pause.setFocusable(false);
        pause.setBackground(app.uiColorManager.BUTTON_COLOR);
        JButton reset = new JButton("r");
        reset.setSize(10,20);
        reset.setFocusable(false);
        reset.setBackground(app.uiColorManager.BUTTON_COLOR);
        buttonpanel.add(play);
        buttonpanel.add(pause);
        buttonpanel.add(reset);
        buttonpanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        this.add(buttonpanel);
        initialButColor = play.getBackground();
        reset.setEnabled(false);
        setBackground(panelBgColor);
        //setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

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
