package com.augusto.employees.payload;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import com.augusto.employees.model.Photo;
import com.augusto.employees.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter()
@Setter
public class EmployeeDto {
    @NotEmpty(message = "O campo nome não pode ser nulo")
    @Size( min = 3, message = "O campo nome precisa ter no mínimo 3 letras")
    private String name;

    @NotEmpty(message = "O campo email não pode ser nulo")
    @Email(message = "Email precisa ser válido")
    private String email;

    @NotEmpty(message = "O campo data de nascimento não pode ser nulo")
    @Past(message = " a data de aniversário não pode ser no futuro")
    private LocalDate birthDate;

    @NotEmpty(message = "O campo CPF não pode ser nulo")
    @CPF(message = "Cpf precisa ser válido")
    private String cpf;

    @NotEmpty(message = "O campo ocupação não pode ser nulo")
    private String occupation;

    @NotEmpty(message = "O campo senha não pode ser nulo")
    @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres")
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private int age;

    @NotEmpty(message= " O campo cargo não pode ser nulo")
    private Set<Role> roles;
    @JsonProperty( value = "uniqueCode", access = JsonProperty.Access.READ_ONLY)
    private String uniqueCode;
    private Photo photo;

}
