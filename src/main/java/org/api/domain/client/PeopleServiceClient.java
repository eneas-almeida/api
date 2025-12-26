package org.api.domain.client;

import org.api.application.dto.PeopleResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeopleServiceClient {
    Mono<PeopleResponse> getPeopleById(int id);
    Flux<PeopleResponse> listPeople();
}
