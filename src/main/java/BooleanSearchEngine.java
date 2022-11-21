import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> answer = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        List<File> listPdf = List.of(Objects.requireNonNull(pdfsDir.listFiles())); // список всех PDF-файлов

        for (File pdf : listPdf) {

            var doc = new PdfDocument(new PdfReader(pdf)); // создаем объект пдф-документа

            for (int i = 0; i < doc.getNumberOfPages(); i++) {

                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i + 1)); // получаем текст со страницы
                var words = text.split("\\P{IsAlphabetic}+"); // разбиваем текст на слова

                //подсчёта частоты слов
                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота

                for (var word : words) {// перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                int count;
                for (var word : freqs.keySet()) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    count = freqs.get(word);
                    List<PageEntry> listPageEntry = new ArrayList<>();
                    listPageEntry.add(new PageEntry(pdf.getName(), i + 1, count));

                    if (answer.containsKey(word)) {
                        answer.get(word).add(new PageEntry(pdf.getName(), i + 1, count));
                    } else {
                        answer.put(word, listPageEntry);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> searchList = new ArrayList<>();
        for (PageEntry pageEntry : answer.get(word)) {
            if (answer.containsKey(word.toLowerCase())) {
                searchList.add(pageEntry);
            }
        }
        Collections.sort(searchList);
        return searchList;
    }
}