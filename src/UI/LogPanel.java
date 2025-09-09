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
        logArea.setCaretColor(Color.white);
        logArea.setBorder(new TitledBorder("Log:"));
        JScrollPane scrollPanel = new JScrollPane(logArea);
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
