package Test;
import UI.SeverInterface;
import UI.MainInterface;

public class ServerTest {
    public static void main(String[] args) {
        new SeverInterface().init();
        new MainInterface().init();
    }
}
