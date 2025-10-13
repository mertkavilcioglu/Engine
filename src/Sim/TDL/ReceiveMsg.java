package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class ReceiveMsg extends Message{
//TODO içini doldur

    public ReceiveMsg(MessageType type, VCSApp app, Entity src, Entity receiver, String msg) {
        super(type, app, src, receiver, msg);
    }

    @Override
    public String getMsgDetail() {
        return "";
    }
}
