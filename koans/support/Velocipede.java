package support;

public class Velocipede {
    public static final String __ = "fill_me_in";
    public static Object __() { return "fill_me_in"; }

    private int gears = 1;

    public int getGears() {
        return gears;
    }

    public void setGears(int gears) {
        this.gears = gears;
    }

    public String ring() {
        return "ring!";
    }
}
