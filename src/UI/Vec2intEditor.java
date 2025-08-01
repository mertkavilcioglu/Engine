package UI;

import Vec.Vec2int;
import javax.swing.*;
import java.awt.*;

public class Vec2intEditor extends JPanel{
    private Vec2int data = new Vec2int();
    private JLabel lblX;
    private JLabel lblY;
    private JTextField txtX;
    private JTextField txtY;

    public Vec2intEditor() {
        lblX = new JLabel("x: ");
        lblY = new JLabel("y: ");
        txtY = new JTextField(10);
        txtX = new JTextField(10);

        this.setLayout(new FlowLayout(FlowLayout.RIGHT));
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
