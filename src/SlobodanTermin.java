
public class SlobodanTermin {
	private Sala sala;
	private int termin, dan;

	public SlobodanTermin(Sala sala, int termin, int dan) {
		super();
		this.sala = sala;
		this.termin = termin;
		this.dan = dan;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public int getTermin() {
		return termin;
	}

	public void setTermin(int termin) {
		this.termin = termin;
	}

	public int getDan() {
		return dan;
	}

	public void setDan(int dan) {
		this.dan = dan;
	}

	public String getTerminCas() {
		switch (termin) {
		case (0):
			return "08:00";
		case (1):
			return "11:30";
		case (2):
			return "15:00";
		case (3):
			return "18:30";
		}
		return null;
	}

}
