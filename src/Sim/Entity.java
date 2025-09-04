package Sim;

import Sim.Orders.Order;
import Var.RGB;
import Vec.Vec2int;

import java.util.*;

public class Entity {
    String name;
    int side = 0;
    Vec2int pos;
    Vec2int speed;
    String type;
    ArrayList<Component> components = new ArrayList<>();
    RGB currentPixelColor;
    private final World w;
    private NodeInfo nodeInfo;
    private Queue<Order> orders = new LinkedList<>();
    private Order currentOrder = null;
    private boolean isCurrentOrderDone = false;
    public int maxSpeed;
    private Vec2int nextPos;
    private RGB nextPosPixelColor = new RGB();
    private RGB posPixelColor = new RGB();
    //TODO current order tutulsun
    //TODO entity içinde orderda kullanılan değişkenleri tut

    public Entity(World w) {
        this.w = w;
        nodeInfo = new NodeInfo();
    }

    public void addOrder(Order order){
        if(orders.isEmpty()){
            currentOrder = order;
        }
        orders.add(order);
    }

    public void updateEntity(String newName, int newSide, Vec2int newPos, Vec2int newSpeed, int newRange, String newType){
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

    public void removeOrder(ArrayList<Order> orderList){
        //TODO: remove order ekle, ayrıca (burası icin degil) peak ile current'e
        // ulaşmak yerine direk currentOrder'ı kullan
        for(Order o : orderList){
            orders.remove(o);
        }
        currentOrder = orders.peek();
    }

    public void completeCurrentOrder(){
        orders.poll();
        currentOrder = orders.peek();
    }
    //TODO
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
        if(!orders.isEmpty() && currentOrder != null)
            currentOrder.update();
        nextPos = new Vec2int(pos.x + speed.x , pos.y - speed.y);

        nextPosPixelColor = w.app.mapView.allPixelColors.get(nextPos.toString());
        if(nextPosPixelColor != null && CanMove(nextPosPixelColor,type)){
            pos = nextPos;
        }else {
            pos = nextStep(pos, nextPos);

        }


/*
        if(CanMove(currentPixelColor,type)){

        }

 */


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
/*
        if(Objects.equals(type, "Ship") && pos.y >= w.map.midY){
            pos.y = w.map.midY;
            speed.x = 0;
            speed.y = 0;
        }

        if(Objects.equals(type, "Tank") && pos.y <= w.map.midY){
            pos.y = w.map.midY;
            speed.x = 0;
            speed.y = 0;
        }
*/
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


        //System.out.format("Entity::update - %s - time: %d\n", this, deltaTime);
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(deltaTime);
        }
    }

    public boolean CanMove(RGB rgb, String type) {

        if((rgb.r == 93 && rgb.g == 94 && rgb.b == 97) && (Objects.equals(type, "Tank"))){
            return true;
        }
        if((rgb.r == 0 && rgb.g == 0 && rgb.b == 0) && (Objects.equals(type, "Ship"))){
            return true;
        }
        if(Objects.equals(type, "Plane")){
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
    public void setCurrentOrderState(boolean state){
        isCurrentOrderDone = state;
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

    public void setSide(int side){
        this.side = side;
    }

    public int getSide(){
        return side;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType(){
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
        if (side == 0) {
            sideName = "Ally";
        }
        else if (side == 1) {
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


}
