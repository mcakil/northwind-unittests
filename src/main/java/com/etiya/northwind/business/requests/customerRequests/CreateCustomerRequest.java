package com.etiya.northwind.business.requests.customerRequests;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {

    @NotNull
    private String customerId;

    @NotNull
    private String companyName;

    private String contactName;

    private String contactTitle;
}
