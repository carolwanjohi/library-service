package com.readstack.library.book;

import com.readstack.library.common.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository repository;
    @InjectMocks
    private BookService service;

    private final Book BOOK_MOCK = Book.builder()
            .title("The Great Gatsby")
            .isbn("9780743273565")
            .publishedYear(1925)
            .build();

    @Test
    public void createsBook() {
        Book input = BOOK_MOCK;

        when(repository.save(input)).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(1L);
            return book;
        });

        Book created = service.createBook(input);

        assertThat(created.getId()).isEqualTo(1L);
        verify(repository).save(input);
    }

    @Test
    public void getOrThrows() {
        BOOK_MOCK.setId(1L);
        when(repository.findById(BOOK_MOCK.getId())).thenReturn(Optional.of(BOOK_MOCK));
        when(repository.findById(9L)).thenReturn(Optional.empty());

        assertThat(service.get(BOOK_MOCK.getId()).getId()).isEqualTo(BOOK_MOCK.getId());
        assertThatThrownBy(() -> service.get(9L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void updates() {
        when(repository.findById(BOOK_MOCK.getId())).thenReturn(Optional.of(BOOK_MOCK));
        when(repository.save(BOOK_MOCK)).thenAnswer(invocation -> invocation.getArgument(0));

        Book updated = service.update(BOOK_MOCK.getId(), Book.builder().title("The Great Gatsby - Updated").isbn(BOOK_MOCK.getIsbn()).publishedYear(1950).build());

        assertThat(updated.getTitle()).isEqualTo("The Great Gatsby - Updated");
        assertThat(updated.getPublishedYear()).isEqualTo(1950);
    }

    @Test
    public void deletesOrThrows() {
        when(repository.existsById(BOOK_MOCK.getId())).thenReturn(true);
        when(repository.existsById(9L)).thenReturn(false);

        service.delete(BOOK_MOCK.getId());

        verify(repository).deleteById(BOOK_MOCK.getId());
        assertThatThrownBy(() -> service.delete(9L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void lists() {
        when(repository.findAll()).thenReturn(List.of(
                BOOK_MOCK,
                Book.builder().id(2L).title("1984").isbn("9780451524935").publishedYear(1949).build()
        ));
        List<Book> books = service.list();
        assertThat(books).hasSize(2);
    }
}
