package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.exception.exeptions.BookNotFoundExceptions;
import com.ifortex.bookservice.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private EntityManager entityManager;

    List<Book> expectedBooks;
    Book book1 = new Book();
    Book book2 = new Book();

    @BeforeEach
    public void init() {
        book1.setGenres(Set.of("Some History"));
        book1.setPublicationDate(LocalDateTime.of(2000, 1, 1, 0, 0));
        book2.setGenres(Set.of("Some"));
        book2.setPublicationDate(LocalDateTime.of(1898, 1, 1, 0, 0));
        expectedBooks = List.of(book1, book2);
    }

    //getAllBooks() method test
    @Test
    void getBooksPositiveTest() {
        TypedQuery<Book> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT b FROM Book b", Book.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedBooks);

        Map<String, Long> expectedGenreCount = new HashMap<>();
        expectedGenreCount.put("Some History", 1L);
        expectedGenreCount.put("Some", 1L);

        Map<String, Long> actualGenreCount = bookService.getBooks();
        assertEquals(expectedGenreCount, actualGenreCount);
        Mockito.verify(entityManager).createQuery("SELECT b FROM Book b", Book.class);
        Mockito.verify(query).getResultList();
    }

    @Test
    void getBooksEmptyListTest() {
        TypedQuery<Book> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT b FROM Book b", Book.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(new ArrayList<>());

        Assertions.assertThrows(BookNotFoundExceptions.class, () -> bookService.getBooks());
        Mockito.verify(entityManager).createQuery("SELECT b FROM Book b", Book.class);
        Mockito.verify(query).getResultList();
    }


    //getAllBooks() method test
    @Test
    void getAllBooksPositiveTest() {
        TypedQuery<Book> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT b FROM Book b", Book.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.getAllBooks();
        assertEquals(expectedBooks, actualBooks);
        assertEquals(2, actualBooks.size());
        Mockito.verify(entityManager).createQuery("SELECT b FROM Book b", Book.class);
        Mockito.verify(query).getResultList();
    }

    @Test
    void getAllBooksPositiveEmptyListTest() {
        TypedQuery<Book> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT b FROM Book b", Book.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(new ArrayList<>());

        List<Book> actualBooks = bookService.getAllBooks();
        assertEquals(0, actualBooks.size());
        Mockito.verify(entityManager).createQuery("SELECT b FROM Book b", Book.class);
        Mockito.verify(query).getResultList();
    }

    //getOldestBookByGenre() method test
    @Test
    void getOldestBookByGenrePositive() {
        String genre = "Some History";

        TypedQuery<Book> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT b FROM Book b", Book.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedBooks);
        Book oldestBook = bookService.getOldestBookByGenre(genre);

        assertEquals(book1, oldestBook);
        Mockito.verify(entityManager).createQuery("SELECT b FROM Book b", Book.class);
        Mockito.verify(query).getResultList();
    }

    @Test
    void getOldestBookByGenreNoGenreProvided() {
        Assertions.assertThrows(BookNotFoundExceptions.class, () ->
                bookService.getOldestBookByGenre(""));
    }

    @Test
    void getOldestBookByGenreNoGenreProvidedNull() {
        Assertions.assertThrows(BookNotFoundExceptions.class, () ->
                bookService.getOldestBookByGenre(null));
    }
}