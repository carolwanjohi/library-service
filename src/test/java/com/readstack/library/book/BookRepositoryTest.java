package com.readstack.library.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BookRepository repository;

    final Book BOOK_MOCK = Book.builder()
            .title("Pragmatic Programmer")
            .isbn("978-0201616224")
            .publishedYear(1999)
            .build();


    @Test
    public void save_and_findByIsbn() {
        entityManager.persist(BOOK_MOCK);

        assertThat(repository.findByIsbn(BOOK_MOCK.getIsbn())).isPresent();
    }

    @Test
    public void unique_isbn_constraint() {
        final Book duplicateIsbnBook = Book.builder()
                .title("Another Book")
                .isbn(BOOK_MOCK.getIsbn())
                .publishedYear(2020)
                .build();

        entityManager.persist(BOOK_MOCK);

        assertThatThrownBy(() -> entityManager.persistAndFlush(duplicateIsbnBook))
                .isInstanceOf(Exception.class);
    }
}
