package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.BookRequestDTO;
import com.ayush.readinghabit.dto.BookResponseDTO;
import com.ayush.readinghabit.entity.Book;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.BookRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService(
            BookRepository bookRepository,
            UserRepository userRepository) {

        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // Create Book
    public BookResponseDTO createBook(BookRequestDTO request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.getUserId()
                        )
                );

        Book book = new Book();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setTotalPages(request.getTotalPages());
        book.setStatus(request.getStatus());
        book.setStartedDate(request.getStartedDate());
        book.setCompletedDate(request.getCompletedDate());
        book.setUser(user);

        Book savedBook = bookRepository.save(book);

        return convertToResponseDTO(savedBook);
    }

    // Get All Books
    public List<BookResponseDTO> getAllBooks() {

        return bookRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get Book By ID
    public BookResponseDTO getBookById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        )
                );

        return convertToResponseDTO(book);
    }

    // Get Books By User ID
    public List<BookResponseDTO> getBooksByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return bookRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Update Book
    public BookResponseDTO updateBook(
            Long id,
            BookRequestDTO request) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        )
                );

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.getUserId()
                        )
                );

        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setGenre(request.getGenre());
        existingBook.setTotalPages(request.getTotalPages());
        existingBook.setStatus(request.getStatus());
        existingBook.setStartedDate(request.getStartedDate());
        existingBook.setCompletedDate(request.getCompletedDate());
        existingBook.setUser(user);

        Book updatedBook = bookRepository.save(existingBook);

        return convertToResponseDTO(updatedBook);
    }

    // Delete Book
    public void deleteBook(Long id) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + id
                        )
                );

        bookRepository.delete(existingBook);
    }

    // Convert Entity → Response DTO
    private BookResponseDTO convertToResponseDTO(Book book) {

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getTotalPages(),
                book.getStatus(),
                book.getStartedDate(),
                book.getCompletedDate(),
                book.getUser().getId()
        );
    }
}
