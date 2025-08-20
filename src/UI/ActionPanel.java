package UI;

import App.VCSApp;
import Sim.Entity;
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
    String selectedUnitName;
    String followerUnitName;
    int side;
    String targetName;
    String movingUnitName;
    Vec2int coordinates;
    boolean isAttacing = false;
    public boolean isMoving = false;

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
            coordinates = meditor.readData();
            currentOrderText.setText("Moving to " + coordinates);
            if (selectedUnitName != null){
                movingUnitName = selectedUnitName;
                app.move.moveTo(movingUnitName, coordinates);
                isMoving = true;
                log(movingUnitName + " moving to " + coordinates);
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
        this.selectedUnitName = entity.getName();
        mainLabel.setText("Selected Entity: " + selectedUnitName);
        this.side = entity.getSide();
        if(side == 1) isEnemy = true;
        else if (side == 0) isEnemy = false;

        cardLayout.show(chooseActionPanel, "empty");
    }

    Map<Entity, JButton> allyButtons = new HashMap<>();
    Map<Entity, JButton> enemyButtons = new HashMap<>();
    Entity targetEntity;
    Entity attackerEntity;
    public void createNewTarget(Entity entity){
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
            currentOrderText.setText(targetName + " selected.");
            if (followerUnitName != null){
                app.attack.attackEntity(attackerEntity, targetEntity);
                isAttacing = true;
                log(followerUnitName + " going to attack " + targetName);
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

    public void deleteTarget(Entity entity){
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
        if (isAttacing){
            app.attack.attackEntity(attackerEntity, targetEntity);
        }
        if (isMoving){
            app.move.moveTo(movingUnitName, coordinates);
        }
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
