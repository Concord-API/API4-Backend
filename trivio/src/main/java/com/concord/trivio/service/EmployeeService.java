package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.entity.Employee;

public interface EmployeeService {

    public Employee cadastrar(Employee employee);

    public Employee atualizar(Long id, Employee employee);

    public List<Employee> listar();

    public Employee buscarPorId(Long id);

}
