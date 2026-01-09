package Sim;

import Sim.Orders.Attack;
import Sim.Orders.Order;
import Sim.TDL.TDLReceiverComp;
import UI.AppWindow;
import UI.EntityEditorView;
import UI.LocalMapView;
import UI.MapView;
import Var.RGB;
import Vec.Vec2int;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class Entity {
    private String id;
    private String name;
    private Side side = Side.ALLY;
    private Vec2int pos;
    private Vec2int speed;
    private Type type;
    private HashMap<Component.ComponentType, Component> components = new HashMap<>();
    public HashMap<Component.ComponentType, Component> componentsToRemove = new HashMap<>();
    private List<Entity> detectedEntities = new ArrayList<>();
    private ArrayList<Entity> infoMsgSendEntities = new ArrayList<>();
    private RGB currentPixelColor;
    public World w;
    private NodeInfo nodeInfo;
    private Queue<Order> orders = new LinkedList<>();
    private Order currentOrder = null;
    private boolean isCurrentOrderDone = false;
    public int maxSpeed;
    private Vec2int nextPos;
    private RGB nextPosPixelColor = new RGB();
    private RGB posPixelColor = new RGB();
    private Stack<Vec2int> previousPositions = new Stack<>();
    private boolean isInLink = false;
    private boolean isDetected = false;
    private JFrame localMapFrame = null;
    private LocalMapView localMapView = null;

    //private String ppliCode;

    private boolean isActive = true;

    private LocalWorld localWorld = new LocalWorld(this);
    public boolean isLocal = false;

    public Entity(World w) {
        this.w = w;
        nodeInfo = new NodeInfo();

    }

    public Entity(World w, String name, Entity.Side side, Vec2int pos, Vec2int speed, Entity.Type type, HashMap<Component.ComponentType, Component> components,  boolean active){
        this.w = w;
        this.name = name;
        this.side = side;
        this.pos = pos;
        this.speed = speed;
        this.type = type;
        this.isActive = active;
        this.components = (HashMap<Component.ComponentType, Component>) components.clone();

    }

    public Entity(World w, String name, Entity.Side side, Vec2int pos, Vec2int speed, Entity.Type type){
        this.name = name;
        this.side = side;
        this.pos = new Vec2int(pos.x, pos.y);
        this.speed = new Vec2int(speed.x, speed.y);
        this.type = type;
        //setPpliCode(type);
        this.w = w;

    }

    public enum Type{
        GROUND("Tank", "J2.2", "J3.2"),
        SURFACE("Ship", "J2.3", "J3.3"),
        AIR("Plane", "J2.1", "J3.1"),
        HQ("HEADQUARTER", "J2.2", "J3.2");

        private String name;
        private String ppliCode;
        private String trackCode;

        Type(String name, String ppliCode, String trackCode){
            this.name = name;
            this.ppliCode = ppliCode;
            this.trackCode = trackCode;
        }
        public String getName(){
            return name;
        }

        public String getPpliCode(){
            return ppliCode;
        }

        public String getTrackCode(){
            return trackCode;
        }

    }

    public enum Side{
        ALLY("Ally"),
        ENEMY("Enemy");

        private String name;

        Side(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }

        public int getIndex(){
            if(name.equals("Ally"))
                return 0;
            return 1;
        }

        public Side getSideByString(String s){
            if(s.equalsIgnoreCase("ally"))
                return ALLY;
            else if(s.equalsIgnoreCase("enemy"))
                return ENEMY;
            System.out.println("ENTITY SIDE IS NULL!!!!!!!!");
            return null;
        }

    }

    public void addOrder(Order order){
        if(orders.isEmpty()){
            currentOrder = order;
        }
        orders.add(order);
    }

    public void removeComponent(Component.ComponentType type){
        for(Component c : components.values()){
            if(c.type == type){
                componentsToRemove.put(c.type, c);
            }
        }
    }

    public void removeComponentInstantly(Component.ComponentType type){
        for(Component c : components.values()){
            if(c.type == type){
                components.remove(c.type);
            }
        }
    }

    public void updateEntity(String newName, Side newSide, Vec2int newPos, Vec2int newSpeed, int newRange, Type newType){
        if (newName.equals("HEADQUARTER")){
            pos = newPos;
            for (Component c : components.values()){
                if(c.getClass() == Radar.class)
                    ((Radar) c).setRange(newRange);
            }
        } else {
            name = newName;
            side = newSide;
            pos = newPos;
            speed = newSpeed;
            type = newType;

            for (Component c : components.values()){
                if(c.getClass() == Radar.class)
                    ((Radar) c).setRange(newRange);
            }
        }
    }

    public void removeOrder(ArrayList<Order> orderList){
        for(Order o : orderList){
            orders.remove(o);
        }
        currentOrder = orders.peek();
    }

    public void completeCurrentOrder(){
        Order doneOrder = orders.poll();
        currentOrder = orders.peek();
        if (doneOrder != null && doneOrder.orderType.equals(Order.OrderType.ATTACK))
            w.app.actionPanel.enableTargetButtonState(this, doneOrder);
    }

    public Vec2int nextStep(Vec2int pos, Vec2int nextPos){
        posPixelColor =w.app.mapView.allPixelColors.get(pos.toString());
        Vec2int newPos = new Vec2int(pos.x,pos.y);

        if(!(posPixelColor != null &&CanMove(posPixelColor,type))){
            speed.x = 0;
            speed.y = 0;

        }else {
            for (int i = 1; i < 100; i++) {
                if(nextPos.x - pos.x < 0 && nextPos.y - pos.y < 0){
                    newPos.x -= 1;
                    newPos.y -= 1;
                }
                if(nextPos.x - pos.x > 0 && nextPos.y - pos.y < 0){
                    newPos.x += 1;
                    newPos.y -= 1;
                }
                if(nextPos.x - pos.x < 0 && nextPos.y - pos.y > 0){
                    newPos.x -= 1;
                    newPos.y += 1;
                }
                if(nextPos.x - pos.x > 0 && nextPos.y - pos.y > 0){
                    newPos.x += 1;
                    newPos.y += 1;
                }


                posPixelColor = w.app.mapView.allPixelColors.get(newPos.toString());

                if (!(posPixelColor != null && CanMove(posPixelColor, type))) {
                    break;
                }
            }
        }

        return newPos;
    }
    void update(int deltaTime) {
        if(!isActive)
            return;

//        if(type != Type.HQ){
//            w.app.debugLogError(String.format("Comp: %d", components.size()));
//        }

        if(!orders.isEmpty() && currentOrder != null)
            currentOrder.update();

//        for(Entity e : localWorld.getEntities()){
//            log(name + " knows: " + e.getId());
//        }

        move();

        if(!isLocal) {
            localWorld.update(deltaTime);
        }

        for(Component c : components.values()){
            c.update(deltaTime);
        }

        for(Component c : componentsToRemove.values()){
            EntityEditorView editorView = w.app.editorPanel;

            switch (c.type){
                case RADAR:
                    if(editorView.getRadarEditor() != null){
                        componentsToRemove.remove(c.type);
                        continue;
                    }
                    break;

                case TRANSMITTER:
                    if(editorView.getTransmitterEditor() != null){
                        componentsToRemove.remove(c.type);
                        continue;
                    }
                    break;

                case RECEIVER:
                    if(editorView.getReceiverEditor() != null) {
                        componentsToRemove.remove(c.type);
                        continue;
                    }
                    break;

            }

            components.remove(c.type);
        }
        //components.removeAll(componentsToRemove);
        componentsToRemove.clear();

        if(localMapView != null)
            localMapView.update();

    }

    public void move(){
        nextPos = new Vec2int(pos.x + speed.x , pos.y - speed.y);

        nextPosPixelColor = w.app.mapView.allPixelColors.get(nextPos.toString());
        if(nextPosPixelColor != null && CanMove(nextPosPixelColor,type)){
            pos = nextPos;
        }
        else {
            pos = nextStep(pos, nextPos);

        }

        if(pos.x <= 0){
            pos.x = 0;
            speed.x = 0;
            speed.y = 0;
        }

        if(pos.x >= w.map.maxX){
            pos.x = w.map.maxX;
            speed.x = 0;
            speed.y = 0;
        }

        if(pos.y <= 0){
            pos.y = 0;
            speed.x = 0;
            speed.y = 0;
        }

        if(pos.y >= w.map.maxY){
            pos.y = w.map.maxY;
            speed.x = 0;
            speed.y = 0;
        }

        maxSpeed = getSpeed().getMagnitudeAsInt();
        if(maxSpeed == 0)
            maxSpeed = 4;
    }

    public boolean CanMove(RGB rgb, Type type) {
        // spawn icin renk kontrolunu, PixelColor classi icindeki isValidForType() yapiyor
        if(!(rgb.r == 25 && rgb.g == 25 && rgb.b == 40) && (Objects.equals(type, Type.GROUND))){
            return true;
        }
        if((rgb.r == 25 && rgb.g == 25 && rgb.b == 40) && (Objects.equals(type, Type.SURFACE))){
            return true;
        }
        if(Objects.equals(type, Type.AIR)){
            return true;
        }
        if (Objects.equals(type, Type.HQ)){
            return true;
        }
        return false;
    }

    public Queue<Order> getOrders(){
        return orders;
    }

    public boolean getCurrentOrderState(){
        return isCurrentOrderDone;
    }
    public void setCurrentOrderState(boolean isDone){
        isCurrentOrderDone = isDone;
    }

    public void setDetectedEntities(Entity detectedEntity){
        detectedEntities.add(detectedEntity);
        String detectMsg = String.format("%s detect %s as a target.", name, detectedEntity.getName());
        log(detectMsg);
    }

    public void removeFromDetectedEntities(Entity outOfRangeEntity){
        detectedEntities.remove(outOfRangeEntity);
        String exitMsg = String.format("The target %s has moved out of %s's radar range.", outOfRangeEntity.getName(), name);
        log(exitMsg);
    }

    public List<Entity> getDetectedEntities(){
        return detectedEntities;
    }

    public void deleteAllDetectedEntities(){
        detectedEntities.clear();
    }

    public void setIsInLink(boolean b){
        isInLink = b;
    }

    public boolean isInLink(){
        return isInLink;
    }

    public ArrayList<Entity> getEntitiesToAttack(){
        ArrayList<Entity> entities = new ArrayList<>();
        Object[] ordersArray = orders.toArray();
        for (int i = 0; i < ordersArray.length; i++){
            if (ordersArray[i].getClass() == Attack.class){
                Attack attack = (Attack) ordersArray[i];
                entities.add(w.entityHashMap.get(attack.getAttackTargetID()));
            }
        }
        return entities;
    }

    //To access and change content of Entity from other packages.

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vec2int getPos(){
        return pos;
    }

    public void setPos(Vec2int pos) {
        this.pos = pos;
    }

    public Vec2int getNextPos(){
        return nextPos;
    }

    public void setSpeed(Vec2int speed) {
        this.speed = speed;
    }

    public Vec2int getSpeed(){return speed;}

    public void addComponent(Component c) {
        this.components.put(c.type, c);
        componentsToRemove.remove(c.type);
    }

    public void setSide(Side side){
        this.side = side;
    }

    public Side getSide(){
        return side;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType(){
        return type;
    }

    public void setColor(RGB color)
    {currentPixelColor = color;}

    public ArrayList<Integer> getColor(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(currentPixelColor.r);
        list.add(currentPixelColor.g);
        list.add(currentPixelColor.b);
        return list;
    }

    public String getSideAsName(){
        String sideName = "";
        if (side == Side.ALLY) {
            sideName = "Ally";
        }
        else if (side == Side.ENEMY) {
            sideName = "Enemy";
        }
        return sideName;
    }

    public boolean isNullName(){
        if(name == null){
            return true;
        }
        return false;
    }

    private void log(String msg){
        w.app.log(msg);
    }

    public String toLog() {
        return String.format("Name: %s - Pos: %s, Speed: %s", name, pos.toString(), speed.toString());
    }

    @Override
    public String toString() {
        return name;
    }

    public NodeInfo getNodeInfo(){
        return nodeInfo;
    }

    public HashMap<Component.ComponentType, Component> getComponents(){
        return components;
    }

    public RGB getPosPixelColor(){
        return currentPixelColor;
    }

    public boolean hasComponent(Component.ComponentType type){
        return components.containsKey(type);
    }

    public Component getComponent(Component.ComponentType componentToGet){
        if(components.containsKey(componentToGet)){
            return components.get(componentToGet);
        }
        return null;
    }

    public boolean isDetected(){
        return isDetected;
    }

    public void isItDetected(boolean b){
        isDetected = b;
    }

    public Stack<Vec2int> getPreviousPositions(){
        return previousPositions;
    }

    public void revertToPreviousPosition(){
        if(!previousPositions.isEmpty()){
            pos = previousPositions.getLast();
            previousPositions.pop();
        }
    }

    public boolean isActive(){
        return isActive;
    }

    public void setActive(boolean a){
        isActive = a;
    }


    public void setInfoMsgSendEntities(Entity entity){
        if (!infoMsgSendEntities.contains(entity)){
            infoMsgSendEntities.add(entity);
        }
    }

    public ArrayList<Entity> getInfoMsgSendEntities(){
        return infoMsgSendEntities;
    }

    public void copyFrom(Entity e){
        name = e.getName();
        side = e.getSide();
        pos = e.getPos();
        speed = e.getSpeed();
        type = e.getType();
        components = e.getComponents();

        if(!isActive && e.isActive){
            w.app.hierarchyPanel.entityAdded(this);
            w.app.actionPanel.createNewTargetButton(this);
            w.app.mapView.setSelectedEntity(this);
            w.app.mapView.repaint();
        }
        else if(isActive && !e.isActive){
            w.app.hierarchyPanel.entityRemoved(this);
            w.app.actionPanel.deleteEntityFromTarget(this);
            w.app.mapView.getHoveredEntities().remove(this);
            w.app.editorPanel.disablePanelData();
            w.app.mapView.setSelectedEntity(null);
            w.app.actionPanel.disablePanel();
            w.app.mapView.repaint();
        }
        else{
            w.app.hierarchyPanel.update(1000);
            w.app.editorPanel.updatePanelData(this);
            w.app.hierarchyPanel.entityChanged();
            w.app.mapView.repaint();
        }
        isActive = e.isActive();
//        components = e.getComponents();
//        detectedEntities = e.getDetectedEntities();
        // ORDERLARI falan da ekleyebilirsin

    }

    public void setId(String  id){
        this.id = id;
    }

    public String  getId(){
        return id;
    }

    public LocalWorld getLocalWorld(){
        return localWorld;
    }

    public boolean isLocal(){
        return isLocal;
    }

    public void createLocalMapFrame(){
        if(localMapFrame != null){
            localMapFrame.requestFocus();
            return;
        }

        String frameName = String.format("Local World of %s (%s)", id, name);
        localMapFrame = new JFrame(frameName);
        localMapFrame.setVisible(true);
        localMapView = new LocalMapView(w.app, this);
        localMapFrame.add(localMapView);
        localMapView.initializeLocalMap(w.app.mapView.getMapResolution());
        localMapFrame.setResizable(false);
        localMapFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        localMapFrame.pack();
        localMapFrame.setAlwaysOnTop(true);
        localMapFrame.setLocation(pos.x, pos.y);
        localMapFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                localMapFrame = null;
                localMapView = null;
            }
        });


    }

}
