package com.readstack.library.book;

import com.readstack.library.book.dto.BookCreateUpdateDto;
import com.readstack.library.book.dto.BookDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookMapperTest {
    private final BookMapper mapper = new BookMapperImpl();

    private final Book BOOK_MOCK = Book.builder()
            .id(1L)
            .title("Harry Potter and the Philosopher's Stone")
            .isbn("9780747532699")
            .publishedYear(1997)
            .build();
    final BookCreateUpdateDto BOOK_CREATE_UPDATE_DTO_MOCK = BookCreateUpdateDto.builder()
            .title(BOOK_MOCK.getTitle())
            .isbn(BOOK_MOCK.getIsbn())
            .publishedYear(BOOK_MOCK.getPublishedYear())
            .build();

    @Test
    void toDTO() {
        final BookDto bookDto = mapper.toDTO(BOOK_MOCK);
        assertThat(bookDto.getId()).isEqualTo(BOOK_MOCK.getId());
        assertThat(bookDto.getTitle()).isEqualTo(BOOK_MOCK.getTitle());
        assertThat(bookDto.getIsbn()).isEqualTo(BOOK_MOCK.getIsbn());
        assertThat(bookDto.getPublishedYear()).isEqualTo(BOOK_MOCK.getPublishedYear());
    }

    @Test
    void toBook() {
        final Book book = mapper.toBook(BOOK_CREATE_UPDATE_DTO_MOCK);

        assertThat(book.getId()).isNull();
        assertThat(book.getTitle()).isEqualTo(BOOK_CREATE_UPDATE_DTO_MOCK.getTitle());
        assertThat(book.getIsbn()).isEqualTo(BOOK_CREATE_UPDATE_DTO_MOCK.getIsbn());
        assertThat(book.getPublishedYear()).isEqualTo(BOOK_CREATE_UPDATE_DTO_MOCK.getPublishedYear());
    }

    @Test
    void updateBookFromDto() {
        mapper.updateBookFromDto(BOOK_CREATE_UPDATE_DTO_MOCK, BOOK_MOCK);

        assertThat(BOOK_MOCK.getTitle()).isEqualTo(BOOK_CREATE_UPDATE_DTO_MOCK.getTitle());
        assertThat(BOOK_MOCK.getIsbn()).isEqualTo(BOOK_CREATE_UPDATE_DTO_MOCK.getIsbn());
        assertThat(BOOK_MOCK.getPublishedYear()).isEqualTo(BOOK_CREATE_UPDATE_DTO_MOCK.getPublishedYear());
    }
}
