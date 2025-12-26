package org.api.infrastructure.client;

import com.people.grpc.ServiceProto;
import com.people.grpc.PeopleServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.api.application.dto.PeopleResponse;
import org.api.domain.client.PeopleServiceClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class PeopleServiceGrpcClientImpl implements PeopleServiceClient {

    @GrpcClient("people-service")
    private PeopleServiceGrpc.PeopleServiceBlockingStub peopleServiceStub;

    private final PeopleGrpcMapper peopleGrpcMapper;

    @Override
    public Mono<PeopleResponse> getPeopleById(int id) {
        return Mono.fromCallable(() -> {
            ServiceProto.PeopleRequestGrpc request = ServiceProto.PeopleRequestGrpc.newBuilder()
                    .setId(id)
                    .build();

            ServiceProto.PeopleResponseGrpc response = peopleServiceStub.getPeople(request);

            return peopleGrpcMapper.toPeopleResponse(response);
        })
        .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<PeopleResponse> listPeople() {
        return Mono.fromCallable(() -> {
            ServiceProto.ListPeopleRequestGrpc request = ServiceProto.ListPeopleRequestGrpc.newBuilder().build();
            return peopleServiceStub.listPeople(request);
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMapMany(response -> Flux.fromIterable(response.getPeopleList())
                .map(peopleGrpcMapper::toPeopleResponse));
    }
}
