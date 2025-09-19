package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class LogPanel extends VCSPanel {
    private JTextPane logArea;

    public LogPanel(VCSApp app) {
        super(app);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(getWidth(),getHeight()));
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));
        this.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setCaretColor(app.uiColorManager.DARK_PANEL_COLOR);
        logArea.setForeground(Color.WHITE);

        TitledBorder logTitledBorder = new TitledBorder("Log: ");
        logTitledBorder.setTitleColor(Color.WHITE);
        logTitledBorder.setBorder(BorderFactory.createLineBorder(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR,2));
        logArea.setBorder(logTitledBorder);
        logArea.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        JScrollPane scrollPanel = new JScrollPane(logArea);
        scrollPanel.setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        scrollPanel.getViewport().getView().setBackground(app.uiColorManager.DARK_PANEL_COLOR);
        scrollPanel.setBorder(null);
        //this.add(label);
        this.add(scrollPanel, BorderLayout.CENTER);
    }

    public void messageToLog(String message){
        String text = message + "\n";
        coloredText(logArea, text, Color.WHITE);
    }

    public void debugLogMessage(String msg){
        String text = msg + "\n";
        coloredText(logArea, text, Color.YELLOW);
    }

    private void coloredText(JTextPane textPane, String msg, Color color){
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.addStyle("ColorStyle", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), msg, style);
        }catch (BadLocationException e){
            e.printStackTrace();
        }
    }

     public void clearLogArea(){
        try {
            logArea.getStyledDocument().remove(0, logArea.getStyledDocument().getLength());
        } catch (BadLocationException e){
            e.printStackTrace();
        }
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
