import java.io.*;
import java.util.*;

public class Raspored {
	Ispit[][][] map; // 1.dimenzija redni broj dana,2.dimenzija termin ,3.sala
	// 18:30
	private ArrayList<Sala> sale = new ArrayList<>();
	private Rok rok;
	private int numOfDays;
	private final static int BROJ_TERMINA = 4;

	private FileWriter steps;

	public Raspored(ArrayList<Sala> sale, Rok rok) {
		this.numOfDays = rok.getLength();
		this.rok = rok;
		this.sale = sale;
		int i=sale.size();
		map = new Ispit[numOfDays][BROJ_TERMINA][i];
	}

	public boolean make() {
		try {
			steps = new FileWriter("steps.txt");

			HashMap<Ispit, Domen> mapExamDomen = new HashMap<>();

			for (Ispit i : rok.getExams()) {
				mapExamDomen.put(i, new Domen(sale, rok.getLength(), i));
			}
			updateDomenForComputersExams(mapExamDomen);

			boolean ret = backtrack(mapExamDomen);

			steps.close();
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private boolean backtrack(HashMap<Ispit, Domen> mapExamDomen) {
		if (mapExamDomen.size() == 0)
			return true;

		Ispit i = mostConstrainedExam(mapExamDomen);
		Domen d = mapExamDomen.remove(i);
		ArrayList<SlobodanTermin> orgTermini = new ArrayList<SlobodanTermin>(d.getSlobodniTermini());

		while (true) {
			ArrayList<SlobodanTermin> termini = d.findBestRooms();

			if (termini == null) {
				d.setSlobodniTermini(orgTermini); // ?? nismo ni uzimali termine iz domena
				mapExamDomen.put(i, d);
				return false;
			}

			d.deleteAppointments(termini);
			HashMap<Ispit, Domen> oldMapExamDomen = new HashMap<>();
			oldMapExamDomen = mapExamDomen;
			mapExamDomen = forwardChecking(mapExamDomen, i, termini);

			writeSteps(i, termini);

			if (backtrack(mapExamDomen)) {
				termini.forEach(t -> {
					map[t.getDan()][t.getTermin()][sale.indexOf(t.getSala())] = i;
				});
				return true;
			}
			mapExamDomen = oldMapExamDomen;
		}
	}

	private Ispit mostConstrainedExam(HashMap<Ispit, Domen> map) {
		Ispit max = null;
		for (Map.Entry<Ispit, Domen> entry : map.entrySet()) {
			if (max == null)
				max = entry.getKey();
			else if (max.isOnComputers()) {
				if (entry.getKey().isOnComputers() && entry.getKey().getNumOfStud() > max.getNumOfStud())
					max = entry.getKey();
			} else {
				if (entry.getKey().isOnComputers())
					max = entry.getKey();
				else if (entry.getKey().getNumOfStud() > max.getNumOfStud())
					max = entry.getKey();
			}
		}
		return max;
	}

	private void writeSteps(Ispit i, ArrayList<SlobodanTermin> termini) {
		try {
			steps.write("Termin ispita " + i.getPassword() + " je " + termini.get(0).getDan() + 1 + ".dana u "
					+ termini.get(0).getTerminCas() + " sati\n");

			steps.write("Dobio je salu/e:\n");
			for (SlobodanTermin st : termini)
				steps.write("Sala: " + st.getSala().getName() + "\n");
			steps.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HashMap<Ispit, Domen> forwardChecking(HashMap<Ispit, Domen> map, Ispit i, ArrayList<SlobodanTermin> list) {
		/*
		 * list-lista novo zauzetih termina za ispit i map domeni svih ispita
		 */
		HashMap<Ispit, Domen> retMap = copy(map);
		int day = list.get(0).getDan();
		int termin = list.get(0).getTermin();

		for (SlobodanTermin s : list) {
			for (Map.Entry<Ispit, Domen> entry : retMap.entrySet()) {
				if (entry.getKey() != i) {
					Domen d = entry.getValue();

					for (SlobodanTermin st : new ArrayList<>(d.getSlobodniTermini())) { /// iterator kroz kopiju liste
																						/// brisemo iz prave
						if (st.getDan() == day && st.getTermin() == termin
								&& st.getSala().getName() == s.getSala().getName()) {
							d.getSlobodniTermini().remove(st);
							continue;
						}
						if (st.getDan() == day && entry.getKey().getYear() == i.getYear() // isti dan i godina
								&& intersect(entry.getKey().getCourse(), i.getCourse())) {// isti odsek

							d.getSlobodniTermini().remove(st);// ne moze na istom odseku u istom danu biti 2 ispita
							continue;
						}

						if (day == st.getDan() && termin == st.getTermin() // isti dan i termin
								&& intersect(entry.getKey().getCourse(), i.getCourse()) && // isti odsek
								Math.abs(entry.getKey().getYear() - i.getYear()) == 1) { // razlika u godinama za 1

							d.getSlobodniTermini().remove(st);// ne moze na 2 susedne godine u istom terminu istog dana
							continue;// // biti 2 ispita
						}
					}
				}

			}
		}
		return retMap;
	}

	public static HashMap<Ispit, Domen> copy(HashMap<Ispit, Domen> original) {
		HashMap<Ispit, Domen> copy = new HashMap<Ispit, Domen>();
		for (Map.Entry<Ispit, Domen> entry : original.entrySet()) {
			copy.put(new Ispit(entry.getKey()), new Domen(entry.getValue()));
		}

		return copy;
	}

	private boolean intersect(ArrayList<String> A, ArrayList<String> B) {
		for (String dto : A) {
			if (B.contains(dto)) {
				return true;
			}
		}
		return false;
	}

	public void updateDomenForComputersExams(HashMap<Ispit, Domen> mapExamDomen) {
		for (Ispit i : rok.getExams()) {
			if (i.isOnComputers()) // check for computers
				mapExamDomen.get(i).deleteNotComputerRooms();
		}
	}

	public void write() {
		try {
			FileWriter csvWriter = new FileWriter("new.csv");

			for (int i = 0; i < numOfDays; i++) {
				csvWriter.append("Dan " + (i + 1));
				csvWriter.append(",");
				for (Sala s : sale) {
					csvWriter.append(s.getName());
					csvWriter.append(",");
				}
				csvWriter.append("\n");
				writeAppointment(i, 0, csvWriter);
				writeAppointment(i, 1, csvWriter);
				writeAppointment(i, 2, csvWriter);
				writeAppointment(i, 3, csvWriter);

				csvWriter.append("\n");// prazan red uzmedju dana
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeAppointment(int day, int app, FileWriter csvWriter) {
		try {
			if (app == 0)
				csvWriter.append("08:00");
			else if (app == 1)
				csvWriter.append("11:30");
			else if (app == 2)
				csvWriter.append("15:00");
			else
				csvWriter.append("18:30");
			csvWriter.append(",");

			for (int l = 0; l < sale.size(); l++) {
				if (map[day][app][l] != null)
					csvWriter.append(map[day][app][l].getPassword());
				else
					csvWriter.append("X");
				csvWriter.append(",");
			}

			csvWriter.append("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
