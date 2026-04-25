package com.bookstore.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    // BUG: @Positive is commented out - allows negative prices
    // @Positive(message = "Price must be positive")
    @NotNull(message = "Price is required")
    private Double price;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @Min(value = 0)
    private Integer stock = 0;

    // BUG: No sanitization - XSS vulnerability
    private String description;
}
