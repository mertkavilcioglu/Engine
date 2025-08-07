package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ActionPanel extends VCSPanel {
    private JPanel orderp;
    private JButton attack;
    private JButton move;
    private JPanel targetp;
    private JButton first;
    private JButton second;
    private JButton third;
    private JPanel movep;
    private JPanel currentp;
    private JLabel label;

    public ActionPanel(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());

        label = new JLabel("Selected Unit:", SwingConstants.CENTER);
        JPanel panel = new JPanel(new GridLayout(1,3,0,0));

        orderp = new JPanel(new GridLayout(5,1));
        attack = new JButton("Attack");
        move = new JButton("Move");

        orderp.add(new JLabel("Give Order"));
        orderp.add(attack);
        orderp.add(move);

        targetp = new JPanel(new GridLayout(5,1));
        first = new JButton("1");
        second = new JButton("2");
        third = new JButton("3");
        targetp.add(new JLabel("Choose Target:"));
        targetp.add(first);
        targetp.add(second);
        targetp.add(third);

        movep = new JPanel(new GridLayout(2,1));
        Vec2intEditor meditor = new Vec2intEditor("Position:");
        JButton mbutton = new JButton("Move");
        mbutton.setFocusable(false);
        //mbutton.setMargin(new Insets(10,10,10,10));   //try to make button smaller.
        movep.add(meditor);
        movep.add(mbutton);

        currentp = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        JTextField text = new JTextField();
        text.setBorder(new TitledBorder("Current Order"));
        text.setEditable(false);
        text.setPreferredSize(new Dimension(120,220));
        currentp.add(new JScrollPane(text));

        JPanel showp = new JPanel(new CardLayout());
        JPanel empty = new JPanel();
        showp.add(empty, "empty");
        showp.add(targetp, "target");
        showp.add(movep, "move");

        orderp.setPreferredSize(new Dimension(120,220));
        orderp.setBorder(BorderFactory.createDashedBorder(Color.black));
        showp.setPreferredSize(new Dimension(120,220));
        showp.setBorder(BorderFactory.createDashedBorder(Color.black));
        currentp.setPreferredSize(new Dimension(120,220));
        //currentp.setBorder(BorderFactory.createDashedBorder(Color.black));

        CardLayout cardLayout = (CardLayout) showp.getLayout();
        attack.addActionListener(e -> cardLayout.show(showp, "target"));
        move.addActionListener(e -> cardLayout.show(showp, "move"));

        //butonların ActionListener ları attactan move a ya da move dan attack a geçince ekrana yazdırıyor ona bakmak lazım....
        first.addActionListener(e -> text.setText("First target"));
        second.addActionListener(e -> text.setText("Second target"));
        third.addActionListener(e -> text.setText("Third target"));
        mbutton.addActionListener(e -> text.setText("Moving to " + meditor.readData()));


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
