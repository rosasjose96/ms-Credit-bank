package com.bootcamp.msCredit.services.impl;

import com.bootcamp.msCredit.models.dto.Customer;
import com.bootcamp.msCredit.models.dto.CustomerDTO;
import com.bootcamp.msCredit.models.entities.Credit;
import com.bootcamp.msCredit.repositories.CreditRepository;
import com.bootcamp.msCredit.services.ICreditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


/**
 * The type Credit service.
 */
@Service
public class CreditServiceImpl implements ICreditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditServiceImpl.class);

    /**
     *
     */
    @Autowired
    private CreditRepository repository;

    /**
     *
     */
    @Qualifier("registrarWebClient")
    @Autowired
    private WebClient.Builder client;

    /**
     *
     * @param credit
     * @return
     */
    @Override
    public Mono<Credit> create(final Credit credit) {
        return repository.save(credit);
    }

    /**
     *
     * @return
     */
    @Override
    public Flux<Credit> findAll() {
        return repository.findAll();
    }

    /**
     *
     * @param id the id
     * @return
     */
    @Override
    public Mono<Credit> findById(final String id) {
        return repository.findById(id);
    }

    /**
     *
     * @param credit
     * @return
     */
    @Override
    public Mono<Credit> update(final Credit credit) {
        return repository.save(credit);
    }

    /**
     *
     * @param credit
     * @return
     */
    @Override
    public Mono<Void> delete(final Credit credit) {
        return repository.delete(credit);
    }

    @Override
    public Mono<Credit> findByContractNumber(final String contractNumber) {
        return repository.findByContractNumber(contractNumber);
    }

    /**
     *
     * @param customerIdentityNumber the customer identity number
     * @return
     */
    @Override
    public Mono<Customer> getCustomer(final String customerIdentityNumber) {
        Map<String, Object> params = new HashMap<>();
        LOGGER.info("initializing client query");
        params.put("customerIdentityNumber", customerIdentityNumber);
        return client.baseUrl("http://CUSTOMER-SERVICE/customer")
                .build()
                .get()
                .uri("/findCustomerCredit/{customerIdentityNumber}", customerIdentityNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Customer.class))
                .doOnNext(c -> LOGGER.info("Customer Response: Customer={}", c.getName()));
    }

    /**
     *
     * @param customerIdentityNumber the customer identity number
     * @return
     */
    @Override
    public Flux<Credit> findAllByCustomerIdentityNumber(final String customerIdentityNumber) {
        return repository.findAllByCustomerIdentityNumber(customerIdentityNumber);
    }

    @Override
    public Mono<Credit> validateCustomerIdentityNumber(final String customerIdentityNumber) {
        return repository.findByCustomerIdentityNumber(customerIdentityNumber)
                .switchIfEmpty(Mono.just(Credit.builder()
                        .customerIdentityNumber(null).build()));
    }
}
