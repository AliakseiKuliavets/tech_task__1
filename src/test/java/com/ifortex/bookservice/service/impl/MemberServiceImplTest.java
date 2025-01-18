package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.exception.exeptions.BookNotFoundExceptions;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private BookServiceImpl bookService;


    List<Member> expectedMembers;
    List<Book> bookList = new ArrayList<>();
    Member member1 = new Member();
    Member member2 = new Member();
    Member member3 = new Member();
    Book book1 = new Book();
    String genre = "Romance";

    @BeforeEach
    public void init() {
        member1.setMembershipDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        member1.setBorrowedBooks(bookList);

        member2.setMembershipDate(LocalDateTime.of(2021, 1, 1, 0, 0));
        member2.setBorrowedBooks(bookList);

        member3.setMembershipDate(LocalDateTime.of(2023, 1, 1, 0, 0));
        member3.setBorrowedBooks(new ArrayList<>(List.of(book1)));
        book1.setPublicationDate(LocalDateTime.of(1800, 1, 1, 0, 0));

        expectedMembers = List.of(member1, member2, member3);
    }

    @Test
    void findMemberPositiveTest() {
        TypedQuery<Member> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT m FROM Member m", Member.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedMembers);
        Mockito.when(bookService.getOldestBookByGenre(genre)).thenReturn(book1);

        Member actualMember = memberService.findMember();
        assertEquals(member3, actualMember);
        Mockito.verify(entityManager).createQuery("SELECT m FROM Member m", Member.class);
        Mockito.verify(bookService).getOldestBookByGenre(genre);
        Mockito.verify(query).getResultList();
    }

    @Test
    void findMemberBookNotFoundExceptionsTest() {
        TypedQuery<Member> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT m FROM Member m", Member.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedMembers);
        Mockito.when(bookService.getOldestBookByGenre(genre)).thenReturn(null);

        Assertions.assertThrows(BookNotFoundExceptions.class, () -> memberService.findMember());

        Mockito.verify(entityManager).createQuery("SELECT m FROM Member m", Member.class);
        Mockito.verify(bookService).getOldestBookByGenre(genre);
        Mockito.verify(query).getResultList();
    }

    @Test
    void findMembersPositiveTest() {
        TypedQuery<Member> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT m FROM Member m", Member.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedMembers);

        List<Member> actualMember = memberService.findMembers();
        assertEquals(1, actualMember.size());
        Mockito.verify(entityManager).createQuery("SELECT m FROM Member m", Member.class);
        Mockito.verify(query).getResultList();
    }

    //getAllMembers() method test
    @Test
    void getAllMembersPositiveTest() {
        TypedQuery<Member> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT m FROM Member m", Member.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(expectedMembers);

        List<Member> actualMember = memberService.getAllMembers();
        assertEquals(expectedMembers, actualMember);
        assertEquals(3, actualMember.size());
        Mockito.verify(entityManager).createQuery("SELECT m FROM Member m", Member.class);
        Mockito.verify(query).getResultList();
    }

    @Test
    void getAllMembersPositiveReturnEmptyListTest() {
        TypedQuery<Member> query = mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery("SELECT m FROM Member m", Member.class)).thenReturn(query);
        Mockito.when(query.getResultList()).thenReturn(new ArrayList<>());

        List<Member> actualMember = memberService.getAllMembers();
        assertEquals(0, actualMember.size());
        Mockito.verify(entityManager).createQuery("SELECT m FROM Member m", Member.class);
        Mockito.verify(query).getResultList();
    }

}