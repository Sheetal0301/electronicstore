package com.bikkadIt.electronicstore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="categories")
public class Category{
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    public Long categotyId;
    @Column(name="category_title",length = 60,nullable = false)
    public String title;
    @Column(name="category_description",length = 500)
    public String description;
    @Column(name="category_image")
    public String coverImage;
}
