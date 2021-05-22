package net.kogics.kojo.driver;

import static net.kogics.kojo.driver.API.*;

public class Main4Java {
    public static void main(String[] args) {
        clear();
        for (int i = 0; i < 4; i++) {
            forward(100);
            right(90);
        }
    }
}