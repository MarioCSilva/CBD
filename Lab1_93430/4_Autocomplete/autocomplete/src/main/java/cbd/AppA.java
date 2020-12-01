package cbd;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import redis.clients.jedis.Jedis;


public class AppA {
	private static Jedis jedis;
	
	public static void autocomplete(String name){ 
        String header = name;
        String tail = name.substring(0,name.length())+new String(new char[] { 254 });

        System.out.println(jedis.zrangeByLex("autocomplete", "(" + header, "("+tail));
	}

	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        jedis = new Jedis("localhost");

		try {
			File myObj = new File("female-names.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				jedis.zadd("autocomplete", 0, data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		String name;
		while (true){
			System.out.print("Search for ('Enter' for quit): ");
			name = sc.nextLine();
			if (name.equals("")){
				break;
			}
			autocomplete(name);
	 	}

	}
}