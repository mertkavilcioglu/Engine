package Sim.Orders;

import App.VCSApp;
import Sim.Entity;

public abstract class Order {
    public final VCSApp app;

    public final Entity source;
    protected Order(VCSApp app, Entity source) {
        this.app = app;
        this.source = source;
    }

    public void addOrder(Order order){
        source.getOrders().add(order);
    }

    public void removeOrder(){
        source.getOrders().poll();
    }


    public void update() {
        actualUpdate();
    }

    protected abstract void actualUpdate();
}
