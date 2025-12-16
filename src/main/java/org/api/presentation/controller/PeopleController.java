package org.api.presentation.controller;

import org.api.application.usecase.GetPeopleUseCase;
import org.api.application.usecase.ListPeopleUseCase;
import org.api.presentation.dto.PeopleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/peoples")
public class PeopleController {

    private final GetPeopleUseCase getPeopleUseCase;
    private final ListPeopleUseCase listPeopleUseCase;

    public PeopleController(GetPeopleUseCase getPeopleUseCase,
                            ListPeopleUseCase listPeopleUseCase) {
        this.getPeopleUseCase = getPeopleUseCase;
        this.listPeopleUseCase = listPeopleUseCase;
    }

    @GetMapping("/{id}")
    public Mono<PeopleResponse> getPeopleById(@PathVariable int id) {
        return getPeopleUseCase.execute(id)
                .map(PeopleResponse::fromDomain);
    }

    @GetMapping
    public Flux<PeopleResponse> listPeople() {
        return listPeopleUseCase.execute()
                .map(PeopleResponse::fromDomain);
    }
}
