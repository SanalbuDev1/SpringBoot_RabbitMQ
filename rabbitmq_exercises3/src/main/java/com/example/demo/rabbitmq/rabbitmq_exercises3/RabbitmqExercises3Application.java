package com.example.demo.rabbitmq.rabbitmq_exercises3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.demo.rabbitmq.rabbitmq_exercises3.tut3.*;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class RabbitmqExercises3Application {

	@Profile("!usage_message")
	@Bean
	public CommandLineRunner tutorial() {
		return new RabbitAmqpTutorialsRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqExercises3Application.class, args);
	}

}
