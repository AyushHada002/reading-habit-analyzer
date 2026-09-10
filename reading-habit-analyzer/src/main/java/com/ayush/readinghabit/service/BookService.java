package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.BookRequestDTO;
import com.ayush.readinghabit.dto.BookResponseDTO;
import com.ayush.readinghabit.entity.Book;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.BookRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.ayush.readinghabit.dto.BookProgressDTO;
import com.ayush.readinghabit.repository.ReadingSessionRepository;
import com.ayush.readinghabit.entity.BookStatus;
import com.ayush.readinghabit.exception.BusinessRuleException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReadingSessionRepository readingSessionRepository;

    public BookService(
            BookRepository bookRepository,
            UserRepository userRepository,
            ReadingSessionRepository readingSessionRepository) {

        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.readingSessionRepository = readingSessionRepository;
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
        book.setStatus(BookStatus.TO_READ);
        book.setStartedDate(null);
        book.setCompletedDate(null);
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

        validateTotalPagesUpdate(
                existingBook,
                request.getTotalPages()
        );

        existingBook.setStatus(request.getStatus());
        existingBook.setStartedDate(request.getStartedDate());
        existingBook.setCompletedDate(request.getCompletedDate());

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

    public BookProgressDTO getBookProgress(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );

        long pagesRead = readingSessionRepository
                .sumPagesReadByBookId(bookId);

        long remainingPages =
                Math.max(book.getTotalPages() - pagesRead, 0);

        double progressPercentage =
                ((double) pagesRead / book.getTotalPages()) * 100;

        progressPercentage =
                Math.min(progressPercentage, 100.0);

        return new BookProgressDTO(
                book.getId(),
                book.getTitle(),
                book.getTotalPages(),
                pagesRead,
                remainingPages,
                roundToTwoDecimals(progressPercentage),
                book.getStatus()
        );
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private void validateBookDatesAreNotFuture(
            BookRequestDTO request) {

        java.time.LocalDate today =
                java.time.LocalDate.now();

        if (request.getStartedDate() != null
                && request.getStartedDate().isAfter(today)) {

            throw new BusinessRuleException(
                    "Started date cannot be in the future"
            );
        }

        if (request.getCompletedDate() != null
                && request.getCompletedDate().isAfter(today)) {

            throw new BusinessRuleException(
                    "Completed date cannot be in the future"
            );
        }
    }

    private void synchronizeBookStatus(Book book) {

        long pagesRead =
                readingSessionRepository
                        .sumPagesReadByBookId(book.getId());

        if (pagesRead <= 0) {

            book.setStatus(BookStatus.TO_READ);
            book.setStartedDate(null);
            book.setCompletedDate(null);

        } else if (pagesRead < book.getTotalPages()) {

            book.setStatus(BookStatus.READING);

            if (book.getStartedDate() == null) {
                book.setStartedDate(
                        java.time.LocalDate.now()
                );
            }

            book.setCompletedDate(null);

        } else {

            book.setStatus(BookStatus.COMPLETED);

            if (book.getStartedDate() == null) {
                book.setStartedDate(
                        java.time.LocalDate.now()
                );
            }

            if (book.getCompletedDate() == null) {
                book.setCompletedDate(
                        java.time.LocalDate.now()
                );
            }
        }

        bookRepository.save(book);
    }

    private void validateTotalPagesUpdate(
            Book book,
            Integer newTotalPages) {

        long pagesRead =
                readingSessionRepository
                        .sumPagesReadByBookId(book.getId());

        if (newTotalPages < pagesRead) {

            throw new BusinessRuleException(
                    "Total pages cannot be less than "
                            + "pages already read. "
                            + "Pages read: "
                            + pagesRead
                            + ", new total pages: "
                            + newTotalPages
            );
        }
    }
}
