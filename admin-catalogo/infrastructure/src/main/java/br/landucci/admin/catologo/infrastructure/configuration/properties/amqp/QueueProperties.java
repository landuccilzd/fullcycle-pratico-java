package br.landucci.admin.catologo.infrastructure.configuration.properties.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class QueueProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(QueueProperties.class);

    private String exchange;
    private String routingKey;
    private String queue;

    @Override
    public void afterPropertiesSet() {
        log.info(toString());
    }

    public String getExchange() {
        return exchange;
    }
    public String getRoutingKey() {
        return routingKey;
    }
    public String getQueue() {
        return queue;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
    public void setQueue(String queue) {
        this.queue = queue;
    }

    @Override
    public String toString() {
        return "QueueProperties{" +
                "exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", queue='" + queue + '\'' +
                '}';
    }

}