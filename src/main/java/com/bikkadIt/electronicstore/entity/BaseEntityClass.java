package com.bikkadIt.electronicstore.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntityClass {

    @Column(name="is_active")
    private String isActive;

    @CreationTimestamp
    @Column(name="created_date",updatable = false)
    private Date createdDate;

    @UpdateTimestamp
    @Column(name="updated_date",updatable = false)
    private Date updatedDate;
}
