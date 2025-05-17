package com.alloymobile.restapi.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.alloymobile.restapi.persistence.Department;
import com.alloymobile.restapi.service.DepartmentService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
public class DepartmentResource {

    DepartmentService service;

    public DepartmentResource(DepartmentService service){
        this.service = service;
    }
    
    @GetMapping(value = "/departments")    
    public List<Department> getAll(){
        return this.service.getAll();
    }

    @GetMapping(value = "/departments/{id}")    
    public Department getById(@PathVariable Long id){
        return this.service.getById(id);
    }

    @PostMapping(value = "/departments")
    public Department add(Department department) {
        return this.service.add(department);
    }

    @PutMapping(value = "/departments/{id}", consumes = "application/json")
    public Department update(@PathVariable Long id, @RequestBody Department department) {
        return this.service.update(id, department);
    }

    @DeleteMapping(value = "/departments/{id}")
    public void delete(@PathVariable Long id){
        this.service.delete(id);
    }   

}
