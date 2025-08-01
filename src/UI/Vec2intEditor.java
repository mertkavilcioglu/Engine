package UI;

import Vec.Vec2int;
import javax.swing.*;
import java.awt.*;

public class Vec2intEditor extends JPanel{
    private Vec2int data = new Vec2int();
    private JLabel nameLbl;
    private JLabel lblX;
    private JLabel lblY;
    private JTextField txtX;
    private JTextField txtY;

    public Vec2intEditor(String label){
        nameLbl = new JLabel(label);
        lblX = new JLabel("X:");
        lblY = new JLabel("Y:");
        txtX = new JTextField(10);
        txtY = new JTextField(10);

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(nameLbl);
        this.add(lblX);
        this.add(txtX);
        this.add(lblY);
        this.add(txtY);

    }

    public void setData(Vec2int data) {
        this.data = data;
        txtX.setText(String.valueOf(data.x));
        txtY.setText(String.valueOf(data.y));
    }

    public Vec2int readData() {
        // sayımı degilmi
        data.x = Integer.parseInt(txtX.getText());
        data.y = Integer.parseInt(txtY.getText());
        return data;
    }
}
