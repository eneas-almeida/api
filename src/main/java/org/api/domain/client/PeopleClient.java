package org.api.domain.client;

import org.api.domain.entity.People;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PeopleClient {
    Mono<People> getPeopleById(int id);
    Flux<People> listPeople();
}
