package EasyRef;
import javafx.beans.property.SimpleStringProperty;

public class unPublished extends Reference {

	public unPublished(String type, String key, String title, String author, String year, String month){
		super(type, key, title, author, year, month);
	}


	public String toString(){
		String printer = "Type: " + getType() + " | Author: " + getAuthor() + " | Title: " + getTitle() + " | Year: " + getYear() + " | Month: " + getMonth() + " | Key: " + getKey();

		return printer;
	}
}