package com.bikkadIt.electronicstore.services;

import com.bikkadIt.electronicstore.dtos.PageableResponse;
import com.bikkadIt.electronicstore.dtos.UserDto;
import com.bikkadIt.electronicstore.entity.User;
import com.bikkadIt.electronicstore.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper mapper;
    private User user;
    private UserDto userDto;
    @BeforeEach
    public void init()
    {

         user = User.builder()
                 .userId(1L)
                .name("sheetal")
                .email("sheetu.jirapure@gmail.com")
                .about("i am java developer")
                .password("sheetu")
                .gender("female")
                .imageName("abc.png")
                .build();
    }
    @Test
    public void createUserTest()
    {
        //arange
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        //act
        UserDto userDto = userService.createUser(mapper.map(user, UserDto.class));
        //Assert
        Assertions.assertEquals("sheetal",userDto.getName());
    }
    @Test
    public void updateUserTest()
    {
        Long Id=1L;
        UserDto userDto = UserDto.builder()
                .name("sheetal Jirapure")
                .email("sheetal03@gmail.com")
                .about("i am tester")
                .gender("female")
                .imageName("xyz.png")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto,Id);
        System.out.println(updatedUser.getName());
        System.out.println(updatedUser.getImageName());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(updatedUser.getName(),userDto.getName(),"Name is not match");
    }
    //delete user test case
    @Test
    public void deleteUserTest()
    {
        Long userId=1L;
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }
    @Test
    public void getAllUsersTest()
    {
       User user1 = User.builder()
                .userId(2L)
                .name("priya")
                .email("priya.jadhav@gmail.com")
                .about("i am tester")
                .password("priya")
                .gender("female")
                .imageName("xyz.png")
                .build();

        User user2 = User.builder()
                .userId(3L)
                .name("vidya")
                .email("vidya.patil@gmail.com")
                .about("i m python developer")
                .password("vidya")
                .gender("female")
                .imageName("abc.png")
                .build();
        List<User> usersList = Arrays.asList(user, user1, user2);
        Page<User> page=new PageImpl<>(usersList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<UserDto> allUser = userService.getAllUser(1,5,"name","desc");
        Assertions.assertEquals(3,allUser.getContent().size());
    }

    @Test
    public void getUserByIdTest()
    {
      Long userId=1L;
      Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
      UserDto userDto1 = userService.getUserById(userId);
      Assertions.assertNotNull(userDto1);
      Assertions.assertEquals(user.getName(),userDto1.getName(),"name not matched");
    }
    @Test
    public void getUserByEmailTest()
    {
        String emailId="sheetu.jirapure@gmail.com";
        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));
        UserDto userEmail = userService.getUserByEmail(emailId);
        Assertions.assertNotNull(userEmail);
        Assertions.assertEquals(user.getEmail(),userEmail.getEmail(),"email not matched");

    }
    @Test
    public void searchUser()
    {

        User user1 = User.builder()
                .userId(2L)
                .name("priya")
                .email("priya.jadhav@gmail.com")
                .about("i am tester")
                .password("priya")
                .gender("female")
                .imageName("xyz.png")
                .build();

        User user2 = User.builder()
                .userId(3L)
                .name("pankaj kumar")
                .email("vidya.patil@gmail.com")
                .about("i m python developer")
                .password("vidya")
                .gender("female")
                .imageName("abc.png")
                .build();

        User user3 = User.builder()
                .userId(4L)
                .name("ankit kumar")
                .email("vidya.patil@gmail.com")
                .about("i m python developer")
                .password("vidya")
                .gender("female")
                .imageName("abc.png")
                .build();
        String keyword="kumar";
        Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(Arrays.asList(user,user1,user2,user3));
        List<UserDto> userDtos = userService.searchUser(keyword);
        System.out.println(userDtos);
        Assertions.assertEquals(4,userDtos.size(),"size not matched");
    }
}
