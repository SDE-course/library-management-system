package com.example.library;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Tag("Layer1")
class ProjectLogicL1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullWorkflow_createAuthorBookMemberBorrowReturn_shouldWork() throws Exception {
        Long authorId = createAuthor("Naguib", "Mahfouz");
        Long bookId = createBook("Cairo Trilogy", "INT-ISBN-2001", authorId);
        Long memberId = createMember("Ali", "Hassan", "ali.int@example.com");

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author.id").value(authorId))
                .andExpect(jsonPath("$.author.firstName").value("Naguib"))
                .andExpect(jsonPath("$.author.lastName").value("Mahfouz"));

        MvcResult borrowResult = mockMvc.perform(post("/api/borrow-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "memberId", memberId,
                                "bookId", bookId
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.returned").value(false))
                .andReturn();

        Long borrowId = extractId(borrowResult.getResponse().getContentAsString());

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));

        mockMvc.perform(put("/api/borrow-records/{id}/return", borrowId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returned").value(true))
                .andExpect(jsonPath("$.returnDate").isNotEmpty());

        mockMvc.perform(get("/api/borrow-records/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void borrowUnavailableBook_shouldReturn409Conflict() throws Exception {
        Long authorId = createAuthor("Taha", "Hussein");
        Long bookId = createBook("The Days", "INT-ISBN-2002", authorId);
        Long memberAId = createMember("Mona", "Saleh", "mona.int@example.com");
        Long memberBId = createMember("Yara", "Kamal", "yara.int@example.com");

        mockMvc.perform(post("/api/borrow-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "memberId", memberAId,
                                "bookId", bookId
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/borrow-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "memberId", memberBId,
                                "bookId", bookId
                        ))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Book is not available for borrowing"));

        mockMvc.perform(get("/api/borrow-records/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void createBookWithUnknownAuthor_shouldReturn404() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Unknown Author Book",
                                "isbn", "INT-ISBN-2003",
                                "genre", "Novel",
                                "publishedYear", "2020",
                                "authorId", 999999
                        ))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Author not found with id: 999999"));
    }

    @Test
    void duplicateMemberEmail_shouldReturn409() throws Exception {
        createMember("Sara", "Ibrahim", "sara.int@example.com");

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "firstName", "Sara2",
                                "lastName", "Ibrahim2",
                                "email", "sara.int@example.com",
                                "phoneNumber", "01012345678"
                        ))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void invalidAuthorPayload_shouldReturn400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "firstName", "",
                                "lastName", ""
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.firstName").exists())
                .andExpect(jsonPath("$.fieldErrors.lastName").exists());
    }

    @Test
    void malformedJson_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Bad\",\"lastName\":\"Json\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    private Long createAuthor(String firstName, String lastName) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "firstName", firstName,
                                "lastName", lastName,
                                "nationality", "Egyptian"
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result.getResponse().getContentAsString());
    }

    private Long createBook(String title, String isbn, Long authorId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", title,
                                "isbn", isbn,
                                "genre", "Novel",
                                "publishedYear", "2000",
                                "authorId", authorId
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result.getResponse().getContentAsString());
    }

    private Long createMember(String firstName, String lastName, String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "firstName", firstName,
                                "lastName", lastName,
                                "email", email,
                                "phoneNumber", "01000000000"
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result.getResponse().getContentAsString());
    }

    private Long extractId(String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);
        assertThat(root.has("id")).isTrue();
        return root.get("id").asLong();
    }
}
