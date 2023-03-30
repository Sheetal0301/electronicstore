package com.bikkadIt.electronicstore.services.impl;
import com.bikkadIt.electronicstore.dtos.PageableResponse;
import com.bikkadIt.electronicstore.dtos.UserDto;
import com.bikkadIt.electronicstore.entity.User;
import com.bikkadIt.electronicstore.exception.ResourceNotFoundException;
import com.bikkadIt.electronicstore.payloads.AppConstants;
import com.bikkadIt.electronicstore.repositories.UserRepository;
import com.bikkadIt.electronicstore.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Value("${user.profile.image.path}")
    private String imagePath;

    /**
     *@author sheetal
     *@apiNote This method is used to create user
     *@param userDto
     * @return
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        logger.info("Initiated dao call for save the user details");
        User user = dtoToEntity(userDto);
        user.setIsActive(AppConstants.YES);
        User savedUser = userRepository.save(user);
        UserDto saveDto = entityToDto(savedUser);
        logger.info("Completed dao call for save the user details");
        return saveDto;
    }
    /**
     *@author sheetal
     *@apiNote This method is used to update user
     *@param userDto
     *@return
     */
    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        logger.info("Initiated dao call for update the user details");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        User updatedUser = userRepository.save(user);
        UserDto updateUser = entityToDto(updatedUser);
        logger.info("Completed dao call for update the user details");
        return updateUser;
    }
    /**
     *@author sheetal
     *@apiNote This method is used to get delete user
     */
    @Override
    public void deleteUser(Long userId) {
        logger.info("Initiated dao call for delete user details :{}"+userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
       // user.setIsActive(AppConstants.NO);
       String fullPath= imagePath + user.getImageName();
       try {
           Path path = Paths.get(fullPath);
           Files.delete(path);
       }
       catch(NoSuchFileException ex) {
           logger.info("user image not found in folder");
          ex.printStackTrace();
       }
       catch(IOException e)
       {
           e.printStackTrace();
       }
        logger.info("Completed dao call for delete user details");
       // userRepository.save(user);     //for soft delete purpose
        userRepository.delete(user);   //for hard delete purpose
    }
    /**
     *@author sheetal
     *@apiNote This method is used to get all user
     *@return
     */
    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        logger.info("Initiated dao call for getting all user details");
        Sort sort=null;
        if(sortDir.equalsIgnoreCase("asc"))
        {
            sort=Sort.by(sortBy).ascending();
        }
        else
        {
            sort=Sort.by(sortBy).descending();
        }
        PageRequest request = PageRequest.of(pageNumber, pageSize,sort);
        Page<User> page = userRepository.findAll(request);
        List<User> users = page.getContent();
        List<UserDto> userDtos = users.stream().map((user) -> entityToDto(user)).collect(Collectors.toList());
        PageableResponse<UserDto> response=new PageableResponse<>();
        response.setContent(userDtos);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLastPage(page.isLast());
        logger.info("Completed dao call for getting all user details");
        return response;
    }
    /**
     *@author sheetal
     *@apiNote This method is used to get single user
     *@return
     */
    @Override
    public UserDto getUserById(Long userId) {
        logger.info("Initiated dao call for getting the user details by userId :{}"+userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        UserDto userDto = entityToDto(user);
        logger.info("Completed dao call for getting the user details");
        return userDto;
    }
    /**
     *@author sheetal
     *@apiNote This method is used to get user by email
     *@return
     */
    @Override
    public UserDto getUserByEmail(String email) {
        logger.info("Initiated dao call for getting user by email :{}"+email);
        User userEmail = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found with email " + email));
        UserDto userDto = entityToDto(userEmail);
        logger.info("Completed dao call for getting user by email");
        return userDto;
    }
    /**
     *@author sheetal
     *@apiNote This method is used to search keyword
     *@return
     */
    @Override
    public List<UserDto> searchUser(String keyword) {
        logger.info("Initiated dao call for getting user by keyword");
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> userDtos = users.stream().map((user) -> entityToDto(user)).collect(Collectors.toList());
        logger.info("Completed dao call for getting user by keyword");
        return userDtos;
    }

    private User dtoToEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .about(userDto.getAbout())
                .gender(userDto.getGender())
                .imageName(userDto.getImageName())
                .build();
        return user;
    }

    private UserDto entityToDto(User savedUser) {
        UserDto userDto = UserDto.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .about(savedUser.getAbout())
                .gender(savedUser.getGender())
                .imageName(savedUser.getImageName())
                .build();
        return userDto;
    }
    }

