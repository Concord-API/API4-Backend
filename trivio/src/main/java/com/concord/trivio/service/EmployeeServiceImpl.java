package com.concord.trivio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concord.trivio.entity.Employee;
import com.concord.trivio.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee cadastrar(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee atualizar(Long id, Employee employee) {
        if (!employeeRepository.existsById(id)) {
            return null;
        }
        
        Employee existente = employeeRepository.findById(id).get();
        existente.setName(employee.getName());
        existente.setAdmin(employee.isAdmin());
        existente.setActive(employee.isActive());
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
        return employeeRepository.findById(id).orElse(null);
    }

}
