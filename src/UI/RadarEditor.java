package UI;

import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RadarEditor extends JPanel {
    private int data;
    private JLabel nameLbl;
    private JLabel lbl;
    private JTextField txt;
    Border defaultBorder;
    //TODO radar comp. eklenmemişse radarsız oluştur, radar varsa ama invalidse kırmızı yap
    public RadarEditor(String label, EntityEditorView editor){
        nameLbl = new JLabel(label);
        lbl = new JLabel("Range:");
        txt = new JTextField();

        this.setLayout(new GridLayout(3,1));
        this.add(nameLbl);
        JPanel vecPnl = new JPanel(new GridLayout(1,3));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,60 ));
        vecPnl.add(lbl);
        vecPnl.add(txt);
        JButton removeButton = new JButton("X");
        removeButton.addActionListener(e -> {
            editor.removeComponent(this, "Radar");
        });
        vecPnl.add(removeButton);
        this.add(vecPnl);
        defaultBorder = txt.getBorder();
        add(new JLabel(" "));
        editor.setRadarPanel(this);
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

    //TODO 2 kere catch yapma günah

    public void dataValidate(){
        int r;
        try {
            r = Integer.parseInt(txt.getText());
            if(Integer.parseInt(txt.getText()) == 0){
                System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), lbl.getText());
                txt.setBorder(new LineBorder(Color.RED, 2));
            }
        }
        catch (NumberFormatException e) {
            System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), lbl.getText());
            txt.setBorder(new LineBorder(Color.RED, 2));
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
}
