package UI;

import App.VCSApp;
import Sim.Entity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ActionPanel extends VCSPanel {
    private JPanel giveOrderPanel;
    private JButton attackButton;
    private JButton moveButton;
    JPanel chooseActionPanel;
    private JPanel chooseTargetPanel;
    private JScrollPane scrollPane;
    private JPanel setMovePanel;
    private JPanel currentOrderPanel;
    private JTextField currentOrderText;
    private JLabel mainLabel;
    boolean isEnemy = false;

    int side;
    JPanel targetAllyPage;
    JPanel targetEnemyPage;

    public ActionPanel(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());

        mainLabel = new JLabel("No unit selected.", SwingConstants.CENTER);
        JPanel panel = new JPanel(new GridLayout(1,3,0,0));

        giveOrderPanel = new JPanel(new GridLayout(5,1));
        attackButton = new JButton("Attack");
        moveButton = new JButton("Move");

        giveOrderPanel.add(attackButton);
        giveOrderPanel.add(moveButton);

        chooseTargetPanel = new JPanel();
        chooseTargetPanel.setLayout(new BoxLayout(chooseTargetPanel, BoxLayout.Y_AXIS));
        //targetPanel.setPreferredSize(new Dimension(panel.getPreferredSize()));

        setMovePanel = new JPanel(new GridLayout(2,1));
        Vec2intEditor moveEditor = new Vec2intEditor("Position:");
        JButton moveButton = new JButton("Move");
        moveButton.setFocusable(false);
        //mbutton.setMargin(new Insets(10,10,10,10));   //try to make button smaller.
        setMovePanel.add(moveEditor);
        setMovePanel.add(moveButton);

        currentOrderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        currentOrderText = new JTextField();
        currentOrderText.setBorder(new TitledBorder("Current Order"));
        currentOrderText.setEditable(false);
        currentOrderText.setPreferredSize(new Dimension(120,220));
        currentOrderPanel.add(new JScrollPane(currentOrderText));

        scrollPane = new JScrollPane(chooseTargetPanel);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.add(chooseTargetPanel);  //TODO düzelt!

        chooseActionPanel = new JPanel(new CardLayout());
        JPanel emptyPanel = new JPanel();
        chooseActionPanel.add(emptyPanel, "empty");
        chooseActionPanel.add(scrollPane, "target");       //TODO
        //chooseActionPanel.add(targetAllyPage, "targetAlly");
        //chooseActionPanel.add(targetEnemyPage, "targetEnemy");
        chooseActionPanel.add(setMovePanel, "move");

        giveOrderPanel.setPreferredSize(new Dimension(120,220));
        giveOrderPanel.setBorder(new TitledBorder("Give Order"));
        chooseActionPanel.setPreferredSize(new Dimension(120,220));
        chooseActionPanel.setBorder(new TitledBorder("Choose"));
        currentOrderPanel.setPreferredSize(new Dimension(120,220));

        CardLayout cardLayout = (CardLayout) chooseActionPanel.getLayout();
        attackButton.addActionListener(e -> cardLayout.show(chooseActionPanel, "empty"));
        attackButton.addActionListener(e -> cardLayout.show(chooseActionPanel, "target"));
        moveButton.addActionListener(e -> cardLayout.show(chooseActionPanel, "move"));
        moveButton.addActionListener(e -> currentOrderText.setText("Moving to " + moveEditor.readData()));


        panel.add(giveOrderPanel);
        panel.add(chooseActionPanel);
        panel.add(currentOrderPanel);

        this.setBorder(BorderFactory.createLineBorder(Color.black,1));

        this.add(mainLabel, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

    }

    public void selectedUnit(Entity entity){
        mainLabel.setText("Selected Unit: "+entity.getName());
        this.side = entity.getSide();
        if(side == 1) {
            //isEnemy = true;
            //targetAllyButton.setVisible(true);
            attackButton.addActionListener(e -> {
                targetEnemyPage.setVisible(false);
                targetAllyPage.setVisible(true);});
        }
        else if (side == 0) {
            //isEnemy = false;
            //targetEnemyButton.setVisible(true);
            attackButton.addActionListener(e -> {
                targetAllyPage.setVisible(false);
                targetEnemyPage.setVisible(true);});
        }
    }

    public void newTarget(Entity entity){
        targetAllyPage = new JPanel();
        targetAllyPage.setLayout(new BoxLayout(targetAllyPage, BoxLayout.Y_AXIS));
        targetEnemyPage = new JPanel();
        targetEnemyPage.setLayout(new BoxLayout(targetEnemyPage, BoxLayout.Y_AXIS));
        JButton targetAllyButton = new JButton();
        JButton targetEnemyButton = new JButton();
        if(entity.getSide() == 0) { /*
            targetAllyButton.add(new JLabel(entity.getName()));
            //targetAllyButton.setVisible(false);
            targetAllyButton.addActionListener(e -> currentOrderText.setText(entity.getName() + " selected."));
            targetAllyPage.add(targetAllyButton);
            chooseTargetPanel.add(targetAllyPage);
            targetAllyButton.setVisible(false); */
        } else if (entity.getSide() == 1) { /*
            targetEnemyButton.add(new JLabel(entity.getName()));
            targetEnemyButton.setVisible(false);
            targetEnemyButton.addActionListener(e -> currentOrderText.setText(entity.getName() + " selected."));
            targetEnemyPage.add(targetEnemyButton);
            chooseTargetPanel.add(targetEnemyPage);
            targetEnemyPage.setVisible(false); */
        }

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
