package com.augusto.employees.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.augusto.employees.exceptions.EmployeeException;
import com.augusto.employees.exceptions.ResourceNotFoundException;
import com.augusto.employees.model.Employees;
import com.augusto.employees.model.Photo;
import com.augusto.employees.model.Role;
import com.augusto.employees.payload.EmployeeDto;
import com.augusto.employees.repository.EmployeesRepository;
import com.augusto.employees.repository.PhotoRepository;
import com.augusto.employees.repository.RoleRepository;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmployeesRepository employeesRepository;

    public EmployeeDto createEmployee(EmployeeDto employeeDto, MultipartFile file) throws IOException {
        var newEmployee = toEmployees(employeeDto);
        newEmployee.setAge(findAge(newEmployee.getBirthDate()));
        verifyEmailAndCpf(newEmployee.getEmail(), newEmployee.getCpf());
        newEmployee.setPassword(encoder.encode(newEmployee.getPassword()));
        newEmployee.setRoles(setRole(employeeDto.getRoles()));
        newEmployee.setUniqueCode(generateUc());
        var newEmployeeDto = toEmployeeDto(employeesRepository.save(newEmployee));
        if (file != null) {
            createPhoto(file, newEmployee);
        }
        return newEmployeeDto;
    }

    private String generateUc() {
        return UUID.randomUUID().toString();
    }

    private Set<Role> setRole(Set<Role> roles) {
        Set<Role> newRoles = roles.stream().map(x -> roleRepository.findByName(x.getName()).get())
                .collect(Collectors.toSet());
        return newRoles;
    }

    private void createPhoto(MultipartFile file, Employees employee) throws IOException {
        var photo = new Photo();
        photo.setData(file.getBytes());
        photo.setType(file.getContentType());
        photo.setName(file.getName());
        photo.setEmployees(employee);
        photoRepository.save(photo);
    }

    private int findAge(LocalDate birthDate) {
        var bDate = birthDate;
        var today = LocalDate.now();
        var age = Period.between(bDate, today).getYears();
        return age;
    }

    private void verifyEmailAndCpf(String email, String cpf) {
        var employee = employeesRepository.findByCpfContainingIgnoreCaseOrEmailContainingIgnoreCase(cpf, email);
        if (employee.isPresent()) {
            throw new EmployeeException(HttpStatus.BAD_REQUEST,
                    "já existe um funcionário com esse email ou cpf, verifique os campos");
        }
    }

    public Page<EmployeeDto> listAll(Pageable pageable) {
        Page<Employees> employeesList = employeesRepository.findAll(pageable);
        Page<EmployeeDto> employeesDtoList = employeesList.map(t -> toEmployeeDto(t));
        return employeesDtoList;
    }

    public EmployeeDto findById(Long id) {
        var employee = employeesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        var employeeDto = toEmployeeDto(employee);
        return employeeDto;
    }

    public EmployeeDto findByCpf(String cpf) {
        var employee = employeesRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "CPF", cpf));

        return toEmployeeDto(employee);
    }

    public EmployeeDto findByEmail(String email) {
        var employee = employeesRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "Email", email));
        return toEmployeeDto(employee);
    }

    public EmployeeDto putEmployee(EmployeeDto employeeDto) {

        var employee = getEmployee();
        employee.setEmail(employeeDto.getEmail());
        verifyUpdateEmail(employee.getEmail());
        employee.setPassword(
                employeeDto.getCpf() == null ? employee.getCpf() : encoder.encode(employeeDto.getPassword()));
        employee.setOccupation(
                employeeDto.getOccupation() == null ? employee.getOccupation() : employeeDto.getOccupation());
        var newEmployeeDto = toEmployeeDto(employeesRepository.save(employee));

        return newEmployeeDto;
    }

    private void verifyUpdateEmail(String email) {
        var employee = employeesRepository.findByEmail(email);
        if (employee.isPresent()) {
            throw new EmployeeException(HttpStatus.BAD_REQUEST, "O email já existe");
        }
    }

    public void removeEmployee(Long id) {
        var employee = employeesRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee", "Id", id);
        }
        employeesRepository.delete(employee.get());
    }

    private Employees toEmployees(EmployeeDto employeeDto) {
        Employees employees = mapper.map(employeeDto, Employees.class);
        return employees;
    }

    private EmployeeDto toEmployeeDto(Employees employee) {
        EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
        return employeeDto;
    }

    public EmployeeDto my_page() {
        return toEmployeeDto(getEmployee());
    }

    public Employees getEmployee() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var name = ((UserDetails) principal).getUsername();
        var employee = employeesRepository.findByCpfOrEmail(name, name);
        return employee.get();
    }

    public EmployeeDto getByUniqueCode(String uniqueCode) {
        var employee = employeesRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "unique code", uniqueCode));
        return toEmployeeDto(employee);
    }

    public Photo getPhoto(Long id) {
        var employee = employeesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo", "id", id));
        return employee.getPhoto() == null ? null : employee.getPhoto();
    }
}
