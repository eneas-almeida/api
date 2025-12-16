package org.api.application.usecase;

import org.api.domain.client.PeopleClient;
import org.api.domain.entity.People;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetPeopleUseCase {

    private final PeopleClient peopleClient;

    public GetPeopleUseCase(PeopleClient peopleClient) {
        this.peopleClient = peopleClient;
    }

    public Mono<People> execute(int id) {
        return peopleClient.getPeopleById(id);
    }
}
