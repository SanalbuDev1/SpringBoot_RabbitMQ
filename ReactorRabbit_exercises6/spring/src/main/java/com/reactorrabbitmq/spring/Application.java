package com.reactorrabbitmq.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Connection;
import com.reactorrabbitmq.spring.Application;

import jakarta.annotation.PreDestroy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Application {

	// Connection to RabbitMQ
	@Autowired
	private Mono<Connection> connectionMono;

	// Name of our Queue
	private static final String EXCHANGE_NAME = "tut.confige.reactive";
	private static final String ROUTING_KEY = "hello-temp";

	// Name of our Queue
	private static final String QUEUE = "hello-temp";

	// slf4j logger
	// slf4j logger
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args).close();
	}

	@PreDestroy
	public void close() throws Exception {
		connectionMono.block().close();
	}

	// Runner class
	@Component
	static class Runner implements CommandLineRunner {

		final Sender sender;

		Runner(Sender sender) {
			this.sender = sender;
		}

		@Override
		public void run(String... args) throws Exception {

			// Number of message that will be sent to the queue
			int messageCount = 10;

			// CountDownLatch to keep track of the threads
			CountDownLatch latch = new CountDownLatch(messageCount);

			// Represent the list of messages that will be sent
			Flux<OutboundMessage> outboundFlux = Flux.range(1, messageCount)
					.map(i -> new OutboundMessage(
							EXCHANGE_NAME,
							ROUTING_KEY, ("Message - " + i).getBytes()));

			/*
			 * 1. Declare the queue.
			 * 2. After the queue declaration is completed, it will call another Publisher
			 * (sendWithPublishConfirms method), which takes the flux of messages declared
			 * earlier.
			 * 3. In case of error, it will log the error.
			 * 4. Finally it subscribe a consumer to this flux, here we can check if the
			 * message if being acknowledged by the queue. We also decrement the
			 * CountDownLatch by one.
			 */
			sender.declareQueue(QueueSpecification.queue(QUEUE))
					.thenMany(sender.sendWithPublishConfirms(outboundFlux))
					.doOnError(e -> LOGGER.error("Send failed", e))
					.subscribe(m -> {
						if (m.isAck()) {
							LOGGER.info("Message [" + latch.getCount() + "] sent");
							latch.countDown();

						}
					});

			// Make sure all the threads are completed.
			latch.await(3L, TimeUnit.SECONDS);
		}

	}
}