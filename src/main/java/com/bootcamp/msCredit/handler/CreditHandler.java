package com.bootcamp.msCredit.handler;

import com.bootcamp.msCredit.models.dto.CustomerDTO;
import com.bootcamp.msCredit.models.entities.Credit;
import com.bootcamp.msCredit.services.ICreditService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * The type Credit handler.
 */
@Slf4j(topic = "credit_handler")
@Component
public class CreditHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditHandler.class);

    /**
     * Insert depencies from ICreditService.
     */
    @Autowired
    private ICreditService service;

    /**
     * Find all mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> findAll(final ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Credit.class);
    }

    /**
     * Find credit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> findCredit(final ServerRequest request) {
        String contractNumber = request.pathVariable("contractNumber");

        return service.findByContractNumber(contractNumber).flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }


    /**
     * New credit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> newCredit(final ServerRequest request) {
        Mono<Credit> creditMono = request.bodyToMono(Credit.class);

        return creditMono.flatMap(credito ->
                        service.getCustomer(credito.getCustomerIdentityNumber())
           .flatMap(customer -> {
           credito.setAmount(credito.getCapital()
                   + (credito.getCapital() * credito.getInterestRate())
                   + credito.getCommission());
           credito.setCustomer(CustomerDTO.builder().name(customer.getName())
                   .code(customer.getCustomerType().getCode())
                   .customerIdentityNumber(customer.getCustomerIdentityNumber())
                   .build());
            credito.setAmountInitial(credito.getAmount());
            return service.validateCustomerIdentityNumber(customer
                            .getCustomerIdentityNumber())
                   .flatMap(accountFound -> {
                       if (accountFound.getCustomerIdentityNumber() != null
                               && (customer.getCustomerType().getCode().equals("1001")
                               || customer.getCustomerType().getCode().equals("1002"))) {
                           LOGGER.info("La cuenta encontrada es: "
                                   + accountFound.getCustomerIdentityNumber());
                           return Mono.empty();
                       } else {
                           LOGGER.info("No se encontró la cuenta ");
                           return service.create(credito);
                       }
                   });
                }))
                .flatMap(c -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(c)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Find all by customer identity number mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> findAllByCustomerIdentityNumber(final ServerRequest request) {
        String customerIdentityNumber = request.pathVariable("customerIdentityNumber");
        return  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(service.findAllByCustomerIdentityNumber(customerIdentityNumber), Credit.class);
    }

    /**
     * Delete credit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> deleteCredit(final ServerRequest request) {

        String id = request.pathVariable("id");

        Mono<Credit> creditMono = service.findById(id);

        return creditMono
                .doOnNext(c -> LOGGER.info("deleteConsumption: consumptionId={}", c.getId()))
                .flatMap(c -> service.delete(c).then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Update credit mono.
     *
     * @param request the request
     * @return the mono
     */
    public Mono<ServerResponse> updateCredit(final ServerRequest request) {
        Mono<Credit> creditMono = request.bodyToMono(Credit.class);
        String contractNumber = request.pathVariable("contractNumber");

        return service.findByContractNumber(contractNumber).zipWith(creditMono, (db, req) -> {
                    db.setAmount(req.getAmount());
                    return db;
                }).flatMap(c -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.update(c), Credit.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
