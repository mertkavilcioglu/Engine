package UI;

import App.VCSapp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class ActionPanel extends VCSpanel{
    private JPanel orderp;
    private JButton attack;
    private JButton move;
    private JPanel targetp;
    private JPanel movep;
    private JPanel currentp;
    private JLabel label;

    public ActionPanel(VCSapp app){
        super(app);
        this.setLayout(new BorderLayout());

        label = new JLabel("Selected Unit:", SwingConstants.CENTER);
        JPanel panel = new JPanel(new GridLayout(1,3));

        orderp = new JPanel(new GridLayout(5,1));
        attack = new JButton("Attack");
        move = new JButton("Move");

        orderp.add(new JLabel("Give Order"));
        orderp.add(attack);
        orderp.add(move);

        targetp = new JPanel(new GridLayout(5,1));
        targetp.add(new JLabel("Choose Target:"));
        targetp.add(new JButton("1"));
        targetp.add(new JButton("2"));
        targetp.add(new JButton("3"));

        movep = new Vec2intEditor("Position:");

        currentp = new JPanel();
        currentp.add(new JLabel("Current Order"));
        currentp.add(new JTextField());

        JPanel showp = new JPanel(new CardLayout());
        JPanel empty = new JPanel();
        showp.add(empty, "empty");
        showp.add(targetp, "target");
        showp.add(movep, "move");

        orderp.setSize(100,200);
        orderp.setBorder(BorderFactory.createDashedBorder(Color.black));
        showp.setSize(100,200);
        showp.setBorder(BorderFactory.createDashedBorder(Color.black));
        currentp.setSize(100,200);
        currentp.setBorder(BorderFactory.createDashedBorder(Color.black));

        CardLayout cardLayout = (CardLayout) showp.getLayout();
        attack.addActionListener(e -> cardLayout.show(showp, "target"));
        move.addActionListener(e -> cardLayout.show(showp, "move"));

        panel.add(orderp);
        panel.add(showp);
        panel.add(currentp);

        this.setBorder(BorderFactory.createLineBorder(Color.black,1));

        this.add(label, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
