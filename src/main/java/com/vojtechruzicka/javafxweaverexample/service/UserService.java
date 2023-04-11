package com.vojtechruzicka.javafxweaverexample.service;

import com.vojtechruzicka.javafxweaverexample.entity.UserEntity;
import com.vojtechruzicka.javafxweaverexample.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo repo;
    public UserService( UserRepo repo) {this.repo = repo;}
    public Iterable<UserEntity> getAll(){return repo.findAll();}
    public void saveUser(UserEntity user){ repo.save(user);}
    public void deleteU(Integer id){ repo.deleteById(id);}
    public UserEntity findId(Integer id){ return repo.findById(id).get();}
    public Boolean logIn(String login, String pass) {
    return repo.findByLoginAndPass(login,pass) == null ? false: true;
    }
    public int getRol(String login, String pass) {
        return repo.findByLoginAndPass(login,pass).getRol();
    }


}
