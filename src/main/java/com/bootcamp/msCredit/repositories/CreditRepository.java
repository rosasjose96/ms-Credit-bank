package com.bootcamp.msCredit.repositories;

import com.bootcamp.msCredit.models.entities.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * The interface Credit repository.
 */
public interface CreditRepository extends ReactiveMongoRepository<Credit,String> {
    /**
     * Find all by customer identity number flux.
     *
     * @param customerIdentityNumber the customer identity number
     * @return the flux
     */
    public Flux<Credit> findAllByCustomerIdentityNumber(String customerIdentityNumber);
}
