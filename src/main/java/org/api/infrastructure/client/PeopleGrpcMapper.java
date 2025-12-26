package org.api.infrastructure.client;

import com.people.grpc.ServiceProto;
import org.api.application.dto.PeopleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
		componentModel = "spring",
		implementationName = "PeopleGrpcMapperImpl"
)
public interface PeopleGrpcMapper {

	@Mapping(target = "id", source = "id")
	@Mapping(target = "name", source = "name")
	@Mapping(target = "email", source = "email")
	PeopleResponse toPeopleResponse(ServiceProto.PeopleResponseGrpc response);
}
