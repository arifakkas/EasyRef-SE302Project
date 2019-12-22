package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Conference  extends Reference{
    private SimpleStringProperty publisher, volume, series,  address, edition, pages, bookTitle, organization;

    public Conference(String type, String key, String title, String author, String year, String month, String publisher, String volume, String series, String address, String edition, String pages, String bookTitle, String organization) {
        super(type, key, title, author, year, month);
        this.publisher = new SimpleStringProperty(publisher);
        this.volume = new SimpleStringProperty(volume);
        this.series = new SimpleStringProperty(series);
        this.address = new SimpleStringProperty(address);
        this.edition = new SimpleStringProperty(edition);
        this.pages = new SimpleStringProperty(pages);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.organization = new SimpleStringProperty(organization);
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

    public String getOrganization() {
        return organization.get();
    }

    public SimpleStringProperty organizationProperty() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization.set(organization);
    }

    public String toString(){

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getPublisher().equals("")){

            printer = printer + " | Publisher: " + getPublisher();
        }
        if (!getVolume().equals("")){

            printer = printer + " | Volume: " + getVolume();
        }
        if (!getSeries().equals("")){

            printer = printer + " | Series: " + getSeries();
        }
        if (!getAddress().equals("")){

            printer = printer + " | Address: " + getAddress();
        }
        if (!getEdition().equals("")){

            printer = printer + " | Edition: " + getEdition();
        }
        if (!getPages().equals("")){

            printer = printer + " | Pages: " + getPages();
        }
        if (!getBookTitle().equals("")){

            printer = printer + " | BookTitle: " + getBookTitle();
        }
        if (!getOrganization().equals("")){

            printer = printer + " | Organization: " + getOrganization();
        }
        return printer;
}
}
