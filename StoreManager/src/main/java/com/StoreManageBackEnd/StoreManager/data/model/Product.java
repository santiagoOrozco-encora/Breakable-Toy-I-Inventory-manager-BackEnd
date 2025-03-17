package com.StoreManageBackEnd.StoreManager.data.model;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
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

    @NonNull
    private LocalDate creationDate;
    @NonNull
    private LocalDate updateDate;

}
