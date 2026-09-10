package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.ReadingSession;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReadingSessionSpecification {

    private ReadingSessionSpecification() {
    }

    public static Specification<ReadingSession> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("user").get("id"),
                        userId
                );
    }

    public static Specification<ReadingSession> hasBookId(Long bookId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("book").get("id"),
                        bookId
                );
    }

    public static Specification<ReadingSession> readingDateGreaterThanOrEqual(
            LocalDate startDate) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("readingDate"),
                        startDate
                );
    }

    public static Specification<ReadingSession> readingDateLessThanOrEqual(
            LocalDate endDate) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("readingDate"),
                        endDate
                );
    }

    public static Specification<ReadingSession> pagesGreaterThanOrEqual(
            Integer minPages) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("pagesRead"),
                        minPages
                );
    }

    public static Specification<ReadingSession> pagesLessThanOrEqual(
            Integer maxPages) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("pagesRead"),
                        maxPages
                );
    }

    public static Specification<ReadingSession> durationGreaterThanOrEqual(
            Integer minDuration) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("durationMinutes"),
                        minDuration
                );
    }

    public static Specification<ReadingSession> durationLessThanOrEqual(
            Integer maxDuration) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("durationMinutes"),
                        maxDuration
                );
    }
}
