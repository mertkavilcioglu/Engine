package UI;

import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;

public class TextEditor extends JPanel {
    private JLabel lbl;
    private JTextField txt;

    public TextEditor(String labelName){
        lbl = new JLabel(labelName);
        txt = new JTextField();

        this.setLayout(new GridLayout(2,1));
        this.add(lbl);
        this.add(txt);
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
        return null;
    }

}
