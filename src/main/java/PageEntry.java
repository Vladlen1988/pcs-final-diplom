public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "\nPageEntry {\n " +
                "pdfName = " + pdfName +
                ",\n page = " + page +
                ",\n count = " + count +
                "\n}";
    }

    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(o.count, count);
    }
}