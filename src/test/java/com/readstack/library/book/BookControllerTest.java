package com.readstack.library.book;

import com.readstack.library.book.dto.BookCreateUpdateDto;
import com.readstack.library.book.dto.BookDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @MockitoBean
    private BookMapper mapper;

    private final Book BOOK_MOCK = Book.builder()
            .id(1L)
            .title("Clean Code")
            .isbn("9780132350884")
            .publishedYear(2008)
            .build();
    private final BookDto BOOK_DTO_MOCK = BookDto.builder()
            .id(BOOK_MOCK.getId())
            .title(BOOK_MOCK.getTitle())
            .isbn(BOOK_MOCK.getIsbn())
            .publishedYear(BOOK_MOCK.getPublishedYear())
            .build();
    private final BookCreateUpdateDto BOOK_CREATE_UPDATE_DTO_MOCK = BookCreateUpdateDto.builder()
            .title(BOOK_MOCK.getTitle())
            .isbn(BOOK_MOCK.getIsbn())
            .publishedYear(BOOK_MOCK.getPublishedYear())
            .build();

    @Test
    public void list_ok() throws Exception {
        when(service.list()).thenReturn(List.of(BOOK_MOCK));
        when(mapper.toDTO(BOOK_MOCK)).thenReturn(BOOK_DTO_MOCK);

        mockMvc.perform(get("/library/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BOOK_DTO_MOCK.getId()))
                .andExpect(jsonPath("$[0].title").value(BOOK_DTO_MOCK.getTitle()));
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
        when(mapper.toBook(any(BookCreateUpdateDto.class))).thenReturn(BOOK_MOCK);
        when(service.createBook(any(Book.class))).thenReturn(BOOK_MOCK);
        when(mapper.toDTO(any(Book.class))).thenReturn(BOOK_DTO_MOCK);

        String payload = String.format("{\"title\": \"%s\", \"isbn\": \"%s\", \"publishedYear\": %s}",
                BOOK_CREATE_UPDATE_DTO_MOCK.getTitle(), BOOK_CREATE_UPDATE_DTO_MOCK.getIsbn(), BOOK_CREATE_UPDATE_DTO_MOCK.getPublishedYear());

        mockMvc.perform(
                post("/library/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOOK_DTO_MOCK.getId()))
                .andExpect(jsonPath("$.title").value(BOOK_DTO_MOCK.getTitle()));

    }

    @Test
    public void search_ok() throws Exception {
        when(repository.findAll(org.mockito.ArgumentMatchers.<Specification<Book>>any()))
                .thenReturn(List.of(BOOK_MOCK));
        when(mapper.toDTO(BOOK_MOCK)).thenReturn(BOOK_DTO_MOCK);

        mockMvc.perform(get("/library/books/search")
                        .param("title", "Clean")
                        .param("fromYear", "2000")
                        .param("toYear", "2020")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BOOK_DTO_MOCK.getId()))
                .andExpect(jsonPath("$[0].title").value(BOOK_DTO_MOCK.getTitle()));
    }
}
