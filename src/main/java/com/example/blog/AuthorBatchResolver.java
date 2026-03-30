package com.example.blog;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorBatchResolver {
    @Autowired
    private AuthorRepository authorRepository;

    public Map<Long, Author> getAuthorsByIds(List<Long> authorIds) {
        List<Author> authors = authorRepository.findAuthorsByIds(authorIds);
        return authors.stream().collect(Collectors.toMap(Author::getId, a -> a));
    }
}
