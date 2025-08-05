package Sim;

import Vec.Vec2int;

import java.util.ArrayList;

public class Entity {
    String name;
    Vec2int pos;
    Vec2int speed;
    ArrayList<Component> components = new ArrayList<>();

    void update(int deltaTime) {
        pos.x += speed.x;
        pos.y += -speed.y;
        System.out.format("Sim.Entity::update - %s - time: %d\n", this, deltaTime);
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(deltaTime);
        }
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

    public boolean isNullName(){
        if(name == null){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Name: %s - Pos: %s, Speed: %s", name, pos.toString(), speed.toString());
    }
}
