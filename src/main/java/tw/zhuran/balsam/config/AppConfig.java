package tw.zhuran.balsam.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import tw.zhuran.balsam.message.RabbitClientFactory;

public class AppConfig {
    public static ConfigRetrieverOptions  config(Vertx vertx) {
        ConfigStoreOptions store = new ConfigStoreOptions();
        store.setType("env");
        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(store);
        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, options);
        configRetriever.getConfig( r -> RabbitClientFactory.init(vertx, r.result()));
        return options;
    }
}
