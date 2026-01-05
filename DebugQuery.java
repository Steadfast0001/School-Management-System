import java.sql.*;
public class DebugQuery {
  public static void main(String[] args) throws Exception {
    String url = "jdbc:postgresql://localhost:5432/school_db";
    String user = "postgres";
    String pass = "SLIM.V.";
    Class.forName("org.postgresql.Driver");
    try (Connection c = DriverManager.getConnection(url, user, pass);
         PreparedStatement ps = c.prepareStatement("SELECT username, role FROM users WHERE username = ?")) {
      ps.setString(1, "ashley");
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          System.out.println("FOUND: " + rs.getString("username") + " role=" + rs.getString("role"));
        } else {
          System.out.println("No user 'ashley' found");
        }
      }
    }
  }
}
