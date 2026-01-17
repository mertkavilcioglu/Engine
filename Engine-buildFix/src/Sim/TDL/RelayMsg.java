package Sim.TDL;

import App.VCSApp;
import UI.UIColorManager;

import java.awt.*;

public class RelayMsg extends Message{
    private final Message realMsg;

    public RelayMsg(VCSApp app, String relaySrcID, String relayTargetID, Message msg) {
        super(MessageType.RELAY, app, relaySrcID, relayTargetID, relaySrcID, "J0.5");
        realMsg = msg;
    }

    @Override
    public String getMsgDetail() {
        return String.format("Relay Message:\nFrom: %s\n To: %s\n By: %s\n Main Message Type: %s", realMsg.getSrcID(), realMsg.getTargetID(), this.getSrcID(), realMsg.type);
    }

    @Override
    public Color getColor() {
        return UIColorManager.DARK_MAP_BG_BLUE_COLOR;
    }

    @Override
    public Message copy() {
        return null;
    }

    public Message getRealMsg(){
        return realMsg;
    }
}
