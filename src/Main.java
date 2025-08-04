import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        VCSapp app = new VCSapp();
        //Thread.currentThread().setName("MAIN THREAD");
        //app.run();
        try {
            app.runWithWindow();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

