package com.ishan.reactivesystem.productservice.service;

import com.ishan.reactivesystem.productservice.dao.Product;
import com.ishan.reactivesystem.productservice.dto.ProductDTO;

public final class ProductMapper {

  private ProductMapper() {
  }

  public static ProductDTO toProductDTO(Product product) {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setUuid(product.getUuid());
    productDTO.setDescription(product.getDescription());
    productDTO.setPrice(product.getPrice());

    return productDTO;
  }

  public static Product toProduct(ProductDTO productDTO) {
    Product product = new Product();
    product.setUuid(productDTO.getUuid());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());

    return product;
  }

}
