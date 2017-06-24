
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class SQLiteJDBC {
    Log log = new Log();

    /////////////////////////////////////////////////////////////
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:stock.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    ///////////////////////////////////////////////////////////
    // Match companies based on Country, Category
    public ArrayList<Integer> baseTargeting(String countryCode, String Category)
            throws IOException {

        ArrayList<Integer> result = new ArrayList<Integer>();
        String sql = "SELECT * FROM COMPANY WHERE Countries LIKE '%"
                + countryCode + "%' AND Category LIKE '%" + Category + "%';";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            String header = "BaseTargeting:\n";
            log.writeFile(header);

            boolean flag = false; // to detect null sql result
            while (rs.next()) {
                flag = true;
                int id = rs.getInt("id");
                result.add(id);
                String s = "{C" + id + ", Passed}\n";
                log.writeFile(s);
                // System.out.println(s);
            }

            String end = "***************\n";
            log.writeFile(end);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    ///////////////////////////////////////////////////////////
    // Check if Companies had some budget to sell stocks
    public ArrayList<Integer> budgetCheck() throws IOException {

        ArrayList<Integer> result = new ArrayList<Integer>();

        String sql = "SELECT * FROM COMPANY WHERE Budget > 0";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            String header = "BudgetCheck:\n";
            log.writeFile(header);

            while (rs.next()) {
                int id = rs.getInt("id");
                result.add(id);
                String s = "{C" + id + ", Passed}\n";
                log.writeFile(s);
                // System.out.println(s);
            }

            String end = "***************\n";
            log.writeFile(end);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /////////////////////////////////////////////////////
    // Check if companies bid is more than API base bid
    public ArrayList<Integer> bidCheck(int n) throws IOException {

        ArrayList<Integer> result = new ArrayList<Integer>();

        String sql = "SELECT * FROM COMPANY WHERE Bid = (SELECT max(Bid) FROM COMPANY WHERE Bid >"
                + n + ");";

        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            String header = "BaseBid:\n";
            log.writeFile(header);

            while (rs.next()) {
                int id = rs.getInt("id");
                result.add(id);
                String s = "{C" + id + ", Passed}\n";
                log.writeFile(s);
                // System.out.println(s);
            }

            String end = "***************\n";
            log.writeFile(end);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /////////////////////////////////////////////////////
    // Reduce Budget​
    public void reduceBudget(int id) {
        float b[] = getBudgetAndBid(id);
        float c = convertFromCentToDollar(b[1]); // convert bid's cent to dollar
        float newBudget = b[0] - c; // budget - bid;
        String sql = "UPDATE Company SET Budget = " + newBudget + " WHERE Id = "
                + id + ";";
        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // Get the Budget and Bid for the company Id.
    public float[] getBudgetAndBid(int id) {
        float result[] = new float[2];
        String sql = "SELECT Budget, Bid FROM COMPANY WHERE Id =" + id + ";";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result[0] = rs.getFloat("Budget");
                // rs.get("Budget");
                result[1] = rs.getInt("Bid");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    // Convert from Cents Dollar
    public float convertFromCentToDollar(float x) {
        return x / 100;
    }

    /////////////////////////////////////////////////////
    // Database Screenshot
    public void databaseScreenshot() {
        String sql = "SELECT * FROM COMPANY ;";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String country = rs.getString("Countries");
                int budget = rs.getInt("Budget");
                int bid = rs.getInt("Bid");
                String category = rs.getString("Category");

                System.out.println("ID = " + id);
                System.out.println("country = " + country);
                System.out.println("budget = " + budget);
                System.out.println("bid = " + bid);
                System.out.println("category = " + category);
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    ///////////////////////////////////////////////////////

}
