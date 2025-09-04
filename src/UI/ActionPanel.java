package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.Orders.Attack;
import Sim.Orders.Follow;
import Sim.Orders.Move;
import Sim.Orders.Order;
import Vec.Vec2int;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.security.PublicKey;
import java.util.*;
import java.util.List;

public class ActionPanel extends VCSPanel {
    //left panel
    private JPanel giveOrderPanel;
    private JButton attackButton;
    private JButton moveButton;
    private JButton followButton;

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

    //follow part of middle panel
    private JPanel followPanel;
    private JComboBox<String> followTargetBox;
    private DefaultComboBoxModel<String> followTargetData;
    private JLabel followTimeLabel;
    private JTextField followTimeText;
    private JButton followCreateButton;
    private ArrayList<Entity> allCreatedEntites = new ArrayList<>();
    private ArrayList<Entity> followEntityList = new ArrayList<>();

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
    boolean isAttack = false;
    int side;
    String type;
    Vec2int coordinates;

    //to update current order panel when an order is completed.
    public Timer timer = new Timer(1000, e->orderRefresher());


    public ActionPanel(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());

        selectedUnitLabel = new JLabel("No unit selected.", SwingConstants.CENTER);
        JPanel mergePanel = new JPanel(new GridLayout(1,3,0,0));

        giveOrderPanel = new JPanel(new GridLayout(3,1, 0, 20));
        attackButton = new JButton("Attack");
        moveButton = new JButton("Move");
        followButton = new JButton("Follow");
        updateOrderButtonsState(false);
        giveOrderPanel.add(attackButton);
        giveOrderPanel.add(moveButton);
        giveOrderPanel.add(followButton);
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

        followPanel = new JPanel();
        followPanel.setLayout(new BoxLayout(followPanel, BoxLayout.Y_AXIS));
        followPanel.setBorder(new TitledBorder("Choose Unit to Follow: "));
        JPanel comboPanel = new JPanel(new BorderLayout());
        followTargetData = new DefaultComboBoxModel<>();
        followTargetBox = new JComboBox<>(followTargetData);
        comboPanel.add(new JLabel("Target: "), BorderLayout.WEST);
        comboPanel.add(followTargetBox, BorderLayout.CENTER);
        JPanel timePanel = new JPanel(new FlowLayout());
        followTimeLabel = new JLabel("Follow Time(s):");
        followTimeText = new JTextField(10);
        timePanel.add(followTimeLabel);
        timePanel.add(followTimeText);
        JPanel createButtonPanel = new JPanel();
        followCreateButton = new JButton("Follow");
        followCreateButton.setFocusable(false);
        followCreateButton.addActionListener(e -> {
            if (selectedEntity == null) return;
            int followIndex = followTargetBox.getSelectedIndex();
            if ((followIndex < 0 && followIndex >= followEntityList.size())) return;
            Entity choosenEntity = followEntityList.get(followIndex);
            int followTime;
            try {
                followTime = Integer.parseInt(followTimeText.getText().trim());
                if (followTime < 0){ followTime = 0; }
            } catch (NumberFormatException ex) {
                followTime = 0;
            }
            selectedEntity.addOrder(new Follow(app, selectedEntity, choosenEntity, followTime));
            refreshCurrentOrderPanel();
        });
        createButtonPanel.add(followCreateButton);
        followPanel.add(comboPanel);
        followPanel.add(Box.createVerticalStrut(8));
        followPanel.add(timePanel);
        followPanel.add(Box.createVerticalStrut(8));
        followPanel.add(createButtonPanel);

        chooseActionLayout = new CardLayout();
        chooseActionPanel = new JPanel(chooseActionLayout);
        chooseActionPanel.add(new JPanel(), "empty");
        chooseActionPanel.add(allyTargetPanel, "ally");
        chooseActionPanel.add(enemyTargetPanel, "enemy");
        chooseActionPanel.add(movePanel, "move");
        chooseActionPanel.add(followPanel, "follow");
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
            hideTargetButtons(selectedEntity);
            isAttack = true;
            if (isEnemy){
                chooseActionLayout.show(chooseActionPanel, "ally");
            }else {
                chooseActionLayout.show(chooseActionPanel, "enemy");
            }
        });

        moveButton.addActionListener(e -> chooseActionLayout.show(chooseActionPanel, "move"));

        followButton.addActionListener(e -> {
            isAttack = false;
            chooseActionLayout.show(chooseActionPanel, "follow");
            followTargetData.removeAllElements();
            followEntityList.clear();
            setFollowTargets();
            followCreateButton.setEnabled((followTargetData.getSize() > 0 && selectedEntity != null));
        });

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
        this.type = entity.getType();

        this.side = entity.getSide();
        if(side == 1) isEnemy = true;
        else if (side == 0) isEnemy = false;

        timer.start();
        updateOrderButtonsState(true);

        selectedUnitLabel.setText("Selected Entity: " + selectedEntity.getName());
        refreshCurrentOrderPanel();
        chooseActionLayout.show(chooseActionPanel, "empty");
    }

    //if hierarchy(not an entity) selected from hierarchy panel
    public void whenRootSelected(Object rootObjectInfo){
        if (rootObjectInfo.equals("Hierarchy")){
            selectedEntity = null;
            selectedUnitLabel.setText("No unit selected.");
            updateOrderButtonsState(false);
            chooseActionLayout.show(chooseActionPanel, "empty");
            isRootSelected = true;
            clearCurrentOrderPanel();
        }else isRootSelected = false;
        timer.stop();
    }

    public void disablePanel(){
        selectedEntity = null;
        selectedUnitLabel.setText("No unit selected.");
        updateOrderButtonsState(false);
        chooseActionLayout.show(chooseActionPanel, "empty");
        isRootSelected = true;
        clearCurrentOrderPanel();
        timer.stop();
    }

    //for creating new button for each available targets based on their assigned sides
    public void createNewTargetButton(Entity entity){
        newTargetButton = new JButton(entity.getName());
        allCreatedEntites.add(entity);
        //createFollowButtonList(entity);
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
            if (isAttack){
                if (attackerEntity != null){
                    attackerEntity.addOrder(new Attack(app, attackerEntity, targetEntity));
                    refreshCurrentOrderPanel();
                }
            }
        });
    }

    public void hideTargetButtons(Entity selectedOne){
        String typeOfSelected = selectedOne.getType();
        List<Entity> detectedEntitiesFromRadar = selectedOne.getDetectedEntities();
        allyTargetPanel.removeAll();
        enemyTargetPanel.removeAll();
        int sideOfSelected = selectedOne.getSide();
        if (typeOfSelected.equals("Plane")){
            if (sideOfSelected == 0){
                for (Entity keyEntity : enemyButtons.keySet()){
                    enemyTargetPanel.add(enemyButtons.get(keyEntity));
                }
            } else {
                for (Entity keyEntity : allyButtons.keySet()){
                    allyTargetPanel.add(allyButtons.get(keyEntity));
                }
            }
        } else if (typeOfSelected.equals("Ship")) {
            if (sideOfSelected == 1){
                for (Entity keyEntity : allyButtons.keySet()){
                    if (keyEntity.getType().equals("Ship")){
                        allyTargetPanel.add(allyButtons.get(keyEntity));
                    } else if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getSide() == 0){
                            allyTargetPanel.add(allyButtons.get(keyEntity));
                        }
                    }
                }
            } else {
                for (Entity keyEntity : enemyButtons.keySet()){
                    if (keyEntity.getType().equals("Ship")){
                        enemyTargetPanel.add(enemyButtons.get(keyEntity));
                    } else if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getSide() == 1){
                            enemyTargetPanel.add(enemyButtons.get(keyEntity));
                        }
                    }
                }
            }
        } else if (typeOfSelected.equals("Tank")) {
            if (sideOfSelected == 1){
                for (Entity keyEntity : allyButtons.keySet()){
                    if (keyEntity.getType().equals("Tank")){
                        allyTargetPanel.add(allyButtons.get(keyEntity));
                    } else if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getSide() == 0){
                            allyTargetPanel.add(allyButtons.get(keyEntity));
                        }
                    }
                }
            } else {
                for (Entity keyEntity : enemyButtons.keySet()){
                    if (keyEntity.getType().equals("Tank")){
                        enemyTargetPanel.add(enemyButtons.get(keyEntity));
                    } else if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getSide() == 1){
                            enemyTargetPanel.add(enemyButtons.get(keyEntity));
                        }
                    }
                }
            }
        }
        allyTargetPanel.revalidate();
        allyTargetPanel.repaint();
        enemyTargetPanel.revalidate();
        enemyTargetPanel.repaint();


//        if (sideOfSelected == 0){
//            allyTargetPanel.removeAll();
//            for (Entity mapEntity : allyButtons.keySet()){
//                if (typeOfSelected.equals("Plane")){
//                    allyTargetPanel.add(allyButtons.get(mapEntity));
//                } else if (typeOfSelected.equals(mapEntity.getType())) {
//                    allyTargetPanel.add(allyButtons.get(mapEntity));
//                }
//            }
//        } else if (sideOfSelected == 1){
//            enemyTargetPanel.removeAll();
//            for (Entity mapEntity : enemyButtons.keySet()){
//                if (typeOfSelected.equals("Plane")){
//                    enemyTargetPanel.add(enemyButtons.get(mapEntity));
//                } else if (typeOfSelected.equals(mapEntity.getType())){
//                    enemyTargetPanel.add(enemyButtons.get(mapEntity));
//                }
//            }
//        }
    }

    //for deleting target button if it's entity destroyed with attack order
    public void deleteTargetButton(Entity entity){
        if(entity == null)
            return;
        //for attack panel
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
        //for follow panel
        for (int i = 0; i < followEntityList.size(); i++){
            if (entity.equals(followEntityList.get(i))){
                followEntityList.remove(entity);
                allCreatedEntites.remove(entity);
                followTargetData.removeElement(entity);
                setFollowTargets();
            }
        }

        revalidate();
        repaint();
    }

    public void setFollowTargets(){
        if (selectedEntity != null){
            for (int i = 0; i < allCreatedEntites.size(); i++ ){
                if (selectedEntity != allCreatedEntites.get(i)){
                    Entity ent = allCreatedEntites.get(i);
                    followEntityList.add(ent);
                    followTargetData.addElement(ent.getName());
                }
            }
        }
    }

    public void orderRefresher(){
        hideTargetButtons(selectedEntity);
        boolean isDone = selectedEntity.getCurrentOrderState();
        if (isDone){
            refreshCurrentOrderPanel();
            isDone = false;
        }
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
                box.addItemListener(l -> {
                    updateDeleteButtonState();
                });

                this.currentOrders.add(box);
                currentOrdersOfEntity.add(order);
                currentOrderListPanel.add(box);
            }
        }
        currentOrderListPanel.revalidate();
        currentOrderListPanel.repaint();
        updateDeleteButtonState();
    }

    private void updateOrderButtonsState(boolean state){
        attackButton.setEnabled(state);
        moveButton.setEnabled(state);
        followButton.setEnabled(state);
    }

    private void updateDeleteButtonState(){
        if (selectedEntity == null){
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
        if (selectedEntity == null) return;
        for (int i = currentOrders.size()-1; i >= 0; i--){
            if (currentOrders.get(i).isSelected()){
                Order delete = currentOrdersOfEntity.get(i);
                ordersToDelete.add(delete);
                String deleteLog = String.format("%s of %s is deleted.", delete.toString(), selectedEntity.getName());
                app.log(deleteLog);
            }
        }
        selectedEntity.removeOrder(ordersToDelete);
        refreshCurrentOrderPanel();
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
