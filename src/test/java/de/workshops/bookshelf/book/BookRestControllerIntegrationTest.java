package de.workshops.bookshelf.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class BookRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRestController bookRestController;

    @Test
    void bookListSizeAndContent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", is("Clean Code")))
                .andReturn();
        String jsonPayload = mvcResult.getResponse().getContentAsString();

        Book[] books = objectMapper.readValue(jsonPayload, Book[].class);
        assertEquals(3, books.length);
        assertEquals("Clean Code", books[1].getTitle());
    }

    @Test
    void isAuthorPresentWithRestAssuredMockMvc() {
        RestAssuredMockMvc.standaloneSetup(bookRestController);
        RestAssuredMockMvc.
                given().
                log().all().
                when().
                get("/book").
                then().
                log().all().
                statusCode(200).
                body("author[0]", equalTo("Erich Gamma"));
    }

    @Test
    void isAuthorPresentWithRestAssured() {
        RestAssured.
                given().
                log().all().
                when().
                get("/book").
                then().
                log().all().
                statusCode(200).
                body("author[0]", equalTo("Erich Gamma"));
    }

    @Test
    void newBookCanBeCreated() {
        RestAssuredMockMvc.standaloneSetup(bookRestController);

        Book book = new Book(
                "Domain-Driven Design: Tackling Complexity in the Heart of Software",
                "This is not a book about specific technologies. It offers readers a systematic approach to domain-driven design, presenting an extensive set of design best practices, experience-based techniques, and fundamental principles that facilitate the development of software projects facing complex domains.",
                "Eric Evans",
                "978-0321125217"
        );

        RestAssuredMockMvc.
                given().
                log().all().
                body(book).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                when().
                post("/book").
                then().
                log().all().
                statusCode(200).
                body("author", equalTo("Eric Evans"));
    }
}
