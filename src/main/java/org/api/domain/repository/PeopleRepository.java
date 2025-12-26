package org.api.domain.repository;

import org.api.application.dto.PeopleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeopleRepository {
	Mono<PeopleResponse> findById(int id);
	Flux<PeopleResponse> findAll();
}
