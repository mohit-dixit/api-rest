package com.alloymobile.restapi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alloymobile.restapi.persistence.RoleRepository;
import com.alloymobile.restapi.persistence.Users;
import com.alloymobile.restapi.persistence.Role;
import com.alloymobile.restapi.persistence.UsersRepository;

@Service
public class UsersService {

    UsersRepository repository;
    RoleRepository rolerepository;

    public UsersService(UsersRepository repository, RoleRepository rolerepository) {
        this.rolerepository = rolerepository;
        this.repository = repository;
    }

    public List<Users> getAll() {
        return this.repository.findAll();
    }

    public Users getById(Long id) {
        return this.repository.findById(id).get();
    }

    public Map<String, Object> add(Users user) {
        Map<String, Object> response = new HashMap<>();
        try {            
            Optional<Users> usr = this.repository.findByUsername(user.getUsername());
            if (usr.isPresent()) {
                response.put("message", "Username already exists. Please choose a different username.");
                response.put("user", null);
                response.put("success", false);
            } else {
                Optional<Role> role = this.rolerepository.findByRolename("USER");
                if (role.isPresent()) {
                    Long roleid = role.get().getRole_id();
                    user.setRole(roleid); // Default role
                    user.setActive(true); // Default active status
                    Users savedUser = this.repository.save(user);
                    response.put("message", "User created successfully.");
                    response.put("user", savedUser);
                    response.put("success", true);
                } else {
                    response.put("message", "Error in creating User. Please check the logs.");
                    response.put("user", null);
                    response.put("success", false);
                }
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in creating User. Please check the logs.");
        }
    }

    public Users update(Long id, Users user) {
        Optional<Users> usr = this.repository.findById(id);
        if (usr.isPresent()) {
            usr.get().setUsername(user.getUsername());
            usr.get().setEmail(user.getEmail());
            usr.get().setPassword(user.getPassword());
            return this.repository.save(usr.get());
        }
        throw new RuntimeException();
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    public Boolean login(Users user) {
        Optional<Users> usr = this.repository.findByUsername(user.getUsername());
        if (usr.isPresent()) {
            return usr.get().getPassword().equals(user.getPassword());
        }
        return false;
    }
}
