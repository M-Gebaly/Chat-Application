package database;

public class DatabaseHandler {

    private static DatabaseHandler databaseHandler;

    private Connection connection;

    private ResultSet resultSet;

    private PreparedStatement pres;

    private DatabaseHandler() {
    }

    public static DatabaseHandler getInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void connect() {
    }

    public void insert(String query) {
    }

    public void update(String query) {
    }

    public int delete(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ResultSet select(String query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
