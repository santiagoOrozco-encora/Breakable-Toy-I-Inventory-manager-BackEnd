package com.StoreManageBackEnd.StoreManager.presentation.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductsDTO {
    @NonNull
    private UUID id;
    @NonNull
    private String name;
    @NonNull
    private String category;
    @NonNull
    private Float unitPrice;

    private LocalDate expirationDate;
    @NonNull
    private Integer stock;


}
