import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DokterController implements Initializable {
    @FXML
    private DatePicker jadwal;

    @FXML
    private TextField namatTF;

    @FXML
    private TextField ruanganTF;

    @FXML
    private Button addBtn;
    @FXML
    private Button uptBtn;
    @FXML
    private Button delBtn;

    @FXML
    private TableView<DokterData> dokterTableView;

    @FXML
    private TableColumn<DokterData, String> jadwalColumn;

    @FXML
    private TableColumn<DokterData, String> pasienColumn;

    @FXML
    private TableColumn<DokterData, String> ruanganColumn;

    private DokterModel dokterModel;
    private DokterData selectedDokter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dokterModel = new DokterModel();
        
        
        jadwalColumn.setCellValueFactory(new PropertyValueFactory<>("jadwal"));
        pasienColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        ruanganColumn.setCellValueFactory(new PropertyValueFactory<>("ruangan"));
        
        
        loadDokterData();

        
        dokterTableView.getSelectionModel().selectedItemProperty().addListener((_, _, newSelection) -> {
            if (newSelection != null) {
                selectedDokter = newSelection;
                
                namatTF.setText(newSelection.getNama());
                ruanganTF.setText(newSelection.getRuangan());
                jadwal.setValue(LocalDate.parse(newSelection.getJadwal()));
            }
        });
    }

    private void loadDokterData() {
        dokterTableView.setItems(dokterModel.getDokterData());
    }

    @FXML
    private void addJadwal() {
        String nama = namatTF.getText().trim();
        String ruangan = ruanganTF.getText().trim();
        String jadwalStr = jadwal.getValue() != null ? jadwal.getValue().toString() : "";

        if (nama.isEmpty() || ruangan.isEmpty() || jadwalStr.isEmpty()) {
            showAlert("Semua field harus diisi!");
            return;
        }

        
        boolean success = dokterModel.addDokter(nama, jadwalStr, ruangan);
        
        if (success) {
            showAlert("Data berhasil ditambahkan!");
            loadDokterData(); 
            clearFields();
        } else {
            showAlert("Gagal menambahkan data!");
        }
    }

    @FXML
    private void updateJadwal() {
        if (selectedDokter == null) {
            showAlert("Pilih data yang akan diupdate!");
            return;
        }

        String nama = namatTF.getText().trim();
        String ruangan = ruanganTF.getText().trim();
        String jadwalStr = jadwal.getValue() != null ? jadwal.getValue().toString() : "";

        if (nama.isEmpty() || ruangan.isEmpty() || jadwalStr.isEmpty()) {
            showAlert("Semua field harus diisi!");
            return;
        }

        
        boolean success = dokterModel.updateDokter(selectedDokter.getId(), nama, jadwalStr, ruangan);
        
        if (success) {
            showAlert("Data berhasil diupdate!");
            loadDokterData(); 
            clearFields();
            selectedDokter = null;
        } else {
            showAlert("Gagal mengupdate data!");
        }
    }

    @FXML
    private void deleteJadwal() {
        if (selectedDokter == null) {
            showAlert("Pilih data yang akan dihapus!");
            return;
        }

        
        boolean success = dokterModel.deleteDokter(selectedDokter.getId());
        
        if (success) {
            showAlert("Data berhasil dihapus!");
            loadDokterData();
            clearFields();
            selectedDokter = null;
        } else {
            showAlert("Gagal menghapus data!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        namatTF.clear();
        ruanganTF.clear();
        jadwal.setValue(null);
    }
}