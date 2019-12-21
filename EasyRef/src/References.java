import java.util.Random;

public class References {

	private String name, title, type;
	private int idNum, year;
	Random rd = new Random();
	
	public References(String name, String title, String type, int year){
		setName(name);
		setTitle(title);
		setType(type);
		setYear(year);
		setIdNum(rd.nextInt(Integer.MAX_VALUE));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIdNum() {
		return idNum;
	}

	public void setIdNum(int idNum) {
		this.idNum = idNum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	
}
