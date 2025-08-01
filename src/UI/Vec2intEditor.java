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
        txtX = new JTextField();
        txtY = new JTextField();

        this.setLayout(new GridLayout(2,1));
        this.add(nameLbl);
        JPanel vecPnl = new JPanel(new GridLayout(1,4));
        vecPnl.add(lblX);
        vecPnl.add(txtX);
        vecPnl.add(lblY);
        vecPnl.add(txtY);
        this.add(vecPnl);

    }

    public void setData(Vec2int data) {
        this.data = data;
        txtX.setText(String.valueOf(data.x));
        txtY.setText(String.valueOf(data.y));
    }

    public Vec2int readData() {
        //TODO sayımı degilmi kontrolünü burada yap
        //TODO buton classı da ekle
        data.x = Integer.parseInt(txtX.getText());
        data.y = Integer.parseInt(txtY.getText());
        return data;
    }
}
