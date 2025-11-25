package UI;

import App.VCSApp;
import Sim.Entity;
import Sim.Orders.Attack;
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
    private JList<Order> currentOrderList;
    private DefaultListModel<Order> orderModel;
    private JButton selectedOrderDeleteButton;

    //main label of whole panel
    private JLabel selectedUnitLabel;

    //layout for middle panel
    private CardLayout orderDetailLayout;

    private Entity selectedEntity;

    boolean isEnemy = false;
    boolean isRootSelected = true;
    boolean isAttackAction = false;
    boolean isPaused = false;
    Entity.Side sideOfEntity;
    Entity.Type typeOfEntity;
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

        setButtonColor(attackButton);
        setButtonColor(moveButton);
        setButtonColor(followButton);

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
        setButtonColor(enableFromMapButton);
        enableFromMapButton.setFocusable(false);
        setButtonColor(disableFromMapButton);
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
        setButtonColor(moveConfirmButton);
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
        setButtonColor(followOrderCreateButton);
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
//            selectedEntity.addOrder(new Follow(app, selectedEntity, choosenEntity, followTime));
//            refreshCurrentOrderPanel();
            selectedEntity.getTdlTransmitter2().createFollowMessage2(app, selectedEntity, choosenEntity, followTime);
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


        currentOrderPanel = new JPanel(new BorderLayout());
        orderModel = new DefaultListModel<>();
        currentOrderList = new JList<>(orderModel);
        currentOrderList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        currentOrderList.setVisibleRowCount(8);
        currentOrderList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Order){
                    Order o = (Order) value;
                    lbl.setText(o.createTextToPrint());
                } return lbl;
            }
        });
        currentOrderList.addListSelectionListener(l-> updateDeleteButtonState());
        currentOrderList.setBackground(panelBgColor);
        currentOrderList.setForeground(Color.WHITE);
        JScrollPane currentOrderScroll = new JScrollPane(currentOrderList);
        currentOrderScroll.getVerticalScrollBar().setBackground(panelBgColor);


        TitledBorder currentOrderTitledBorder = new TitledBorder("Current Order");
        currentOrderTitledBorder.setTitleColor(Color.WHITE);
        currentOrderTitledBorder.setBorder(BorderFactory.createLineBorder(borderColor,2));
        currentOrderScroll.setBorder(currentOrderTitledBorder);
        currentOrderScroll.setBackground(panelBgColor);

        selectedOrderDeleteButton = new JButton("Delete");
        setButtonColor(selectedOrderDeleteButton);
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
            isAttackAction = true;
            if (isEnemy){
                showTargetButtonsForEnemy(selectedEntity);
                orderDetailLayout.show(orderDetailPanel, "ally");
            }else {
                showTargetButtonsForAlly(selectedEntity);
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
//                    selectedEntity.addOrder(new Move(app, selectedEntity, new Vec2int(coordinatesToMove.x, coordinatesToMove.y)));
//                    refreshCurrentOrderPanel();
                    selectedEntity.getTdlTransmitter2().createMoveMessage2(app, selectedEntity, new Vec2int(coordinatesToMove.x, coordinatesToMove.y));
                    setMoveMode(false);
                }
            }
            catch(Exception ex){
                moveEditor.dataValidate();
            }
        });

        //merge left, middle and right panels into one
        mergePanel.add(giveOrderPanel);
        mergePanel.add(orderDetailPanel);
        mergePanel.add(currentOrderPanel);

        this.add(selectedUnitLabel, BorderLayout.NORTH);
        this.add(mergePanel, BorderLayout.CENTER);
    }

    //find the selected entity from hierarchy panel to give order
    public void setSelectedEntity(Entity entity){
        setMoveMode(false);
        this.selectedEntity = entity;
        this.isRootSelected = false;
        this.typeOfEntity = entity.getType();

        this.sideOfEntity = entity.getSide();
        if(sideOfEntity == Entity.Side.ENEMY) isEnemy = true;
        else if (sideOfEntity == Entity.Side.ALLY) isEnemy = false;

        timer.start();

        updateOrderMode(selectedEntity);


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
        setButtonColor(targetButton);
        allCreatedEntites.add(entity);
        //createFollowButtonList(entity);
        if (entity.getSide() == Entity.Side.ALLY){
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
                    attackerEntity.getTdlTransmitter2().createAttackMessage2(app,attackerEntity.getId(),targetEntity.getId());
                }
            }
        });
    }

    //mapdeki entitylerin sideları ve typelarına göre targetların gösterilmesi
    public void showTargetButtonsForAlly(Entity selectedOne){
        if (!selectedOne.getSide().equals(Entity.Side.ALLY)) showTargetButtonsForEnemy(selectedOne);
        Entity.Type typeOfSelected = selectedOne.getType();
        enemyTargetPanel.removeAll();
        if (typeOfSelected.equals(Entity.Type.AIR)){
                for (Entity keyEntity : enemyButtons.keySet()) {
                    if (app.getHQ().getLocalWorld().getEntityHashMap().containsKey(keyEntity.getId())) {
                        enemyTargetPanel.add(enemyButtons.get(keyEntity));
                        if (selectedOne.getEntitiesToAttack().contains(keyEntity)){
                            enemyButtons.get(keyEntity).setEnabled(false);
                        }
                    }
                }
        } else if (typeOfSelected.equals(Entity.Type.SURFACE)) {
                for (Entity keyEntity : enemyButtons.keySet()){
                    if (app.getHQ().getLocalWorld().getEntityHashMap().containsKey(keyEntity.getId())) {
                        if (keyEntity.getType().equals(Entity.Type.SURFACE)){
                            enemyTargetPanel.add(enemyButtons.get(keyEntity));
                            if (selectedOne.getEntitiesToAttack().contains(keyEntity)){
                                enemyButtons.get(keyEntity).setEnabled(false);
                            }
                        }
                    }
                }

        } else if (typeOfSelected.equals(Entity.Type.GROUND)) {
                for (Entity keyEntity : enemyButtons.keySet()){
                    if (app.getHQ().getLocalWorld().getEntityHashMap().containsKey(keyEntity.getId())) {
                        if (keyEntity.getType().equals(Entity.Type.GROUND)){
                            enemyTargetPanel.add(enemyButtons.get(keyEntity));
                            if (selectedOne.getEntitiesToAttack().contains(keyEntity)){
                                enemyButtons.get(keyEntity).setEnabled(false);
                            }
                        }
                    }
                }
        }
        enemyTargetPanel.revalidate();
        enemyTargetPanel.repaint();
    }

    public void showTargetButtonsForEnemy(Entity selectedOne){
        List<Entity> detectedEntitiesFromRadar = new ArrayList<>();
        if (!selectedOne.getSide().equals(Entity.Side.ENEMY)) showTargetButtonsForAlly(selectedOne);
        Entity.Type typeOfSelected = selectedOne.getType();
        allyTargetPanel.removeAll();
        for (Entity e : allCreatedEntites){
            if (selectedOne.getSide().equals(e.getSide())){
                for (Entity entity : e.getLocalWorld().getEntities()){
                    if (entity.getSide().equals(Entity.Side.ALLY))
                        detectedEntitiesFromRadar.add(entity);
                }
            } else if (selectedOne.getLocalWorld().getEntityHashMap().containsKey(e.getId()))
                detectedEntitiesFromRadar.add(e);
        }
        if (typeOfSelected.equals(Entity.Type.AIR)){
                for (Entity keyEntity : allyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        allyTargetPanel.add(allyButtons.get(keyEntity));
                        if (selectedOne.getEntitiesToAttack().contains(keyEntity)){
                            allyButtons.get(keyEntity).setEnabled(false);
                        }
                    }
                }
        } else if (typeOfSelected.equals(Entity.Type.SURFACE)) {
                for (Entity keyEntity : allyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getType().equals(Entity.Type.SURFACE)){
                            allyTargetPanel.add(allyButtons.get(keyEntity));
                            if (selectedOne.getEntitiesToAttack().contains(keyEntity)){
                                allyButtons.get(keyEntity).setEnabled(false);
                            }
                        }
                    }
                }
        } else if (typeOfSelected.equals(Entity.Type.GROUND)) {
                for (Entity keyEntity : allyButtons.keySet()){
                    if (detectedEntitiesFromRadar.contains(keyEntity)) {
                        if (keyEntity.getType().equals(Entity.Type.GROUND)){
                            allyTargetPanel.add(allyButtons.get(keyEntity));
                            if (selectedOne.getEntitiesToAttack().contains(keyEntity)){
                                allyButtons.get(keyEntity).setEnabled(false);
                            }
                        }
                    }
                }
        }
        allyTargetPanel.revalidate();
        allyTargetPanel.repaint();
    }

    //for deleting target if it's entity destroyed with attack order
    public void deleteEntityFromTarget(Entity entity){
        if(entity == null)
            return;
        //for attack panel
        JButton deletedButton;
        if (entity.getSide().equals(Entity.Side.ALLY)){
            deletedButton = allyButtons.remove(entity);
            if(deletedButton != null)
                allyTargetPanel.remove(deletedButton);
        } else if (entity.getSide().equals(Entity.Side.ENEMY)) {
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
            for (Entity entity : allCreatedEntites){
                if (!selectedEntity.equals(entity)){
                    if (selectedEntity.getLocalWorld().getEntityHashMap().containsKey(entity.getId())){
                        followableEntityList.add(entity);
                        followTargetData.addElement(entity.getName());
                    }
                }
            }
        }
    }

    public void currentOrderRefresher(){
        showTargetButtonsForAlly(selectedEntity);
        boolean isDone = selectedEntity.getCurrentOrderState();
        if (!isPaused){
            if (isDone){
                refreshCurrentOrderPanel();
            }
        }
    }

    //change the current order panel based on the selected entity
    public void refreshCurrentOrderPanel(){
        orderModel.removeAllElements();
        if (selectedEntity != null){
            Queue<Order> currentOrders = selectedEntity.getOrders();
            for (Order order : currentOrders){
                orderModel.addElement(order);
            }
        }
        currentOrderPanel.revalidate();
        currentOrderPanel.repaint();
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
        boolean anySelected = !currentOrderList.isSelectionEmpty();
        selectedOrderDeleteButton.setEnabled(anySelected);
    }

    //clear the current order panel for each new selected entity and root
    private void clearCurrentOrderPanel(){
        orderModel.removeAllElements();

        currentOrderPanel.revalidate();
        currentOrderPanel.repaint();
        updateDeleteButtonState();
    }

    private void deleteSelectedOrders(){
        if (selectedEntity == null) return;
        List<Order> toDelete = currentOrderList.getSelectedValuesList();
        for (int i = toDelete.size()-1; i >= 0; i--){
                Order o = toDelete.get(i);
                String deleteLog = String.format("%s of %s is deleted.", o.toString(), selectedEntity.getName());
                app.log(deleteLog);
                if (o.toString().equals("Attack order")){
                    Attack a = (Attack) o;
                    if (selectedEntity.getSide() == Entity.Side.ALLY){
                        JButton b = enemyButtons.get(selectedEntity.w.entityHashMap.get(a.getAttackTargetID())); // TODO: Artık entity ref değil String entity ID alması lazım
                        b.setEnabled(true);
                    } else {
                        JButton b = allyButtons.get(selectedEntity.w.entityHashMap.get(a.getAttackTargetID())); // TODO: Artık entity ref değil String entity ID alması lazım
                        b.setEnabled(true);
                    }
                }
        }
        selectedEntity.removeOrder((ArrayList<Order>) toDelete);
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
        app.mapView.setCaptureMode(isMove);
        if (!isMove){
            setButtonColor(enableFromMapButton);
        }
    }

    private void setButtonColor(JButton button){
        button.setBackground(app.uiColorManager.BUTTON_COLOR);
        button.setForeground(app.uiColorManager.DARK_MAP_BG_BLUE_COLOR);
    }

    public void updateOrderMode(Entity selectedEntity){
        if (selectedEntity.getType() != Entity.Type.HQ && ((selectedEntity.getSide() == Entity.Side.ENEMY) ||
                app.getHQ().getLocalWorld().getEntityHashMap().containsKey(selectedEntity.getId()))){
            updateOrderButtonsState(true);
        } else updateOrderButtonsState(false);
    }

    @Override
    public void selectedEntityChanged(Entity entity) {

    }
}
