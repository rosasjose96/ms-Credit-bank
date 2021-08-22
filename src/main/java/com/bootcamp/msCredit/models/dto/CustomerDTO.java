package com.bootcamp.msCredit.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Customer dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDTO {
    /**
     * @name is the field that represent the name of the customer.
     */
    private String name;
    private String code;
    private String customerIdentityNumber;
}
