package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.BookRequestDTO;
import com.ayush.readinghabit.dto.BookResponseDTO;
import com.ayush.readinghabit.dto.BookProgressDTO;
import com.ayush.readinghabit.dto.PageResponseDTO;
import com.ayush.readinghabit.entity.BookStatus;
import com.ayush.readinghabit.service.AuthenticatedUserService;
import com.ayush.readinghabit.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/books")
@Tag(
        name = "Books",
        description = "Book management and progress APIs"
)
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;
    private final AuthenticatedUserService authenticatedUserService;

    public BookController(
            BookService bookService,
            AuthenticatedUserService authenticatedUserService) {

        this.bookService = bookService;
        this.authenticatedUserService =
                authenticatedUserService;
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @Valid @RequestBody BookRequestDTO request) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        request.setUserId(currentUserId);

        return new ResponseEntity<>(
                bookService.createBook(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                bookService.getBooksByUserId(currentUserId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BookStatus status) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                bookService.searchBooks(
                        currentUserId,
                        title,
                        author,
                        genre,
                        status
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @PathVariable Long id) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                bookService.getBookById(
                        id,
                        currentUserId
                )
        );
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<BookProgressDTO> getBookProgress(
            @PathVariable Long id) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        return ResponseEntity.ok(
                bookService.getBookProgress(
                        id,
                        currentUserId
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO request) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        request.setUserId(currentUserId);

        return ResponseEntity.ok(
                bookService.updateBook(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        bookService.deleteBook(
                id,
                currentUserId
        );

        return ResponseEntity.noContent().build();
    }
}
