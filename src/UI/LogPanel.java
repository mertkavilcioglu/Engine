package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LogPanel extends VCSPanel {
    private JTextArea logArea;

    public LogPanel(VCSApp app) {
        super(app);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(getWidth(),getHeight()));
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));
        logArea = new JTextArea(10,40);
        logArea.setEditable(false);
        logArea.setCaretColor(app.uiColorManager.DARK_PANEL_COLOR);
        logArea.setForeground(Color.WHITE);

        TitledBorder logTitledBorder = new TitledBorder("Log: ");
        logTitledBorder.setTitleColor(Color.WHITE);
        logTitledBorder.setBorder(BorderFactory.createLineBorder(Color.WHITE,1));
        logArea.setBorder(logTitledBorder);
        logArea.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        JScrollPane scrollPanel = new JScrollPane(logArea);
        scrollPanel.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        //this.add(label);
        this.add(scrollPanel, BorderLayout.CENTER);
    }

    public void messageToLog(String message){
        logArea.append(message + "\n");

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
