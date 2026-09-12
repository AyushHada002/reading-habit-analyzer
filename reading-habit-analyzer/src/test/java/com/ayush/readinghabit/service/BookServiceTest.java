package com.ayush.readinghabit.service;

import com.ayush.readinghabit.dto.BookRequestDTO;
import com.ayush.readinghabit.dto.BookResponseDTO;
import com.ayush.readinghabit.dto.BookProgressDTO;
import com.ayush.readinghabit.entity.Book;
import com.ayush.readinghabit.entity.BookStatus;
import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.BusinessRuleException;
import com.ayush.readinghabit.repository.BookRepository;
import com.ayush.readinghabit.repository.ReadingSessionRepository;
import com.ayush.readinghabit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReadingSessionRepository readingSessionRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private BookService bookService;

    private User user;
    private Book book;
    private BookRequestDTO request;

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

        request = new BookRequestDTO();

        request.setTitle("Atomic Habits");
        request.setAuthor("James Clear");
        request.setGenre("Self Help");
        request.setTotalPages(320);
        request.setUserId(1L);
    }

    @Test
    void createBook_shouldCreateBookSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepository.save(any(Book.class)))
                .thenReturn(book);

        BookResponseDTO response =
                bookService.createBook(request);

        assertNotNull(response);

        assertEquals(
                10L,
                response.getId()
        );

        assertEquals(
                "Atomic Habits",
                response.getTitle()
        );

        assertEquals(
                "James Clear",
                response.getAuthor()
        );

        assertEquals(
                "Self Help",
                response.getGenre()
        );

        assertEquals(
                320,
                response.getTotalPages()
        );

        assertEquals(
                BookStatus.TO_READ,
                response.getStatus()
        );

        verify(userRepository)
                .findById(1L);

        verify(bookRepository)
                .save(any(Book.class));
    }

    @Test
    void createBook_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.createBook(request)
        );

        verify(bookRepository, never())
                .save(any(Book.class));
    }

    @Test
    void getBookById_shouldReturnBookForOwner() {

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        doNothing().when(currentUserService)
                .validateUserAccess(1L, 1L);

        BookResponseDTO response =
                bookService.getBookById(
                        10L,
                        1L
                );

        assertNotNull(response);

        assertEquals(
                10L,
                response.getId()
        );

        assertEquals(
                "Atomic Habits",
                response.getTitle()
        );

        verify(bookRepository)
                .findById(10L);

        verify(currentUserService)
                .validateUserAccess(1L, 1L);
    }

    @Test
    void getBookById_shouldThrowExceptionWhenBookNotFound() {

        when(bookRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.getBookById(
                        999L,
                        1L
                )
        );

        verify(bookRepository)
                .findById(999L);

        verify(currentUserService, never())
                .validateUserAccess(anyLong(), anyLong());
    }

    @Test
    void getBookById_shouldRejectAccessForAnotherUser() {

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        doThrow(new AccessDeniedException(
                "You do not have permission to access this resource"
        ))
                .when(currentUserService)
                .validateUserAccess(2L, 1L);

        assertThrows(
                AccessDeniedException.class,
                () -> bookService.getBookById(10L, 2L)
        );

        verify(currentUserService)
                .validateUserAccess(2L, 1L);
    }

    @Test
    void getBookProgress_shouldCalculateProgressCorrectly() {

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        when(readingSessionRepository
                .sumPagesReadByBookId(10L))
                .thenReturn(80L);

        BookProgressDTO response =
                bookService.getBookProgress(
                        10L,
                        1L
                );

        assertNotNull(response);

        assertEquals(
                10L,
                response.getBookId()
        );

        assertEquals(
                320,
                response.getTotalPages()
        );

        assertEquals(
                80L,
                response.getPagesRead()
        );

        assertEquals(
                240L,
                response.getRemainingPages()
        );

        assertEquals(
                25.0,
                response.getProgressPercentage()
        );
    }

    @Test
    void getBookProgress_shouldReturnZeroForNewBook() {

        when(bookRepository.findById(10L))
                .thenReturn(Optional.of(book));

        when(readingSessionRepository
                .sumPagesReadByBookId(10L))
                .thenReturn(0L);

        BookProgressDTO response =
                bookService.getBookProgress(
                        10L,
                        1L
                );

        assertNotNull(response);

        assertEquals(
                10L,
                response.getBookId()
        );

        assertEquals(
                320,
                response.getTotalPages()
        );

        assertEquals(
                0L,
                response.getPagesRead()
        );

        assertEquals(
                320L,
                response.getRemainingPages()
        );

        assertEquals(
                0.0,
                response.getProgressPercentage()
        );
    }

    @Test
    void searchBooksPaginated_shouldRejectNegativePage() {

        assertThrows(
                BusinessRuleException.class,
                () -> bookService.searchBooksPaginated(
                        1L,
                        null,
                        null,
                        null,
                        null,
                        -1,
                        10,
                        "title",
                        "asc"
                )
        );
    }

    @Test
    void searchBooksPaginated_shouldRejectInvalidSize() {

        assertThrows(
                BusinessRuleException.class,
                () -> bookService.searchBooksPaginated(
                        1L,
                        null,
                        null,
                        null,
                        null,
                        0,
                        101,
                        "title",
                        "asc"
                )
        );
    }

    @Test
    void searchBooksPaginated_shouldRejectInvalidSortField() {

        assertThrows(
                BusinessRuleException.class,
                () -> bookService.searchBooksPaginated(
                        1L,
                        null,
                        null,
                        null,
                        null,
                        0,
                        10,
                        "password",
                        "asc"
                )
        );
    }
}
