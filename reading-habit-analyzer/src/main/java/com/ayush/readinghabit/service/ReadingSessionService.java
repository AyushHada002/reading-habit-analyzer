package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.ReadingSessionRequestDTO;
import com.ayush.readinghabit.dto.ReadingSessionResponseDTO;
import com.ayush.readinghabit.entity.Book;
import com.ayush.readinghabit.entity.ReadingSession;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.BookRepository;
import com.ayush.readinghabit.repository.ReadingSessionRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.ayush.readinghabit.exception.BusinessRuleException;
import com.ayush.readinghabit.entity.BookStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReadingSessionService {

    private final ReadingSessionRepository readingSessionRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReadingSessionService(
            ReadingSessionRepository readingSessionRepository,
            UserRepository userRepository,
            BookRepository bookRepository) {

        this.readingSessionRepository = readingSessionRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Create Reading Session
    @Transactional
    public ReadingSessionResponseDTO createSession(
            ReadingSessionRequestDTO request) {

        User user = findUser(request.getUserId());

        Book book = findBook(request.getBookId());

        validateBookOwnership(book, user);

        validateReadingDate(request.getReadingDate());

        validateBookStatus(book);

        validatePagesReadPerSession(
                request.getPagesRead(),
                book
        );

        validateTotalPagesAfterSession(
                request.getPagesRead(),
                book
        );

        ReadingSession session = new ReadingSession();

        session.setReadingDate(request.getReadingDate());
        session.setDurationMinutes(request.getDurationMinutes());
        session.setPagesRead(request.getPagesRead());
        session.setNotes(request.getNotes());
        session.setUser(user);
        session.setBook(book);

        ReadingSession savedSession =
                readingSessionRepository.save(session);

        updateBookStatus(book);

        return convertToResponseDTO(savedSession);
    }

    private void validateReadingDate(
            java.time.LocalDate readingDate) {

        if (readingDate.isAfter(java.time.LocalDate.now())) {

            throw new BusinessRuleException(
                    "Reading date cannot be in the future"
            );
        }
    }

    private void validateBookStatus(Book book) {

        if (book.getStatus() == BookStatus.COMPLETED) {

            throw new BusinessRuleException(
                    "Cannot create a reading session for a completed book"
            );
        }
    }

    private void validatePagesReadPerSession(
            Integer pagesRead,
            Book book) {

        if (pagesRead > book.getTotalPages()) {

            throw new BusinessRuleException(
                    "Pages read in a single session cannot exceed "
                            + "the book's total pages"
            );
        }
    }

    private void validateTotalPagesAfterSession(
            Integer pagesRead,
            Book book) {

        long alreadyRead =
                readingSessionRepository
                        .sumPagesReadByBookId(book.getId());

        long totalAfterSession =
                alreadyRead + pagesRead;

        if (totalAfterSession > book.getTotalPages()) {

            throw new BusinessRuleException(
                    "Total pages read cannot exceed "
                            + "the book's total pages. "
                            + "Already read: "
                            + alreadyRead
                            + ", attempting to add: "
                            + pagesRead
                            + ", total pages: "
                            + book.getTotalPages()
            );
        }
    }

    // Get All Reading Sessions
    public List<ReadingSessionResponseDTO> getAllSessions() {

        return readingSessionRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get Reading Session By ID
    public ReadingSessionResponseDTO getSessionById(Long id) {

        ReadingSession session =
                readingSessionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading session not found with id: " + id
                                )
                        );

        return convertToResponseDTO(session);
    }

    // Get Sessions By User
    public List<ReadingSessionResponseDTO> getSessionsByUserId(
            Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return readingSessionRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get Sessions By Book
    public List<ReadingSessionResponseDTO> getSessionsByBookId(
            Long bookId) {

        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException(
                    "Book not found with id: " + bookId
            );
        }

        return readingSessionRepository.findByBookId(bookId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get Sessions By User And Book
    public List<ReadingSessionResponseDTO> getSessionsByUserAndBook(
            Long userId,
            Long bookId) {

        User user = findUser(userId);

        Book book = findBook(bookId);

        validateBookOwnership(book, user);

        return readingSessionRepository
                .findByUserIdAndBookId(userId, bookId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Update Reading Session
    @Transactional
    public ReadingSessionResponseDTO updateSession(
            Long id,
            ReadingSessionRequestDTO request) {

        ReadingSession existingSession =
                readingSessionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading session not found with id: " + id
                                )
                        );

        User user = findUser(request.getUserId());

        Book book = findBook(request.getBookId());

        validateBookOwnership(book, user);

        validateReadingDate(request.getReadingDate());

        validateBookStatusForUpdate(
                book,
                existingSession
        );

        validatePagesReadPerSession(
                request.getPagesRead(),
                book
        );

        validateTotalPagesAfterUpdate(
                request.getPagesRead(),
                existingSession,
                book
        );

        existingSession.setReadingDate(
                request.getReadingDate()
        );

        existingSession.setDurationMinutes(
                request.getDurationMinutes()
        );

        existingSession.setPagesRead(
                request.getPagesRead()
        );

        existingSession.setNotes(
                request.getNotes()
        );

        existingSession.setUser(user);

        existingSession.setBook(book);

        ReadingSession updatedSession =
                readingSessionRepository.save(existingSession);

        updateBookStatus(book);

        return convertToResponseDTO(updatedSession);
    }

    private void validateBookStatusForUpdate(
            Book book,
            ReadingSession existingSession) {

        if (book.getStatus() == BookStatus.COMPLETED
                && existingSession.getBook().getId()
                .equals(book.getId())) {

            throw new BusinessRuleException(
                    "Cannot update a reading session for a completed book"
            );
        }
    }

    private void validateTotalPagesAfterUpdate(
            Integer newPagesRead,
            ReadingSession existingSession,
            Book book) {

        long totalPagesRead =
                readingSessionRepository
                        .sumPagesReadByBookId(book.getId());

        long totalWithoutCurrentSession =
                totalPagesRead
                        - existingSession.getPagesRead();

        long totalAfterUpdate =
                totalWithoutCurrentSession
                        + newPagesRead;

        if (totalAfterUpdate > book.getTotalPages()) {

            throw new BusinessRuleException(
                    "Total pages read cannot exceed "
                            + "the book's total pages. "
                            + "Pages after update: "
                            + totalAfterUpdate
                            + ", total pages: "
                            + book.getTotalPages()
            );
        }
    }

    private void updateBookStatus(Book book) {

        long totalPagesRead =
                readingSessionRepository
                        .sumPagesReadByBookId(book.getId());

        if (totalPagesRead >= book.getTotalPages()) {

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

        } else if (totalPagesRead > 0) {

            book.setStatus(BookStatus.READING);

            if (book.getStartedDate() == null) {
                book.setStartedDate(
                        java.time.LocalDate.now()
                );
            }

            book.setCompletedDate(null);

        } else {

            book.setStatus(BookStatus.TO_READ);

            book.setStartedDate(null);
            book.setCompletedDate(null);
        }

        bookRepository.save(book);
    }

    // Delete Reading Session
    @Transactional
    public void deleteSession(Long id) {

        ReadingSession existingSession =
                readingSessionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading session not found with id: " + id
                                )
                        );

        Book book = existingSession.getBook();

        readingSessionRepository.delete(existingSession);

        updateBookStatus(book);
    }

    // Find User
    private User findUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );
    }

    // Find Book
    private Book findBook(Long bookId) {

        return bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + bookId
                        )
                );
    }

    // Validate Book Ownership
    private void validateBookOwnership(
            Book book,
            User user) {

        if (!book.getUser().getId().equals(user.getId())) {

            throw new BusinessRuleException(
                    "Book does not belong to the specified user"
            );
        }
    }

    // Convert Entity → Response DTO
    private ReadingSessionResponseDTO convertToResponseDTO(
            ReadingSession session) {

        return new ReadingSessionResponseDTO(
                session.getId(),
                session.getReadingDate(),
                session.getDurationMinutes(),
                session.getPagesRead(),
                session.getNotes(),
                session.getUser().getId(),
                session.getBook().getId(),
                session.getBook().getTitle()
        );
    }



}
