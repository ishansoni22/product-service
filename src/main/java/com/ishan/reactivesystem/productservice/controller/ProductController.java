package com.ishan.reactivesystem.productservice.controller;

import com.ishan.reactivesystem.productservice.dto.ProductDTO;
import com.ishan.reactivesystem.productservice.service.ProductService;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ProductDTO> getAll(
      @RequestParam(value = "priceMin", required = false) BigDecimal priceMin,
      @RequestParam(value = "priceMax", required = false) BigDecimal priceMax) {
    if (Objects.nonNull(priceMin) && Objects.nonNull(priceMax)) {
      return productService.findInPriceRange(priceMin, priceMax);
    }
    return productService.getAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> get(@PathVariable("id") String id) {
    return productService.get(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Mono<ProductDTO> add(@RequestBody Mono<ProductDTO> productDTO) {
    return productService.insert(productDTO);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> update(@PathVariable("id") String id,
      @RequestBody Mono<ProductDTO> productDTO) {
    return productService.update(id, productDTO)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public Mono<Void> delete(@PathVariable("id") String id) {
    return productService.delete(id);
  }

  @GetMapping(value = "/feed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ProductDTO> subscribeNewProductFeed() {
    return productService.subscribeNewProductFeed();
  }

}
