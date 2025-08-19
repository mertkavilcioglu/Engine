package Sim.Orders;

import App.VCSApp;

public abstract class Order {
    public final VCSApp app;

    protected Order(VCSApp app) {
        this.app = app;
    }
}
