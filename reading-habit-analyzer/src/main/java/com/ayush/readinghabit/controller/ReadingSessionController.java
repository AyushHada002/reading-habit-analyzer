package com.ayush.readinghabit.controller;

import com.ayush.readinghabit.dto.ReadingSessionRequestDTO;
import com.ayush.readinghabit.dto.ReadingSessionResponseDTO;
import com.ayush.readinghabit.service.AuthenticatedUserService;
import com.ayush.readinghabit.service.ReadingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ayush.readinghabit.dto.PageResponseDTO;
import java.time.LocalDate;
import com.ayush.readinghabit.dto.TopBookDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/reading-sessions")
@Tag(
        name = "Reading Sessions",
        description = "Track reading activity and sessions"
)
@SecurityRequirement(name = "bearerAuth")
public class ReadingSessionController {

    private final ReadingSessionService readingSessionService;
    private final AuthenticatedUserService authenticatedUserService;

    public ReadingSessionController(
            ReadingSessionService readingSessionService,
            AuthenticatedUserService authenticatedUserService) {

        this.readingSessionService = readingSessionService;
        this.authenticatedUserService =
                authenticatedUserService;
    }

    // Create Reading Session
    @PostMapping
    public ResponseEntity<ReadingSessionResponseDTO> createSession(
            @Valid @RequestBody ReadingSessionRequestDTO request) {

        ReadingSessionResponseDTO createdSession =
                readingSessionService.createSession(request);

        return new ResponseEntity<>(
                createdSession,
                HttpStatus.CREATED
        );
    }

    // Get All Reading Sessions
    @GetMapping
    public ResponseEntity<List<ReadingSessionResponseDTO>> getAllSessions() {

        List<ReadingSessionResponseDTO> sessions =
                readingSessionService.getAllSessions();

        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReadingSessionResponseDTO>> searchSessions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration) {

        return ResponseEntity.ok(
                readingSessionService.searchSessions(
                        userId,
                        bookId,
                        startDate,
                        endDate,
                        minPages,
                        maxPages,
                        minDuration,
                        maxDuration
                )
        );
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<PageResponseDTO<ReadingSessionResponseDTO>>
    searchSessionsPaginated(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "readingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                readingSessionService.searchSessionsPaginated(
                        userId,
                        bookId,
                        startDate,
                        endDate,
                        minPages,
                        maxPages,
                        minDuration,
                        maxDuration,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<PageResponseDTO<ReadingSessionResponseDTO>>
    getRecentActivity(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
                readingSessionService.getRecentActivity(
                        userId,
                        page,
                        size
                )
        );
    }

    @GetMapping("/user/{userId}/top-books")
    public ResponseEntity<List<TopBookDTO>> getTopBooks(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {

        return ResponseEntity.ok(
                readingSessionService.getTopBooks(
                        userId,
                        limit
                )
        );
    }

    // Get Reading Session By ID
    @GetMapping("/{id}")
    public ResponseEntity<ReadingSessionResponseDTO> getSessionById(
            @PathVariable Long id) {

        ReadingSessionResponseDTO session =
                readingSessionService.getSessionById(id);

        return ResponseEntity.ok(session);
    }

    // Get Sessions By User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadingSessionResponseDTO>> getSessionsByUserId(
            @PathVariable Long userId) {

        List<ReadingSessionResponseDTO> sessions =
                readingSessionService.getSessionsByUserId(userId);

        return ResponseEntity.ok(sessions);
    }

    // Get Sessions By Book
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReadingSessionResponseDTO>> getSessionsByBookId(
            @PathVariable Long bookId) {

        List<ReadingSessionResponseDTO> sessions =
                readingSessionService.getSessionsByBookId(bookId);

        return ResponseEntity.ok(sessions);
    }

    // Get Sessions By User And Book
    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<List<ReadingSessionResponseDTO>>
    getSessionsByUserAndBook(
            @PathVariable Long userId,
            @PathVariable Long bookId) {

        List<ReadingSessionResponseDTO> sessions =
                readingSessionService.getSessionsByUserAndBook(
                        userId,
                        bookId
                );

        return ResponseEntity.ok(sessions);
    }

    // Update Reading Session
    @PutMapping("/{id}")
    public ResponseEntity<ReadingSessionResponseDTO> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody ReadingSessionRequestDTO request) {

        ReadingSessionResponseDTO updatedSession =
                readingSessionService.updateSession(id, request);

        return ResponseEntity.ok(updatedSession);
    }

    // Delete Reading Session
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long id) {

        readingSessionService.deleteSession(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<PageResponseDTO<ReadingSessionResponseDTO>>
    getSessionsByUserIdPaginated(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "readingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                readingSessionService.getSessionsByUserIdPaginated(
                        userId,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/book/{bookId}/paginated")
    public ResponseEntity<PageResponseDTO<ReadingSessionResponseDTO>>
    getSessionsByBookIdPaginated(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "readingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                readingSessionService.getSessionsByBookIdPaginated(
                        bookId,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

}
