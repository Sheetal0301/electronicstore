package com.bikkadIt.electronicstore.services;

import com.bikkadIt.electronicstore.dtos.PageableResponse;
import com.bikkadIt.electronicstore.dtos.UserDto;

import java.util.List;

public interface UserService {
     public UserDto createUser(UserDto userDto);
     public UserDto updateUser(UserDto userDto,Long userId);
     public void deleteUser(Long userId);
     public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
     public UserDto getUserById(Long userId);
     public UserDto getUserByEmail(String email);
     public List<UserDto> searchUser(String keyword);
}
