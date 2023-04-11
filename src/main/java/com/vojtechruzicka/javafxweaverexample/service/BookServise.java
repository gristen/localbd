package com.vojtechruzicka.javafxweaverexample.service;

import com.vojtechruzicka.javafxweaverexample.entity.BookEntity;
import com.vojtechruzicka.javafxweaverexample.repo.BookRepo;
import org.springframework.stereotype.Service;

@Service
public class BookServise {

    private final BookRepo repo;
    public BookServise( BookRepo repo) {this.repo = repo;}
    public Iterable<BookEntity> getAll(){return repo.findAll();}
    public BookEntity saveBook(BookEntity book){ repo.save(book);
        return repo.save(book);
    }
    public void deleteBook(Long id){ repo.deleteById(id);}
}
