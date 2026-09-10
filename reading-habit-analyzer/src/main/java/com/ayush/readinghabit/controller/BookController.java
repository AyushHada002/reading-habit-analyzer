package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.BookRequestDTO;
import com.ayush.readinghabit.dto.BookResponseDTO;
import com.ayush.readinghabit.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ayush.readinghabit.dto.BookProgressDTO;
import com.ayush.readinghabit.entity.BookStatus;
import com.ayush.readinghabit.dto.PageResponseDTO;

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

    @GetMapping("/{id}/progress")
    public ResponseEntity<BookProgressDTO> getBookProgress(
            @PathVariable Long id) {

        BookProgressDTO progress =
                bookService.getBookProgress(id);

        return ResponseEntity.ok(progress);
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
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BookStatus status) {

        List<BookResponseDTO> books =
                bookService.searchBooks(
                        userId,
                        title,
                        author,
                        genre,
                        status
                );

        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<PageResponseDTO<BookResponseDTO>> searchBooksPaginated(

            @RequestParam(required = false) Long userId,

            @RequestParam(required = false) String title,

            @RequestParam(required = false) String author,

            @RequestParam(required = false) String genre,

            @RequestParam(required = false) BookStatus status,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        PageResponseDTO<BookResponseDTO> result =
                bookService.searchBooksPaginated(
                        userId,
                        title,
                        author,
                        genre,
                        status,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(result);
    }
}
