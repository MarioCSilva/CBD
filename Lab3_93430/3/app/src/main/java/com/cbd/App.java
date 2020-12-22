package com.cbd;
import com.datastax.driver.core.*;
import java.sql.Date;

public class App 
{
    private static Cluster cluster;
    private static Session session;

    public static void connect(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        session = cluster.connect();
    }
    
    public static Session getSession() {
        return session;
    }

    public static void close() {
        session.close();
        cluster.close();
    }

    public static void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor) {
        StringBuilder sb = 
            new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
            .append(keyspaceName).append(" WITH replication = {")
            .append("'class':'").append(replicationStrategy)
            .append("','replication_factor':").append(replicationFactor)
            .append("};");
                
            String query = sb.toString();
            session.execute(query);
    }

    public static void useKeyspace(String keyspaceName) {
        session.execute("USE " + keyspaceName + ";");
    }

    public static void insertAuthor(Author author) {
        StringBuilder sb = new StringBuilder("INSERT INTO ")
          .append("Author").append("(username, email, name, creation_date) ")
          .append("VALUES ('").append(author.getUsername())
          .append("', '").append(author.getEmail())
          .append("', '").append(author.getName())
          .append("', '").append(author.getTimestamp())
          .append("');");
        
        String query = sb.toString();
        System.out.println(query);
        session.execute(query);
    }

    public static void searchAuthor() {
        StringBuilder sb = new StringBuilder("Select * from ")
        .append("Author;");

        String query = sb.toString();
        ResultSet results = session.execute(query);
        results.forEach(res -> System.out.println(res));
    }

    public static void searchAuthorByUsername(String username) {
        StringBuilder sb = new StringBuilder("Select * from ")
        .append("Author").append(" where ")
        .append("username = '")
        .append(username).append("';");

        String query = sb.toString();
        ResultSet results = session.execute(query);
        results.forEach(res -> System.out.println(res));
    }

    public static void searchAuthorByEmail(String email) {
        StringBuilder sb = new StringBuilder("Select * from ")
        .append("Author").append(" where ")
        .append("email = '")
        .append(email).append("' allow filtering;");

        String query = sb.toString();
        ResultSet results = session.execute(query);
        results.forEach(res -> System.out.println(res));
    }

    public static void searchAuthorByName(String name) {
        StringBuilder sb = new StringBuilder("Select * from ")
        .append("Author").append(" where ")
        .append("name = '")
        .append(name).append("' allow filtering;");

        String query = sb.toString();
        ResultSet results = session.execute(query);
        results.forEach(res -> System.out.println(res));
    }

    public static void searchAuthorByTimestamp(Date timestamp) {
        StringBuilder sb = new StringBuilder("Select * from ")
        .append("Author").append(" where ")
        .append("creation_date = '")
        .append(timestamp.toString()).append("' allow filtering;");

        String query = sb.toString();
        ResultSet results = session.execute(query);
        results.forEach(res -> System.out.println(res));
    }

    public static void editAuthor(Author author) {
        StringBuilder sb = new StringBuilder("Update Author ")
        .append("Set name = '")
        .append(author.getName())
        .append("', email = '")
        .append(author.getEmail())
        .append("', creation_date = '")
        .append(author.getTimestamp())
        .append("' where username = '")
        .append(author.getUsername())
        .append("';");

        String query = sb.toString();
        session.execute(query);
        searchAuthorByUsername(author.getUsername());
    }


    public static void main( String[] args )
    {
        connect("127.0.0.1");
        session = getSession();
        
        // createKeyspace("CBD_Java", "SimpleStrategy", 3);
        // going to use an already existing keyspace
        useKeyspace("video");


        // Data Insertion
        System.out.println("Data Insertion");
        Author author = new Author("userteste", "userteste@gmail.com", "João", new Date(System.currentTimeMillis()));
        insertAuthor(author);


        // Data Search
        System.out.println("\nData Search");

        System.out.println("\nSearch Table Author");
        searchAuthor();

        System.out.println("\nSearch Table Author by Username");
        searchAuthorByUsername(author.getUsername());

        System.out.println("\nSearch Table Author by Email with Allow Filtering");
        searchAuthorByEmail(author.getEmail());

        System.out.println("\nSearch Table Author by Name with Allow Filtering");
        searchAuthorByName(author.getName());

        System.out.println("\nSearch Table Author by Creation Date with Allow Filtering");
        searchAuthorByTimestamp(author.getTimestamp());


        // Data Edit
        System.out.println("\nData Edit");

        System.out.println("\nEdit Author Email");
        author.setEmail("joao@gmail.com");
        editAuthor(author);

        System.out.println("\nEdit Author Email");
        author.setName("João Silva");
        editAuthor(author);

        System.out.println("\nEdit Author Creation Date");
        author.setTimestamp(new Date(System.currentTimeMillis()));
        editAuthor(author);

        close();
    }
}



