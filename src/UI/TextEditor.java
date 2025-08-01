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

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(lbl);
        this.add(txt);
    }

    public void setData(String text) {
        txt.setText(text);
    }
    //public Vec2int readData() {
        // sayımı degilmi
        //data.x = Integer.parseInt(txtX.getText());
        //data.y = Integer.parseInt(txtY.getText());
        //return data;
    //}

}
