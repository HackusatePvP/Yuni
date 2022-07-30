package dev.hcr.yuni.database.types;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.hcr.yuni.configurations.types.PropertiesConfiguration;
import dev.hcr.yuni.database.IStorage;
import dev.hcr.yuni.punishments.Punishment;
import dev.hcr.yuni.punishments.types.BanType;
import dev.hcr.yuni.ranks.Rank;
import dev.hcr.yuni.users.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MongoStorage implements IStorage {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> ranks;
    private final MongoCollection<Document> bans;

    private final Gson gson = new Gson();
    private final Type type = new TypeToken<HashMap>(){}.getType();

    public MongoStorage() {
        PropertiesConfiguration configuration = (PropertiesConfiguration) PropertiesConfiguration.getPropertiesConfiguration("mongo.properties");
        MongoClient mongoClient;
        if (configuration.getBoolean("db-auth")) {
            // Please note the difference between database and authentication database. The authentication database is a database within mongo that stores user and user permissions.
            // Which is why you have to specify the auth database. By default windows installation it is "admin"
            mongoClient = new MongoClient(new ServerAddress(configuration.getString("host"), configuration.getInteger("port")), MongoCredential.createCredential(
                    configuration.getString("db-auth-user"),
                    configuration.getString("db-auth-db"),
                    configuration.getString("db-auth-password").toCharArray()
            ), MongoClientOptions.builder().build());
        } else {
            mongoClient = new MongoClient(new ServerAddress(configuration.getString("host"), configuration.getInteger("port")));
        }
        MongoDatabase database = mongoClient.getDatabase(configuration.getString("database"));
        users = database.getCollection("users");
        ranks = database.getCollection("ranks");
        // All punishment collections, this may be redundant but I don't know a better way.
        bans = database.getCollection("bans");
    }

    @Override
    public void loadUser(UUID uuid) {
        Document document = users.find(Filters.eq("uuid", uuid.toString())).first();
        if (document != null) {
          /*
          Old method, removed it because object.toMap() because it wanted the raw use of Map instead of Map<String, Object> leaving it commented out because it could be more efficient then the other method.
          BasicDBObject object = BasicDBObject.parse(document.toJson());
          new User(uuid, object.toMap());
            */

            // In order to properly use multiple database options we have
            // to do some conversions from database specific objects into java objects.
            // We are taking the Document which only works if you are using mongodb and converting it to a map.
            // Which can be used by any java project.

            Map<String, Object> map = new HashMap<>();
            document.forEach(map::put); // This is a lambda function, this just simplifies "document.put(key, value);"

            // Once the map is created and all values are stored create the user with the map.
            User user = new User(uuid, map);

            // Once the user obj is created we need to load punishments.
            loadPunishments(user);
        }
    }

    private void loadPunishments(User user) {
        for (Document document : bans.find(Filters.eq("uuid", user.getUniqueID().toString()))) {
            Map<String, Object> map = new HashMap<>();
            document.forEach(map::put);
            user.addBan(new BanType(user.getUniqueID(), map));
        }
    }

    @Override
    public User loadUser(String name) {
        // This is super scuffed
        Document document = users.find(Filters.eq("name", name)).first();
        if (document != null) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            Map<String, Object> map = new HashMap<>();
            document.forEach(map::put); // This is a lambda function, this just simplifies "document.put(key, value);"

            // Once the map is created and all values are stored create the user with the map.
            User user = new User(uuid, map);

            // Once the user obj is created we need to load punishments.
            loadPunishments(user);
            return user;
        }

        return null;
    }

    @Override
    public void loadUserAsync(UUID uuid) {
        // TODO: 7/29/2022  
    }

    @Override
    public void saveUser(User user) {
        Bson filters = Filters.eq("uuid", user.getUniqueID().toString());

        // We need to convert the user map back into a Document.
        // Credit goes to https://devqa.io/how-to-convert-java-map-to-json/

        String gsonString = gson.toJson(user.toMap(), type);
        Document document = Document.parse(gsonString);

        // Instead of creating functions for saving and creating we can just create in one function and use mongos upsert operations. 
        // The upsert option will insert the document if it cannot find an entry to update.
        Bson update = new BasicDBObject("$set", document);
        UpdateOptions options = new UpdateOptions().upsert(true);
        users.updateOne(filters, update, options);
    }

    @Override
    public void saveUserAsync(User user) {
        // TODO: 7/29/2022  
    }

    @Override
    public boolean userExists(UUID uuid) {
        return users.find(Filters.eq("uuid", uuid.toString())).first() != null;
    }

    @Override
    public boolean userExists(String name) {
        return users.find(Filters.eq("name", name)).first() != null;
    }


    @Override
    public void loadRanks() {
        for (Document document : ranks.find()) {
            Map<String, Object> map = new HashMap<>();
            String name = document.getString("name");
            document.forEach(map::put);
            new Rank(name, map);
        }
    }

    @Override
    public void saveRank(Rank rank) {
        Bson filters = Filters.eq("name", rank.getName());

        String gsonString = gson.toJson(rank.toMap(), type);
        Document document = Document.parse(gsonString);

        Bson update = new BasicDBObject("$set", document);
        UpdateOptions options = new UpdateOptions().upsert(true);
        ranks.updateOne(filters, update, options);
    }

    @Override
    public void savePunishments(Punishment punishment) {
        if (punishment instanceof BanType) {
            BanType banType = (BanType) punishment;
            Bson filters = Filters.eq("id", banType.getId());

            String gsonString = gson.toJson(banType.toMap(), type);
            Document document = Document.parse(gsonString);

            Bson update = new BasicDBObject("$set", document);
            UpdateOptions options = new UpdateOptions().upsert(true);
            bans.updateOne(filters, update, options);
        }
    }


}
