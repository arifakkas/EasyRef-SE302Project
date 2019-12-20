package EasyRef;

import javafx.beans.property.SimpleStringProperty;

public class Unpublished extends Reference {

    private SimpleStringProperty month;

    public Unpublished(String type, String key, String author, String year, String month, String title){
        super(type, key, title, author, year, month);
        this.month = new SimpleStringProperty(month);
    }

    public String getMonth() {
        return month.get();
    }

    public SimpleStringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public String toString(){
        String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

        if (!getMonth().equals("null")) {
            printer = printer + " | month" + getMonth();
        }
        return printer;
    }
}