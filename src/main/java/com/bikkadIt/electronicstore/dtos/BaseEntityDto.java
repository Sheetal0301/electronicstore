package com.bikkadIt.electronicstore.dtos;

import javax.persistence.Column;
import java.time.LocalDate;

public class BaseEntityDto {

    private String isActive;

    private LocalDate createdDate;

    private LocalDate updatedDate;
}
