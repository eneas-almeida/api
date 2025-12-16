package org.api.application.usecase;

import org.api.domain.client.PeopleClient;
import org.api.domain.entity.People;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ListPeopleUseCase {

    private final PeopleClient peopleClient;

    public ListPeopleUseCase(PeopleClient peopleClient) {
        this.peopleClient = peopleClient;
    }

    public Flux<People> execute() {
        return peopleClient.listPeople();
    }
}
