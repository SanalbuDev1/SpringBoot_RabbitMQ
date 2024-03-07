package com.rabbitmq.exercises.topics.exchangetopic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.rabbitmq.exercises.topics.exchangetopic.tut5.RabbitAmqpTutorialsRunner;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ExchangetopicApplication {

	@Profile("!usage_message")
	@Bean
	public CommandLineRunner tutorial() {
		return new RabbitAmqpTutorialsRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(ExchangetopicApplication.class, args);
	}

}
