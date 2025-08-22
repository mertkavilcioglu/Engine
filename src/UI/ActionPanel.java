package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Move;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ActionPanel extends VCSPanel {
    private JPanel giveOrderPanel;
    private JButton attackButton;
    private JButton moveButton;
    private JPanel allyTargetPanel;
    private JPanel enemyTargetPanel;
    private JButton newTargetButton;
    private JPanel chooseActionPanel;
    private CardLayout cardLayout;
    private JPanel movePanel;
    private JPanel currentOrderPanel;
    private JTextField currentOrderText;
    private JLabel mainLabel;

    boolean isEnemy = false;
    int side;
    Vec2int coordinates;

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

        movePanel = new JPanel(/*new GridLayout(2,1)*/);
        movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.Y_AXIS));
        Vec2intEditor moveEditor = new Vec2intEditor("Position:");
        JButton moveConfirmButton = new JButton("Move");
        moveConfirmButton.setFocusable(false);
        movePanel.add(moveEditor);
        movePanel.add(moveConfirmButton);

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

        moveConfirmButton.addActionListener(e -> {
            coordinates = moveEditor.readData();
            currentOrderText.setText("Moving to " + coordinates);
            if (selectedEntity != null){
                selectedEntity.addOrder(new Move(app, selectedEntity, new Vec2int(coordinates.x, coordinates.y)));

            }

        });

        panel.add(giveOrderPanel);
        panel.add(chooseActionPanel);
        panel.add(currentOrderPanel);

        this.setBorder(BorderFactory.createLineBorder(Color.black,1));

        this.add(mainLabel, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

    }

    Entity selectedEntity;
    public void selectedUnit(Entity entity){
        selectedEntity = entity;
        mainLabel.setText("Selected Entity: " + selectedEntity.getName());
        this.side = entity.getSide();
        if(side == 1) isEnemy = true;
        else if (side == 0) isEnemy = false;

        cardLayout.show(chooseActionPanel, "empty");
    }

    Map<Entity, JButton> allyButtons = new HashMap<>();
    Map<Entity, JButton> enemyButtons = new HashMap<>();
    Entity targetEntity;
    Entity attackerEntity;
    public void createNewTargetButton(Entity entity){
        newTargetButton = new JButton(entity.getName());
        if (entity.getSide() == 0){
            allyButtons.put(entity, newTargetButton);
            allyTargetPanel.add(newTargetButton);
        } else {
            enemyButtons.put(entity, newTargetButton);
            enemyTargetPanel.add(newTargetButton);
        }
        newTargetButton.addActionListener(e -> {
            targetEntity = entity;
            attackerEntity = selectedEntity;
            currentOrderText.setText(targetEntity.getName() + " selected.");
            if (attackerEntity != null){
                attackerEntity.addOrder(new Attack(app, attackerEntity, targetEntity));
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

    public void deleteTargetButton(Entity entity){
        JButton deletedButton;
        if (entity.getSide() == 0){
            deletedButton = allyButtons.remove(entity);
            allyTargetPanel.remove(deletedButton);
        } else if (entity.getSide() == 1) {
            deletedButton = enemyButtons.remove(entity);
            enemyTargetPanel.remove(deletedButton);
        }
        revalidate();
        repaint();
    }

    public void update(int deltaTime){
//        if (isAttacing){
//            //app.attack.attackEntity(targetEntity);
//        }
//        if (isMoving){
//            //app.move.moveTo(coordinates);
//        }
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
