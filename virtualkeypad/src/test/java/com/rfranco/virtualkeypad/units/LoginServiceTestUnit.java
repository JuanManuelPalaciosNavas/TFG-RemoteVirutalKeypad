package com.rfranco.virtualkeypad.units;

import com.flextrade.jfixture.JFixture;
import com.rfranco.virtualkeypad.autogenerated.dtos.LoginRequest;
import com.rfranco.virtualkeypad.autogenerated.dtos.UserRequest;
import com.rfranco.virtualkeypad.configurations.VirtualKeypadConfiguration;
import com.rfranco.virtualkeypad.exceptions.ConflictException;
import com.rfranco.virtualkeypad.exceptions.UnauthorizedException;
import com.rfranco.virtualkeypad.models.UserModel;
import com.rfranco.virtualkeypad.repositories.LoginRepository;
import com.rfranco.virtualkeypad.repositories.UserRepository;
import com.rfranco.virtualkeypad.services.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {LoginService.class})
public class LoginServiceTestUnit {
    @MockBean
    private LoginRepository loginRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VirtualKeypadConfiguration virtualKeypadConfiguration;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginService loginService;


    JFixture fixture = new JFixture();

    @Test(expected = ConflictException.class)
    public void doRegisterUserForUserNameAlreadyRegistered(){
        UserRequest userRequest = fixture.create(UserRequest.class);
        UserModel userModel = UserModel.builder().userId(110)
                .userName(userRequest.getUsername())
                .lastname("test")
                .email("test@test.com").build();

        Mockito.when(this.userRepository.findByUserName(userRequest.getUsername())).thenReturn(userModel);
        this.loginService.registerUser(userRequest);
    }

    @Test
    public void doRegisterUserSuccesfully(){
        UserRequest userRequest = fixture.create(UserRequest.class);
        Mockito.when(this.userRepository.findByUserName(userRequest.getUsername())).thenReturn(null);
        Mockito.when(this.userRepository.findByEmail(userRequest.getEmail())).thenReturn(null);
        this.loginService.registerUser(userRequest);

        Mockito.verify(this.loginRepository, Mockito.times(1)).save(any(UserModel.class));
    }

    @Test(expected = UnauthorizedException.class)
    public void doLoginUserWithWrongPassword(){
        LoginRequest loginRequest = fixture.create(LoginRequest.class);
        UserModel userModel = UserModel.builder().password(loginRequest.getPassword()).build();

        Mockito.when(this.passwordEncoder.matches(loginRequest.getPassword(), userModel.getPassword())).thenReturn(Boolean.FALSE);
        Mockito.when(this.userRepository.findByUserName(loginRequest.getLogin())).thenReturn(userModel);
        this.loginService.loginUser(loginRequest);
    }

    @Test
    public void doLoginUserSuccesfully(){
        LoginRequest loginRequest = fixture.create(LoginRequest.class);
        UserModel userModel = UserModel.builder().userName(loginRequest.getLogin()).password(loginRequest.getPassword()).build();
        Mockito.when(this.userRepository.findByUserName(loginRequest.getLogin())).thenReturn(userModel);
        Mockito.when(this.passwordEncoder.matches(loginRequest.getPassword(), userModel.getPassword())).thenReturn(Boolean.TRUE);
        Mockito.when(this.virtualKeypadConfiguration.getSecretKey()).thenReturn("any");
        Assert.assertNotNull(this.loginService.loginUser(loginRequest));
    }



}
