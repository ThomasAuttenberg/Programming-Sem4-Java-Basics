import Controllers.WindowController;
import Models.WindowModel;
import Views.WindowView;

public class Main {
    public static void main(String[] args) {
        WindowModel model = new WindowModel();
        WindowView view = new WindowView(model,1400,700);
        WindowController controller = new WindowController(model,view);
    }
}