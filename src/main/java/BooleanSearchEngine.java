import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    Map<String, List<PageEntry>> wordsMap = new HashMap<>();
    List<PageEntry> list;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        List<File> listPdf = List.of(pdfsDir.listFiles());
        for (File pdf : listPdf) {
            var doc = new PdfDocument(new PdfReader(pdf));
            String pdfName = pdf.getName();
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                PdfPage page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (Map.Entry<String, Integer> kv : freqs.entrySet()) {
                    PageEntry pageEntry = new PageEntry(pdfName, i, kv.getValue());
                    if (!wordsMap.containsKey(kv.getKey())) {
                        list = new ArrayList<>();
                        list.add(pageEntry);
                        wordsMap.put(kv.getKey(), list);
                    } else {
                        list = wordsMap.get(kv.getKey());
                        list.add(pageEntry);
                        wordsMap.put(kv.getKey(), list);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> result = new ArrayList<>();
        if (wordsMap.containsKey(word)) {
            result = wordsMap.get(word);
            Collections.sort(result);
            return result;
        }
        return result;
    }

}

