package tw.zhuran.balsam.message;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.rabbitmq.RabbitMQClient;

public class RabbitClientFactory {

    public static RabbitClientFactory
            RABBIT_CLIENT_FACTORY_INSTANCE = new RabbitClientFactory();
    private static RabbitMQClient rabbitClient;

    private RabbitClientFactory() {}

    public RabbitMQClient getRabbitClient() {
        return rabbitClient;
    }

    public static synchronized void init(Vertx vertx, JsonObject config) {
        if (rabbitClient == null) {
            JsonObject c = new JsonObject();
            c.put("uri", config.getString("rabbitmq_address"));
            rabbitClient = RabbitMQClient.create(vertx, c);
        }
    }
}