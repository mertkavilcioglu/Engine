package UI;

import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class RadarEditor extends JPanel {
    private int data;
    private JLabel nameLbl;
    private JLabel lbl;
    private JTextField txt;
    Border defaultBorder;
    private List<Character> numbers = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private Color borderColor;

    //TODO radar comp. eklenmemişse radarsız oluştur, radar varsa ama invalidse kırmızı yap
    public RadarEditor(String label, EntityEditorView editor){
        borderColor = editor.borderColor;
        setBackground(editor.panelComponentColor);
        nameLbl = new JLabel(label);
        nameLbl.setForeground(Color.WHITE);
        lbl = new JLabel("Range:");
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(editor.panelColor);
        txt = new JTextField();
        setBorder(BorderFactory.createLineBorder(editor.borderColor, 1));

        this.setLayout(new GridLayout(3,1));
        this.add(nameLbl);
        JPanel vecPnl = new JPanel(new GridLayout(1,3));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,60 ));
        vecPnl.add(lbl);
        vecPnl.add(txt);
        vecPnl.setBackground(editor.panelComponentColor);
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
}
