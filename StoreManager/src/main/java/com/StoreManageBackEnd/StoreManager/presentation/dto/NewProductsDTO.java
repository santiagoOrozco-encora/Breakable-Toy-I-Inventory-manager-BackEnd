package com.StoreManageBackEnd.StoreManager.presentation.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class NewProductsDTO {
    @NonNull
    private String name;
    @NonNull
    private String category;
    @NonNull
    private Float unitPrice;
    @NonNull
    private Integer stock;

    private LocalDate expirationDate;

}
