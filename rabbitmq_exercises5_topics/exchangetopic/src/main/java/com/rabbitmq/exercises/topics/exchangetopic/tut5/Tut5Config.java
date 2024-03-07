package com.rabbitmq.exercises.topics.exchangetopic.tut5;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({ "tut5", "topics" })
@Configuration
public class Tut5Config {

	@Bean
	public TopicExchange topic() {
		return new TopicExchange("tut.topic");
	}

	@Profile("receiver")
	private static class ReceiverConfig {

		@Bean
		public Tut5Receiver receiver() {
			return new Tut5Receiver();
		}

		@Bean
		public Queue autoDeleteQueue1() {
			return new AnonymousQueue();
		}

		@Bean
		public Queue autoDeleteQueue2() {
			return new AnonymousQueue();
		}

		@Bean
		public Queue autoDeleteQueue3() {
			return new Queue("hello");
		}

		@Bean
		public Binding binding1a(TopicExchange topic,
				Queue autoDeleteQueue1) {
			return BindingBuilder.bind(autoDeleteQueue1)
					.to(topic)
					.with("*.car.*");
		}

		@Bean
		public Binding binding1b(TopicExchange topic,
				Queue autoDeleteQueue1) {
			return BindingBuilder.bind(autoDeleteQueue1)
					.to(topic)
					.with("*.*.motorcycle");
		}

		@Bean
		public Binding binding2a(TopicExchange topic,
				Queue autoDeleteQueue2) {
			return BindingBuilder.bind(autoDeleteQueue2)
					.to(topic)
					.with("lazy.#");
		}

		@Bean
		public Binding binding3c(TopicExchange topic,
				Queue autoDeleteQueue3) {
			return BindingBuilder.bind(autoDeleteQueue3)
					.to(topic)
					.with("*.orange.*");
		}

	}

	@Profile("sender")
	@Bean
	public Tut5Sender sender() {
		return new Tut5Sender();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPrefetchCount(1);
		return factory;
	}

}