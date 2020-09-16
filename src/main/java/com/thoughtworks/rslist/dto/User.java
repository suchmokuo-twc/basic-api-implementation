package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @EqualsAndHashCode.Include
    @NotEmpty
    private String userName;

    private Integer age;

    private String gender;

    private String email;

    private String phone;
}
