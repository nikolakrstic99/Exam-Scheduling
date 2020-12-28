import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Sala {
	private String name;
	private int cap;
	private boolean computers;
	private int teachers;
	private int inETF;

	public String getName() {
		return name;
	}

	public int getCap() {
		return cap;
	}

	public int getTeachers() {
		return teachers;
	}

	public boolean isComputers() {
		return computers;
	}

	public int isInETF() {
		return inETF;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public void setTeachers(int teachers) {
		this.teachers = teachers;
	}

	public void setComputers(boolean computers) {
		this.computers = computers;
	}

	public void setInETF(int inETF) {
		this.inETF = inETF;
	}

	public Sala() {

	}

	public double grade() {
		if (inETF == 1)
			return teachers;
		else
			return teachers + 1.2;
	}

	public Sala(String name, int cap, int teachers, boolean computers, int inETF) {
		super();
		this.name = name;
		this.cap = cap;
		this.teachers = teachers;
		this.computers = computers;
		this.inETF = inETF;
	}

	public static ArrayList<Sala> readRoom(String filePath) {
		ArrayList<Sala> rooms = new ArrayList<Sala>();
		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray j = new JSONArray(content);
		for (int i = 0; i < j.length(); i++) {
			JSONObject o = j.getJSONObject(i);
			Sala s = new Sala();
			s.setCap(o.getInt("kapacitet"));
			s.setComputers(o.getInt("racunari") == 1 ? true : false);
			s.setInETF(o.getInt("etf"));
			s.setName(o.getString("naziv"));
			s.setTeachers(o.getInt("dezurni"));
			rooms.add(s);
		}
		return rooms;
	}
}
