package com.bookstore.service;

import com.bookstore.dto.BookRequest;
import com.bookstore.dto.BookResponse;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_ReturnsListOfBooks() {
        Book book1 = new Book(1L, "Title1", "Author1", 10.0, "1234567890", 5, "Desc1");
        Book book2 = new Book(2L, "Title2", "Author2", 20.0, "0987654321", 3, "Desc2");
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<BookResponse> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_ExistingId_ReturnsBook() {
        Book book = new Book(1L, "Test Book", "Author", 15.0, "1234567890", 10, "Description");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void getBookById_NonExistingId_ReturnsNull() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        BookResponse result = bookService.getBookById(99L);

        assertNull(result);
    }

    @Test
    void createBook_ValidRequest_ReturnsCreatedBook() {
        BookRequest request = new BookRequest();
        request.setTitle("New Book");
        request.setAuthor("New Author");
        request.setPrice(25.0);
        request.setIsbn("1112222333");
        request.setStock(5);

        Book savedBook = new Book(1L, "New Book", "New Author", 25.0, "1112222333", 5, null);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponse result = bookService.createBook(request);

        assertNotNull(result);
        assertEquals("New Book", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_ExistingId_ReturnsUpdatedBook() {
        Book existingBook = new Book(1L, "Old Title", "Author", 10.0, "1234567890", 5, null);
        BookRequest updateRequest = new BookRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Author");
        updateRequest.setPrice(15.0);
        updateRequest.setIsbn("1234567890");
        updateRequest.setStock(10);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        BookResponse result = bookService.updateBook(1L, updateRequest);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook_ExistingId_DeletesSuccessfully() {
        doNothing().when(bookRepository).deleteById(1L);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
