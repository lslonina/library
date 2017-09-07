package com.slonina.library;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByTitle(String lastName);

    List<Book> findByYear(Integer publicationYear);
}