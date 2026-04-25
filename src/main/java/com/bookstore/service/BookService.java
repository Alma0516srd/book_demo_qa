package com.bookstore.service;

import com.bookstore.dto.BookRequest;
import com.bookstore.dto.BookResponse;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public List<BookResponse> searchByAuthor(String author) {
        return bookRepository.findByAuthor(author).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookResponse> getLowStockBooks() {
        return bookRepository.findLowStockBooks().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        Book book = mapToEntity(request);
        Book saved = bookRepository.save(book);
        return mapToResponse(saved);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return null;
        }
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setIsbn(request.getIsbn());
        book.setStock(request.getStock());
        book.setDescription(request.getDescription());
        return mapToResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getIsbn(),
                book.getStock(),
                // BUG: No XSS sanitization - JavaScript in description will be executed
                book.getDescription()
        );
    }

    private Book mapToEntity(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setIsbn(request.getIsbn());
        book.setStock(request.getStock());
        // BUG: No XSS sanitization - malicious scripts saved to DB
        book.setDescription(request.getDescription());
        return book;
    }
}
