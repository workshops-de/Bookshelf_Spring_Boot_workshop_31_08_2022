package de.workshops.bookshelf;

import de.workshops.bookshelf.user.BookshelfUser;
import de.workshops.bookshelf.user.BookshelfUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class BookshelfApplication {

	private final BookshelfUserRepository bookshelfUserRepository;

	@Value("${custom.admin.password}")
	private String adminPassword;

	public static void main(String[] args) {
		SpringApplication.run(BookshelfApplication.class, args);
	}

	@PostConstruct
	public void initUsers() {
		log.info("Initializing Bookshelf users ...");

		bookshelfUserRepository.save(
				BookshelfUser.builder()
						.username("admin")
						.password(adminPassword)
						.build()
		);
	}
}
