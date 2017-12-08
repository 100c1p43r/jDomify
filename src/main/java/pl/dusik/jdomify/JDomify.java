package pl.dusik.jdomify;

import pl.dusik.jdomify.controller.ControllerFactory;
import pl.dusik.jdomify.controller.JDomifyController;

public class JDomify {

    public static void main(String... args) throws Exception {
        JDomifyController controller = new ControllerFactory().create();
        controller.goTo("http://serializer.io");
        Thread.sleep(5000);
        System.out.println(controller.getSource());
        controller.render();
        Thread.sleep(1000);
        System.exit(0);
    }
}
