package com.example.bookapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "openLibraryClient", url = "https://openlibrary.org")
public interface OpenLibraryClient {

    @GetMapping("/api/books?format=json&jscmd=data")
    Map<String, Object> getBookDataByIsbn(@RequestParam("bibkeys") String isbnKey);
}