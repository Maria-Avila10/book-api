package com.example.bookapi.controller;

import com.example.bookapi.model.Book;
import com.example.bookapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private List<Book> mockBooks;

    @BeforeEach
    void setUp() {
        mockBooks = List.of(
                new Book(1L, "Libro Test", "Autor Test", "123", 2021, "http://url")
        );
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }
}