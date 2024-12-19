import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DokterModel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/rumah_sakit";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Method to get database connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public ObservableList<DokterData> getDokterData() {
        ObservableList<DokterData> dokterList = FXCollections.observableArrayList();
        
        String query = "SELECT * FROM dokter"; // Adjust table name as needed
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            
            while (rs.next()) {
                DokterData dokter = new DokterData(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("jadwal"),
                    rs.getString("ruangan")
                );
                dokterList.add(dokter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dokterList;
    }
    
    // Method to add new dokter
    public boolean addDokter(String nama, String jadwal, String ruangan) {
        String query = "INSERT INTO dokter (nama, jadwal, ruangan) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setString(1, nama);
            pst.setString(2, jadwal);
            pst.setString(3, ruangan);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }
    public boolean updateDokter(int id, String nama, String jadwal, String ruangan) {
        String query = "UPDATE dokter SET nama = ?, jadwal = ?, ruangan = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setString(1, nama);
            pst.setString(2, jadwal);
            pst.setString(3, ruangan);
            pst.setInt(4, id);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete method
    public boolean deleteDokter(int id) {
        String query = "DELETE FROM dokter WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            
            pst.setInt(1, id);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}