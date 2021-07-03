package com.rfranco.virtualkeypad.services;

import com.rfranco.virtualkeypad.autogenerated.dtos.UserReponse;
import com.rfranco.virtualkeypad.exceptions.InternalServerException;
import com.rfranco.virtualkeypad.exceptions.NotFoundException;
import com.rfranco.virtualkeypad.models.UserModel;
import com.rfranco.virtualkeypad.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userrepository;


    //TODO: cuando se devuelve un usuario se crea un bucle de template y usuario
    public Optional<UserModel> getUser(Integer userId){
        if(!checkUser(userId)){
            log.error("User not found in the system");
            throw new NotFoundException("User not found in the system");
        }else {
            return this.userrepository.findById((long) userId);
        }
    }

    public List<UserModel> getUsers(String name,String email, String lastname){
        try {
            if (name == null && email == null && lastname == null) {
                List<UserModel> users = (List<UserModel>)this.userrepository.findAll();
                return users;
            } else if (name == null && email == null) {
                return this.userrepository.findByLastname(lastname);
            } else if (email == null && lastname == null) {
                return this.userrepository.findByName(name);
            } else if (name == null && lastname == null) {
                List<UserModel> emailList = new ArrayList<>();
                if(this.userrepository.findByEmail(email) != null) {
                    emailList.add(this.userrepository.findByEmail(email));
                }
                return emailList;
            } else if (name == null) {
                return this.userrepository.findByEmailAndLastname(email, lastname);
            } else if (lastname == null) {
                return this.userrepository.findByNameAndEmail(name, email);
            } else if (email == null) {
                return this.userrepository.findByNameAndLastname(name, lastname);
            } else {
                return this.userrepository.findByNameAndEmailAndLastname(name, email, lastname);
            }
        }catch (Exception e){
            log.error("Error getting user information.", e);
            throw new InternalServerException("Error getting users information");
        }
    }
    public void deleteUser(Integer userId){
        if(!checkUser(userId)){
            log.error("User not found in the system");
            throw new NotFoundException("User not found in the system");
        }else {
            this.userrepository.deleteById((long) userId);
        }
    }

    public void updateUser(Integer userId, UserReponse user){
        if(!checkUser(userId)){
            log.error("User not found in the system");
            throw new NotFoundException("User not found in the system");
        }else{
            try {
                UserModel usermodel = this.userrepository.findById((long) userId).get();
                usermodel.setName(user.getName());
                usermodel.setEmail(user.getEmail());
                usermodel.setLastname(user.getLastname());
                usermodel.setUserName(user.getUsername());

                this.userrepository.save(usermodel);
            }catch (Exception e){
                log.error("Error updating user", e);
                throw new InternalServerException("Error updating user");
            }
        }
    }

    public boolean checkUser(Integer userId){
        if(this.userrepository.findById((long) userId).isEmpty()){
            return false;
        }else{
            return true;
        }
    }

}