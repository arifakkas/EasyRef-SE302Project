package EasyRef ;
import javafx.beans.property.SimpleStringProperty;

public class Online extends Reference {
    private SimpleStringProperty url;

    public Online(String type, String key, String title, String author, String year, String month, String url){

        super(type, key, title, author, year, month);
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

        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getUrl().equals("")) {

            printer = printer + " | Url: " + getUrl();
        }
        return printer;
    }
}
