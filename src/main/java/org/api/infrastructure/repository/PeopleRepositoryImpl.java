package org.api.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.api.application.dto.PeopleResponse;
import org.api.domain.client.PeopleServiceClient;
import org.api.domain.repository.PeopleRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PeopleRepositoryImpl implements PeopleRepository {
	private final PeopleServiceClient peopleClient;

	@Override
	public Mono<PeopleResponse> findById(int id) {
		return peopleClient.getPeopleById(id);
	}

	@Override
	public Flux<PeopleResponse> findAll() {
		return peopleClient.listPeople();
	}
}
