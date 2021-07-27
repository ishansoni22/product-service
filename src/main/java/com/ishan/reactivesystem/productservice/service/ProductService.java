package com.ishan.reactivesystem.productservice.service;

import com.ishan.reactivesystem.productservice.dao.Product;
import com.ishan.reactivesystem.productservice.dao.ProductRepository;
import com.ishan.reactivesystem.productservice.dto.ProductDTO;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks.Many;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private Many<ProductDTO> sink;

  @Autowired
  private Flux<ProductDTO> productBroadcast;

  public Flux<ProductDTO> getAll() {
    return productRepository.findAll()
        .map(ProductMapper::toProductDTO);
  }

  public Mono<ProductDTO> get(String id) {
    return this.productRepository.findById(id)
        .doOnNext(this::simulateChaos)
        .map(ProductMapper::toProductDTO);
  }

  public Mono<ProductDTO> insert(Mono<ProductDTO> productDTO) {
    return productDTO
        .map(ProductMapper::toProduct)
        .flatMap(this.productRepository::insert)
        .map(ProductMapper::toProductDTO)
        .doOnNext(sink::tryEmitNext);
  }

  public Mono<ProductDTO> update(String uuid, Mono<ProductDTO> productDTO) {
    return this.productRepository.findById(uuid)
        .flatMap(
            product -> productDTO
                .map(ProductMapper::toProduct)
                .doOnNext(p -> p.setUuid(product.getUuid()))
        )
        .flatMap(this.productRepository::save)
        .map(ProductMapper::toProductDTO);
  }

  public Mono<Void> delete(String uuid) {
    return this.productRepository.deleteById(uuid);
  }

  public Flux<ProductDTO> findInPriceRange(BigDecimal start, BigDecimal end) {
    return this.productRepository
        .findByPriceBetween(Range.closed(start, end))
        .map(ProductMapper::toProductDTO);
  }

  public Flux<ProductDTO> subscribeNewProductFeed() {
    return productBroadcast;
  }

  private void simulateChaos(Product product) {
    int chaosNumber = ThreadLocalRandom.current().nextInt(1, 10);
    if (chaosNumber > 5) {
      throw new RuntimeException(
          "Something went wrong while fetching " + product.getDescription() + ". Please try again");
    }
  }

}
