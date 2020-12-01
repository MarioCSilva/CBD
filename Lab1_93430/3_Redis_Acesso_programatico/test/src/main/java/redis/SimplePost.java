package redis;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import redis.clients.jedis.Jedis;
 
public class SimplePost {
 
	private Jedis jedis;
	public static String USERS = "users"; // Key set for users' name
	public static String USERS_LIST = "list_users"; // List for users' name
	public static String USERS_HASHMAP = "users_hashmap"; // HashMap for users' name
	public static Integer hash_incrementer = -1;

	public SimplePost() {
		this.jedis = new Jedis("localhost");
	}
 
	public void saveUser(String username) {
		jedis.sadd(USERS, username);
	}

	public Set<String> getUser() {
		return jedis.smembers(USERS);
	}

	public Set<String> getAllKeys() {
		return jedis.keys("*");
	}
	
	
	public void saveUserList(String username) {
		jedis.lpush(USERS_LIST, username);
	}

	public List<String> getUserList() {
		return jedis.lrange(USERS_LIST,0,-1);
	}


	public void saveUserHashMap(String username) {
		jedis.hset(USERS_HASHMAP, (hash_incrementer++).toString(), username);
	}

	public Map<String, String> getUserHashMap() {
		return jedis.hgetAll(USERS_HASHMAP);
	}
	
 
	public static void main(String[] args) {
		SimplePost board = new SimplePost();
		// set some users
		String[] users = { "Ana", "Pedro", "Maria", "Luis" };
		System.out.println("--------------Sets--------------");
		for (String user: users) 
			board.saveUser(user);
		board.getAllKeys().stream().forEach(System.out::println);
		board.getUser().stream().forEach(System.out::println);

		// list some users
		System.out.println("--------------Lists--------------");
		for (String user: users) 
			board.saveUserList(user);
		board.getUserList().stream().forEach(System.out::println);

		// map some users
		System.out.println("--------------HashMaps--------------");
		for (String user: users) 
			board.saveUserHashMap(user);
		System.out.println(board.getUserHashMap());
	}
}



