package Sim.Orders;

import App.VCSApp;
import Sim.Entity;

public abstract class Order {
    public final VCSApp app;

    public final Entity receiver;
    public final Entity sender;

    public enum OrderType{
        ATTACK("Attack Order"),
        MOVE("Move Order"),
        FOLLOW("Follow Order");

        private String name;

        OrderType(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }
    }

    public OrderType orderType;

    protected Order(VCSApp app, Entity receiver, Entity sender, OrderType type) {
        this.app = app;
        this.receiver = receiver;
        this.sender = sender;
        orderType = type;
        app.actionPanel.refreshCurrentOrderPanel(receiver);
    }


    public void update() {
        actualUpdate();
    }

    protected void finish(Entity entity){
        app.actionPanel.refreshCurrentOrderPanel(entity);
    }

    protected abstract void printToLog();
    protected abstract void actualUpdate();
    public abstract String createTextToPrint();

}
