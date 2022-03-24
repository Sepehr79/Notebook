package com.kucess.notebook.model.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kucess.notebook.bussiness.validation.UniqueUserName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@SuperBuilder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
public class UserIO {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @UniqueUserName
    private String userName;

    @Size(min = 8, message = "Password should have more than 8 characters")
    private String password;


}
