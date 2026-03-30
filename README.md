
# Blog GraphQL Benchmark (Breadth vs Depth)

This project is a Spring Boot GraphQL demo inspired by Shopify's breadth-first execution blog. It benchmarks two resolver strategies:

- **Breadth (Batch) Resolver**: Fetches authors in batches (DataLoader style)
- **Depth Resolver**: Fetches authors one-by-one (N+1 style)

## How to Run

```
mvn test -Dtest=BlogResolverBenchmarkTest
```

## What It Does
- Runs two tests with 10,000 blog IDs:
  - `benchmarkDepthResolver`: Depth (N+1) resolver
  - `benchmarkBreadthResolver`: Breadth (batch) resolver
- Prints timing for each approach.

## Project Structure
- `BlogRepository`/`AuthorRepository`: In-memory data for 10,000 blogs, 1,000 authors
- `BlogGraphQLController`: Switchable resolver logic
- `BlogResolverBenchmarkTest`: Benchmark test

## Requirements
- Java 17+
- Maven 3.8+

## Schema Example
```
type Blog {
  id: ID!
  title: String!
  author: Author!
}

type Author {
  id: ID!
  name: String!
}

type Query {
  blogs(ids: [ID!]!): [Blog!]!
}
```

## Notes
- Adjust the number of blogs/authors in the repositories for different scales.
- For real-world use, connect to a database and use DataLoader for batching.
