package com.example.fluxdemo.domain;

import com.example.fluxdemo.DBinit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(DBinit.class)
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findOne() {
        customerRepository.findById(2L).subscribe(c -> System.out.println(c));

        StepVerifier
                .create(customerRepository.findById(2L))
                .expectNextMatches(c -> c.getFirstName().equals("Chloe"))
                .expectComplete()
                .verify();
    }

}
