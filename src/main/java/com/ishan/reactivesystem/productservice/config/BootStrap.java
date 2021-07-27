package com.ishan.reactivesystem.productservice.config;

import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import com.ishan.reactivesystem.productservice.dao.Product;
import com.ishan.reactivesystem.productservice.dao.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BootStrap {

  @Autowired
  private ProductRepository productRepository;

  @PostConstruct
  public void testData() {
    log.info("Test Data Generation | ---------- Starting...");
    Commerce commerce = Faker.instance().commerce();
    List<Product> products = new ArrayList<>();

    int batch = 1;
    int batchSize = 1000;
    int totalProducts = 100_000;
    for (int i = 1; i <= totalProducts; i++) {
      Product product = new Product();
      product.setDescription(commerce.productName());
      product.setPrice(new BigDecimal(commerce.price()));
      products.add(product);

      //Batch and Save
      if (i % batchSize == 0) {
        log.info("Test Data Generation | ---------- Saving Batch: " + batch + "/"
            + totalProducts / batchSize);
        productRepository.saveAll(products).subscribe();
        products.clear();
        ++batch;
      }

    }

    log.info("Test Data Generation | ---------- Done");

  }

}