package Var;

public class RGB {
    public int r;
    public int g;
    public int b;

    public RGB(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RGB(){

    }

    @Override
    public String toString() {
        return String.format("%d , %d , %d", r,g,b);
    }
}
