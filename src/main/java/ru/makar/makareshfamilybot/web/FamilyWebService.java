package ru.makar.makareshfamilybot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.makar.makareshfamilybot.model.BasketProduct;
import ru.makar.makareshfamilybot.model.CommentProduct;
import ru.makar.makareshfamilybot.model.Product;
import ru.makar.makareshfamilybot.model.QuantityProduct;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyWebService {

    private final RestTemplate restTemplate;
    private final RequestUrlSet requestUrlSet;

    public List<String> listExistedProducts() {
        ResponseEntity<List<String>> listResponseEntity = restTemplate.exchange(
                requestUrlSet.getProductExisted(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return (listResponseEntity.getStatusCode().is2xxSuccessful() && listResponseEntity.hasBody())
                ? listResponseEntity.getBody()
                : List.of();
    }

    public List<String> getProductsFromBasket() {
        ResponseEntity<List<String>> listResponseEntity = restTemplate.exchange(
                requestUrlSet.getProductsInBasket(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return (listResponseEntity.getStatusCode().is2xxSuccessful() && listResponseEntity.hasBody())
                ? listResponseEntity.getBody()
                : List.of();
    }

    public ResponseEntity<Void> addProductToBasket(String productName) {
        return restTemplate.postForEntity(
                requestUrlSet.getAddProduct(),
                new Product(UUID.nameUUIDFromBytes(productName.getBytes()), productName),
                Void.class
        );
    }

    public List<BasketProduct> getBasketList() {
        ResponseEntity<List<BasketProduct>> productsList = restTemplate.exchange(
                requestUrlSet.getProductsShow(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return (productsList.getStatusCode().is2xxSuccessful() && productsList.hasBody())
                ? productsList.getBody()
                : List.of();
    }

    public String updateProductQuantity(String name, Double quantity, String sign) {
        Integer integer = restTemplate.postForObject(
                requestUrlSet.getUpdateQuantity(),
                new QuantityProduct(UUID.nameUUIDFromBytes(name.getBytes()), name, quantity, sign),
                Integer.class
        );
        if (integer != null) {
            return (integer > 0)
                    ? String.format("Количество %s изменилось на %s", name, quantity)
                    : "Количество не изменилось...";
        } else {
            return "Something went wrong...";
        }
    }

    public String updateProductComment(String name, String comment) {
        Integer integer = restTemplate.postForObject(
                requestUrlSet.getUpdateComment(),
                new CommentProduct(UUID.nameUUIDFromBytes(name.getBytes()), name, comment),
                Integer.class
        );
        if (integer != null) {
            return (integer > 0)
                    ? String.format("Комментарий к продукту %s был изменен", name)
                    : "коменнтарий не изменился...";
        }else {
            return "Something went wrong...";
        }
    }

    public void emptyBasket() {
        restTemplate.exchange(requestUrlSet.getEmptyBasket(), HttpMethod.GET, null, Void.class);
    }
}
