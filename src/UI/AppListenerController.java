package UI;

public class AppListenerController {
    public enum Mode {
        NORMAL,
        MOVE_POS_CAPTURE
    }
    private Mode mode = Mode.NORMAL;

    public boolean isCaptureMode(){
        return mode == Mode.MOVE_POS_CAPTURE;
    }

    public void setCaptureMode(boolean isModeOn){
        if (isModeOn){
            mode = Mode.MOVE_POS_CAPTURE;
        } else mode = Mode.NORMAL;
    }
}
