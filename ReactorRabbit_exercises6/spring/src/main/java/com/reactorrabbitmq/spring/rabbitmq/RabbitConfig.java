package com.reactorrabbitmq.spring.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.BindingSpecification;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Sender;
import reactor.rabbitmq.SenderOptions;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Exchange.DeclareOk;

@Configuration
public class RabbitConfig {

    /**
     * Bean que proporciona un Mono de Connection.
     * Este método crea una ConnectionFactory, la configura para usar NIO,
     * y devuelve un Mono a partir de una nueva conexión con el nombre
     * "reactor-rabbit".
     *
     * @return un Mono de Connection
     */
    @Bean
    Mono<Connection> connectionMono() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        // connectionFactory.setVirtualHost("/");
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }

    /**
     * Bean que proporciona SenderOptions.
     * Este método crea nuevas SenderOptions con el Mono de Connection proporcionado
     * y configura el programador de gestión de recursos para boundedElastic.
     *
     * @param connectionMono un Mono de Connection
     * @return SenderOptions con el Mono de Connection proporcionado y el
     *         programador de gestión de recursos configurado para boundedElastic
     */

    @Bean
    public Mono<AMQP.Queue.BindOk> setup(Sender sender) {
        String EXCHANGE_NAME = "tut.confige.reactive";
        String ROUTING_KEY = "hello-temp";
        String QUEUE = "hello-temp";

        return sender.declareExchange(ExchangeSpecification.exchange(EXCHANGE_NAME).type("direct"))
                .then(sender.declareQueue(QueueSpecification.queue(QUEUE)))
                .then(sender.bind(BindingSpecification.binding(EXCHANGE_NAME, ROUTING_KEY, QUEUE)));
    }

    @Bean
    public SenderOptions senderOptions(Mono<Connection> connectionMono) {
        return new SenderOptions()
                .connectionMono(connectionMono)
                .resourceManagementScheduler(Schedulers.boundedElastic());
    }

    /**
     * Bean que proporciona un Sender.
     * Este método crea un Sender con las SenderOptions proporcionadas.
     *
     * @param senderOptions las SenderOptions a utilizar al crear el Sender
     * @return un Sender con las SenderOptions proporcionadas
     */
    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }

}
