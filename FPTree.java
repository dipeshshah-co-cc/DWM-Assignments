//Author: Dipesh Shah
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class FPTree {
	private static Scanner sc;
	private static File file;
	private static StringBuffer temp;
	private static String ip[];
	private static Map<Character, Integer> counts;

	public static void readFileByLine() {
		try {
			file = new File("/media/sda5/sem5/Datawarehousing/Assignments/FPTree/data.txt");
			sc = new Scanner(file);
			temp = new StringBuffer();
			while (sc.hasNextLine())
				temp.append(sc.nextLine() + "\n");
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ip = temp.toString().split("\\n");
	}

	public static void makeHashMap(int m_sup) {
		String input = "";
		char ch;
		for (int i = 0; i < ip.length; i++)
			input += ip[i];

		counts = new HashMap<Character, Integer>(Math.min(input.length(),Character.MAX_VALUE) / 2);
		for (int i = 0; i < input.length(); ++i) {
			ch = input.charAt(i);
			if (!counts.containsKey(ch))
				counts.put(ch, 0);
			counts.put(ch, counts.get(ch) + 1);
		}

		Iterator<Map.Entry<Character, Integer>> iter = counts.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Character, Integer> entry = iter.next();
			if (entry.getValue() < m_sup || entry.getKey().equals(','))
				iter.remove();
		}
	}

	public static <K, V extends Comparable> Map sortByValues(final Map counts) {
		Comparator valueComparator = new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = (counts.get(k2).toString()).compareTo(counts.get(k1).toString());
				if (compare == 0)	return 1;
				else	return compare;
			}
		};

		Map sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(counts);
		return sortedByValues;
	}

	public static void makeList() {
		temp = new StringBuffer();
		counts = sortByValues(counts);
		for (int i = 0; i < ip.length; i++) {
			for (Map.Entry<Character, Integer> entry : counts.entrySet())
				if (ip[i].contains(entry.getKey().toString()))
					temp.append(entry.getKey().toString());
			temp.append("\n");
		}
		ip = temp.toString().split("\\n");
	}

	public static void makeTree() {
		ip = new HashSet<String>(Arrays.asList(ip)).toArray(new String[0]);
		for (int i = 0; i < ip.length; i++) {
			System.out.println(ip[i]);
		}
	}

	public static void main(String[] args) throws Exception {
		readFileByLine();
		makeHashMap(Integer.parseInt(args[0]));
		makeList();
		makeTree();
	}
}
