package com.example.bookapi.service;

import com.example.bookapi.client.OpenLibraryClient;
import com.example.bookapi.model.Book;
import com.example.bookapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final OpenLibraryClient openLibraryClient;

    public Book createBook(Book bookRequest) {
        String isbnKey = "ISBN:" + bookRequest.getIsbn().replace("-", "");

        Map<String, Object> response = openLibraryClient.getBookDataByIsbn(isbnKey);
        if (!response.containsKey(isbnKey)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ISBN no válido o libro no encontrado");
        }

        Map<String, Object> bookData = (Map<String, Object>) response.get(isbnKey);

        // Corrección de datos desde la API
        String title = (String) bookData.getOrDefault("title", bookRequest.getTitle());
        Integer year = null;
        try {
            List<Map<String, Object>> publishDates = (List<Map<String, Object>>) bookData.get("publish_date");
            if (publishDates != null && !publishDates.isEmpty()) {
                Map<String, Object> firstDate = (Map<String, Object>) publishDates.get(0);
                String yearStr = (String) firstDate.get("value");
                year = Integer.parseInt(yearStr.substring(yearStr.length() - 4));
            }
        } catch (Exception ignored) {}

        List<Map<String, Object>> authors = (List<Map<String, Object>>) bookData.get("authors");
        String author = bookRequest.getAuthor();
        if (authors != null && !authors.isEmpty()) {
            Map<String, Object> firstAuthor = authors.get(0);
            author = (String) firstAuthor.getOrDefault("name", bookRequest.getAuthor());
        }

        String url = (String) bookData.getOrDefault("url", null);

        Book book = Book.builder()
                .title(title)
                .author(author)
                .isbn(bookRequest.getIsbn())
                .year(year != null ? year : bookRequest.getYear())
                .url("https://openlibrary.org" + url)
                .build();

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book existing = getBookById(id);
        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setYear(updatedBook.getYear());
        existing.setUrl(updatedBook.getUrl());

        return bookRepository.save(existing);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no encontrado");
        }
        bookRepository.deleteById(id);
    }
}