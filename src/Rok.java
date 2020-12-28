import java.util.*;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Rok {
	private int length;
	LinkedList<Ispit> exams = new LinkedList<Ispit>();

	public Rok(int len) {
		length = len;
	}

	public Rok(String filePath) {
		read(filePath);
	}

	public void read(String filePath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String line = null, message = new String();
		try {
			while ((line = br.readLine()) != null) {
				message += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject jsonRok = new JSONObject(message);

		JSONArray ispiti = jsonRok.getJSONArray("ispiti");
		length = jsonRok.getInt("trajanje_u_danima");

		for (int i = 0; i < ispiti.length(); i++) {
			JSONObject o = ispiti.getJSONObject(i);
			Ispit exam = new Ispit();
			exam.setPassword(o.getString("sifra"));
			exam.setNumOfStud(o.getInt("prijavljeni"));

			exam.setOnComputers(o.getInt("racunari") == 1 ? true : false);
			JSONArray courses = o.getJSONArray("odseci");
			for (int j = 0; j < courses.length(); j++) {
				exam.addCourse(courses.getString(j));
			}
			exams.add(exam);
		}
	}

	public void addExam(Ispit e) {
		exams.add(e);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public LinkedList<Ispit> getExams() {
		return exams;
	}

	public void setExams(LinkedList<Ispit> exams) {
		this.exams = exams;
	}

}
