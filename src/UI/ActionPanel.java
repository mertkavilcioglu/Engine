package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ActionPanel extends VCSPanel {
    private JPanel orderPanel;
    private JButton attackButton;
    private JButton moveButton;
    private JPanel targetPanel;
    private JScrollPane scrollPane;
    private JPanel movePanel;
    private JPanel currentOrderPanel;
    private JTextField currentOrderText;
    private JLabel mainLabel;

    public ActionPanel(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());

        mainLabel = new JLabel("Selected Unit:", SwingConstants.CENTER);
        JPanel panel = new JPanel(new GridLayout(1,3,0,0));

        orderPanel = new JPanel(new GridLayout(5,1));
        attackButton = new JButton("Attack");
        moveButton = new JButton("Move");

        orderPanel.add(attackButton);
        orderPanel.add(moveButton);

        targetPanel = new JPanel();
        targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.Y_AXIS));
        //targetPanel.setPreferredSize(new Dimension(panel.getPreferredSize()));

        movePanel = new JPanel(new GridLayout(2,1));
        Vec2intEditor meditor = new Vec2intEditor("Position:");
        JButton mbutton = new JButton("Move");
        mbutton.setFocusable(false);
        //mbutton.setMargin(new Insets(10,10,10,10));   //try to make button smaller.
        movePanel.add(meditor);
        movePanel.add(mbutton);

        currentOrderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        currentOrderText = new JTextField();
        currentOrderText.setBorder(new TitledBorder("Current Order"));
        currentOrderText.setEditable(false);
        currentOrderText.setPreferredSize(new Dimension(120,220));
        currentOrderPanel.add(new JScrollPane(currentOrderText));

        scrollPane = new JScrollPane(targetPanel);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.add(targetPanel);  //TODO düzelt!

        JPanel showPanel = new JPanel(new CardLayout());
        JPanel emptyPanel = new JPanel();
        showPanel.add(emptyPanel, "empty");
        showPanel.add(scrollPane, "target");       //TODO
        showPanel.add(movePanel, "move");

        orderPanel.setPreferredSize(new Dimension(120,220));
        orderPanel.setBorder(new TitledBorder("Give Order"));;
        showPanel.setPreferredSize(new Dimension(120,220));
        showPanel.setBorder(new TitledBorder("Choose"));;
        currentOrderPanel.setPreferredSize(new Dimension(120,220));

        CardLayout cardLayout = (CardLayout) showPanel.getLayout();
        attackButton.addActionListener(e -> cardLayout.show(showPanel, "target"));
        moveButton.addActionListener(e -> cardLayout.show(showPanel, "move"));

        mbutton.addActionListener(e -> currentOrderText.setText("Moving to " + meditor.readData()));


        panel.add(orderPanel);
        panel.add(showPanel);
        panel.add(currentOrderPanel);

        this.setBorder(BorderFactory.createLineBorder(Color.black,1));

        this.add(mainLabel, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

    }

    public void newTarget(Entity entity){
        JButton targetButton = new JButton(entity.getName());
        targetPanel.add(targetButton);
        targetButton.addActionListener(e -> currentOrderText.setText(entity.getName() + " selected."));
    }
    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
