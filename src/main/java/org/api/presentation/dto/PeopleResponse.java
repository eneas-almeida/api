package org.api.presentation.dto;

import org.api.domain.entity.People;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeopleResponse {
    private int id;
    private String name;
    private String email;

    public static PeopleResponse fromDomain(People people) {
        return new PeopleResponse(people.getId(), people.getName(), people.getEmail());
    }
}
