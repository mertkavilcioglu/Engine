package Sim.Orders;

import App.VCSApp;
import Sim.Entity;

public abstract class Order {
    public final VCSApp app;

    public final Entity receiver;
    public final Entity sender;

    protected Order(VCSApp app, Entity receiver, Entity sender) {
        this.app = app;
        this.receiver = receiver;
        this.sender = sender;
    }


    public void update() {
        actualUpdate();
    }

    protected abstract void printToLog();
    protected abstract void actualUpdate();
    public abstract String createTextToPrint();

}
