package fr.blueflower.nekk.aerosrvutils.data;

import java.sql.SQLException;

public class DatabaseSchema {
    public static void init(){
        try {
            PlayerReceivedCurrencyTable.create();
            // add more tables here
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
