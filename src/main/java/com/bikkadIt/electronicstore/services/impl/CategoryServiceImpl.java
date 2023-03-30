package com.bikkadIt.electronicstore.services.impl;
import com.bikkadIt.electronicstore.dtos.CategoryDto;
import com.bikkadIt.electronicstore.dtos.PageableResponse;
import com.bikkadIt.electronicstore.dtos.UserDto;
import com.bikkadIt.electronicstore.entity.Category;
import com.bikkadIt.electronicstore.exception.ResourceNotFoundException;
import com.bikkadIt.electronicstore.repositories.CategoryRepository;
import com.bikkadIt.electronicstore.services.CategoryService;
import lombok.experimental.Helper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${category.profile.image.path}")
    private String imagePath;
    private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);
    /**
     *@author sheetal
     *@apiNote This method is used to create category
     *@param categotyDto
     *@return
     */
    @Override
    public CategoryDto createCategory(CategoryDto categotyDto) {
        logger.info("Initiated Dao call for save Category details");
        Category category = this.modelMapper.map(categotyDto, Category.class);
        Category saveCat = this.categoryRepository.save(category);
        logger.info("Completed Dao call for save Category details");
        return this.modelMapper.map(saveCat,CategoryDto.class);
    }
    /**
     *@author sheetal
     *@apiNote This method is used to update category
     *@param categoryDto,CategoryId
     *@return
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long CategoryId) {
        logger.info("Initiated dao call for update the Category details");
        Category category = this.categoryRepository.findById(CategoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", CategoryId));
        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updateCat = categoryRepository.save(category);
        logger.info("Completed dao call for update the Category details");
        return this.modelMapper.map(updateCat,CategoryDto.class);
    }
    /**
     *@author sheetal
     *@apiNote This method is used to delete category
     *@param CategoryId
     *@return
     */
    @Override
    public void deleteCategory(long CategoryId) {
        logger.info("Initiated dao call for delete Category details :{}"+CategoryId);
        Category category = this.categoryRepository.findById(CategoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", CategoryId));
        String fullPath= imagePath + category.getCoverImage();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }
        catch(NoSuchFileException ex) {
            logger.info("category image not found in folder");
            ex.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        logger.info("Completed dao call for delete Category details");
        this.categoryRepository.delete(category);
    }
    /**
     *@author sheetal
     *@apiNote This method is used to get category by Id
     *@param CategoryId
     *@return
     */
    @Override
    public CategoryDto getCategoryById(long CategoryId) {
        logger.info("Initiated dao call for getting Category details :{}"+CategoryId);
        Category category = this.categoryRepository.findById(CategoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", CategoryId));
        logger.info("Completed dao call for getting Category details");
        return this.modelMapper.map(category,CategoryDto.class);
    }
    /**
     *@author sheetal
     *@apiNote This method is used to get all category
     *@return
     */
    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
        logger.info("Initiated dao call for getting all Category details");
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> page = this.categoryRepository.findAll(pageRequest);
        List<Category> categories = page.getContent();
        List<CategoryDto> categoryDtoList = categories.stream().map((cat) -> this.modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
        PageableResponse<CategoryDto> response=new PageableResponse<>();
        response.setContent(categoryDtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLastPage(page.isLast());
        logger.info("Completed dao call for getting all Category details");
        return response;
    }
}
