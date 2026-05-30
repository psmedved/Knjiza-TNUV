package si.uni_lj.fe.tnuv.knjiza;

public class Knjiga {

    private String knjiga;
    private String avtor;
    private int leto;

    public Knjiga(String knjiga, String avtor, int leto) {
        this.knjiga = knjiga;
        this.avtor = avtor;
        this.leto = leto;
    }

    //Matode priklica - getterji
    public String getKnjiga() {
            return this.knjiga;
        }
    public String getAvtor() {
        return this.avtor;
    }
    public int getLeto() { return this.leto; }
    public String getLetoToString() { return String.valueOf(this.leto); }

    //Metode za nastavljanje - setterji
    public void setKnjiga(String knjiga) { this.knjiga = knjiga; }
    public void setAvtor(String avtor) {
        this.avtor = avtor;
    }
    public void setLeto(int leto) { this.leto = leto; }
}
