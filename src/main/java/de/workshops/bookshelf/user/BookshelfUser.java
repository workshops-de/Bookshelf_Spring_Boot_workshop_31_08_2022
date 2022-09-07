package de.workshops.bookshelf.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class BookshelfUser {

    @Id
    private String username;

    private String password;
}
