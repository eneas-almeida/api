package org.api.infrastructure.grpc;

import com.people.grpc.ServiceProto;
import com.people.grpc.PeopleServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.api.domain.client.PeopleClient;
import org.api.domain.entity.People;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class PeopleGrpcClient implements PeopleClient {

    @GrpcClient("people-service")
    private PeopleServiceGrpc.PeopleServiceBlockingStub peopleServiceStub;

    @Override
    public Mono<People> getPeopleById(int id) {
        return Mono.fromCallable(() -> {
            ServiceProto.PeopleRequest request = ServiceProto.PeopleRequest.newBuilder()
                    .setId(id)
                    .build();

            ServiceProto.PeopleResponse response = peopleServiceStub.getPeople(request);

            return new People(response.getId(), response.getName(), response.getEmail());
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<People> listPeople() {
        return Mono.fromCallable(() -> {
            ServiceProto.ListPeopleRequest request = ServiceProto.ListPeopleRequest.newBuilder().build();
            return peopleServiceStub.listPeople(request);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMapMany(response -> Flux.fromIterable(response.getPeopleList())
                .map(people -> new People(people.getId(), people.getName(), people.getEmail())));
    }
}
