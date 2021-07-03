package com.rfranco.virtualkeypad.controllers;

import com.rfranco.virtualkeypad.autogenerated.api.AccessApi;
import com.rfranco.virtualkeypad.autogenerated.dtos.LoginRequest;
import com.rfranco.virtualkeypad.autogenerated.dtos.LoginResponse;
import com.rfranco.virtualkeypad.autogenerated.dtos.UserRequest;
import com.rfranco.virtualkeypad.services.LoginService;
import com.rfranco.virtualkeypad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
public class AccessApiController implements AccessApi {

    @Autowired
    private LoginService loginservice;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<Void> createUser(@Valid UserRequest userRequest) {
        this.loginservice.registerUser(userRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<LoginResponse> login(@Valid LoginRequest loginRequest) {
        return new ResponseEntity(this.loginservice.loginUser(loginRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> logout(@Valid LoginRequest loginRequest) {
        this.loginservice.logoutUser(loginRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}