package com.alloymobile.restapi.resource;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import com.alloymobile.restapi.persistence.Users;
import com.alloymobile.restapi.service.UsersService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UsersResource {

    UsersService service;

    public UsersResource(UsersService service) {
        this.service = service;
    }
    
    @GetMapping(value = "/users")    
    public List<Users> getAll(){
        return this.service.getAll();
    }

    @GetMapping(value = "/users/{id}")    
    public Users getById(@PathVariable Long id){
        return this.service.getById(id);
    }

    @PostMapping(value = "/signup")
    public Map<String, Object> add(Users user) {
        return this.service.add(user);
    }

    @PutMapping(value = "/users/{id}", consumes = "application/json")
    public Users update(@PathVariable Long id, @RequestBody Users user) {
        return this.service.update(id, user);
    }

    @DeleteMapping(value = "/users/{id}")
    public void delete(@PathVariable Long id){
        this.service.delete(id);
    }
}
