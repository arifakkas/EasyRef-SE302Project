package EasyRef ;
import javafx.beans.property.SimpleStringProperty;

public class inProceedings extends Reference {
    private SimpleStringProperty publisher;
    private SimpleStringProperty volume;
    private SimpleStringProperty series;
    private SimpleStringProperty address;
    private SimpleStringProperty edition;
    private SimpleStringProperty bookTitle;
    private SimpleStringProperty editor;
    private SimpleStringProperty chapter;
    private SimpleStringProperty pages;
    private SimpleStringProperty organization;
    private SimpleStringProperty note;

    public inProceedings(String type, String key, String title, String author, String year, String month, String publisher, String volume, String series, String address, String edition, String pages, String bookTitle, String organization, String editor, String chapter, String note) {
        super(type, key, title, author, year, month);
        this.publisher = new SimpleStringProperty(publisher);
        this.volume = new SimpleStringProperty(volume);
        this.series = new SimpleStringProperty(series);
        this.address = new SimpleStringProperty(address);
        this.edition = new SimpleStringProperty(edition);
        this.pages = new SimpleStringProperty(pages);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.organization = new SimpleStringProperty(organization);
        this.editor = new SimpleStringProperty(editor);
        this.chapter = new SimpleStringProperty(chapter);
        this.note = new SimpleStringProperty(note);
    }

    public String getNote() {
        return note.get();
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public String getPublisher() {

        return publisher.get();
    }

    public SimpleStringProperty publisherProperty() {

        return publisher;
    }

    public void setPublisher(String publisher) {

        this.publisher.set(publisher);
    }

    public String getVolume() {

        return volume.get();
    }

    public SimpleStringProperty volumeProperty() {

        return volume;
    }

    public void setVolume(String volume) {

        this.volume.set(volume);
    }

    public String getSeries() {

        return series.get();
    }

    public SimpleStringProperty seriesProperty() {

        return series;
    }

    public void setSeries(String series) {

        this.series.set(series);
    }

    public String getAddress() {

        return address.get();
    }

    public SimpleStringProperty addressProperty() {

        return address;
    }

    public void setAddress(String address) {

        this.address.set(address);
    }

    public String getEdition() {

        return edition.get();
    }

    public SimpleStringProperty editionProperty() {

        return edition;
    }

    public void setEdition(String edition) {

        this.edition.set(edition);
    }

    public String getPages() {

        return pages.get();
    }

    public SimpleStringProperty pagesProperty() {

        return pages;
    }

    public void setPages(String pages) {

        this.pages.set(pages);
    }

    public String getBookTitle() {

        return bookTitle.get();
    }

    public SimpleStringProperty bookTitleProperty() {

        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {

        this.bookTitle.set(bookTitle);
    }

    public String getEditor() {

        return editor.get();
    }

    public SimpleStringProperty editorProperty() {

        return editor;
    }

    public void setChapter(String chapter) {

        this.chapter.set(chapter);
    }

    public String getChapter() {

        return chapter.get();
    }

    public SimpleStringProperty chapterProperty() {

        return chapter;
    }

    public void setEditor(String editor) {
        this.editor.set(editor);
    }

    public String getOrganization() {

        return organization.get();
    }

    public SimpleStringProperty organizationProperty() {

        return organization;
    }

    public void setOrganization(String organization) {

        this.organization.set(organization);
    }

    public String toString() {

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getPublisher().equals("")) {

            printer = printer + " | Publisher: " + getPublisher();
        }
        if (!getVolume().equals("")) {

            printer = printer + " | Volume: " + getVolume();
        }
        if (!getSeries().equals("")) {

            printer = printer + " | Series: " + getSeries();
        }
        if (!getAddress().equals("")) {

            printer = printer + " | Address: " + getAddress();
        }
        if (!getEdition().equals("")) {

            printer = printer + " | Edition: " + getEdition();
        }
        if (!getPages().equals("")) {

            printer = printer + " | Pages: " + getPages();
        }
        if (!getBookTitle().equals("")) {

            printer = printer + " | BookTitle: " + getBookTitle();
        }
        if (!getOrganization().equals("")) {

            printer = printer + " | Organization: " + getOrganization();
        }
        if (!getEditor().equals("")) {
            printer = printer + " | Editor: " + getEditor();
        }

        if (!getChapter().equals("")) {
            printer = printer + " | Chapter: " + getChapter();
        }
        if (!getNote().equals("")){
            printer = printer + " | Note: " + getNote();
        }

        return printer;
    }
}
