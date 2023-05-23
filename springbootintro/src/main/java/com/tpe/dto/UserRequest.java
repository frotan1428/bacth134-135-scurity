package com.tpe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    @NotBlank(message = "Please Provide First Name")
    private String firstName;


    @NotBlank(message = "Please Provide Last  Name")
    private String lastName;


    @NotBlank(message = "Please Provide User Name")
    @Size(min = 4,max=10,message = "Please provide a user name between {min} and {max}")
    private  String userName;


    @NotBlank(message = "Please Provide  password")
    @Size(min = 4,max=20,message = "Please provide a password  between {min} and {max}")
    private String password;

}
