import java.util.ArrayList;

public class Ispit {
	private String password;
	private int numOfStud;
	private boolean onComputers;
	private int year;
	private ArrayList<String> course = new ArrayList<String>();

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setPassword(String password) {
		this.password = password;
		year = Character.getNumericValue(password.charAt(5));
	}

	public void setCourse(ArrayList<String> course) {
		this.course = course;
	}

	public void addCourse(String s) {
		course.add(s);
	}

	public void setNumOfStud(int numOfStud) {
		this.numOfStud = numOfStud;
	}

	public void setOnComputers(boolean onComputers) {
		this.onComputers = onComputers;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<String> getCourse() {
		return course;
	}

	public int getNumOfStud() {
		return numOfStud;
	}

	public boolean isOnComputers() {
		return onComputers;
	}

	public Ispit(String password, ArrayList<String> course, int numOfStud, boolean onComputers) {
		super();
		this.password = password;
		year = Character.getNumericValue(password.charAt(5));
		this.course = course;
		this.numOfStud = numOfStud;
		this.onComputers = onComputers;
	}

	public Ispit(Ispit i) {
		this(i.getPassword(),i.getCourse(),i.getNumOfStud(),i.isOnComputers());
	}
	
	public Ispit() {
		
	}

}
