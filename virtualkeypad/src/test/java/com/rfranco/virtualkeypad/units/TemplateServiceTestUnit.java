package com.rfranco.virtualkeypad.units;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flextrade.jfixture.JFixture;
import com.rfranco.virtualkeypad.autogenerated.dtos.TemplateResponse;
import com.rfranco.virtualkeypad.exceptions.NotFoundException;
import com.rfranco.virtualkeypad.models.TemplateModel;
import com.rfranco.virtualkeypad.models.UserModel;
import com.rfranco.virtualkeypad.repositories.TemplateRepository;
import com.rfranco.virtualkeypad.repositories.UserRepository;
import com.rfranco.virtualkeypad.services.TemplateService;
import com.rfranco.virtualkeypad.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TemplateService.class})
public class TemplateServiceTestUnit {
    @MockBean
    private TemplateRepository templaterepository;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private TemplateService templateService;

    JFixture fixture = new JFixture();

    @Test(expected = NotFoundException.class)
    public void doUpdateTemplateForWrongUser() throws JsonProcessingException {
        int userId = 50;
        int templateId = 20;
        Mockito.when(this.userRepository.findById((long)userId)).thenReturn(null);
        this.templateService.updateTemplate(userId,templateId, fixture.create(TemplateResponse.class));

    }

    @Test
    public void doUpdateTemplateSuccesfully() throws JsonProcessingException {
        int userId = 13;
        int templateId= 13;
        List<TemplateModel> list = new ArrayList<>();
        TemplateModel template = TemplateModel.builder()
                .templateId(templateId)
                .name("test")
                .buttons("Btest")
                .texts("Ttest")
                .build();
        list.add(template);
        UserModel user = UserModel.builder().userId(userId).templateModelList(list).build();
        Mockito.when(this.userService.getUser(userId)).thenReturn(Optional.of(user));
        this.templateService.updateTemplate(userId,templateId,fixture.create(TemplateResponse.class));
        Mockito.verify(this.templaterepository, Mockito.times(1)).save(any(TemplateModel.class));
    }

    @Test
    public void doDeleteTemplateSuccesfully(){
        int userId = 110;
        int templateId= 50;
        List<TemplateModel> list = new ArrayList<>();
        TemplateModel template = TemplateModel.builder()
                .templateId(templateId)
                .name("test")
                .buttons("Btest")
                .texts("Ttest")
                .build();
        list.add(template);
        UserModel user = UserModel.builder().userId(userId).templateModelList(list).build();
        Mockito.when(this.userService.getUser(userId)).thenReturn(Optional.of(user));
        this.templateService.deleteTemplate(userId,templateId);

        Mockito.verify(this.templaterepository, Mockito.times(1)).deleteById(any(long.class));
    }

    @Test(expected = NotFoundException.class)
    public void doDeleteTemplateForTemplateNotCreated(){
        int userId = 110;
        int templateId= 50;
        List<TemplateModel> list = new ArrayList<>();
        UserModel user = UserModel.builder().userId(userId).templateModelList(list).build();
        Mockito.when(this.userService.getUser(userId)).thenReturn(Optional.of(user));
        this.templateService.deleteTemplate(userId,templateId);
    }

    @Test(expected = NotFoundException.class)
    public void doGetTemplateForTemplateNotCreated() throws JsonProcessingException {
        int userId = 110;
        int templateId= 50;
        List<TemplateModel> list = new ArrayList<>();
        UserModel user = UserModel.builder().userId(userId).templateModelList(list).build();
        Mockito.when(this.userService.getUser(userId)).thenReturn(Optional.of(user));
        this.templateService.getTemplate(userId,templateId);
    }

    @Test(expected = NotFoundException.class)
    public void doGetTemplateForOtherUserOwner() throws JsonProcessingException {
        int userId1= 110;
        int userId2= 220;
        int templateId= 50;
        List<TemplateModel> list1 = new ArrayList<>();
        List<TemplateModel> list2 = new ArrayList<>();

        TemplateModel template = TemplateModel.builder()
                .templateId(templateId)
                .name("test")
                .buttons("Btest")
                .texts("Ttest")
                .build();
        list1.add(template);
        UserModel user1 = UserModel.builder().userId(userId1).templateModelList(list1).build();
        UserModel user2 = UserModel.builder().userId(userId2).templateModelList(list2).build();
        Mockito.when(this.userService.getUser(userId1)).thenReturn(Optional.of(user1));
        Mockito.when(this.userService.getUser(userId2)).thenReturn(Optional.of(user2));
        this.templateService.getTemplate(userId2,templateId);
    }
    @Test
    public void doGetTemplateSuccesfully() throws JsonProcessingException {
        int userId = 110;
        int templateId= 50;
        List<TemplateModel> list = new ArrayList<>();
        TemplateModel template = TemplateModel.builder()
                .templateId(templateId)
                .name("test")
                .buttons(" [{\"commandKey\":\"jnf9n2\",\"payload\":\"dq23jjg88\",\"positionX\":18.0156,\"positionY\":1.078,\"condition\":{\"commandKeyReceived\":\"fjkasdk2\",\"action\":true}}]")
                .texts("[{\"content\":\"this is the text\",\"positionX\":9.56,\"positionY\":0.078,\"condition\":{\"commandKeyReceived\":\"fjkasdk2\",\"action\":true}}]")
                .build();
        list.add(template);
        UserModel user = UserModel.builder().userId(userId).templateModelList(list).build();
        Mockito.when(this.userService.getUser(userId)).thenReturn(Optional.of(user));
        Mockito.when(this.templaterepository.findById((long)templateId)).thenReturn(Optional.of(template));
        this.templateService.getTemplate(userId,templateId);

        Mockito.verify(this.templaterepository, Mockito.times(1)).findById(any(long.class));
    }

}
