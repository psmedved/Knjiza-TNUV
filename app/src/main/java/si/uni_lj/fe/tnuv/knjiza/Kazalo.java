package si.uni_lj.fe.tnuv.knjiza;

public class Kazalo {

    private String knjiga;
    private int stran;

    public Kazalo(String knjiga,  int stran) {
        this.knjiga = knjiga;
        this.stran = stran;
    }

    //Matode priklica - getterji
    public String getKnjiga() {
        return this.knjiga;
    }
    public int getStran() {
        return this.stran;
    }

    public String getStranToString() {
        return String.valueOf(this.stran);
    }

    //Metode za nastavljanje - setterji
    public void setKnjiga(String knjiga) {
        this.knjiga = knjiga;
    }
    public void setStran(int stran) {
        this.stran = stran;
    }
}
