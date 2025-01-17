package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.exception.exeptions.BookNotFoundExceptions;
import com.ifortex.bookservice.exception.message.ErrorMessage;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final EntityManager entityManager;

    //Implement a method that retrieves the total count of books for each genre, ordered from the genre with the most
    //books to the least
    @Override
    public Map<String, Long> getBooks() {
        List<Book> books = getAllBooks();
        if (books.isEmpty()) {
            throw new BookNotFoundExceptions(ErrorMessage.BOOK_NOT_FOUND);
        }
        Map<String, Long> genreCount = new HashMap<>();

        for (Book book : books) {
            for (String genre : book.getGenres()) {
                genreCount.put(genre, genreCount.getOrDefault(genre, 0L) + 1);
            }
        }

        return genreCount.entrySet().stream()
                .sorted((book1, book2) ->
                        Long.compare(book2.getValue(), book1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (book1, book2) -> book1,
                        LinkedHashMap::new
                ));
    }

    // эти методы можно было добавить в BookService и использовать позже для других логик, но нельзя изменять файл
    // these methods could be added to the BookService and used later for other logic, but the file cannot be modified
    public List<Book> getAllBooks() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);
        return query.getResultList();
    }

    public Book getOldestBookByGenre(String genre) {
        if (genre == null || genre.isEmpty()) {
            throw new BookNotFoundExceptions(ErrorMessage.NO_GENRE_PROVIDED);
        }
        Book oldesBook = null;
        List<Book> books = getAllBooks();
        LocalDateTime oldestDate = LocalDateTime.now();

        for (Book book : books) {
            if (book.getGenres().contains(genre) && book.getPublicationDate().isBefore(oldestDate)) {
                oldestDate = book.getPublicationDate();
                oldesBook = book;
            }
        }
        return oldesBook;
    }
}
