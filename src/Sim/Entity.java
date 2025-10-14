package Sim;

import Sim.Orders.Attack;
import Sim.Orders.Order;
import Sim.TDL.TDLReceiver;
import Sim.TDL.TDLTransmitter;
import Var.RGB;
import Vec.Vec2int;

import java.util.*;

public class Entity {
    private String name;
    private Side side = Side.ALLY;
    private Vec2int pos;
    private Vec2int speed;
    private Type type;
    private ArrayList<Component> components = new ArrayList<>();
    public ArrayList<Component> componentsToRemove = new ArrayList<>();
    private List<Entity> detectedEntities = new ArrayList<>();
    private RGB currentPixelColor;
    protected final World w;
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

    private ArrayList<Entity> knownEntities = new ArrayList<>();
    private TDLReceiver tdlReceiver = new TDLReceiver(this);
    private TDLTransmitter tdlTransmitter = new TDLTransmitter(this);

    private boolean isActive = true;
    //TODO: stack içinde tutulacak entityleri createEntity fonksiyonu ile koymuştun bu yanlış,
    // bunları new Entity ile yapacak şekilde değiştir.

    public Entity(World w) {
        this.w = w;
        nodeInfo = new NodeInfo();
    }

    public Entity(World w, String name, Entity.Side side, Vec2int pos, Vec2int speed, int range, Entity.Type type, boolean active){
        this.w = w;
        this.name = name;
        this.side = side;
        this.pos = pos;
        this.speed = speed;
        if(range != 0){
            addComponents(new Radar(this, w.entities));
            ((Radar)getComponent("Radar")).setRange(range);
        }
        this.type = type;
        this.isActive = active;
    }

    public enum Type{
        GROUND("Tank"),
        SURFACE("Ship"),
        AIR("Plane"),
        HQ("HEADQUARTER");

        private String name;

        Type(String name){
            this.name = name;
        }
        public String getName(){
            return name;
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

    }

    public void addOrder(Order order){
        if(orders.isEmpty()){
            currentOrder = order;
        }
        orders.add(order);
    }

    public void removeComponent(String s){
        switch (s){
            case "Radar":
                for (Component c : components){
                    if(c instanceof Radar){
                        componentsToRemove.add(c);
                    }
                }
                break;
        }
    }

    public void removeComponentInstantly(String s){
        switch (s){
            case "Radar":
                for (Component c : components){
                    if(c instanceof Radar){
                        components.remove(c);
                    }
                }
                break;
        }
    }

    public void updateEntity(String newName, Side newSide, Vec2int newPos, Vec2int newSpeed, int newRange, Type newType){
        if (newName.equals("HEADQUARTER")){
            pos = newPos;
            for (Component c : components){
                if(c.getClass() == Radar.class)
                    ((Radar) c).setRange(newRange);
            }
        } else {
            name = newName;
            side = newSide;
            pos = newPos;
            speed = newSpeed;
            type = newType;

            for (Component c : components){
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
        orders.poll();
        currentOrder = orders.peek();
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

        if(!orders.isEmpty() && currentOrder != null)
            currentOrder.update();

        move();

        //System.out.format("Entity::update - %s - time: %d\n", this, deltaTime);
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(deltaTime);
        }

        tdlTransmitter.update();

        components.removeAll(componentsToRemove);
        componentsToRemove.clear();
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
                entities.add(attack.getTargetEntity());
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

    public void addComponents(Component c) {
        this.components.add(c);
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

    public ArrayList<Component> getComponents(){
        return components;
    }

    public RGB getPosPixelColor(){
        return currentPixelColor;
    }

    public boolean hasComponent(String componentToSearch){
        for(Component c : components){
            if(componentToSearch.equals("Radar")){
                if(c instanceof Radar && ((Radar) c).getRange() != 0){
                    return true;
                }
                else{
                    return false;
                }
            }

        }
        return false;
    }

    public Component getComponent(String componentToGet){
        for(Component c : components){
            if(componentToGet.equals("Radar")){
                if(c instanceof Radar){
                    return c;
                }
                else{
                    return null;
                }
            }

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

    public ArrayList<Entity> getKnownEntities(){
        return knownEntities;
    }

    public void addKnownEntity(Entity e){
        if(!knownEntities.contains(e))
            knownEntities.add(e);
    }

    public void removeKnownEntity(Entity e){
        knownEntities.remove(e);
    }

    public TDLReceiver getTdlReceiver(){
        return tdlReceiver;
    }

    public TDLTransmitter getTdlTransmitter(){
        return tdlTransmitter;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setActive(boolean a){
        isActive = a;
    }

    public List<Entity> getAllyEntitiesInRange(){
        List<Entity> allyEntities = new ArrayList<>();
        if (isInLink){
            for (Entity entity : w.app.headQuarter.getKnownEntities()){
                if (entity.getSide() == Side.ALLY)
                    allyEntities.add(entity);
            }
        } else return null;
        return allyEntities;
    }

    //TODO list mi tek tek mi kararsızım tek tek info mesaj atılabilir ama tek basar bakıcam
//    public void sendInfoMessages(){
//        List<Entity> entities = getAllyEntitiesInRange();
//        if (entities.isEmpty()){
//            return;
//        } else {
//            this.getTdlTransmitter().createInfoMessage(w.app, this, entities);
//        }
//    }

    public void copyFrom(Entity e){
        name = e.getName();
        side = e.getSide();
        pos = e.getPos();
        speed = e.getSpeed();
        type = e.getType();
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
            w.app.mapView.repaint();
        }
        isActive = e.isActive();
//        components = e.getComponents();
//        detectedEntities = e.getDetectedEntities();
        // ORDERLARI falan da ekleyebilirsin

    }

}
