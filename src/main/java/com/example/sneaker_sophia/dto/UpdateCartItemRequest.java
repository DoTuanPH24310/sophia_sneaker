package com.example.sneaker_sophia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemRequest {
    private Long cartItemId;
    private Integer newQuantity;
}
