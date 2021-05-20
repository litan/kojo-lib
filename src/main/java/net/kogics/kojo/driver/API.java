package net.kogics.kojo.driver;

import net.kogics.kojo.lite.Builtins;
import net.kogics.kojo.lite.KojoFrame;
import net.kogics.kojo.turtle.TurtleWorldAPI;

class API {
    static KojoFrame kojo = new KojoFrame();
    static Builtins b = kojo.builtins();
    static TurtleWorldAPI t = b.TurtleAPI();

    public static void forward(double n) {
        t.forward(n);
    }
    public static void right(double n){
        t.right(n);
    }
}

