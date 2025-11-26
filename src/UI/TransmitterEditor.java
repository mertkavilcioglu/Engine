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

public class TransmitterEditor extends JPanel {
    private int data;
    private JLabel nameLbl;
    private JLabel lbl;
    public JTextField txt;
    private Border defaultBorder;
    private List<Character> numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private Color borderColor;
    private boolean isFocused = false;

    //TODO radar comp. eklenmemişse radarsız oluştur, radar varsa ama invalidse kırmızı yap
    public TransmitterEditor(String label, EntityEditorView editor, Component.ComponentType type, String defaultRange){
        borderColor = editor.borderColor;
        setBackground(editor.panelComponentColor);
        nameLbl = new JLabel(label);
        nameLbl.setForeground(Color.WHITE);
        lbl = new JLabel("Range:");
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(editor.panelColor);
        txt = new JTextField();
        setBorder(BorderFactory.createLineBorder(editor.borderColor, 1));
        txt.setText(defaultRange);

        this.setLayout(new GridLayout(3,1));
        this.add(nameLbl);
        JPanel vecPnl = new JPanel(new GridLayout(1,3));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,60 ));
        vecPnl.add(lbl);
        vecPnl.add(txt);
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
        defaultBorder = txt.getBorder();
        add(new JLabel(" "));
        editor.setTransmitterEditor(this);

        txt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.updateSelectedEntity();
            }
        });

        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
            }
        });
    }

    public void setData(int data) {
        this.data = data;
        txt.setText(String.valueOf(data));
    }

    public Integer readData() {
        data = Integer.parseInt(txt.getText());
        if(data != 0)
            return data;
        return null;
    }

    // 2 kere catch yapma günah

    public void dataValidate(){

        if(txt.getText().isBlank() || txt.getText() == null){
            System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), txt.getText());
            txt.setBorder(new LineBorder(Color.RED, 2));
        }
        else{
            for(char c : txt.getText().trim().toCharArray()){
                if(!numbers.contains(c)){
                    System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), lbl.getText());
                    txt.setBorder(new LineBorder(Color.RED, 2));
                }
            }
        }

        new Thread(()->{
            try {
                Thread.sleep(1000);
                //txt.setBackground(Color.WHITE);
                txt.setBorder(defaultBorder);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public boolean getIsFocused(){
        return isFocused;
    }

    public JTextField getTxt(){
        return txt;
    }
}
