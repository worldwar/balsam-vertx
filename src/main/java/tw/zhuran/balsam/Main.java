package tw.zhuran.balsam;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import tw.zhuran.balsam.config.AppConfig;
import tw.zhuran.balsam.message.RabbitListenerVerticle;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        AppConfig.config(vertx);
        vertx.deployVerticle(RabbitListenerVerticle.class.getName());
        new Launcher().dispatch(args);
    }
}
