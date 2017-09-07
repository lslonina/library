package com.slonina.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(LibraryApplication.class);

	@Autowired BooksRepository booksRepository;
	@Autowired BooksScanner booksScanner;

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LibraryApplication.class);
        app.run(args);
	}

    @Override
    public void run(String... args) throws Exception {
        log.info("Books indexed: " + booksRepository.count());

        booksScanner.scan(args[0]);
    }

}