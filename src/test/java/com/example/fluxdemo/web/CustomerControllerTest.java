package com.example.fluxdemo.web;

import com.example.fluxdemo.config.Container;
import com.example.fluxdemo.domain.Customer;
import com.example.fluxdemo.domain.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;


//@SpringBootTest 모든 빈 다 가져오려고 풀스캔 때림
//@AutoConfigureWebTestClient 스프링부트테스트에서 WebTestClient 쓰려면 이거 추가해야됨

@WebFluxTest
@Import(Container.class) // 인터페이스는 안됨
public class CustomerControllerTest {
    @Autowired
    private WebTestClient webClient; // 비동기로 http 요청, WebFluxTest 어노테이션이 있으니까 Bean에 등록됨

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void findByIdTest() {
        // given
        Mono<Customer> givenData = Mono.just(new Customer("Jack", "Bauer"));

        // stub -> 행동지시
        when(customerRepository.findById(1L)).thenReturn(givenData); // 가짜 객체니까 (null)

        webClient.get().uri("/customer/{id}", 1L)
                .exchange()
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Jack")
                .jsonPath("$.lastName").isEqualTo("Bauer");
    }
}
