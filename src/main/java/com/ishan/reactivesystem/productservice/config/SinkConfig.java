package com.ishan.reactivesystem.productservice.config;

import com.ishan.reactivesystem.productservice.dto.ProductDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Configuration
public class SinkConfig {

  @Bean
  public Many<ProductDTO> sink() {
    return Sinks.many()
        .replay()
        .limit(1);
  }

  @Bean
  public Flux<ProductDTO> productBroadcast(Many<ProductDTO> sink) {
    return sink.asFlux();
  }

}
