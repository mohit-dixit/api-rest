package com.alloymobile.restapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.alloymobile.restapi.persistence.Department;
import com.alloymobile.restapi.persistence.DepartmentRepository;

@Service
public class DepartmentService {

    DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<Department> getAll(){
        return this.repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Department getById(Long id){
        return this.repository.findById(id).get();
    }

    public Department add(Department department){
        return this.repository.save(department);
    }

    public Department update(Long id, Department department){
        Optional<Department> dept = this.repository.findById(id);
        if(dept.isPresent()){
            dept.get().setName(department.getName());
            return this.repository.save(dept.get());
        }
        throw new RuntimeException();
    }

    public void delete(Long id){
        this.repository.deleteById(id);
    }
}
