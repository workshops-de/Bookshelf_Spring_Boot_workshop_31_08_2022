package de.workshops.bookshelf.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookshelfUserRepository extends MongoRepository<BookshelfUser, String> {

    BookshelfUser findByUsername(String username);
}
