package org.api.application.service;

import org.api.application.dto.PeopleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeopleService {
	Mono<PeopleResponse> getById(int id);
	Flux<PeopleResponse> listAll();
}
