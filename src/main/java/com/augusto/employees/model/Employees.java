package com.augusto.employees.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.augusto.employees.payload.EmployeeDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "cpf" }),
        @UniqueConstraint(columnNames = { "email" }),
        @UniqueConstraint(columnNames = { "unique_code" })
})
public class Employees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "employees", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private Photo photo;
    private String name;
    private int age;
    private String email;
    private LocalDate birthDate;
    private String cpf;
    @OneToMany(mappedBy = "employees")
    @JsonManagedReference
    private Set<ClockIn> points;
    @Column(name = "unique_code")
    private String uniqueCode;
    private String password;
    private String occupation;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonManagedReference
    private Set<Role> roles;

    public Employees(EmployeeDto employeeDto) {
        this.name = employeeDto.getName();
        this.age = employeeDto.getAge();
        this.email = employeeDto.getEmail();
        this.birthDate = employeeDto.getBirthDate();
        this.cpf = employeeDto.getCpf();
        this.uniqueCode = employeeDto.getUniqueCode();
        this.password = employeeDto.getPassword();
        this.occupation = employeeDto.getOccupation();
    }
}
