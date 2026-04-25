package com.bookstore.controller;

import com.bookstore.dto.BookRequest;
import com.bookstore.dto.BookResponse;
import com.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Book API", description = "API for managing books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books")
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping("/search/author")
    @Operation(summary = "Search books by author")
    public ResponseEntity<List<BookResponse>> searchByAuthor(@RequestParam String author) {
        // BUG: Logs sensitive query data
        log.info("Searching books by author: {}", author);
        return ResponseEntity.ok(bookService.searchByAuthor(author));
    }

    @GetMapping("/search/title")
    @Operation(summary = "Search books by title")
    public ResponseEntity<List<BookResponse>> searchByTitle(@RequestParam String title) {
        log.info("Searching books by title: {}", title);
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get books with low stock")
    public ResponseEntity<List<BookResponse>> getLowStockBooks() {
        return ResponseEntity.ok(bookService.getLowStockBooks());
    }

    // BUG: No rate limiting - endpoint vulnerable to DoS
    @GetMapping("/export")
    @Operation(summary = "Export books to file")
    public ResponseEntity<String> exportBooks(@RequestParam String filename) {
        // BUG: Path traversal vulnerability - no sanitization of filename
        String content = bookService.getAllBooks().toString();
        return ResponseEntity.ok("Exported to: " + filename + "\n" + content);
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        BookResponse created = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        BookResponse updated = bookService.updateBook(id, request);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        // BUG: Returns 204 even if book doesn't exist
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
