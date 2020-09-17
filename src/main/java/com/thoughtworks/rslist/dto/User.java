package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseDto {

    @JsonProperty("user_id")
    private Integer id;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Size(max = 8)
    @JsonProperty("user_name")
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    private Integer age;

    @NotEmpty
    @JsonProperty("user_gender")
    private String gender;

    @NotEmpty
    @Email
    @JsonProperty("user_email")
    private String email;

    @NotEmpty
    @Pattern(regexp = "^1\\d{10}$")
    @JsonProperty("user_phone")
    private String phone;
}
