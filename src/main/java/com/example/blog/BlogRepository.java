package com.example.blog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class BlogRepository {
    private static final List<Blog> BLOGS = createBlogs();

    private static List<Blog> createBlogs() {
        List<Blog> blogs = new ArrayList<>(10000);
        for (long i = 1; i <= 10000; i++) {
            blogs.add(new Blog(i, "Blog Post " + i, (i % 1000) + 1)); // 1000 authors
        }
        return blogs;
    }

    public List<Blog> findBlogsByIds(List<Long> ids) {
        List<Blog> result = new ArrayList<>();
        for (Blog blog : BLOGS) {
            if (ids.contains(blog.getId())) {
                result.add(blog);
            }
        }
        return result;
    }
}
