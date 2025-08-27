package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Move;
import Sim.Orders.Order;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ActionPanel extends VCSPanel {
    //left panel
    private JPanel giveOrderPanel;
    private JButton attackButton;
    private JButton moveButton;

    //middle panel
    private JPanel chooseActionPanel;

    //attack part of middle panel
    private JPanel allyTargetPanel;
    private JPanel enemyTargetPanel;
    private JButton newTargetButton;
    private Map<Entity, JButton> allyButtons = new HashMap<>();
    private Map<Entity, JButton> enemyButtons = new HashMap<>();
    private Entity targetEntity;
    private Entity attackerEntity;

    //move part of middle panel
    private JPanel movePanel;

    //right panel
    private JPanel currentOrderPanel;
    private JPanel currentOrderListPanel;
    private JButton selectedOrderDeleteButton;
    ArrayList <JCheckBox> currentOrders = new ArrayList<>();
    ArrayList <Order> currentOrdersOfEntity = new ArrayList<>();

    //main label of whole panel
    private JLabel selectedUnitLabel;

    //layout for middle panel
    private CardLayout chooseActionLayout;

    private Entity selectedEntity;

    boolean isEnemy = false;
    boolean isRootSelected = true;
    int side;
    Vec2int coordinates;

    public ActionPanel(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());

        selectedUnitLabel = new JLabel("No unit selected.", SwingConstants.CENTER);
        JPanel mergePanel = new JPanel(new GridLayout(1,3,0,0));

        giveOrderPanel = new JPanel(new GridLayout(5,1));
        attackButton = new JButton("Attack");
        moveButton = new JButton("Move");
        attackButton.setEnabled(false);
        moveButton.setEnabled(false);
        giveOrderPanel.add(attackButton);
        giveOrderPanel.add(moveButton);
        giveOrderPanel.setPreferredSize(new Dimension(120,220));
        giveOrderPanel.setBorder(new TitledBorder("Give Order"));

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

        chooseActionLayout = new CardLayout();
        chooseActionPanel = new JPanel(chooseActionLayout);
        chooseActionPanel.add(new JPanel(), "empty");
        chooseActionPanel.add(allyTargetPanel, "ally");
        chooseActionPanel.add(enemyTargetPanel, "enemy");
        chooseActionPanel.add(movePanel, "move");
        chooseActionPanel.setPreferredSize(new Dimension(120,220));
        chooseActionPanel.setBorder(new TitledBorder(""));


        currentOrderPanel = new JPanel(new BorderLayout());
        currentOrderListPanel = new JPanel();
        currentOrderListPanel.setLayout(new BoxLayout(currentOrderListPanel, BoxLayout.Y_AXIS));
        JScrollPane currentOrderScroll = new JScrollPane(currentOrderListPanel);
        currentOrderScroll.setBorder(new TitledBorder("Current Order"));

        selectedOrderDeleteButton = new JButton("Delete");
        selectedOrderDeleteButton.setEnabled(false);
        selectedOrderDeleteButton.addActionListener(e -> {
            deleteSelectedOrders();
        });

        currentOrderPanel.add(currentOrderScroll, BorderLayout.CENTER);
        currentOrderPanel.add(selectedOrderDeleteButton, BorderLayout.SOUTH);
        currentOrderPanel.setPreferredSize(new Dimension(120,220));

        //action listeners for open specific middle panel
        attackButton.addActionListener(e -> {
            if (isEnemy){
                chooseActionLayout.show(chooseActionPanel, "ally");
            }else {
                chooseActionLayout.show(chooseActionPanel, "enemy");
            }
        });
        moveButton.addActionListener(e -> chooseActionLayout.show(chooseActionPanel, "move"));

        //action listener for create move order
        moveConfirmButton.addActionListener(e -> {
            coordinates = moveEditor.readData();
            //currentOrderText.append("Move to " + coordinates + "\n");
            if (selectedEntity != null){
                selectedEntity.addOrder(new Move(app, selectedEntity, new Vec2int(coordinates.x, coordinates.y)));
                refreshCurrentOrderPanel();
            }

        });

        //merge left, middle and right panels into one
        mergePanel.add(giveOrderPanel);
        mergePanel.add(chooseActionPanel);
        mergePanel.add(currentOrderPanel);

        this.add(selectedUnitLabel, BorderLayout.NORTH);
        this.add(mergePanel, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createLineBorder(Color.black,1));

    }

    //find the selected entity from hierarchy panel to give order
    public void selectedUnit(Entity entity){
        this.selectedEntity = entity;
        this.isRootSelected = false;

        this.side = entity.getSide();
        if(side == 1) isEnemy = true;
        else if (side == 0) isEnemy = false;

        attackButton.setEnabled(true);
        moveButton.setEnabled(true);

        selectedUnitLabel.setText("Selected Entity: " + selectedEntity.getName());
        refreshCurrentOrderPanel();
        chooseActionLayout.show(chooseActionPanel, "empty");
    }

    //if hierarchy(not an entity) selected from hierarchy panel
    public void whenRootSelected(Object rootObjectInfo){
        if (rootObjectInfo.equals("Hierarchy")){
            selectedEntity = null;
            selectedUnitLabel.setText("No unit selected.");
            attackButton.setEnabled(false);
            moveButton.setEnabled(false);
            chooseActionLayout.show(chooseActionPanel, "empty");
            isRootSelected = true;
            clearCurrentOrderPanel();
        }else isRootSelected = false;
    }

    //for creating new button for each available targets based on their assigned sides
    public void createNewTargetButton(Entity entity){
        newTargetButton = new JButton(entity.getName());
        if (entity.getSide() == 0){
            allyButtons.put(entity, newTargetButton);
            allyTargetPanel.add(newTargetButton);
        } else {
            enemyButtons.put(entity, newTargetButton);
            enemyTargetPanel.add(newTargetButton);
        }
        //action listener for create attack order
        newTargetButton.addActionListener(e -> {
            targetEntity = entity;
            attackerEntity = selectedEntity;
            if (attackerEntity != null){
                attackerEntity.addOrder(new Attack(app, attackerEntity, targetEntity));
                refreshCurrentOrderPanel();
            }
        });
    }

    //for deleting target button if it's entity destroyed with attack order
    public void deleteTargetButton(Entity entity){
        if(entity == null)
            return;
        JButton deletedButton;
        if (entity.getSide() == 0){
            deletedButton = allyButtons.remove(entity);
            if(deletedButton != null)
                allyTargetPanel.remove(deletedButton);
        } else if (entity.getSide() == 1) {
            deletedButton = enemyButtons.remove(entity);
            if(deletedButton != null)
                enemyTargetPanel.remove(deletedButton);
        }
        revalidate();
        repaint();
    }

    //change the current order panel based on the selected entity
    private void refreshCurrentOrderPanel(){
        currentOrders.clear();
        currentOrdersOfEntity.clear();
        currentOrderListPanel.removeAll();
        if (selectedEntity != null){
            Queue<Order> currentOrders = selectedEntity.getOrders();
            for (Order order : currentOrders){
                JCheckBox box = new JCheckBox(order.createTextToPrint());
                box.addItemListener(l -> updateDeleteButtonState());

                this.currentOrders.add(box);
                currentOrdersOfEntity.add(order);
                currentOrderListPanel.add(box);
            }
        }
        currentOrderListPanel.revalidate();
        currentOrderListPanel.repaint();
        updateDeleteButtonState();
    }

    private void updateDeleteButtonState(){
        if (selectedEntity.equals(null)){
            selectedOrderDeleteButton.setEnabled(false);
            return;
        }
        boolean anySelected = false;
        for (JCheckBox cb : currentOrders){
            if (cb.isSelected()){
                anySelected = true;
                break;
            }
        }
        selectedOrderDeleteButton.setEnabled(anySelected);
    }


    //clear the current order panel for each new selected entity and root
    private void clearCurrentOrderPanel(){
        currentOrdersOfEntity.clear();
        currentOrders.clear();
        currentOrderListPanel.removeAll();
        currentOrderListPanel.revalidate();
        currentOrderListPanel.repaint();
        updateDeleteButtonState();
    }

    private void deleteSelectedOrders(){
        ArrayList<Order> ordersToDelete = new ArrayList<>();
        if (selectedEntity.equals(null)) return;
        for (int i = currentOrders.size()-1; i >= 0; i--){
            if (currentOrders.get(i).isSelected()){
                Order delete = currentOrdersOfEntity.get(i);
                ordersToDelete.add(delete);
            }
        }
        selectedEntity.removeOrder(ordersToDelete);
        refreshCurrentOrderPanel();
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
