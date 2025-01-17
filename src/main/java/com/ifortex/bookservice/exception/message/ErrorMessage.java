package com.ifortex.bookservice.exception.message;

public class ErrorMessage {

    private ErrorMessage() {
        throw new IllegalStateException("Utility class");
    }

    public static final String NO_BOOK_FOUND_WITH_GENRE = "No book found with genre";
    public static final String BOOK_NOT_FOUND = "Book not found";
    public static final String NO_GENRE_PROVIDED = "No genre provided";
}
