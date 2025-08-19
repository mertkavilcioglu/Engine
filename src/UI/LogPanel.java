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
        this.setSize(100,200);
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));
        //JLabel label = new JLabel("Log: ");
        //JTextField text = new JTextField();
        //text.setBorder(new TitledBorder("Log:"));
        //text.setPreferredSize(new Dimension(300,200));
        //text.setText("Log will be shown in here.");
        logArea = new JTextArea(10,40);
        logArea.setEditable(false);
        logArea.setCaretColor(Color.white);
        logArea.setBorder(new TitledBorder("Log:"));
        //this.add(label);
        this.add(logArea);
    }

    public void messageToLog(String message){
        logArea.append(message + "\n");

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
