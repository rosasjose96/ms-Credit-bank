package com.bootcamp.msCredit.config;

import com.bootcamp.msCredit.handler.CreditHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * The type Router config.
 */
@Configuration
public class RouterConfig {

    /**
     * Routes router function.
     *
     * @param creditHandler the credit handler
     * @return the router function
     */
    @Bean
    public RouterFunction<ServerResponse> routes(final CreditHandler creditHandler) {

        return route(GET("/api/credit"), creditHandler::findAll)
                .andRoute(GET("/api/credit/customer/{customerIdentityNumber}"), creditHandler::findAllByCustomerIdentityNumber)
                .andRoute(GET("/api/credit/{contractNumber}"), creditHandler::findCredit)
                .andRoute(POST("/api/credit"), creditHandler::newCredit)
                .andRoute(PUT("/api/credit/{contractNumber}"), creditHandler::updateCredit)
                .andRoute(DELETE("/api/credit/{id}"), creditHandler::deleteCredit);
    }

}
