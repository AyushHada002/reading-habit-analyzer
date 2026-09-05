package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.BookRequestDTO;
import com.ayush.readinghabit.dto.BookResponseDTO;
import com.ayush.readinghabit.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Create Book
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @Valid @RequestBody BookRequestDTO request) {

        BookResponseDTO createdBook =
                bookService.createBook(request);

        return new ResponseEntity<>(
                createdBook,
                HttpStatus.CREATED
        );
    }

    // Get All Books
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {

        List<BookResponseDTO> books =
                bookService.getAllBooks();

        return ResponseEntity.ok(books);
    }

    // Get Book By ID
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @PathVariable Long id) {

        BookResponseDTO book =
                bookService.getBookById(id);

        return ResponseEntity.ok(book);
    }

    // Get Books By User ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookResponseDTO>> getBooksByUserId(
            @PathVariable Long userId) {

        List<BookResponseDTO> books =
                bookService.getBooksByUserId(userId);

        return ResponseEntity.ok(books);
    }

    // Update Book
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO request) {

        BookResponseDTO updatedBook =
                bookService.updateBook(id, request);

        return ResponseEntity.ok(updatedBook);
    }

    // Delete Book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }
}
