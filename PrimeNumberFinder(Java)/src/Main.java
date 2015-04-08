
public class Main {

	public static void main(String[] args) {
		MultiThreadPrimeNumberFinder finder = new MultiThreadPrimeNumberFinder("src/Prime Number.txt", false);
		System.out.println(finder.findPrimesUnder(9));
	}

}
