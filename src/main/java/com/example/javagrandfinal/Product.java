package com.example.javagrandfinal;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private int id;
    private String name;
    private int price;
    private int quantity;
}
