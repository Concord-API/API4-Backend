package com.concord.trivio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.entity.Employee;
import com.concord.trivio.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public Employee cadastrar(Employee employee) {
        if (employee == null ||
                employee.getName() == null || employee.getName().isBlank() ||
                employee.getEmail() == null || employee.getEmail().isBlank() ||
                employee.getPassword() == null || employee.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Colaborador com informações inválidas");
        }
        employee.setActive(definirActive(employee.getActive()));
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee atualizar(Long id, Employee employee) {
        Employee existente = buscarPorId(id);
        if (employee == null ||
                employee.getName() == null || employee.getName().isBlank() ||
                employee.getEmail() == null || employee.getEmail().isBlank() ||
                employee.getPassword() == null || employee.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Colaborador com informações inválidas");
        }

        existente.setName(employee.getName());
        existente.setAdmin(employee.isAdmin());
        existente.setActive(definirActive(employee.getActive()));
        existente.setEmail(employee.getEmail());
        existente.setPassword(employee.getPassword());
        
        return employeeRepository.save(existente);
    }

    @Override
    public List<Employee> listar() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee buscarPorId(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colaborador não encontrado"));
    }

    private Boolean definirActive(Boolean active) {
        return !Boolean.FALSE.equals(active);
    }
}
