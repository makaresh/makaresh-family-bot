package ru.makar.makareshfamilybot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketProduct {
    private UUID productId;
    private String productName;
    private Double quantity;
    private String comment;
}
