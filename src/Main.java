import java.util.*;

public class Main {

	public static void main(String[] args) {

		String num = "5.json";
		String filePathRok = "rok" + num;
		String filePathRoom = "sale" + num;
		Rok rok = new Rok(filePathRok);
		ArrayList<Sala> sale = Sala.readRoom(filePathRoom);

		Raspored r = new Raspored(sale, rok);
		r.make();
		r.write();

		System.out.println("kraj");
	}
}
