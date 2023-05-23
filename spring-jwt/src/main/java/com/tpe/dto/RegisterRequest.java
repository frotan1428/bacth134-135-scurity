package com.tpe.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "enter first name")
    @NotNull
    private String firstName;

    @NotBlank(message = "enter last name")
    @NotNull
    private String lastName;

    @NotBlank(message = "enter user name")
    @NotNull
    @Size(min=5, max=10, message="Please provide a username between {min} and {max}")
    private  String userName;

    @NotBlank(message = "enter passord")
    private String password;


    private Set<String> roles;


}
