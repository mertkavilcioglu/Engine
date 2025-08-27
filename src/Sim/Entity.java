package Sim;

import Sim.Orders.Order;
import Var.RGB;
import Vec.Vec2int;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

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

    public void completeCurrentOrder(){
        orders.poll();
    }

    void update(int deltaTime) {

        if(CanMove(currentPixelColor,type)){
            pos.x += speed.x;
            pos.y += -speed.y;
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


        System.out.format("Entity::update - %s - time: %d\n", this, deltaTime);
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(deltaTime);
        }

        if(!orders.isEmpty())
            orders.peek().update();
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

    public String getSideasName(){
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
