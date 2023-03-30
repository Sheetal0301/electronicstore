package com.bikkadIt.electronicstore.services;

import com.bikkadIt.electronicstore.dtos.CategoryDto;
import com.bikkadIt.electronicstore.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {
    public CategoryDto createCategory(CategoryDto categotyDto);

    public CategoryDto updateCategory(CategoryDto categoryDto,long CategoryId);

    public void deleteCategory(long CategoryId);

    public CategoryDto getCategoryById(long CategoryId);

    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

}
