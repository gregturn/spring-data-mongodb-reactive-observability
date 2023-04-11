package com.example.springdatamongodbreactiveobservability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.observability.ContextProviderFactory;
import org.springframework.data.mongodb.observability.MongoObservationCommandListener;

@SpringBootApplication
public class SpringDataMongodbReactiveObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataMongodbReactiveObservabilityApplication.class, args);
	}

	// Setting up MongoDB

	@Bean
	CommandLineRunner initDatabase(EmployeeRepository repository, ObservationRegistry registry) {

		return args -> {
			Observation.createNotStarted("init-database", registry).observe(() -> {
				repository.deleteAll().block();
				repository.save(new Employee("Frodo", "ring bearer")).block();
				repository.save(new Employee("Bilbo", "burglar")).block();
			});
		};
	}

	// Configuring MongoDB + Micrometer

	@Bean
	MongoClientSettingsBuilderCustomizer mongoMetricsSynchronousContextProvider(ObservationRegistry registry) {

		return (clientSettingsBuilder) -> {
			clientSettingsBuilder.contextProvider(ContextProviderFactory.create(registry))
					.addCommandListener(new MongoObservationCommandListener(registry));
		};
	}
}
