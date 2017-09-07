package com.slonina.library;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class BooksScanner {

    @Autowired
    BooksRepository booksRepository;

    public void scan(String libraryFolder) throws IOException {
        Path folder = Paths.get(libraryFolder);
        Files
                .list(folder)
                .filter(p -> p.toString().endsWith("pdf"))
                .forEach(this::processPath);
    }

    private void processPath(Path path) {
        Optional<Book> book = booksRepository.findByTitle(getTitle(path));
        if (book.isPresent()) {
            System.out.println("--- " + book.get());
        } else {
            booksRepository.save(createBook(path));
        }
    }

    private Book createBook(Path path) {
        String title = getTitle(path);
        Integer pages = countPages(path);
        Integer year = getYear(path);

        return new Book(title, year, pages);
    }

    private static Integer getYear(Path a) {
        String name = a.toString();
        String nameWithoutExtension = name.substring(name.length() - 8, name.length() - 4);
        return Integer.valueOf(nameWithoutExtension);
    }

    private static Integer countPages(Path fileName) {
        String nameWithoutExtension = getTitle(fileName);
        try (PDDocument doc = PDDocument.load(fileName.toFile())) {
            int pages = doc.getNumberOfPages();
            System.out.print(".");
            return pages;
        } catch (IOException ioe) {
            System.out.println(nameWithoutExtension + ": -1");
            return -1;
        }
    }

    private static String getTitle(Path fileName) {
        return com.google.common.io.Files.getNameWithoutExtension(fileName.getFileName().toString());
    }
}
