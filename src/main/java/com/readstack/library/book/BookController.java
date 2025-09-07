package com.readstack.library.book;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody @Valid Book input) {
        return service.createBook(input);
    }

    @GetMapping
    public List<Book> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody @Valid Book input) {
        return service.update(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam(required = false) String title,
                             @RequestParam(required = false) Integer fromYear,
                             @RequestParam(required = false) Integer toYear
    ) {
        Specification<Book> specification = Specification.<Book>unrestricted()
                .and(titleContains(title))
                .and(yearGTE(fromYear))
                .and(yearLTE(toYear));
        return repository.findAll(specification);
    }
}
