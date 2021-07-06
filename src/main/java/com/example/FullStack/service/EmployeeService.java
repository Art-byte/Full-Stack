package com.example.FullStack.service;

import com.example.FullStack.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    //METODS REST FULL
    List<Employee> getAllEmployees();
    void saveEmployee(Employee employee);
    Employee getEmployeeById(Long id);
    void deleteEmployee(Long id);

    //PAGINATION
    Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}
