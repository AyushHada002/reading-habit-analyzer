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
import com.ayush.readinghabit.repository.BookSpecification;
import org.springframework.data.jpa.domain.Specification;
import com.ayush.readinghabit.dto.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    public List<BookResponseDTO> searchBooks(
            Long userId,
            String title,
            String author,
            String genre,
            BookStatus status) {

        Specification<Book> specification =
                Specification.unrestricted();

        if (userId != null) {

            if (!userRepository.existsById(userId)) {
                throw new ResourceNotFoundException(
                        "User not found with id: " + userId
                );
            }

            specification =
                    specification.and(
                            BookSpecification.hasUserId(userId)
                    );
        }

        if (title != null && !title.isBlank()) {

            specification =
                    specification.and(
                            BookSpecification.titleContains(title)
                    );
        }

        if (author != null && !author.isBlank()) {

            specification =
                    specification.and(
                            BookSpecification.authorContains(author)
                    );
        }

        if (genre != null && !genre.isBlank()) {

            specification =
                    specification.and(
                            BookSpecification.hasGenre(genre)
                    );
        }

        if (status != null) {

            specification =
                    specification.and(
                            BookSpecification.hasStatus(status)
                    );
        }

        return bookRepository
                .findAll(specification)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public PageResponseDTO<BookResponseDTO> searchBooksPaginated(
            Long userId,
            String title,
            String author,
            String genre,
            BookStatus status,
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new BusinessRuleException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new BusinessRuleException(
                    "Page size must be between 1 and 100"
            );
        }

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }

        List<String> allowedSortFields = List.of(
                "id",
                "title",
                "author",
                "genre",
                "totalPages",
                "status",
                "startedDate",
                "completedDate"
        );

        if (!allowedSortFields.contains(sortBy)) {

            throw new BusinessRuleException(
                    "Invalid sort field: " + sortBy
            );
        }

        Sort.Direction sortDirection;

        try {
            sortDirection =
                    Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {

            throw new BusinessRuleException(
                    "Sort direction must be 'asc' or 'desc'"
            );
        }

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sortBy)
                );

        Specification<Book> specification =
                Specification.unrestricted();

        if (userId != null) {

            if (!userRepository.existsById(userId)) {
                throw new ResourceNotFoundException(
                        "User not found with id: " + userId
                );
            }

            specification =
                    specification.and(
                            BookSpecification.hasUserId(userId)
                    );
        }

        if (title != null && !title.isBlank()) {

            specification =
                    specification.and(
                            BookSpecification.titleContains(title)
                    );
        }

        if (author != null && !author.isBlank()) {

            specification =
                    specification.and(
                            BookSpecification.authorContains(author)
                    );
        }

        if (genre != null && !genre.isBlank()) {

            specification =
                    specification.and(
                            BookSpecification.hasGenre(genre)
                    );
        }

        if (status != null) {

            specification =
                    specification.and(
                            BookSpecification.hasStatus(status)
                    );
        }

        Page<Book> bookPage =
                bookRepository.findAll(
                        specification,
                        pageable
                );

        List<BookResponseDTO> content =
                bookPage.getContent()
                        .stream()
                        .map(this::convertToResponseDTO)
                        .toList();

        return new PageResponseDTO<>(
                content,
                bookPage.getNumber(),
                bookPage.getSize(),
                bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.isFirst(),
                bookPage.isLast()
        );
    }
}
