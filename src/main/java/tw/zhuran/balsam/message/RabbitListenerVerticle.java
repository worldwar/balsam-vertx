package tw.zhuran.balsam.message;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rabbitmq.RabbitMQClient;
import tw.zhuran.balsam.parser.Parser;

import java.io.IOException;
import java.util.Map;

public class RabbitListenerVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(RabbitListenerVerticle.class);

    @Override
    public void start(Future<Void> fut) throws InterruptedException {
        try {
            RabbitMQClient rClient = RabbitClientFactory.RABBIT_CLIENT_FACTORY_INSTANCE.getRabbitClient();
            vertx.eventBus().consumer("book-vertx", msg -> {
                JsonObject json = (JsonObject) msg.body();
                String body = json.getString("body");
                System.out.println("Got message: " + body);
                Map map = Json.decodeValue(body, Map.class);
                Object url = map.get("url");
                try {
                    Parser.catalog(url.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            rClient.start(sRes -> {
                if (sRes.succeeded()) {
                    rClient.basicConsume("add-book", "book-vertx", bcRes -> {
                        if (bcRes.succeeded()) {
                            System.out.println("Message received: " + bcRes.result());
                        } else {
                            System.out.println("Message receipt failed: " + bcRes.cause());
                        }
                    });
                } else {
                    System.out.println("Connection failed: " + sRes.cause());
                }
            });

            log.info("RabbitListenerVerticle started");
            fut.complete();
        } catch (Throwable t) {
            log.error("failed to start RabbitListenerVerticle: " + t.getMessage(), t);
            fut.fail(t);
        }
    }
}