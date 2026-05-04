package fr.blueflower.nekk.aerosrvutils.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

// it might be worth it, in the future, to use abstract classes or interfaces instead
public class PlayerReceivedCurrencyTable {
    public static void create() throws SQLException{
        try (Statement statement = DatabaseManager.get().createStatement()){
            // note: sqlite doesnt have a boolean type, we gotta use an integer instead
            statement.execute("""
                CREATE TABLE IF NOT EXISTS received_currency(
                    uuid TEXT PRIMARY KEY,
                    received INTEGER
                )
        """);
        }
    }

    public static void save(UUID uuid, boolean received) throws SQLException{
        try (PreparedStatement ps = DatabaseManager.get().prepareStatement(
                "REPLACE INTO received_currency(uuid, received) VALUES (?,?)"
        )){
            ps.setString(1, uuid.toString());
            ps.setInt(2, received ? 1 : 0);
            ps.executeUpdate();
        }
    }

    public static boolean hasReceivedCurrency(UUID uuid) throws SQLException{
        try (PreparedStatement ps = DatabaseManager.get().prepareStatement(
                "SELECT received FROM received_currency WHERE uuid = ?"
        )){
            ps.setString(1, uuid.toString());

            try (ResultSet rs = ps.executeQuery()){
                return rs.next() ? rs.getInt("received") == 1 : false;
            }
        }
    }
}
