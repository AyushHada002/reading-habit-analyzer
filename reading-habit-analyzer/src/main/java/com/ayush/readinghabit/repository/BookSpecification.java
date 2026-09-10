package com.ayush.readinghabit.repository;

import com.ayush.readinghabit.entity.Book;
import com.ayush.readinghabit.entity.BookStatus;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> hasUserId(
            Long userId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("user").get("id"),
                        userId
                );
    }

    public static Specification<Book> titleContains(
            String title) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("title")
                        ),
                        "%" + title.toLowerCase() + "%"
                );
    }

    public static Specification<Book> authorContains(
            String author) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("author")
                        ),
                        "%" + author.toLowerCase() + "%"
                );
    }

    public static Specification<Book> hasGenre(
            String genre) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(
                                root.get("genre")
                        ),
                        genre.toLowerCase()
                );
    }

    public static Specification<Book> hasStatus(
            BookStatus status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }
}
