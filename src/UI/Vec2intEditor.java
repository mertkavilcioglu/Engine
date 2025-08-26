package UI;

import Vec.Vec2int;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Vec2intEditor extends JPanel{
    private Vec2int data = new Vec2int();
    private JLabel nameLbl;
    private JLabel lblX;
    private JLabel lblY;
    private JTextField txtX;
    private JTextField txtY;
    Border defaultBorder;
    private String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public Vec2intEditor(String label){
        nameLbl = new JLabel(label);
        lblX = new JLabel("X:");
        lblY = new JLabel("Y:");
        txtX = new JTextField();
        txtY = new JTextField();

        this.setLayout(new GridLayout(2,1));
        this.add(nameLbl);
        JPanel vecPnl = new JPanel(new GridLayout(1,4));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,40 ));
        vecPnl.add(lblX);
        vecPnl.add(txtX);
        vecPnl.add(lblY);
        vecPnl.add(txtY);
        this.add(vecPnl);
        defaultBorder = txtX.getBorder();

    }

    public void setData(Vec2int data) {
        this.data = data;
        txtX.setText(String.valueOf(data.x));
        txtY.setText(String.valueOf(data.y));
    }

    public Vec2int readData() {
        data.x = Integer.parseInt(txtX.getText());
        data.y = Integer.parseInt(txtY.getText());
        return data;
    }

    //TODO 2 kere catch yapma günah

    public void dataValidate(){
        int x,y;

        for(char c : txtX.getText().toCharArray()){
            if(!Arrays.stream(numbers).toList().contains(new String(String.format("%s",c)).trim())
            || txtX.getText().trim().isBlank() || txtX.getText().trim().isEmpty() || txtX.getText() == nulls){
                System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), lblX.getText());
                txtX.setBorder(new LineBorder(Color.RED, 2));
            }
        }

        for(char c :txtY.getText().toCharArray()){
            if(!Arrays.stream(numbers).toList().contains(c)){
                System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), lblY.getText());
                txtY.setBorder(new LineBorder(Color.RED, 2));
            }
        }

        new Thread(()->{
            try {
                Thread.sleep(1000);
                //txt.setBackground(Color.WHITE);
                txtX.setBorder(defaultBorder);
                txtY.setBorder(defaultBorder);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();


//        try {
//            x = Integer.parseInt(txtX.getText());
//        }
//        catch (NumberFormatException e) {
//            System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), lblX.getText());
//            txtX.setBorder(new LineBorder(Color.RED, 2));
//        }
//
//        try {
//            y = Integer.parseInt(txtY.getText());
//        }
//        catch (NumberFormatException e) {
//            System.out.format("%s %s IS NOT VALID\n", nameLbl.getText(), lblY.getText());
//            txtY.setBorder(new LineBorder(Color.RED, 2));
//        }
//        new Thread(()->{
//            try {
//                Thread.sleep(1000);
//                //txt.setBackground(Color.WHITE);
//                txtX.setBorder(defaultBorder);
//                txtY.setBorder(defaultBorder);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();

    }

    public void error(){
        txtX.setBorder(new LineBorder(Color.RED, 2));
        txtY.setBorder(new LineBorder(Color.RED, 2));

        new Thread(()->{
            try {
                Thread.sleep(1000);
                //txt.setBackground(Color.WHITE);
                txtX.setBorder(defaultBorder);
                txtY.setBorder(defaultBorder);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
