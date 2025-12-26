package org.api.application.service;

import lombok.RequiredArgsConstructor;
import org.api.application.dto.PeopleResponse;
import org.api.domain.repository.PeopleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

	private final PeopleRepository peopleRepository;

	@Override
	public Mono<PeopleResponse> getById(int id) {
		return peopleRepository.findById(id);
	}

	@Override
	public Flux<PeopleResponse> listAll() {
		return peopleRepository.findAll();
	}
}
