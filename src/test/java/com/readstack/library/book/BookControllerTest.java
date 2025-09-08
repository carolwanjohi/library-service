package com.readstack.library.book;

import com.readstack.library.common.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookService service;
    @MockitoBean
    private BookRepository repository;

    private final Book BOOK_MOCK = Book.builder()
            .id(1L)
            .title("Clean Code")
            .isbn("9780132350884")
            .publishedYear(2008)
            .build();

    @Test
    public void list_ok() throws Exception {
        when(service.list()).thenReturn(List.of(BOOK_MOCK));

        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BOOK_MOCK.getId()))
                .andExpect(jsonPath("$[0].title").value(BOOK_MOCK.getTitle()));
    }

    @Test
    public void get_notFound() throws Exception {
        final Long bookId = 99L;
        when(service.get(bookId)).thenThrow(new NotFoundException("Book not found: " + bookId));

        mockMvc.perform(get("/library/books/{id}", bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found: " + bookId));
    }

    @Test
    public void create_valid() throws Exception {
        BOOK_MOCK.setId(10L);
        when(service.createBook(any(Book.class))).thenReturn(BOOK_MOCK);

        String payload = String.format("{\"title\": \"%s\", \"isbn\": \"%s\", \"publishedYear\": %s}",
                BOOK_MOCK.getTitle(), BOOK_MOCK.getIsbn(), BOOK_MOCK.getPublishedYear());

        mockMvc.perform(
                post("/library/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOOK_MOCK.getId()))
                .andExpect(jsonPath("$.title").value(BOOK_MOCK.getTitle()));

    }

    @Test
    public void search_ok() throws Exception {
        when(repository.findAll(org.mockito.ArgumentMatchers.<Specification<Book>>any()))
                .thenReturn(List.of(BOOK_MOCK));

        mockMvc.perform(get("/library/books/search")
                        .param("title", "Clean")
                        .param("fromYear", "2000")
                        .param("toYear", "2020")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BOOK_MOCK.getId()))
                .andExpect(jsonPath("$[0].title").value(BOOK_MOCK.getTitle()));
    }
}
