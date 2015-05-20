package library.main.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import library.main.model.Book;

public class BookGrouper {

	public static void groupBasedOn(List<Book> bookList,
			Function<Book, String> mapper, Consumer<String> consumer) {
		bookList.stream().map(mapper).distinct()
				.forEach(category -> consumer.accept(category));
	}
}
