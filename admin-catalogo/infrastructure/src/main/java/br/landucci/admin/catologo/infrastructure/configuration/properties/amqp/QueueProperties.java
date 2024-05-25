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

    public QueueProperties setExchange(String exchange) {
        this.exchange = exchange;
        return this;
    }
    public QueueProperties setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }
    public QueueProperties setQueue(String queue) {
        this.queue = queue;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{ ")
                .append("exchange='").append(exchange).append("', ")
                .append("routingKey='").append(routingKey).append("', ")
                .append("queue='").append(queue).append("' ")
                .append("}").toString();
    }

}