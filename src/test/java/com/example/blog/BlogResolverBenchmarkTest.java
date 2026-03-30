package com.example.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogResolverBenchmarkTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void benchmarkDepthResolver() throws Exception {
        String query = "query($ids: [ID!]!, $mode: String!) { blogs(ids: $ids, mode: $mode) { id title author { id name } } }";
        // Generate 10,000 blog IDs
        StringBuilder idsBuilder = new StringBuilder();
        for (int i = 1; i <= 10000; i++) {
            if (i > 1)
                idsBuilder.append(",");
            idsBuilder.append(i);
        }
        String idsArray = idsBuilder.toString();
        String payload = String.format("{\"query\":\"%s\",\"variables\":{\"ids\":[%s],\"mode\":\"depth\"}}",
                query.replace("\"", "\\\""), idsArray);
        long start = System.nanoTime();
        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk());
        long end = System.nanoTime();
        System.out.println("Depth resolver time: " + (end - start) / 1_000_000 + " ms");
    }

    @Test
    void benchmarkBreadthResolver() throws Exception {
        String query = "query($ids: [ID!]!, $mode: String!) { blogs(ids: $ids, mode: $mode) { id title author { id name } } }";
        // Generate 10,000 blog IDs
        StringBuilder idsBuilder = new StringBuilder();
        for (int i = 1; i <= 10000; i++) {
            if (i > 1)
                idsBuilder.append(",");
            idsBuilder.append(i);
        }
        String idsArray = idsBuilder.toString();
        String payload = String.format("{\"query\":\"%s\",\"variables\":{\"ids\":[%s],\"mode\":\"breadth\"}}",
                query.replace("\"", "\\\""), idsArray);
        long start = System.nanoTime();
        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk());
        long end = System.nanoTime();
        System.out.println("Breadth resolver time: " + (end - start) / 1_000_000 + " ms");
    }
}
