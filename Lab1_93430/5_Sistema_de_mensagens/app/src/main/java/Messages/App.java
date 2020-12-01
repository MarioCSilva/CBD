package Messages;

import java.util.Scanner;
import java.util.Set;
import java.util.List;
import redis.clients.jedis.Jedis;

public class App {
    private static Jedis jedis;
    private static String active_user = "";
    private static Scanner sc = new Scanner ( System.in );

    // 1 lista, chat:messages<user>
    // 1 hash, chat:users:<user> username e chat:users:<user> password
    // 1 set, chat:subs:<user>

    public static void displayMenuInit() {
        System.out.println ( "\n1) Register\n2) Sign In\n0) Close Chat App\n" );
        System.out.print ( "Selection: " );

        switch ( sc.nextLine() ) {
            case "0":
                System.out.println ( "Closing Chat App..." );
                System.exit(1);
            case "1":
                register();
                break;
            case "2":
                if (login()) {
                    displayMenuUser();
                };
                break;
            default:
                System.err.println ( "Unrecognized option" );
                break;
        }
        displayMenuInit();
    }

    public static void displayMenuUser() {
        System.out.println ( "\n1) Follow an User\n2) Send a Message\n3) Read Messages\n4) Sign Out\n0) Close Chat App\n" );
        System.out.print ( "Selection: " );

        switch ( sc.nextLine() ) {
            case "0":
                System.out.println ( "Closing Chat App..." );
                System.exit(1);
            case "1":
                getAllUsers();
                System.out.print("User to follow: ");
                String user = sc.nextLine();
                subscribe(user);
                break;
            case "2":
                System.out.print("Message to send: ");
                String msg = sc.nextLine();
                publishMessage(msg);
                break;
            case "3":
                readMessage();
                break;
            case "4":
                System.out.println ( "Sigining Out..." );
                active_user = "";
                displayMenuInit();
            default:
                System.err.println ( "Unrecognized option" );
                break;
        }
        displayMenuUser();
    }

    public static void register() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        if (checkUser(username)) {
            System.out.println("Username already in use.");
            return;
        }
        jedis.hset("chat:users:" + username, "username", username);
        System.out.print("Password: ");
        String password = sc.nextLine();
        jedis.hset("chat:users:" + username, "password", password);
    }

    public static Boolean login() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        
        if (checkUser(username)) {
            if (jedis.hget("chat:users:" + username, "password").equals(password)) {
                active_user = username;
                return true;
            }
        }
        System.out.println("Invalid login.");
        return false;
    }

    public static void getAllUsers() {
        System.out.println("All Users:");
        Set<String> all_users = jedis.keys("chat:users:*");
        for (String user : all_users) {
            user = user.split(":")[2];
            if (!active_user.equals(user)) {
                System.out.println("\t- " + user);
            }
        }
    }

    public static Boolean checkUser(String username) {
        Set<String> all_users = jedis.keys("chat:users:"+username);
        Boolean result = (all_users).size() == 0 ? false : true;
        return result;
    }

    public static void subscribe(String user) {
        if (!checkUser(user) || user.equals(active_user)) {
            System.out.println("Invalid User.");
            return;
        }
        Set<String> subbed_users = jedis.smembers("chat:subs:" + active_user);
        if (subbed_users.contains(user)) {
            System.out.println("Already subscribed to that User.");
            return;
        }
        jedis.sadd("chat:subs:" + active_user, user);
    }

    public static void publishMessage(String msg) {
        jedis.rpush("chat:messages:"+active_user, msg);
    }

    public static void readMessage() {
        Set<String> subbed_users = jedis.smembers("chat:subs:" + active_user);
        for (String subbed_user : subbed_users) {
            System.out.println("From user: " + subbed_user + ":");

            List<String> subbed_user_msg = jedis.lrange("chat:messages:"+subbed_user,0,-1);    
            for (String msg : subbed_user_msg) {
                System.out.println("\t- " + msg);
            }
        }
    }

    public static void main ( String[] args ) {
        jedis = new Jedis("localhost");
        displayMenuInit();
    }
}