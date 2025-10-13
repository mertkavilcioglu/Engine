package Sim.TDL;

import App.VCSApp;
import Sim.Entity;

public class ResultMsg extends Message{
//TODO içini doldur

    public ResultMsg(MessageType type, VCSApp app, Entity src, Entity receiver, String msg) {
        super(type, app, src, receiver, msg);
    }

    @Override
    public String getMsgDetail() {
        return "";
    }
}
