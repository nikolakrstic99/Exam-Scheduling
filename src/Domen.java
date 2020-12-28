import java.util.*;

public class Domen {
	private ArrayList<SlobodanTermin> slobodniTermini = new ArrayList<>(); // 1.dimenzija redni broj dana,2.dimenzija
																			// termin 8:00 || 11:30
	// || 15:00
	// ||
	// 18:30
	private ArrayList<Sala> sale = new ArrayList<Sala>();
	private final static int BROJ_TERMINA = 4;
	private Ispit exam;
	private int days;

	public Domen(Domen d) {
		this.sale = d.getSale();
		this.exam = d.getIspit();
		this.days = d.getDays();
		for (SlobodanTermin st : d.getSlobodniTermini()) {
			slobodniTermini.add(new SlobodanTermin(st.getSala(), st.getTermin(), st.getDan()));
		}
	}

	public Domen(ArrayList<Sala> sale, int numOfDays, Ispit exam) {
		this.sale = sale;
		this.exam = exam;
		this.days = numOfDays;
		for (int i = 0; i < numOfDays; i++) {
			for (int j = 0; j < BROJ_TERMINA; j++) {
				for (Sala s : sale) {
					slobodniTermini.add(new SlobodanTermin(s, j, i));
				}
			}
		}
	}

	public Ispit getIspit() {
		return exam;
	}

	public int getDays() {
		return days;
	}

	public ArrayList<Sala> getSale() {
		return sale;
	}

	public ArrayList<SlobodanTermin> getSlobodniTermini() {
		return slobodniTermini;
	}

	public void setSlobodniTermini(ArrayList<SlobodanTermin> slobodniTermini) {
		this.slobodniTermini = slobodniTermini;
	}

	public void deleteNotComputerRooms() {
		for (SlobodanTermin st : new ArrayList<>(slobodniTermini))
			if (!st.getSala().isComputers())
				slobodniTermini.remove(st);
	}

	public ArrayList<SlobodanTermin> findBestRooms() {
		return find(exam.getNumOfStud(), slobodniTermini);
	}

	private ArrayList<SlobodanTermin> find(int rest, ArrayList<SlobodanTermin> freeAppointment) {
		if (rest <= 0)
			return new ArrayList<SlobodanTermin>();

		if (freeAppointment.isEmpty())
			return null;

		ArrayList<SlobodanTermin> bestRooms = null;
		int bestGrade = -1;

		for (int i = 0; i < freeAppointment.size(); i++) {
			ArrayList<SlobodanTermin> choosen = new ArrayList<SlobodanTermin>();
			SlobodanTermin st = freeAppointment.remove(i);
			choosen.add(st);

			ArrayList<SlobodanTermin> restOfFreeAppointments = new ArrayList<SlobodanTermin>();
			for (int j = 0; j < freeAppointment.size(); j++) {
				SlobodanTermin a = freeAppointment.get(j);
				if (a.getDan() == st.getDan() && a.getTermin() == st.getTermin())
					restOfFreeAppointments.add(a);
			}

			List<SlobodanTermin> restAppointment = find(rest - st.getSala().getCap(), restOfFreeAppointments);

			if (restAppointment == null) {
				freeAppointment.add(i, st);
				continue;
			}

			choosen.addAll(restAppointment);

			int grade = 0;
			for (SlobodanTermin q : choosen)
				grade += q.getSala().grade();

			if (bestGrade == -1 || grade < bestGrade) {
				bestGrade = grade;
				bestRooms = choosen;
			}
			freeAppointment.add(i, st);
		}

		return bestRooms;
	}

	public void deleteAppointments(List<SlobodanTermin> termini) {
		slobodniTermini.removeAll(termini);
	}
}
