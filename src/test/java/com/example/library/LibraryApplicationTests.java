package com.example.library;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.dto.BorrowRecordDTO;
import com.example.library.dto.MemberRequestDTO;
import com.example.library.dto.MemberResponseDTO;
import com.example.library.service.AuthorService;
import com.example.library.service.BookService;
import com.example.library.service.BorrowRecordService;
import com.example.library.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LibraryApplicationTests {

	@Autowired
	private AuthorService authorService;

	@Autowired
	private BookService bookService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private BorrowRecordService borrowRecordService;

	@Test
	void contextLoads() {
	}

	@Test
	void createAuthorAndBook_relationshipWorks() {
		AuthorDTO.Request authorRequest = AuthorDTO.Request.builder()
				.firstName("Naguib")
				.lastName("Mahfouz")
				.nationality("Egyptian")
				.build();

		AuthorDTO.Response author = authorService.createAuthor(authorRequest);

		BookDTO.Request bookRequest = BookDTO.Request.builder()
				.title("Cairo Trilogy")
				.isbn("ISBN-1001")
				.genre("Novel")
				.publishedYear("1956")
				.authorId(author.getId())
				.build();

		BookDTO.Response createdBook = bookService.createBook(bookRequest);
		List<BookDTO.Response> booksByAuthor = authorService.getBooksByAuthor(author.getId());

		assertThat(createdBook.getAuthorName()).isEqualTo("Naguib Mahfouz");
		assertThat(createdBook.isAvailable()).isTrue();
		assertThat(booksByAuthor).hasSize(1);
		assertThat(booksByAuthor.get(0).getTitle()).isEqualTo("Cairo Trilogy");
	}

	@Test
	void borrowBookFlow_updatesAvailability() {
		AuthorDTO.Response author = authorService.createAuthor(AuthorDTO.Request.builder()
				.firstName("Taha")
				.lastName("Hussein")
				.build());

		BookDTO.Response book = bookService.createBook(BookDTO.Request.builder()
				.title("The Days")
				.isbn("ISBN-1002")
				.genre("Memoir")
				.publishedYear("1929")
				.authorId(author.getId())
				.build());

		MemberRequestDTO memberRequestDTO = new MemberRequestDTO();
		memberRequestDTO.firstName = "Ali";
		memberRequestDTO.lastName = "Hassan";
		memberRequestDTO.email = "ali@example.com";
		memberRequestDTO.phoneNumber = "01000000000";

		MemberResponseDTO member = memberService.create(memberRequestDTO);

		BorrowRecordDTO.Response borrowRecord = borrowRecordService.borrowBook(
				BorrowRecordDTO.Request.builder()
						.memberId(member.id)
						.bookId(book.getId())
						.build()
		);

		BookDTO.Response updatedBook = bookService.getBookById(book.getId());

		assertThat(borrowRecord.isReturned()).isFalse();
		assertThat(updatedBook.isAvailable()).isFalse();
	}

	@Test
	void returnBookFlow_marksRecordReturnedAndBookAvailable() {
		AuthorDTO.Response author = authorService.createAuthor(AuthorDTO.Request.builder()
				.firstName("Youssef")
				.lastName("Idris")
				.build());

		BookDTO.Response book = bookService.createBook(BookDTO.Request.builder()
				.title("The Cheapest Nights")
				.isbn("ISBN-1003")
				.genre("Short Stories")
				.publishedYear("1954")
				.authorId(author.getId())
				.build());

		MemberRequestDTO memberRequestDTO = new MemberRequestDTO();
		memberRequestDTO.firstName = "Mona";
		memberRequestDTO.lastName = "Saleh";
		memberRequestDTO.email = "mona@example.com";
		memberRequestDTO.phoneNumber = "01111111111";

		MemberResponseDTO member = memberService.create(memberRequestDTO);

		BorrowRecordDTO.Response borrowed = borrowRecordService.borrowBook(BorrowRecordDTO.Request.builder()
				.memberId(member.id)
				.bookId(book.getId())
				.build());

		BorrowRecordDTO.Response returned = borrowRecordService.returnBook(borrowed.getId());
		BookDTO.Response updatedBook = bookService.getBookById(book.getId());

		assertThat(returned.isReturned()).isTrue();
		assertThat(returned.getReturnDate()).isNotNull();
		assertThat(updatedBook.isAvailable()).isTrue();
	}

}
