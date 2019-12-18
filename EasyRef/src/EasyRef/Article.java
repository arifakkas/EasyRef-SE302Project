package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Article extends Reference {

    private SimpleStringProperty volume, number, pages;

    public Article(String type, String key, String title, String author, String year, String month, String volume, String number, String pages) {
        super(type, key, title, author, year, month);
        this.volume = new SimpleStringProperty(volume);
        this.number = new SimpleStringProperty(number);
        this.pages = new SimpleStringProperty(pages);
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

    public String getNumber() {
        return number.get();
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public void setNumber(String number) {
        this.number.set(number);
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

    public String toString(){

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getVolume().equals("null")){

            printer = printer + " | Volume: " + getVolume();
        }
        if (!getNumber().equals("null")){

            printer = printer + " | Number: " + getVolume();
        }
        if (!getPages().equals("null")){

            printer = printer + " | Pages: " + getVolume();
        }

        return printer;
    }
}