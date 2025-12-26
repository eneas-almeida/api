package org.api.infrastructure.config;

import org.api.domain.client.PeopleServiceClient;
import org.api.domain.repository.PeopleRepository;
import org.api.infrastructure.repository.PeopleRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

	@Bean
	public PeopleRepository peopleRepository(PeopleServiceClient peopleClient) {
		return new PeopleRepositoryImpl(peopleClient);
	}
}
