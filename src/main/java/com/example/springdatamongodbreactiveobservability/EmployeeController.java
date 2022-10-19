package com.example.springdatamongodbreactiveobservability;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	private final EmployeeRepository repository;

	public EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/api/employees")
	Flux<Employee> all() {
		return repository.findAll();
	}

	@GetMapping("/api/employees/{id}")
	Mono<Employee> one(@PathVariable Mono<String> id) {
		return id.flatMap(repository::findById);
	}
}
