package de.workshops.bookshelf.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BookshelfUser {

    @Id
    private String username;

    private String password;
}
