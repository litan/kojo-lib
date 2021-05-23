package driver;

import net.kogics.kojo.lite.Builtins;
import net.kogics.kojo.lite.DrawingCanvasAPI;
import net.kogics.kojo.lite.KojoFrame;
import net.kogics.kojo.turtle.TurtleWorldAPI;

class API {
    static KojoFrame kojo = null;
    static Builtins b = null;
    static TurtleWorldAPI t = null;
    static DrawingCanvasAPI c = null;
    public static void createKojoFrame() {
        kojo = KojoFrame.create();
        initBuiltins();
    }
    public static void createKojoFrame(int width, int height) {
        kojo = KojoFrame.create(width, height);
        initBuiltins();
    }
    public static void initBuiltins() {
        b = kojo.builtins();
        t = b.TurtleAPI();
        c = b.CanvasAPI();
    }

    public static void clear() {
        c.clear();
    }
    public static void forward(double n) {
        t.forward(n);
    }
    public static void right(double n){
        t.right(n);
    }
}

