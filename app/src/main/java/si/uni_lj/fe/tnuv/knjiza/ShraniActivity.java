package si.uni_lj.fe.tnuv.knjiza;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ShraniActivity extends MainActivity {

    private static final String TAG = ShraniActivity.class.getSimpleName();
    private TextView vnosnoPoljeCitat;
    private EditText vnosnoPoljeKnjiga;
    private EditText vnosnoPoljeAvtor;
    private EditText vnosnoPoljeLeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shrani);
        poimenujStran(R.string.text_shrani);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(false);

        vnosnoPoljeCitat = findViewById(R.id.polje_besedilo_shrani);
        vnosnoPoljeKnjiga = findViewById(R.id.polje_knjiga_shrani);
        vnosnoPoljeAvtor = findViewById(R.id.polje_avtor_shrani);
        vnosnoPoljeLeto = findViewById(R.id.polje_leto_shrani);
        findViewById(R.id.btn_s_shrani).setOnClickListener(v -> shraniCitat());
        findViewById(R.id.btn_s_preklici).setOnClickListener(v -> pocistiPolja());
        if(savedInstanceState == null) {
            String prebranoBesedilo = getIntent().getStringExtra("Besedilo");
            vnosnoPoljeCitat.setText(prebranoBesedilo);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle izhodnoStanje) {
        super.onSaveInstanceState(izhodnoStanje);
        izhodnoStanje.putString("citat", vnosnoPoljeCitat.getText().toString());
        izhodnoStanje.putString("knjiga", vnosnoPoljeKnjiga.getText().toString());
        izhodnoStanje.putString("avtor", vnosnoPoljeAvtor.getText().toString());
        izhodnoStanje.putString("leto", vnosnoPoljeLeto.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle shranjenoStanje) {
        super.onRestoreInstanceState(shranjenoStanje);
        vnosnoPoljeCitat.setText(shranjenoStanje.getString("citat"));
        vnosnoPoljeKnjiga.setText(shranjenoStanje.getString("knjiga"));
        vnosnoPoljeAvtor.setText(shranjenoStanje.getString("avtor"));
        vnosnoPoljeLeto.setText(shranjenoStanje.getString("leto"));
    }

    public void shraniCitat() {
        boolean[] uporabniskiVnosi = new boolean[4];
        String vpisCitat = vnosnoPoljeCitat.getText().toString();
        uporabniskiVnosi[0] = preveriUporabniskiVnos(vpisCitat, "s", 0);
        String vpisKnjiga = vnosnoPoljeKnjiga.getText().toString();
        uporabniskiVnosi[1] = preveriUporabniskiVnos(vpisKnjiga, "s", 1);
        String vpisAvtor = vnosnoPoljeAvtor.getText().toString();
        uporabniskiVnosi[2] = preveriUporabniskiVnos(vpisAvtor, "s", 2);
        String vpisLeto = vnosnoPoljeLeto.getText().toString();
        uporabniskiVnosi[3] = preveriUporabniskiVnos(vpisLeto, "i", 3);
        boolean ovrednotenVnos = ovrednotiUporabniskiVnos(uporabniskiVnosi);
        if(!ovrednotenVnos) {
            return;
        }
        try {
            FileOutputStream datoteka = openFileOutput(getString(R.string.datoteka_citati), MODE_APPEND);
            //Zapisovanje v NDJSON
            OutputStreamWriter zapisovalec = new OutputStreamWriter(datoteka, StandardCharsets.UTF_8);
            JSONObject objektCitat = new JSONObject();
            objektCitat.put("citat", vpisCitat);
            objektCitat.put("knjiga", vpisKnjiga);
            objektCitat.put("avtor", vpisAvtor);
            objektCitat.put("leto", vpisLeto);
            zapisovalec.write(objektCitat.toString());
            zapisovalec.write("\n");
            zapisovalec.close();
            //Pobrisi vnosna polja
            pocistiPolja();

            Dialog obvestilo = new Dialog(this);
            obvestilo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            obvestilo.setContentView(R.layout.obvestilo_shrani);
            obvestilo.getWindow().setBackgroundDrawableResource(R.drawable.dialog_ozadje);
            obvestilo.setCancelable(false);
            obvestilo.show();
            obvestilo.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.89),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            Button nazaj = obvestilo.findViewById(R.id.btn_obvestilo_shrani_nazaj);
            Button domov = obvestilo.findViewById(R.id.btn_obvestilo_shrani_domov);
            Button novCitat = obvestilo.findViewById(R.id.btn_obvestilo_shrani_citat);
            nazaj.setOnClickListener(v -> {
                Intent odpriShrani = new Intent(this, ShraniActivity.class);
                odpriShrani.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(odpriShrani);
                finish();
                obvestilo.dismiss();
            });
            domov.setOnClickListener(v -> {
                Intent odpriDomov = new Intent(this, MainActivity.class);
                odpriDomov.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(odpriDomov);
                finish();
            });
            novCitat.setOnClickListener(v -> {
                Intent odpriSlikaj = new Intent(this, SlikajActivity.class);
                odpriSlikaj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(odpriSlikaj);
                finish();
            });
        } catch (Exception e) {
        }
    }

    public void pocistiPolja() {
        vnosnoPoljeCitat.setText("");
        vnosnoPoljeKnjiga.setText("");
        vnosnoPoljeAvtor.setText("");
        vnosnoPoljeLeto.setText("");
    }

    public boolean preveriUporabniskiVnos(String vnos, String zastavica, int indeks) {
        int[] praznoPolje = {R.string.obvestilo_sporocilo_napaka_citat_prazno, R.string.obvestilo_sporocilo_napaka_knjiga_prazno,R.string.obvestilo_sporocilo_napaka_avtor_prazno, R.string.obvestilo_sporocilo_napaka_leto_prazno};
        int[] niStevilo = {R.string.obvestilo_sporocilo_napaka_leto_ni_stevilka, R.string.obvestilo_sporocilo_napaka_leto_predolgo};
        Log.d(TAG, vnos);
        if(vnos == null || vnos.trim().isEmpty()) {
            obvestiloONapaki(getString(praznoPolje[indeks]));
            switch(indeks) {
                case 0:
                    vnosnoPoljeCitat.setText("");
                    break;
                case 1:
                    vnosnoPoljeKnjiga.setText("");
                    break;
                case 2:
                    vnosnoPoljeAvtor.setText("");
                    break;
                case 3:
                    vnosnoPoljeLeto.setText("");
                    break;
                default:
            }
            return false;
        }
        /*if(zastavica.equals("s")) {

        }*/
        if(zastavica.equals("i")) {
            if(!jeStevilka(vnos)) {
                obvestiloONapaki(getString(niStevilo[0]));
                vnosnoPoljeLeto.setText("");
                return false;
            } else if(vnos.length() > 4) {
                obvestiloONapaki(getString(niStevilo[1]));
                vnosnoPoljeLeto.setText("");
                return false;
            }
        }
        return true;
    }

    public boolean jeStevilka(String vnos) {
        for(int i = 0; i < vnos.length(); i++) {
            int x = vnos.charAt(i);
            //x < '0' || x > '9' v ASCII vrednostih
            if(x < 48 || x > 57) {
                return false;
            }
        }
        return true;
    }

    public boolean ovrednotiUporabniskiVnos(boolean[] vnos) {
        boolean pravilno = true;
        for(int i = 0; i < vnos.length; i++) {
            if(!vnos[i]) {
                return false;
            }
        }
        return pravilno;
    }

    public void obvestiloONapaki(String sporociloNapake) {
        Dialog obvestilo = new Dialog(this);
        obvestilo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        obvestilo.setContentView(R.layout.obvestilo);
        obvestilo.getWindow().setBackgroundDrawableResource(R.drawable.dialog_ozadje);
        //obvestilo.setCancelable(false);
        TextView naslov = obvestilo.findViewById(R.id.naslov_obvestila);
        naslov.setText(R.string.obvestilo_naslov_napaka);
        TextView sporocilo = obvestilo.findViewById(R.id.sporocilo_obvestila);
        sporocilo.setText(sporociloNapake);
        obvestilo.show();
        obvestilo.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.80),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        Button potrdi = obvestilo.findViewById(R.id.btn_obvestilo);
        potrdi.setOnClickListener(v -> {
            obvestilo.dismiss();
        });
    }
}
