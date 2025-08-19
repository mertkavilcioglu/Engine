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
    private JPanel allyTargetPanel;
    private JPanel enemyTargetPanel;
    private JPanel chooseActionPanel;
    private CardLayout cardLayout;
    private JPanel movePanel;
    private JPanel currentOrderPanel;
    private JTextField currentOrderText;
    private JLabel mainLabel;

    boolean isEnemy = false;
    String selectedUnitName;
    int side;

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

        allyTargetPanel = new JPanel();
        allyTargetPanel.setLayout(new BoxLayout(allyTargetPanel, BoxLayout.Y_AXIS));
        allyTargetPanel.setBorder(new TitledBorder("Choose Target: "));

        enemyTargetPanel = new JPanel();
        enemyTargetPanel.setLayout(new BoxLayout(enemyTargetPanel, BoxLayout.Y_AXIS));
        enemyTargetPanel.setBorder(new TitledBorder("Choose Target: "));

        movePanel = new JPanel(new GridLayout(2,1));
        Vec2intEditor meditor = new Vec2intEditor("Position:");
        JButton mbutton = new JButton("Move");
        mbutton.setFocusable(false);
        //try to make button smaller to good look.
        movePanel.add(meditor);
        movePanel.add(mbutton);

        currentOrderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        currentOrderText = new JTextField();
        currentOrderText.setBorder(new TitledBorder("Current Order"));
        currentOrderText.setEditable(false);
        currentOrderText.setPreferredSize(new Dimension(120,220));
        currentOrderPanel.add(new JScrollPane(currentOrderText));

        cardLayout = new CardLayout();
        chooseActionPanel = new JPanel(cardLayout);
        chooseActionPanel.add(new JPanel(), "empty");
        chooseActionPanel.add(allyTargetPanel, "ally");
        chooseActionPanel.add(enemyTargetPanel, "enemy");
        chooseActionPanel.add(movePanel, "move");

        giveOrderPanel.setPreferredSize(new Dimension(120,220));
        giveOrderPanel.setBorder(new TitledBorder("Give Order"));;
        chooseActionPanel.setPreferredSize(new Dimension(120,220));
        chooseActionPanel.setBorder(new TitledBorder(""));;
        currentOrderPanel.setPreferredSize(new Dimension(120,220));

        attackButton.addActionListener(e -> {
            if (isEnemy){
                cardLayout.show(chooseActionPanel, "ally");
            }else {
                cardLayout.show(chooseActionPanel, "enemy");
            }
        });
        moveButton.addActionListener(e -> cardLayout.show(chooseActionPanel, "move"));

        mbutton.addActionListener(e -> {
            currentOrderText.setText("Moving to " + meditor.readData());
            if (selectedUnitName != null){
                log(selectedUnitName + " moving to " + meditor.readData());
            }

        });

        panel.add(giveOrderPanel);
        panel.add(chooseActionPanel);
        panel.add(currentOrderPanel);

        this.setBorder(BorderFactory.createLineBorder(Color.black,1));

        this.add(mainLabel, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

    }

    public void selectedUnit(Entity entity){
        this.selectedUnitName = entity.getName();
        mainLabel.setText("Selected Entity: " + selectedUnitName);
        this.side = entity.getSide();
        if(side == 1) isEnemy = true;
        else if (side == 0) isEnemy = false;

        cardLayout.show(chooseActionPanel, "empty");
    }

    public void newTarget(Entity entity){
        JButton targetButton = new JButton(entity.getName());
        if (entity.getSide() == 0){
            allyTargetPanel.add(targetButton);
        } else {
            enemyTargetPanel.add(targetButton);
        }
        targetButton.addActionListener(e -> {
            currentOrderText.setText(entity.getName() + " selected.");
            if (selectedUnitName != null){
                log(selectedUnitName + " going to attack " + entity.getName());
            }
        });


        /* JButton targetAllyButton = new JButton();
        JButton targetEnemyButton = new JButton();
        if(entity.getSide() == 0) {
            targetAllyButton.add(new JLabel(entity.getName()));
            targetAllyButton.setVisible(isEnemy);
            targetPanel.add(targetAllyButton);
            targetAllyButton.addActionListener(e -> currentOrderText.setText(entity.getName() + " selected."));
        } else if (entity.getSide() == 1) {
            targetEnemyButton.add(new JLabel(entity.getName()));
            targetEnemyButton.setVisible(!isEnemy);
            targetPanel.add(targetEnemyButton);
            targetEnemyButton.addActionListener(e -> currentOrderText.setText(entity.getName() + " selected."));
        } */

    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
