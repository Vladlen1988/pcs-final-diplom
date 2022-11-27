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

                for (var word : freqs.keySet()) {
                    if (word.isEmpty()) {
                        continue;
                    }

                    List<PageEntry> listPageEntry = new ArrayList<>();                   

                    if (answer.containsKey(word)) {
                        answer.get(word).add(new PageEntry(pdf.getName(), i + 1, freqs.get(word)));
                    } else {
                        listPageEntry.add(new PageEntry(pdf.getName(), i + 1, freqs.get(word)));
                        answer.put(word, listPageEntry);
                    }
                    answer.get(word).sort(Collections.reverseOrder());
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return answer.get(word.toLowerCase());
    }
}
