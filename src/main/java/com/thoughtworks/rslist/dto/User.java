package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = 8)
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;

    @NotEmpty
    private String gender;

    private String email;

    private String phone;
}
