package com.example.blog;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;

@Controller
public class BlogGraphQLController {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private AuthorBatchResolver authorBatchResolver;
    @Autowired
    private AuthorRepository authorRepository;

    @QueryMapping
    public List<Blog> blogs(@Argument List<Long> ids, DataFetchingEnvironment env) {
        // Accept 'mode' as a top-level argument for benchmarking
        String mode = env.getArgumentOrDefault("mode", "breadth");
        env.getGraphQlContext().put("mode", mode);
        return blogRepository.findBlogsByIds(ids);
    }

    /**
     * Switchable resolver for Blog.author: depth (one-by-one) or breadth (batch).
     * Pass mode="depth" or mode="breadth" as a GraphQL argument for benchmarking.
     */
    @SchemaMapping(typeName = "Blog", field = "author")
    public Author author(Blog blog, DataFetchingEnvironment env) {
        String mode = env.getGraphQlContext().getOrDefault("mode", "breadth");
        Long authorId = blog.getAuthorId();
        if ("depth".equalsIgnoreCase(mode)) {
            // Depth: fetch author one-by-one (no batching)
            List<Author> authors = authorRepository.findAuthorsByIds(Collections.singletonList(authorId));
            return authors.isEmpty() ? null : authors.get(0);
        } else {
            // Breadth: batch resolver
            Map<Long, Author> map = authorBatchResolver.getAuthorsByIds(Collections.singletonList(authorId));
            return map.get(authorId);
        }
    }
}
