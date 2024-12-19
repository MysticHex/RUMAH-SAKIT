public class DokterData {
    private int id;
    private String nama;
    private String jadwal;
    private String ruangan;

    public DokterData(int id, String nama, String jadwal, String ruangan) {
        this.id = id;
        this.nama = nama;
        this.jadwal = jadwal;
        this.ruangan = ruangan;
    }

    public int getId() { return id; }

    public String getNama() { return nama; }
    public String getJadwal() { return jadwal; }
    public String getRuangan() { return ruangan; }
}