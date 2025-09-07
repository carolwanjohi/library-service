package com.readstack.library.book;

import com.readstack.library.common.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BookService {
    private final  BookRepository bookRepository;

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> list() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book get(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found: " + id));
    }

    public Book update(Long id, Book patch) {
        Book book = get(id);
        book.setTitle(patch.getTitle());
        book.setIsbn(patch.getIsbn());
        book.setPublishedYear(patch.getPublishedYear());
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) throw new NotFoundException("Book not found: " + id);
        bookRepository.deleteById(id);
    }
}
