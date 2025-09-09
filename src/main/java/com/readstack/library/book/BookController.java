package com.readstack.library.book;

import com.readstack.library.book.dto.BookCreateUpdateDto;
import com.readstack.library.book.dto.BookDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.readstack.library.book.BookSpecs.titleContains;
import static com.readstack.library.book.BookSpecs.yearGTE;
import static com.readstack.library.book.BookSpecs.yearLTE;

@RestController
@RequestMapping("library/books")
@AllArgsConstructor
public class BookController {
    private BookService service;
    private BookRepository repository;
    private BookMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody @Valid BookCreateUpdateDto input) {
        Book created = service.createBook(mapper.toBook(input));
        return mapper.toDTO(created);
    }

    @GetMapping
    public List<BookDto> list() {
        return service.list().stream().map(mapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public BookDto get(@PathVariable Long id) {
        return mapper.toDTO(service.get(id));
    }

    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id, @RequestBody @Valid BookCreateUpdateDto input) {
        Book existing = service.get(id);
        mapper.updateBookFromDto(input, existing);
        return mapper.toDTO(service.update(id, existing));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<BookDto> search(@RequestParam(required = false) String title,
                             @RequestParam(required = false) Integer fromYear,
                             @RequestParam(required = false) Integer toYear
    ) {
        Specification<Book> specification = Specification.<Book>unrestricted()
                .and(titleContains(title))
                .and(yearGTE(fromYear))
                .and(yearLTE(toYear));
        return repository.findAll(specification).stream().map(mapper::toDTO).toList();
    }
}
