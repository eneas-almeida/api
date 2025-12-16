package org.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class People {
    private final int id;
    private final String name;
    private final String email;
}
