import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PagesCounter {

    public static void main(String[] args) throws IOException {
        Path folder = Paths.get(args[0]);
        Map<Integer, List<Path>> booksOrdered = Files
                .list(folder)
                .filter(f -> f.toString().endsWith("pdf"))
                .collect(Collectors.groupingBy(PagesCounter::getYear, toSortedList(Comparator.comparing(PagesCounter::count))));

        booksOrdered.forEach((k, v) -> {
            System.out.println(k);
            v.forEach(b -> System.out.println(" " + b));
        });
    }

    private static Integer getYear(Path a) {
        String name = a.toString();
        String nameWithoutExtension = name.substring(name.length() - 8, name.length() - 4);
        return Integer.valueOf(nameWithoutExtension);
    }

    private static <T> Collector<T, ?, List<T>> toSortedList(Comparator<? super T> c) {
        return Collectors.collectingAndThen(
                Collectors.toCollection(ArrayList::new), l -> {
                    l.sort(c);
                    return l;
                });
    }

    private static Integer count(Path fileName) {
        String nameWithoutExtension = com.google.common.io.Files.getNameWithoutExtension(fileName.getFileName().toString());
        try (PDDocument doc = PDDocument.load(fileName.toFile())) {
            int pages = doc.getNumberOfPages();
            System.out.println(nameWithoutExtension + ": " + pages);
            return pages;
        } catch (IOException ioe) {
            System.out.println(nameWithoutExtension + ": -1");
            return -1;
        }
    }
}
