package com.bikkadIt.electronicstore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="products")
public class Product {
      @Id
      @GeneratedValue(strategy= GenerationType.IDENTITY)
      private Long productId;
      @Column(name="title")
      private String title;
      @Column(name="description",length = 1000)
      private String description;
      @Column(name="price")
      private double price;
      @Column(name="discounted_Price")
      private double discountedPrice;
      @Column(name="quantity")
      private Integer quantity;
      @Column(name="added_Date")
      private Date addedDate;
      @Column(name="live")
      private boolean live;
      @Column(name="stock")
      private boolean stock;
}
