
public class Main {

	public static void main(String[] args) {
		PrimeNumberFinder finder = new PrimeNumberFinder("src/Prime Number.txt", false);
		System.out.println(finder.findPrimesUnder(5));
	}

}
