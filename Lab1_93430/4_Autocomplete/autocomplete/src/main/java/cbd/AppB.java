package cbd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import redis.clients.jedis.Jedis;
import java.util.Set;
import redis.clients.jedis.Tuple;

public class AppB {
    private static Jedis jedis;
    private static String names_group = "name_autocomplete";
    private static String scores_group = "score_autocomplete";
	
	public static Set<String> autocomplete(String name) {
        String tail = name.substring(0,name.length())+new String(new char[] { 254 });
        return jedis.zrangeByLex(names_group, "[" + name, "("+tail);
    }
    
    public static void score_autocomplete(Set<String> people) { 
        Set<redis.clients.jedis.Tuple> people_scores = jedis.zrevrangeByScoreWithScores(scores_group, "+inf", "-inf");

        for (redis.clients.jedis.Tuple person : people_scores) {
            if (people.contains(person.getElement())) {
                System.out.println(person.getElement()+" - "+person.getScore());
            }
        }
	}

	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        jedis = new Jedis("localhost");

        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader("nomes-registados-2020.csv"))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                String name = data[0];
                Float score = Float.parseFloat(data[2]);
                jedis.zadd(names_group, 0, name);
                jedis.zadd(scores_group, score, name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

		String name = "";;
		while (true) {
			System.out.print("Search for ('Enter' for quit): ");
			name = sc.nextLine();
			if (name.equals("")){
				break;
			}
            
            Set<String> people = autocomplete(name);

            score_autocomplete(people);
	 	}

	}
}