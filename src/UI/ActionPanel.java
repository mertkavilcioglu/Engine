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
import java.awt.*;
import java.util.*;
import java.util.List;

public class ActionPanel extends VCSPanel {
    //left panel
    private JPanel giveOrderPanel;
    private JButton attackButton;
    private JButton moveButton;
    private JButton followButton;

    //middle panel
    private JPanel orderDetailPanel;

    //attack part of middle panel
    private JPanel allyTargetPanel;
    private JPanel enemyTargetPanel;
    private JButton targetButton;
    private Map<Entity, JButton> allyButtons = new HashMap<>();
    private Map<Entity, JButton> enemyButtons = new HashMap<>();
    private Entity targetEntity;
    private Entity attackerEntity;

    //move part of middle panel
    private JPanel movePanel;
    private Vec2intEditor moveEditor;
    private JButton enableFromMapButton;
    private JButton disableFromMapButton;

    //follow part of middle panel
    private JPanel followPanel;
    private JComboBox<String> followTargetBox;
    private DefaultComboBoxModel<String> followTargetData;
    private JLabel followTimeLabel;
    private JTextField followTimeText;
    private JButton followOrderCreateButton;
    private ArrayList<Entity> allCreatedEntites = new ArrayList<>();
    private ArrayList<Entity> followableEntityList = new ArrayList<>();

    //right panel
    private JPanel currentOrderPanel;
    private JPanel currentOrdersListPanel;
    private JButton selectedOrderDeleteButton;
    ArrayList <JCheckBox> currentOrderBoxes = new ArrayList<>();
    ArrayList <Order> currentOrdersOfEntity = new ArrayList<>();

    //main label of whole panel
    private JLabel selectedUnitLabel;

    //layout for middle panel
    private CardLayout orderDetailLayout;

    private Entity selectedEntity;

    boolean isEnemy = false;
    boolean isRootSelected = true;
    boolean isAttackAction = false;
    boolean isPaused = false;
    int sideOfEntity;
    String typeOfEntity;
    Vec2int coordinatesToMove;
    private Color panelBgColor;
    private Color borderColor;

    //to update current order panel when an order is completed.
    public Timer timer = new Timer(300, e-> currentOrderRefresher());


    public ActionPanel(VCSApp app){
        super(app);
        this.setLayout(new BorderLayout());

        borderColor = app.uiColorManager.DARK_MAP_BG_BLUE_COLOR;
        panelBgColor = app.uiColorManager.DARK_PANEL_COLOR;
        setBackground(panelBgColor);
        selectedUnitLabel = new JLabel("No unit selected.", SwingConstants.CENTER);
        selectedUnitLabel.setForeground(Color.WHITE);
        JPanel mergePanel = new JPanel(new GridLayout(1,3,0,0));
        mergePanel.setBackground(panelBgColor);

        giveOrderPanel = new JPanel(new GridLayout(3,1, 0, 20));
        giveOrderPanel.setBackground(panelBgColor);

        attackButton = new JButton("Attack");
        moveButton = new JButton("Move");
        followButton = new JButton("Follow");

        attackButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        moveButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        followButton.setBackground(app.uiColorManager.BUTTON_COLOR);

        attackButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        moveButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        followButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);

        updateOrderButtonsState(false);
        giveOrderPanel.add(attackButton);
        giveOrderPanel.add(moveButton);
        giveOrderPanel.add(followButton);
        giveOrderPanel.setPreferredSize(new Dimension(app.getWindow().getWidth(),220));

        TitledBorder giveOrderTitledBorder = new TitledBorder("Give Order");
        giveOrderTitledBorder.setTitleColor(Color.WHITE);
        giveOrderTitledBorder.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        giveOrderPanel.setBorder(giveOrderTitledBorder);

        allyTargetPanel = new JPanel();
        allyTargetPanel.setLayout(new BoxLayout(allyTargetPanel, BoxLayout.Y_AXIS));
        allyTargetPanel.setBackground(panelBgColor);

        TitledBorder chooseTargetTitledBorder = new TitledBorder("Choose Target: ");
        chooseTargetTitledBorder.setTitleColor(Color.WHITE);
        chooseTargetTitledBorder.setBorder(BorderFactory.createLineBorder(borderColor,2));
        allyTargetPanel.setBorder(chooseTargetTitledBorder);

        enemyTargetPanel = new JPanel();
        enemyTargetPanel.setLayout(new BoxLayout(enemyTargetPanel, BoxLayout.Y_AXIS));
        enemyTargetPanel.setBorder(chooseTargetTitledBorder);
        enemyTargetPanel.setBackground(panelBgColor);

        movePanel = new JPanel();
        movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.Y_AXIS));
        movePanel.setBackground(panelBgColor);

        TitledBorder setMovePosTitledBorder = new TitledBorder("Set Position to Move: ");
        setMovePosTitledBorder.setTitleColor(Color.WHITE);
        setMovePosTitledBorder.setBorder(BorderFactory.createLineBorder(borderColor,2));
        movePanel.setBorder(setMovePosTitledBorder);

        JLabel chooseModeLabel = new JLabel("Choose Position From Map:");
        chooseModeLabel.setForeground(Color.WHITE);
        chooseModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        enableFromMapButton = new JButton("ON");
        disableFromMapButton = new JButton("OFF");
        enableFromMapButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        enableFromMapButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        enableFromMapButton.setFocusable(false);
        disableFromMapButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        disableFromMapButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        disableFromMapButton.setFocusable(false);
        JPanel moveFomMapPanel = new JPanel();
        moveFomMapPanel.setLayout(new BoxLayout(moveFomMapPanel, BoxLayout.Y_AXIS));
        moveFomMapPanel.setBackground(panelBgColor);
        JPanel fromMapButtonsPanel = new JPanel(new FlowLayout());
        fromMapButtonsPanel.setBackground(panelBgColor);
        fromMapButtonsPanel.add(enableFromMapButton);
        fromMapButtonsPanel.add(disableFromMapButton);
        moveFomMapPanel.add(new JLabel(" "));
        moveFomMapPanel.add(chooseModeLabel);
        moveFomMapPanel.add(fromMapButtonsPanel);
        moveEditor = new Vec2intEditor("Manuel Mode:", app);
        JButton moveConfirmButton = new JButton("Move");
        moveConfirmButton.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        moveConfirmButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        moveConfirmButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        moveConfirmButton.setFocusable(false);
        movePanel.add(moveEditor);
        movePanel.add(moveConfirmButton);
        movePanel.add(moveFomMapPanel);

        followPanel = new JPanel();
        followPanel.setLayout(new BoxLayout(followPanel, BoxLayout.Y_AXIS));
        followPanel.setBackground(panelBgColor);

        TitledBorder chooseUnitTitledBorder = new TitledBorder("Choose Unit to Follow: ");
        chooseUnitTitledBorder.setTitleColor(Color.WHITE);
        chooseUnitTitledBorder.setBorder(BorderFactory.createLineBorder(borderColor,2));
        followPanel.setBorder(chooseUnitTitledBorder);

        JPanel comboPanel = new JPanel(new BorderLayout());
        followTargetData = new DefaultComboBoxModel<>();
        followTargetBox = new JComboBox<>(followTargetData);
        //followTargetBox.setBackground(app.uiColorManager.BUTTON_COLOR);
        followTargetBox.setRequestFocusEnabled(false);
        followTargetBox.setForeground(Color.BLACK);
        comboPanel.setBackground(panelBgColor);
        JLabel targetLabel = new JLabel("Target: ");
        targetLabel.setForeground(Color.WHITE);
        comboPanel.add(targetLabel, BorderLayout.WEST);
        comboPanel.add(followTargetBox, BorderLayout.CENTER);
        JPanel timePanel = new JPanel(new FlowLayout());
        timePanel.setBackground(panelBgColor);
        followTimeLabel = new JLabel("Follow Time(s):");
        followTimeLabel.setBackground(panelBgColor);
        followTimeLabel.setForeground(Color.WHITE);
        followTimeText = new JTextField(10);
        timePanel.add(followTimeLabel);
        timePanel.add(followTimeText);
        JPanel createButtonPanel = new JPanel();
        followOrderCreateButton = new JButton("Follow");
        followOrderCreateButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        followOrderCreateButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        followOrderCreateButton.setFocusable(false);
        followOrderCreateButton.addActionListener(e -> {
            if (selectedEntity == null) return;
            int followIndex = followTargetBox.getSelectedIndex();
            if ((followIndex < 0 && followIndex >= followableEntityList.size())) return;
            Entity choosenEntity = followableEntityList.get(followIndex);
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
        createButtonPanel.setBackground(panelBgColor);
        createButtonPanel.add(followOrderCreateButton);
        followPanel.add(comboPanel);
        followPanel.add(Box.createVerticalStrut(8));
        followPanel.add(timePanel);
        followPanel.add(Box.createVerticalStrut(8));
        followPanel.add(createButtonPanel);

        orderDetailLayout = new CardLayout();
        orderDetailPanel = new JPanel(orderDetailLayout);
        orderDetailPanel.setBackground(panelBgColor);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(panelBgColor);
        TitledBorder emptyBorder = new TitledBorder("Order details:");
        emptyBorder.setTitleColor(Color.WHITE);
        emptyBorder.setBorder(BorderFactory.createLineBorder(borderColor,2));
        emptyPanel.setBorder(emptyBorder);
        orderDetailPanel.add(emptyPanel, "empty");
        orderDetailPanel.add(allyTargetPanel, "ally");
        orderDetailPanel.add(enemyTargetPanel, "enemy");
        orderDetailPanel.add(movePanel, "move");
        orderDetailPanel.add(followPanel, "follow");
        orderDetailPanel.setPreferredSize(new Dimension(app.getWindow().getWidth(),220));
        //chooseActionPanel.setBorder(new TitledBorder(""));


        currentOrderPanel = new JPanel(new BorderLayout());
        currentOrdersListPanel = new JPanel();
        currentOrdersListPanel.setLayout(new BoxLayout(currentOrdersListPanel, BoxLayout.Y_AXIS));
        currentOrdersListPanel.setBackground(panelBgColor);
        JScrollPane currentOrderScroll = new JScrollPane(currentOrdersListPanel);
        currentOrderScroll.getVerticalScrollBar().setBackground(panelBgColor);


        TitledBorder currentOrderTitledBorder = new TitledBorder("Current Order");
        currentOrderTitledBorder.setTitleColor(Color.WHITE);
        currentOrderTitledBorder.setBorder(BorderFactory.createLineBorder(borderColor,2));
        currentOrderScroll.setBorder(currentOrderTitledBorder);
        currentOrderScroll.setBackground(panelBgColor);

        selectedOrderDeleteButton = new JButton("Delete");
        selectedOrderDeleteButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        selectedOrderDeleteButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        selectedOrderDeleteButton.setEnabled(false);
        selectedOrderDeleteButton.addActionListener(e -> {
            deleteSelectedOrders();
        });

        currentOrderPanel.add(currentOrderScroll, BorderLayout.CENTER);
        currentOrderPanel.add(selectedOrderDeleteButton, BorderLayout.SOUTH);
        currentOrderPanel.setBackground(panelBgColor);
        currentOrderPanel.setPreferredSize(new Dimension(app.getWindow().getWidth(),220));
        attackButton.setFocusable(false);

        //action listeners for open specific middle panel
        attackButton.addActionListener(e -> {
            setMoveMode(false);
            showTargetButtons(selectedEntity);
            isAttackAction = true;
            if (isEnemy){
                orderDetailLayout.show(orderDetailPanel, "ally");
            }else {
                orderDetailLayout.show(orderDetailPanel, "enemy");
            }
        });

        moveButton.setFocusable(false);
        moveButton.addActionListener(e -> {
            orderDetailLayout.show(orderDetailPanel, "move");
        });
        enableFromMapButton.addActionListener(e -> {
            setMoveMode(true);
            enableFromMapButton.setBackground(Color.GRAY);
            enableFromMapButton.setForeground(Color.lightGray);
        });
        disableFromMapButton.addActionListener(e -> {
            setMoveMode(false);
        });

        followButton.setFocusable(false);
        followButton.addActionListener(e -> {
            isAttackAction = false;
            setMoveMode(false);
            orderDetailLayout.show(orderDetailPanel, "follow");
            followTargetData.removeAllElements();
            followableEntityList.clear();
            setFollowTargets();
            followOrderCreateButton.setEnabled((followTargetData.getSize() > 0 && selectedEntity != null));
        });

        //action listener for create move order
        moveConfirmButton.addActionListener(e -> {
            try{
                coordinatesToMove = moveEditor.readData();
                if (selectedEntity != null){
                    selectedEntity.addOrder(new Move(app, selectedEntity, new Vec2int(coordinatesToMove.x, coordinatesToMove.y)));
                    refreshCurrentOrderPanel();
                    setMoveMode(false);
                }
            }
            catch(Exception ex){
                moveEditor.dataValidate();
            }
            //currentOrderText.append("Move to " + coordinates + "\n");


        });

        //merge left, middle and right panels into one
        mergePanel.add(giveOrderPanel);
        mergePanel.add(orderDetailPanel);
        mergePanel.add(currentOrderPanel);

        this.add(selectedUnitLabel, BorderLayout.NORTH);
        this.add(mergePanel, BorderLayout.CENTER);
        //this.setBorder(BorderFactory.createLineBorder(Color.black,1));



    }

    //find the selected entity from hierarchy panel to give order
    public void setSelectedEntity(Entity entity){
        setMoveMode(false);
        this.selectedEntity = entity;
        this.isRootSelected = false;
        this.typeOfEntity = entity.getType();

        this.sideOfEntity = entity.getSide();
        if(sideOfEntity == 1) isEnemy = true;
        else if (sideOfEntity == 0) isEnemy = false;

        timer.start();
        updateOrderButtonsState(true);

        selectedUnitLabel.setText("Selected Entity: " + selectedEntity.getName());
        refreshCurrentOrderPanel();
        orderDetailLayout.show(orderDetailPanel, "empty");
    }

    //if hierarchy(not an entity) selected from hierarchy panel
    public void whenRootSelected(Object rootObjectInfo){
        if (rootObjectInfo.equals("Hierarchy")){
            selectedEntity = null;
            selectedUnitLabel.setText("No unit selected.");
            updateOrderButtonsState(false);
            orderDetailLayout.show(orderDetailPanel, "empty");
            isRootSelected = true;
            clearCurrentOrderPanel();
        }else isRootSelected = false;
        timer.stop();
    }

    public void disablePanel(){
        selectedEntity = null;
        selectedUnitLabel.setText("No unit selected.");
        updateOrderButtonsState(false);
        orderDetailLayout.show(orderDetailPanel, "empty");
        isRootSelected = true;
        clearCurrentOrderPanel();
        timer.stop();
    }

    //for creating new button for each available targets based on their assigned sides
    public void createNewTargetButton(Entity entity){
        targetButton = new JButton(entity.getName());
        targetButton.setBackground(app.uiColorManager.BUTTON_COLOR);
        targetButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        allCreatedEntites.add(entity);
        //createFollowButtonList(entity);
        if (entity.getSide() == 0){
            allyButtons.put(entity, targetButton);
            allyTargetPanel.add(targetButton);
        } else {
            enemyButtons.put(entity, targetButton);
            enemyTargetPanel.add(targetButton);
        }
        //action listener for create attack order
        targetButton.addActionListener(e -> {
            targetEntity = entity;
            attackerEntity = selectedEntity;
            if (isAttackAction){
                if (attackerEntity != null){
                    attackerEntity.addOrder(new Attack(app, attackerEntity, targetEntity));
                    refreshCurrentOrderPanel();
                }
            }
        });
    }

    //mapdeki entitylerin sideları ve typelarına göre targetların gösterilmesi
    public void showTargetButtons(Entity selectedOne){
        String typeOfSelected = selectedOne.getType();
        List<Entity> detectedEntitiesFromRadar = new ArrayList<>();
        allyTargetPanel.removeAll();
        enemyTargetPanel.removeAll();
        int sideOfSelected = selectedOne.getSide();
        for (Entity e : allCreatedEntites){
            if (sideOfSelected == e.getSide()){
                List<Entity> entities = e.getDetectedEntities();
                for (int i = 0; i < entities.size(); i++){
                    detectedEntitiesFromRadar.add(entities.get(i));
                }
            }
        }
        if (typeOfSelected.equals("Plane")){
            if (sideOfSelected == 0){
                for (Entity keyEntity : enemyButtons.keySet()) {
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        enemyTargetPanel.add(enemyButtons.get(keyEntity));
                    }
                }
            } else {
                for (Entity keyEntity : allyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        allyTargetPanel.add(allyButtons.get(keyEntity));
                    }
                }
            }
        } else if (typeOfSelected.equals("Ship")) {
            if (sideOfSelected == 1){
                for (Entity keyEntity : allyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getType().equals("Ship")){
                            allyTargetPanel.add(allyButtons.get(keyEntity));
                        }
                    }
                }
            } else if (sideOfSelected == 0){
                for (Entity keyEntity : enemyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getType().equals("Ship")){
                            enemyTargetPanel.add(enemyButtons.get(keyEntity));
                        }
                    }
                }
            }
        } else if (typeOfSelected.equals("Tank")) {
            if (sideOfSelected == 1){
                for (Entity keyEntity : allyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getType().equals("Tank")){
                            allyTargetPanel.add(allyButtons.get(keyEntity));
                        }
                    }
                }
            } else if (sideOfSelected == 0){
                for (Entity keyEntity : enemyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getType().equals("Tank")){
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
    }

    //for deleting target if it's entity destroyed with attack order
    public void deleteEntityFromTarget(Entity entity){
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
        for (int i = 0; i < followableEntityList.size(); i++){
            if (entity.equals(followableEntityList.get(i))){
                followableEntityList.remove(entity);
                allCreatedEntites.remove(entity);
                followTargetData.removeElement(entity);
                setFollowTargets();
            }
        }
        entity.deleteAllDetectedEntities();

        revalidate();
        repaint();
    }

    public void setFollowTargets(){
        if (selectedEntity != null){
            for (int i = 0; i < allCreatedEntites.size(); i++ ){
                if (selectedEntity != allCreatedEntites.get(i)){
                    Entity ent = allCreatedEntites.get(i);
                    followableEntityList.add(ent);
                    followTargetData.addElement(ent.getName());
                }
            }
        }
    }

    public void currentOrderRefresher(){
        showTargetButtons(selectedEntity);
        boolean isDone = selectedEntity.getCurrentOrderState();
        if (!isPaused){
            if (isDone){
                refreshCurrentOrderPanel();
            }
        }
    }

    //change the current order panel based on the selected entity
    private void refreshCurrentOrderPanel(){
        currentOrderBoxes.clear();
        currentOrdersOfEntity.clear();
        currentOrdersListPanel.removeAll();
        if (selectedEntity != null){
            Queue<Order> currentOrders = selectedEntity.getOrders();
            for (Order order : currentOrders){
                JCheckBox box = new JCheckBox(order.createTextToPrint());
                box.setBackground(panelBgColor);
                box.setForeground(Color.white);
                box.addItemListener(l -> {
                    updateDeleteButtonState();
                });
                this.currentOrderBoxes.add(box);
                currentOrdersOfEntity.add(order);
                currentOrdersListPanel.add(box);
            }
        }
        currentOrdersListPanel.revalidate();
        currentOrdersListPanel.repaint();
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
        for (JCheckBox cb : currentOrderBoxes){
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
        currentOrderBoxes.clear();
        currentOrdersListPanel.removeAll();
        currentOrdersListPanel.revalidate();
        currentOrdersListPanel.repaint();
        updateDeleteButtonState();
    }

    private void deleteSelectedOrders(){
        ArrayList<Order> ordersToDelete = new ArrayList<>();
        if (selectedEntity == null) return;
        for (int i = currentOrderBoxes.size()-1; i >= 0; i--){
            if (currentOrderBoxes.get(i).isSelected()){
                Order deletedOrder = currentOrdersOfEntity.get(i);
                ordersToDelete.add(deletedOrder);
                String deleteLog = String.format("%s of %s is deleted.", deletedOrder.toString(), selectedEntity.getName());
                app.log(deleteLog);
            }
        }
        selectedEntity.removeOrder(ordersToDelete);
        refreshCurrentOrderPanel();
    }

    public void setIfPaused(boolean isPaused){
        this.isPaused = isPaused;
    }

    Vec2int posFromMap;
    public void setPosFromMap (Vec2int pos){
        posFromMap = pos;
        if (posFromMap != null){
            moveEditor.setData(posFromMap);
        }
    }

    public void setMoveMode(boolean isMove){
        app.mapView.setActionPanelUsingMouseEvent(isMove);
        app.appListenerController.setCaptureMode(isMove);
        if (isMove == false){
            enableFromMapButton.setBackground(app.uiColorManager.BUTTON_COLOR);
            enableFromMapButton.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
        }
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
