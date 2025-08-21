package com.alloymobile.restapi.resource;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.alloymobile.restapi.persistence.Department;
import com.alloymobile.restapi.service.DepartmentEventsPublisher;
import com.alloymobile.restapi.service.DepartmentService;

@RestController
public class DepartmentResource {

    private final DepartmentService service;
    private final DepartmentEventsPublisher publisher;

    public DepartmentResource(DepartmentService service, DepartmentEventsPublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    @GetMapping(value = "/departments")
    public List<Department> getAll() {
        return this.service.getAll();
    }

    @GetMapping(value = "/departments/{id}")
    public Department getById(@PathVariable Long id) {
        return this.service.getById(id);
    }

    @PostMapping(value = "/departments", consumes = "application/json")
    public Department add(@RequestBody Department department) {
        Department saved = this.service.add(department);
        publisher.created(saved);
        return saved;
    }

    @PutMapping(value = "/departments/{id}", consumes = "application/json")
    public Department update(@PathVariable Long id, @RequestBody Department department) {
        Department updated = this.service.update(id, department);
        publisher.updated(updated);
        return updated;
    }

    @DeleteMapping(value = "/departments/{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
        publisher.deleted(id);
    }
}
