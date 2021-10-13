package ru.makar.makareshfamilybot.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "web")
public class RequestUrlSet {
    private String addProduct;
    private String productExisted;
    private String productsShow;
    private String productsInBasket;
    private String updateQuantity;
    private String updateComment;
    private String emptyBasket;
}
