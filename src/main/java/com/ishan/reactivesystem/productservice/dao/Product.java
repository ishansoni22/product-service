package com.ishan.reactivesystem.productservice.dao;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Product {

  @Id
  private String uuid;

  private String description;

  private BigDecimal price;

}
