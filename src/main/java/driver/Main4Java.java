package driver;

import static driver.API.*;

public class Main4Java {
    public static void main(String[] args) {
        clear();
        for (int i = 0; i < 4; i++) {
            forward(100);
            right(90);
        }
    }
}