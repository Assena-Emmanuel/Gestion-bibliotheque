package com.biblio.app.common.constant;

/**
 * Centralise les messages exposés par l'API.
 */
public final class ApiMessages {

    private ApiMessages() {
    }

    public static final String ADMIN_WELCOME = "Bienvenue administrateur";
    public static final String AUTHORIZATION_TOKEN_REQUIRED = "Authorization token is required";
    public static final String LOGOUT_SUCCESSFUL = "Logout successful";

    public static final String USERS_RETRIEVED = "Users retrieved successfully";
    public static final String USER_RETRIEVED = "User retrieved successfully";
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_UPDATED = "User updated successfully";
    public static final String USER_ROLE_UPDATED = "User role updated successfully";
    public static final String USER_PWD_UPDATED = "User password updated successfully";
    public static final String USER_DELETED = "User deleted successfully";
    public static final String EMAIL_ALREADY_EXISTS = "This email already exists";
    public static final String EMAIL_DOES_NOT_EXIST = "This email does not exist";
    public static final String ID_DOES_NOT_EXIST = "This id does not exist";
    public static final String USER_NOT_FOUND = "User not found";

    public static final String AUTHORS_RETRIEVED = "Authors retrieved successfully";
    public static final String AUTHOR_RETRIEVED = "Author retrieved successfully";
    public static final String AUTHOR_CREATED = "Author created successfully";
    public static final String AUTHORS_CREATED = "More authors created successfully";
    public static final String AUTHOR_DELETED = "Author deleted successfully";
    public static final String ALL_AUTHORS_DELETED = "All authors deleted successfully";

    public static final String BOOKS_RETRIEVED = "Books retrieved successfully";
    public static final String BOOK_RETRIEVED = "Book retrieved successfully";
    public static final String BOOK_CREATED = "Book created successfully";
    public static final String BOOK_DELETED = "Book deleted successfully";
    public static final String BOOK_UPDATED = "Book updated successfully";
    public static final String AUTHOR_NOT_FOUND = "Author not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String AVAILABLE_COPIES_EXCEED_TOTAL = "Available copies cannot be greater than total copies";
    public static final String TOTAL_COPIES_NEGATIVE = "Total copies cannot be negative";
    public static final String BOOK_ALREADY_EXISTS = "Book already exists";

    public static final String CATEGORIES_RETRIEVED = "Categories retrieved successfully";
    public static final String CATEGORY_RETRIEVED = "Category retrieved successfully";
    public static final String CATEGORY_CREATED = "Category created successfully";
    public static final String CATEGORIES_CREATED = "More categories created successfully";
    public static final String CATEGORY_DELETED = "Category deleted successfully";
    public static final String CATEGORY_UPDATED = "Category updated successfully";

    public static String authorNotFoundWithId(Object id) {
        return "Author not found with id: " + id;
    }

    public static String authorNotFoundWithName(String name) {
        return "Author not found with name: " + name;
    }

    public static String authorAlreadyExistsWithName(String name) {
        return "Author already exists with name: " + name;
    }

    public static String bookNotFoundWithId(Object id) {
        return "Book not found with id: " + id;
    }

    public static String categoryNotFoundWithId(Object id) {
        return "Category not found with id: " + id;
    }

    public static String categoryAlreadyExistsWithName(String name) {
        return "Category already exists with name: " + name;
    }

    public static String userNotFoundWithId(Object id) {
        return "User not found with id: " + id;
    }
}
