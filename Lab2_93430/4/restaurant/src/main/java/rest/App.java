package rest;

import com.mongodb.ServerAddress;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Iterator;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.Arrays;
import java.util.HashMap;

import org.bson.Document;


public class App 
{
    public static void main( String[] args )
    {
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("cbd");
        MongoCollection<Document> collection = database.getCollection("rest");

        collection.createIndex(new Document("localidade", 1));
        collection.createIndex(new Document("gastronomia", 1));
        collection.createIndex(new Document("nome", 1));


        Document d = new Document();
        List<Double> coords = new ArrayList<Double>();
        coords.add(-72.9000);
        coords.add(39.23);
        List<Document> grades = new ArrayList<Document>();
        grades.add(new Document("date", new Document("date", Long.valueOf("1409961600000"))).append("grade","A").append("score",3));
        grades.add(new Document("date", new Document("date", Long.valueOf("1404172800000"))).append("grade","A").append("score",23));
        grades.add(new Document("date", new Document("date", Long.valueOf("1404172800000"))).append("grade","A").append("score",18));
        d.append("address",new Document("building", "1007").append("coord", coords).append("rua", "Mário Sacramento").append("zipcode","10463"));
        d.append("localidade","Aveiro");
        d.append("gastronomia","Italian");
        d.append("grades",grades);
        d.append("nome", "My Restaurant");
        d.append("restaurant_id","300754459");

        insertOne(collection, d);

        findOne(collection, d);

        countLocalidades(collection);

        countRestByLocalidade(collection);

        countRestByLocalidadeByGastronomia(collection);

        getRestWithNameCloserTo(collection, "Park");

    }


        public static void insertOne(MongoCollection<Document> col, Document doc){
            col.insertOne(doc);
        }

        public static void insertMany(MongoCollection<Document> col, ArrayList<Document> docs){
            col.insertMany(docs);
        }

        public static void edit(MongoCollection<Document> col, Document doc, Document doc_edited) {
            String id = doc.getString("restaurant_id");
            Document foundDoc = col.find(eq("restaurant_id", id)).first();
            col.deleteOne(foundDoc);
            col.insertOne(doc_edited);
        }

        public static void findAll(MongoCollection<Document> col){
            Iterator<Document> iterator = col.find().iterator();
            while(iterator.hasNext()) {
                System.out.println(iterator.next().toJson().toString());
            }
        }

        public static void findOne(MongoCollection<Document> col, Document doc){
            String id = doc.getString("restaurant_id");
            Document foundDoc = col.find(eq("restaurant_id", id)).first();
            System.out.println(foundDoc.toJson());
            
        }

        public static int countLocalidades(MongoCollection<Document> col) {
            Iterator<Document> it = col.aggregate(Arrays.asList(
                    Aggregates.group(asList("$localidade"))
            )).iterator();
            int count = 0;
            while (it.hasNext()) {
                it.next();
                count += 1;
            }
            System.out.println("Número de localidades distintas: " + String.valueOf(count));
            System.out.println();

            return  count;
        }

        public static Map<String, Integer> countRestByLocalidade(MongoCollection<Document> col) {
            Iterator<Document> it = col.find().iterator();

            Map<String, Integer> restLocal = new HashMap<>();

            while (it.hasNext()) {
                Document rest = it.next();
                if (restLocal.containsKey(rest.getString("localidade"))) {
                    restLocal.put(rest.getString("localidade"), restLocal.get(rest.getString("localidade")) + 1);
                } else{
                    restLocal.put(rest.getString("localidade").toString(), 1);
                }
            }

            System.out.println("Número de restaurantes por localidade:");
            for(String local : restLocal.keySet()) {
                System.out.println(" -> " + local + " - " + restLocal.get(local));           
            }
            System.out.println();

            return restLocal;
        }


        public static Map<String, Integer> countRestByLocalidadeByGastronomia( MongoCollection<Document> col ){
            Map<String, Integer> cityandFoodMap = new HashMap<>();
            AggregateIterable<Document> diffcities = col.aggregate(Arrays.asList(
                    Aggregates.group(asList("$localidade","$gastronomia"),Accumulators.sum("count",1)),Aggregates.sort(new Document("count",1))
            ));
            diffcities.forEach(new Consumer<Document>() {
                @Override
                public void accept(final Document document) {
                    List key = (ArrayList) document.get("_id");
                    cityandFoodMap.put((String)key.get(0) + " | " + (String) key.get(1) , (Integer) document.get("count"));
                }
            });

            System.out.println("Número de restaurantes por localidade e gastronomia:");
            for(String local : cityandFoodMap.keySet()) {
                System.out.println(" -> " + local + " - " + cityandFoodMap.get(local));           
            }
            System.out.println();
    
            return cityandFoodMap;
        }

        public static List<String> getRestWithNameCloserTo(MongoCollection<Document> col, String name){
            List<String> closer_rests = new ArrayList<>();
            List <Document> f = col.find(regex("nome",".*"+ name + ".*"))
                    .projection(Projections.include("nome"))
                    .into(new ArrayList<Document>());
    
            f.forEach(new Consumer<Document>() {
                @Override
                public void accept(final Document document) {
                    closer_rests.add(document.getString("nome"));
                }
            });

            System.out.println("Nome de restaurantes contendo" + name + " no nome:");
            for(String nome : closer_rests) {
                System.out.println(" -> " + nome);           
            }
            System.out.println();

            return closer_rests;
        }

}