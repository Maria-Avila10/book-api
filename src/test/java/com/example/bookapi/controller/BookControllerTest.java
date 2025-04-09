package com.example.bookapi.controller;

import com.example.bookapi.controller.BookController;
import com.example.bookapi.model.Book;
import com.example.bookapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(BookControllerTest.MockedBookServiceConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    private List<Book> mockBooks;

    @BeforeEach
    void setUp() {
        mockBooks = List.of(
                new Book(1L, "Libro Test", "Autor Test", "123", 2021, "http://url")
        );
        when(bookService.getAllBooks()).thenReturn(mockBooks);
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockedBookServiceConfig {
        @Bean
        @Primary
        public BookService bookService() {
            return Mockito.mock(BookService.class);
        }
    }
}
