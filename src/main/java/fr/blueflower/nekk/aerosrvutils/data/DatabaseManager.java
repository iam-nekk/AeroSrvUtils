package fr.blueflower.nekk.aerosrvutils.data;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static Connection connection;

    public static void init(Path path){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path.toAbsolutePath());

            try (Statement statement = connection.createStatement()){
                // Crash safety config
                statement.execute("PRAGMA journal_mode=WAL;");
                statement.execute("PRAGMA synchronous=NORMAL;");
            }

            DatabaseSchema.init();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection get(){
        return connection;
    }
}
