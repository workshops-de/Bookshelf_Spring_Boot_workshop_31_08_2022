package de.workshops.bookshelf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("prod")
class EnvironmentTest {

	@Value("${server.port}")
	private int port;

	@Test
	void verifyProdPort() {
		System.out.println("Port: " + port);

		assertEquals(8090, port);
	}
}
