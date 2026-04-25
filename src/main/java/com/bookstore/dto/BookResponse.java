package com.bookstore.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private Double price;
    private String isbn;
    private Integer stock;
    private String description;
}
