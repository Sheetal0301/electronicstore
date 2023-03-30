package com.bikkadIt.electronicstore.controller;

import com.bikkadIt.electronicstore.dtos.CategoryDto;
import com.bikkadIt.electronicstore.dtos.PageableResponse;
import com.bikkadIt.electronicstore.dtos.UserDto;
import com.bikkadIt.electronicstore.payloads.ApiResponse;
import com.bikkadIt.electronicstore.payloads.AppConstants;
import com.bikkadIt.electronicstore.payloads.ImageResponse;
import com.bikkadIt.electronicstore.services.CategoryService;
import com.bikkadIt.electronicstore.services.FileService;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String imageUploadPath;
    private Logger logger= LoggerFactory.getLogger(CategoryController.class);
    /**
     * @author Sheetal
     * @apiNote This Api is to save category data
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        logger.info("Initiated request for save the Category details");
        CategoryDto savedCategory = this.categoryService.createCategory(categoryDto);
        logger.info("Completed request for save the Category details");
        return new ResponseEntity<CategoryDto>(savedCategory, HttpStatus.CREATED);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to update category data
     * @return
     */
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable long categoryId)
    {
        logger.info("Initiated request for update the Category details");
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        logger.info("Completed request for update the Category details");
        return new ResponseEntity<CategoryDto>(updateCategory,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to get category by Id
     * @return
     */
    @GetMapping("/getCategory/{categoryId}")
    public ResponseEntity<CategoryDto> getSingleCategoryById(@PathVariable long categoryId)
    {
        logger.info("Initiated request for getting Category details: {}"+categoryId);
        CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
        logger.info("Completed request for getting  Category details");
        return new ResponseEntity<CategoryDto>(categoryDto,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to get all category
     * @return
     */
    @GetMapping("/getAllCategory")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
            @RequestParam(value = "pageNumber",defaultValue =AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.SORT_BY_CAT,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false) String sortDir
    )
    {
        logger.info("Initiated request for getting all Category details");
        PageableResponse<CategoryDto> allCategory = this.categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Completed request for getting all Category details");
        return new ResponseEntity<PageableResponse<CategoryDto>>(allCategory,HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to delete category
     * @return
     */
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable long categoryId)
    {
        logger.info("Initiated request for delete the Category details:{}"+categoryId);
        this.categoryService.deleteCategory(categoryId);
        logger.info("Completed request for delete the Category details");
        return new ResponseEntity<ApiResponse>(new ApiResponse(AppConstants.DELETE_CATEGORY,true),HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to upload category image
     * @return
     */
    //upload user image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("categoryImage") MultipartFile image, @PathVariable long categoryId) throws IOException {
        logger.info("Initiated request for upload categoty image");
        String uploadImageName = fileService.uploadImage(image, imageUploadPath);
        CategoryDto category = categoryService.getCategoryById(categoryId);
        category.setCoverImage(uploadImageName);
        categoryService.updateCategory(category,categoryId);
        logger.info("Completed request for upload categoty image");
        return new ResponseEntity<ImageResponse>(new ImageResponse("Image updated successfuly",true),HttpStatus.OK);
    }
    /**
     * @author Sheetal
     * @apiNote This Api is to serve category image
     * @return
     */
    //serve image
    @GetMapping("/image/{categoryId}")
    public void serveCategoryImage(@PathVariable long categoryId, HttpServletResponse response) throws IOException {
        logger.info("Initiated request for serve categoty image");
        CategoryDto category = categoryService.getCategoryById(categoryId);
        logger.info("category image name :{}"+category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath,category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Completed request for serve categoty image");
    }
}
