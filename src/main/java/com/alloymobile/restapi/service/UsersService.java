package com.alloymobile.restapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alloymobile.restapi.persistence.Users;
import com.alloymobile.restapi.persistence.UsersRepository;

@Service
public class UsersService {

    UsersRepository repository;

    public UsersService(UsersRepository repository) {
        this.repository = repository;
    }

    public List<Users> getAll(){
        return this.repository.findAll();
    }

    public Users getById(Long id){
        return this.repository.findById(id).get();
    }

    public Users add(Users user){
        user.setRole("USER"); // Default role
        user.setActive(true); // Default active status
        return this.repository.save(user);
    }

    public Users update(Long id, Users user){
        Optional<Users> usr = this.repository.findById(id);
        if(usr.isPresent()){
            usr.get().setUsername(user.getUsername());
            usr.get().setEmail(user.getEmail());
            usr.get().setPassword(user.getPassword());
            return this.repository.save(usr.get());
        }
        throw new RuntimeException();
    }

    public void delete(Long id){
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
