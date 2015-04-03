import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * finds prime numbers
 * 
 * @author rosspa. Created Jun 18, 2014.
 */
public class MultiThreadPrimeNumberFinder {
//	private static long numberToLookTo;
	private ArrayList<Long> primeNumbers;
	private File primeNumbersFile;
	private int counter;
	private boolean findNewNumbers;
	private boolean doStats;
	private static JLabel label;

	public MultiThreadPrimeNumberFinder(String filePath, Boolean doStats) {
		this.primeNumbersFile = new File(filePath);
		this.doStats = doStats;
//		 NUMBER_TO_LOOK_TO = Integer.MAX_VALUE;
//		NUMBER_TO_LOOK_TO = 1_000_000_000L;
//		 numberToLookTo = 7823;
		// NUMBER_TO_LOOK_TO = (long) Math.pow(2,31);
	}
	
	public ArrayList<Long> findPrimesUnder(long numberToLookTo) {
		if (numberToLookTo <= 2) {
			System.out.println("There are no prime numbers under " + numberToLookTo);
			return new ArrayList<Long>();
		}
		System.out.printf("finding all primes under %,d \n", numberToLookTo);

		long startLoad = System.nanoTime();
		JFrame frame = new JFrame();
		frame.setSize(200, 100);
		frame.add(label = new JLabel("Loading Prime Numbers", SwingConstants.CENTER));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		double speed = loadPrimeList(numberToLookTo);
		label.setText("0.000 %");

		long startingNumb = this.primeNumbers.get(this.counter - 1);
//		System.out.println(startingNumb + "*");
		long percentMax = numberToLookTo - startingNumb;
		long start = System.nanoTime();
		long start1 = System.nanoTime();
		int counter;
		switch ((int) startingNumb % 10) {
		case 1:
			counter = 4;
			break;
		case 3:
			counter = 5;
			break;
		case 5:
			counter = 1;
			break;
		case 7:
			counter = 2;
			break;
		case 9:
			counter = 3;
			break;
		default: 
			counter = 6;
		}
		for (long number = startingNumb + 2; number < numberToLookTo; number += 2,counter++) {
			System.out.println("number = " + number + ", counter = " + counter);
			if (counter !=5) {
				if (isPrime(number)) {
					this.primeNumbers.add(number);
					this.findNewNumbers = true;
					if ((System.nanoTime() - start1) > 8_333_333) {
						label.setText(String.format("%.3f %%", 100
								* (number - startingNumb) / (double) (percentMax)));
						start1 = System.nanoTime();
					}
				}
			} else {
				counter = 0;
			}
		}
		long start2 = System.nanoTime();
		label.setText("Saving, Please Wait");
		overwrite();
		long end = System.nanoTime();
		if (this.primeNumbers.size() < 1000) {
			System.out.println("prime numbers below");
			for (long number : this.primeNumbers) {
				System.out.println(number);
			}
			System.out.println();
			System.out.println("DONE");
		} else {
			System.out.println();
			System.out.println("DONE");
			System.out.println("Too many prime numbers to print out");
		}
		System.out.printf("Found %,d prime numbers from 2 through %,d\n",
				this.primeNumbers.size(), numberToLookTo - 1);
		System.out.printf(
				"Aproxemently %f%% of numbers between 2 and %,d are prime\n", 100
						* (long) (this.primeNumbers.size())
						/ (double) (numberToLookTo - 2), numberToLookTo);
		System.out.println();
		long start3 = System.nanoTime();
		if (this.doStats) {
			label.setText("Loading Stats, Please Wait");
			// double primeMean = this.getMean();
			// double primeVar = this.getVariance(primeMean);
			// double primeSD = Math.sqrt(primeVar);
			int[] distances = this.getDistances();
			double distanceMean = this.getMean(distances);
			double distanceVar = this.getVariance(distances, distanceMean);
			double distanceSD = Math.sqrt(distanceVar);
			// System.out.println(primeMean + " is the Mean Prime Number");
			// System.out.println(primeVar +
			// " is the Variance of Prime Numbers");
			// System.out.println(primeSD +
			// " is the Standard Deviation of Prime Numbers");
			System.out.println(distanceMean
					+ " is the Mean Distance between Prime Numbers");
			System.out.println(distanceVar
					+ " is the Variance in Distance between Prime Numbers");
			System.out.println(distanceSD
					+ " is the Standard Devation in Distance between Prime Numbers");
	
			System.out.println();
		}
		long end1 = System.nanoTime();
		System.out.println("Took " + getTimeUnits(end + end1 - start3 - startLoad)
				+ " total");
		System.out.println("Took " + getTimeUnits(System.nanoTime() - startLoad) + " to load");
		System.out.println("Took " + getTimeUnits(start2 - start) + " to solve");
		System.out.println("Took " + getTimeUnits(end - start2) + " to save");
		if (this.doStats) {
			System.out.println("Took " + getTimeUnits(end1 - start3) + " to load stats");
		}
		System.out.printf("Loaded %,.3f lines per second\n", speed);

		frame.dispose();
		return primeNumbers;
	}

	/**
	 * returns a string that gives the given time difference in easily read time
	 * units
	 * 
	 * @param time
	 * @return
	 */
	private String getTimeUnits(long time) {
		double newTime = time;
		if (time < 1000) {
			return String.format("%d NanoSeconds", time);
		} else {
			newTime = time / 1000.0;
			if (newTime < 1000) {
				return String.format("%f MicroSeconds", newTime);
			} else {
				newTime /= 1000.0;
				if (newTime < 1000) {
					return String.format("%f MiliSeconds", newTime);
				} else {
					newTime /= 1000.0;
					if (newTime < 300) {
						return String.format("%f Seconds", newTime);
					} else {
						newTime /= 60.0;
						if (newTime < 180) {
							return String.format("%f Minutes", newTime);
						} else {
							return String.format("%f Hours", newTime / 60.0);
						}
					}
				}
			}
		}
	}

	/**
	 * checks to see if number is prime
	 * 
	 * @param number
	 * @return
	 */
	private boolean isPrime(long number) {
		int k = 0;
		long prime;
		double cap = Math.sqrt(number);
		while ((prime = this.primeNumbers.get(k)) <= cap) {
			// System.out.println(prime);
			k++;
			if (number % prime == 0) {
				return false;
			}
		}
		return true;
	}

//	private double loadsPrimeList() {
//		long end = -1;
//		long start = 0;
//		this.counter = 0;
////		try {
////			this.primeNumbers = Files.readAllLines(Paths.get("src/Prime Number.txt"), Charset.forName("US-ASCII"));
////		} catch (IOException exception) {
////			// TODO Auto-generated catch-block stub.
////			exception.printStackTrace();
////		}
//		return this.counter / ((end - start) / 1_000_000_000.0);
//	}

	private double loadPrimeList(long numberToLoadTo) {
		this.primeNumbers = new ArrayList<Long>((int) (numberToLoadTo / 10));
		this.primeNumbers.ensureCapacity((int) (numberToLoadTo / 10));
		BufferedReader reader = null;
		long start = 0;
		long end = -1;
		// try to read prime number file
		try {
			start = System.nanoTime();
			reader = new BufferedReader(new FileReader(this.primeNumbersFile));
			String number;
			this.counter = 0;
			while ((number = reader.readLine()) != null) {
				long numb = Long.parseLong(number);
				if (numb >= numberToLoadTo) {
					break;
				}
				this.counter++;
				// parses the line to a number if possible, if not, throws
				// an error
				this.primeNumbers.add(numb);
			}
			end = System.nanoTime();
			this.primeNumbers.trimToSize();
		} catch (FileNotFoundException e) {
			try {
				// if no file, create a new one and try again
				newFile();
				loadPrimeList(numberToLoadTo);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this.counter / ((end - start) / 1_000_000_000.0);
	}

	/**
	 * overwrites the Prime Number text file
	 * 
	 */
	private void overwrite() {
		if (this.findNewNumbers) {
			try {
				PrintWriter writer = new PrintWriter(this.primeNumbersFile);
				try {
					for (long number : this.primeNumbers) {
						writer.println(number);
					}
				} finally {
 					writer.close();
				}
			} catch (IOException e) {
				String msg = "Unable to overwrite Prime Number file: " + e.getMessage();
				System.out.println(msg);
				e.printStackTrace();
			}
		}
	}

	private void newFile() throws IOException {
		this.primeNumbersFile.createNewFile();
		this.primeNumbers.add(2L);
		this.primeNumbers.add(3L);
		this.primeNumbers.add(5L);
		try {
			PrintWriter writer = new PrintWriter(this.primeNumbersFile);
			try {
				for (int k = 0; k < this.primeNumbers.size(); k++) {
					writer.println(this.primeNumbers.get(k));
				}
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			System.out.println("error creating new file");
			e.printStackTrace();
		}
	}

	private int[] getDistances() {
		int[] list = new int[this.primeNumbers.size() - 1];
		int numb = this.primeNumbers.size() - 1;
		long smaller = this.primeNumbers.get(numb);
		long bigger;
		for (int k = 0; k < numb; k++) {
			// System.out.println(k);
			// list[k] = this.primeNumbers.get(k + 1) -
			// this.primeNumbers.get(k);
			bigger = smaller;
			smaller = this.primeNumbers.get(numb - k - 1);
			list[k] = (int) (bigger - smaller);
			// list[k] = (int) (this.primeNumbers.get(numb - k) -
			// this.primeNumbers.get(numb
			// - k - 1));
			this.primeNumbers.remove(numb - k);
			if (k % 10000 == 0) {
				label.setText(String.format("%.2f %% of stats",
						100 * (k / (double) (numb))));
			}
		}
		return list;
	}

	private double getMean() {
		long total = 0;
		for (long numb : this.primeNumbers) {
			total += numb;
		}
		return total / (double) (this.primeNumbers.size());
	}

	private double getMean(int[] list) {
		long total = 0;
		for (int numb : list) {
			total += numb;
		}
		return total / (double) (list.length);
	}

	private double getVariance() {
		double mean = this.getMean();
		double temp = 0;
		for (double a : this.primeNumbers)
			temp += (mean - a) * (mean - a);
		return temp / this.primeNumbers.size();
	}

	private double getVariance(double mean) {
		double temp = 0;
		for (double a : this.primeNumbers)
			temp += (mean - a) * (mean - a);
		return temp / this.primeNumbers.size();
	}

	private double getVariance(int[] list, double mean) {
		double temp = 0;
		for (int numb : list)
			temp += (mean - numb) * (mean - numb);
		return temp / (double) (list.length);
	}
}
