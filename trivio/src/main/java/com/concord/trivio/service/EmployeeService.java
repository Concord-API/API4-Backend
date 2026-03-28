package com.concord.trivio.service;

import com.concord.trivio.entity.Employee;

public interface EmployeeService {

    public Employee cadastrar(Employee employee);

    public Employee atualizar(Long id, Employee employee);

}
