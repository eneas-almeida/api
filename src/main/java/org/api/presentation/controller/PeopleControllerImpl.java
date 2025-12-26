package org.api.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.api.application.dto.PeopleResponse;
import org.api.application.service.PeopleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/peoples")
@RequiredArgsConstructor
public class PeopleControllerImpl {

    private final PeopleService peopleService;

    @GetMapping("/{id}")
    public Mono<PeopleResponse> getPeopleById(@PathVariable int id) {
        return peopleService.getById(id);
    }

    @GetMapping
    public Flux<PeopleResponse> listPeople() {
        return peopleService.listAll();
    }
}
