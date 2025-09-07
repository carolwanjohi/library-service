package com.readstack.library.book;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class BookSpecs {
    public static Specification<Book> titleContains(String bookQuery) {
        return (root, query, criteriaBuilder) -> (bookQuery == null || bookQuery.isBlank()) ? null
                : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                "%" + bookQuery.toLowerCase() + "%"
        );
    }

    public static Specification<Book> yearGTE(Integer year) {
        return (root, query, criteriaBuilder) -> (year == null) ? null
                : criteriaBuilder.greaterThanOrEqualTo(root.get("publishedYear"), year);
    }

    public static Specification<Book> yearLTE(Integer year) {
        return  (root, query, criteriaBuilder) -> (year == null) ? null
                : criteriaBuilder.lessThanOrEqualTo(root.get("publishedYear"), year);
    }
}
