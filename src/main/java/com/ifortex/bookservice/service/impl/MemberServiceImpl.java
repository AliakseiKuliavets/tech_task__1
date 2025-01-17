package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.exception.exeptions.BookNotFoundExceptions;
import com.ifortex.bookservice.exception.message.ErrorMessage;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final EntityManager entityManager;
    private final BookServiceImpl bookService;

    public List<Member> getAllMembers() {
        TypedQuery<Member> memberTypedQuery = entityManager.createQuery(
                "SELECT m FROM Member m", Member.class);
        return memberTypedQuery.getResultList();
    }

    //  Implement a method that finds and returns the member who has read the oldest “Romance” genre book and who was
    //most recently registered on the platform.
    // Я мог схитрить и написать сразу в наглую под каким id у меня лежит книга, но я не хочу этого делать
    @Override
    public Member findMember() {
        List<Member> members = getAllMembers();
        Book book = bookService.getOldestBookByGenre("Romance");
        if (book == null) {
            throw new BookNotFoundExceptions(ErrorMessage.BOOK_NOT_FOUND);
        }
        long oldestBookId = book.getId();
        Member memberWithLatestRegistration = null;

        for (Member member : members) {
            if (member.getBorrowedBooks().isEmpty()) {
                continue;
            }
            for (Book book1 : member.getBorrowedBooks()) {
                if (book1.getId() == oldestBookId) {
                    if (memberWithLatestRegistration == null ||
                            member.getMembershipDate().isAfter(memberWithLatestRegistration.getMembershipDate())) {
                        memberWithLatestRegistration = member;
                    }
                    break;
                }
            }
        }
        return memberWithLatestRegistration;
    }

    // Implement a method that finds and returns members who registered in 2023 but have not read any books.
    @Override
    public List<Member> findMembers() {
        List<Member> members = getAllMembers();
        List<Member> membersNoBookAndFrom2023 = new ArrayList<>();

        for (Member member : members) {
            if (member.getMembershipDate().getYear() == 2023 && member.getBorrowedBooks().isEmpty()) {
                membersNoBookAndFrom2023.add(member);
            }
        }
        return membersNoBookAndFrom2023;
    }
}
