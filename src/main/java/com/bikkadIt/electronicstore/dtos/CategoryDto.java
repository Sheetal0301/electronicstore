package com.bikkadIt.electronicstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto{
    public Long categotyId;
    @NotBlank
    @Size(min=4,message="title must be minimum of 4 characters!!")
    public String title;
    @NotBlank(message = "description required!!")
    public String description;
    public String coverImage;
}
