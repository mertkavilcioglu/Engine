package UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StringEditor extends JPanel {
    private JLabel lbl;
    private JTextField txt;
    Border defaultBorder;

    public StringEditor(String labelName){
        lbl = new JLabel(labelName);
        lbl.setForeground(Color.WHITE);
        txt = new JTextField();

        this.setLayout(new GridLayout(2,1));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        this.add(lbl);
        this.add(txt);
        defaultBorder = txt.getBorder();
    }

    public void setData(String text) {
        txt.setText(text);
    }

    public JTextField getInputField(){
        return txt;
    }
    public String readData() {
        if(txt.getText() != null && !txt.getText().trim().isEmpty()){
            return txt.getText().trim();
        }
        System.out.format("%s IS NOT VALID\n", lbl.getText());
        //txt.setBackground(Color.RED);
        txt.setBorder(new LineBorder(Color.RED, 2));

        new Thread(()->{
            try {
                Thread.sleep(1000);
                //txt.setBackground(Color.WHITE);
                txt.setBorder(defaultBorder);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return null;
    }

}
