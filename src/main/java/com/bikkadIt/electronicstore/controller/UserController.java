package com.bikkadIt.electronicstore.controller;

import com.bikkadIt.electronicstore.dtos.PageableResponse;
import com.bikkadIt.electronicstore.dtos.UserDto;
import com.bikkadIt.electronicstore.payloads.ApiResponse;
import com.bikkadIt.electronicstore.payloads.AppConstants;
import com.bikkadIt.electronicstore.payloads.ImageResponse;
import com.bikkadIt.electronicstore.services.FileService;
import com.bikkadIt.electronicstore.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController{
    Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    /**
     * @author Sheetal
     * @apiNote This Api is to save user data
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
    {
        logger.info("Initiated request for save the user details");
        UserDto createdUser = userService.createUser(userDto);
        logger.info("Completed request for save the user details");
        return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to update user data
     * @return
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable Long userId)
    {
        logger.info("Initiated request for update the user details");
        UserDto updateUser = userService.updateUser(userDto, userId);
        logger.info("Completed request for update the user details");
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to delete user data
     * @return
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId)
    {
        logger.info("Initiated request for delete the user details:{}"+userId);
        userService.deleteUser(userId);
        logger.info("Completed request for delete the user details");
        return new ResponseEntity<ApiResponse>(new ApiResponse(AppConstants.DELETE_USER,true),HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to get all user data
     * @return
     */
    @GetMapping("/getAll")
    public ResponseEntity<PageableResponse<UserDto>> getALLUser(
            @RequestParam(value = "pageNumber",defaultValue =AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir)
    {
        logger.info("Initiated request for getting all user details");
        PageableResponse<UserDto> allUser = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Completed request for getting all user details");
        return new ResponseEntity<PageableResponse<UserDto>>(allUser,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to get user by userId
     * @return
     */
    @GetMapping("/getById/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Long userId)
    {
        logger.info("Initiated request for getting the user details by userId:{}"+userId);
        UserDto newDto = userService.getUserById(userId);
        logger.info("Completed request for getting the user details");
        return new ResponseEntity<UserDto>(newDto,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to get user by email
     * @return
     */
    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        logger.info("Initiated request call for getting user by email :{}"+email);
        UserDto userByEmail = userService.getUserByEmail(email);
        logger.info("Completed request call for getting user by email");
        return new ResponseEntity<UserDto>(userByEmail,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to get all user by keyword
     * @return
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword)
    {
        logger.info("Initiated request call for getting user by keyword");
        List<UserDto> results = userService.searchUser(keyword);
        logger.info("Completed request call for getting user by keyword");
        return new ResponseEntity<List<UserDto>>(results,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to upload user image
     * @return
     */
    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile image,@PathVariable long userId) throws IOException {
        logger.info("Initiated request for upload user image");
        String uploadImageName = fileService.uploadImage(image, imageUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setImageName(uploadImageName);
        userService.updateUser(user, userId);
        logger.info("Initiated request for upload user image");
        return new ResponseEntity<ImageResponse>(new ImageResponse("Image updated successfuly",true),HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to serve user image
     * @return
     */
    //serve image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable long userId, HttpServletResponse response) throws IOException {
        logger.info("Initiated request for serve user image");
        UserDto user = userService.getUserById(userId);
        logger.info("user image name :{}"+user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Completed request for serve user image");
    }
}
