package com.example.blog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class AuthorRepository {
    private static final List<Author> AUTHORS = createAuthors();

    private static List<Author> createAuthors() {
        List<Author> authors = new ArrayList<>(1000);
        for (long i = 1; i <= 1000; i++) {
            authors.add(new Author(i, "Author " + i));
        }
        return authors;
    }

    public List<Author> findAuthorsByIds(List<Long> ids) {
        List<Author> result = new ArrayList<>();
        for (Author author : AUTHORS) {
            if (ids.contains(author.getId())) {
                result.add(author);
            }
        }
        return result;
    }
}
