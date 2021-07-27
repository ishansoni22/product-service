package com.ishan.reactivesystem.productservice.dao;

import java.math.BigDecimal;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

  Flux<Product> findByPriceBetween(Range<BigDecimal> range);

}
