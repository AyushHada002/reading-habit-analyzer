package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.ReadingSessionRequestDTO;
import com.ayush.readinghabit.dto.ReadingSessionResponseDTO;
import com.ayush.readinghabit.entity.Book;
import com.ayush.readinghabit.entity.BookStatus;
import com.ayush.readinghabit.entity.ReadingSession;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.BusinessRuleException;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.BookRepository;
import com.ayush.readinghabit.repository.ReadingSessionRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingSessionServiceTest {

    @Mock
    private ReadingSessionRepository readingSessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private ReadingSessionService readingSessionService;

    private User user;
    private Book book;
    private ReadingSession session;
    private ReadingSessionRequestDTO request;

    @BeforeEach
    void setUp() {

        user = new User(
                "Ayush",
                "ayush@example.com",
                "encodedPassword"
        );

        user.setId(1L);

        book = new Book();

        book.setId(10L);
        book.setTitle("Atomic Habits");
        book.setAuthor("James Clear");
        book.setGenre("Self Help");
        book.setTotalPages(320);
        book.setStatus(BookStatus.TO_READ);
        book.setUser(user);

        session = new ReadingSession();

        session.setId(100L);
        session.setReadingDate(
                LocalDate.of(2026, 9, 11)
        );
        session.setDurationMinutes(45);
        session.setPagesRead(20);
        session.setNotes("Evening reading");
        session.setUser(user);
        session.setBook(book);

        request = new ReadingSessionRequestDTO();

        request.setReadingDate(
                LocalDate.of(2026, 9, 11)
        );
        request.setDurationMinutes(45);
        request.setPagesRead(20);
        request.setNotes("Evening reading");
        request.setUserId(1L);
        request.setBookId(10L);
    }

    @Test
    void createSession_shouldCreateSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        when(readingSessionRepository
                .sumPagesReadByBookId(10L))
                .thenReturn(0L);

        when(readingSessionRepository.save(
                any(ReadingSession.class)
        )).thenReturn(session);

        ReadingSessionResponseDTO response =
                readingSessionService.createSession(request);

        assertNotNull(response);

        assertEquals(
                100L,
                response.getId()
        );

        assertEquals(
                20,
                response.getPagesRead()
        );

        assertEquals(
                45,
                response.getDurationMinutes()
        );

        verify(userRepository)
                .findById(1L);

        verify(bookRepository)
                .findById(10L);

        verify(readingSessionRepository)
                .save(any(ReadingSession.class));
    }

    @Test
    void createSession_shouldRejectFutureReadingDate() {

        request.setReadingDate(
                LocalDate.now().plusDays(1)
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        assertThrows(
                BusinessRuleException.class,
                () -> readingSessionService
                        .createSession(request)
        );

        verify(readingSessionRepository, never())
                .save(any(ReadingSession.class));
    }

    @Test
    void createSession_shouldRejectPagesGreaterThanBookTotal() {

        request.setPagesRead(400);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        assertThrows(
                BusinessRuleException.class,
                () -> readingSessionService
                        .createSession(request)
        );

        verify(readingSessionRepository, never())
                .save(any(ReadingSession.class));
    }

    @Test
    void createSession_shouldRejectWhenCumulativePagesExceedTotal() {

        request.setPagesRead(20);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        when(readingSessionRepository
                .sumPagesReadByBookId(10L))
                .thenReturn(310L);

        assertThrows(
                BusinessRuleException.class,
                () -> readingSessionService
                        .createSession(request)
        );

        verify(readingSessionRepository, never())
                .save(any(ReadingSession.class));
    }

    @Test
    void createSession_shouldRejectCompletedBook() {

        book.setStatus(BookStatus.COMPLETED);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        assertThrows(
                BusinessRuleException.class,
                () -> readingSessionService
                        .createSession(request)
        );

        verify(readingSessionRepository, never())
                .save(any(ReadingSession.class));
    }

    @Test
    void createSession_shouldRejectUnknownUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> readingSessionService
                        .createSession(request)
        );

        verify(bookRepository, never())
                .findById(anyLong());

        verify(readingSessionRepository, never())
                .save(any(ReadingSession.class));
    }

    @Test
    void createSession_shouldRejectUnknownBook() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> readingSessionService
                        .createSession(request)
        );

        verify(readingSessionRepository, never())
                .save(any(ReadingSession.class));
    }

    @Test
    void createSession_shouldCompleteBookWhenAllPagesAreRead() {

        request.setPagesRead(20);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        when(readingSessionRepository
                .sumPagesReadByBookId(10L))
                .thenReturn(300L)
                .thenReturn(320L);

        when(readingSessionRepository.save(
                any(ReadingSession.class)
        )).thenReturn(session);

        when(bookRepository.save(
                any(Book.class)
        )).thenReturn(book);

        readingSessionService.createSession(request);

        assertEquals(
                BookStatus.COMPLETED,
                book.getStatus()
        );

        assertNotNull(
                book.getCompletedDate()
        );
    }

    
}
