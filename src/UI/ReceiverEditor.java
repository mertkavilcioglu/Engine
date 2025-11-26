package UI;

import Sim.Component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.List;

public class ReceiverEditor extends JPanel {
    private JLabel nameLbl;
    private JLabel lbl;

    //TODO radar comp. eklenmemişse radarsız oluştur, radar varsa ama invalidse kırmızı yap
    public ReceiverEditor(String label, EntityEditorView editor, Component.ComponentType type){
        setBackground(editor.panelComponentColor);
        nameLbl = new JLabel(label);
        nameLbl.setForeground(Color.WHITE);
        lbl = new JLabel("");
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(editor.panelColor);
        setBorder(BorderFactory.createLineBorder(editor.borderColor, 1));

        this.setLayout(new GridLayout(3,1));
        this.add(nameLbl);
        JPanel vecPnl = new JPanel(new GridLayout(1,3));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,60 ));
        vecPnl.add(lbl);
        vecPnl.add(new JLabel(""));
        vecPnl.setBackground(editor.panelComponentColor);
        JButton removeButton = new JButton("X");
        removeButton.setBackground(editor.app.uiColorManager.BUTTON_COLOR);
        removeButton.addActionListener(e -> {
            editor.removeComponent(this, type);
            editor.app.mapView.getSelectedEntity().removeComponent(type);
            editor.updateSelectedEntity();
        });
        vecPnl.add(removeButton);
        this.add(vecPnl);
        add(new JLabel(" "));
        editor.setReceiverEditor(this);
    }
}
