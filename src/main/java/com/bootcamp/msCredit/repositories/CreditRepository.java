package com.bootcamp.msCredit.repositories;

import com.bootcamp.msCredit.models.entities.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    Flux<Credit> findAllByCustomerIdentityNumber(String customerIdentityNumber);

    /**
     * Find credit by contractNumber Mono.
     *
     * @param contractNumber the contract number
     * @return the Mono
     */
    Mono<Credit> findByContractNumber(String contractNumber);

    /**
     * Find by customer identity number mono.
     *
     * @param customerIdentityNumber the customer identity number
     * @return the mono
     */
    Mono<Credit> findByCustomerIdentityNumber(String customerIdentityNumber);
}
