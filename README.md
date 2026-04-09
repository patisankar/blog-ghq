
# Blog GraphQL Benchmark (Breadth vs Depth)

This project is a Spring Boot GraphQL demo inspired by Shopify's breadth-first execution blog. It benchmarks two resolver strategies:

- **Breadth (Batch) Resolver**: Fetches authors in batches (DataLoader style)
- **Depth Resolver**: Fetches authors one-by-one (N+1 style)

## STAR
**Situation**

At PayPal scale, our GraphQL APIs were serving large datasets such as transaction histories. We observed that latency increased significantly for high-volume queries.

Upon analysis, we found the root cause was the default Depth-First Execution (DFE) model in GraphQL, where each field resolver was executed per object. For large datasets, this resulted in thousands of repeated resolver executions and excessive async overhead.

**Task**

We needed to optimize API performance for large queries without changing the client contract, by addressing inefficiencies in the GraphQL execution model itself, particularly reducing the cost of DFE.

**Action**

We redesigned our resolver strategy to mimic Breadth-First Execution (BFE) behavior.

Instead of resolving fields per object (DFE), we resolved fields across all objects together (BFE)

Introduced batching via DataLoader / batch resolvers

Refactored services to support bulk APIs (e.g., fetch transactions for multiple accounts in one call)

Reduced repeated resolver invocations and async promise chains

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
