package EasyRef ;
import javafx.beans.property.SimpleStringProperty;

import java.lang.ref.Reference;

public class Online extends Reference {
    private SimpleStringProperty url;

    public Online(String key, String title, String author, int year, String month, String url){

        super(key, title, author, year, month);
        this.url = new SimpleStringProperty(url);
    }

    public String getUrl() {

        return url.get();
    }

    public SimpleStringProperty urlProperty() {

        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String toString() {

        String printer = "Note: " + getNote() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getUrl().equals("null")) {

            printer = printer + " | Url: " + getUrl();
        }
        return printer;
    }
}
