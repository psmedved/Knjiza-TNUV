package si.uni_lj.fe.tnuv.knjiza;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;

public class CitatUrediActivity extends MainActivity  {

    private static final String TAG = CitatUrediActivity.class.getSimpleName();
    private ArrayList<Citat> seznamCitatov;
    private int indeksCitata;
    private EditText vnosnoPoljeCitat;
    private EditText vnosnoPoljeKnjiga;
    private EditText vnosnoPoljeAvtor;
    private EditText vnosnoPoljeLeto;

   private ArrayList<HashMap<Integer, Integer>> preslikavaIndeksov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_citat_uredi);
        poimenujStran(R.string.text_citat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(false);

        vnosnoPoljeCitat = findViewById(R.id.citat_citat_uredi);
        vnosnoPoljeKnjiga = findViewById(R.id.citat_knjiga_uredi);
        vnosnoPoljeAvtor = findViewById(R.id.citat_avtor_uredi);
        vnosnoPoljeLeto = findViewById(R.id.citat_leto_uredi);
        findViewById(R.id.btn_u_preklici).setOnClickListener(v -> zavrziSpremembe());
        findViewById(R.id.btn_u_shrani).setOnClickListener(v -> urediInShraniCitat());
        indeksCitata = getIntent().getIntExtra("indeks", -1);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        PreberiPodatke bralnik = new PreberiPodatke(this);
        seznamCitatov = bralnik.preberiCitate();
        nastaviPolja(indeksCitata);
    }

    public void urediInShraniCitat() {
        Citat vpis = seznamCitatov.get(indeksCitata);
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
            FileOutputStream datoteka = openFileOutput(getString(R.string.datoteka_citati), MODE_PRIVATE);
            //Zapisovanje v NDJSON
            OutputStreamWriter zapisovalec = new OutputStreamWriter(datoteka, StandardCharsets.UTF_8);
            int vrstica = 0;
            for(Citat c : seznamCitatov) {
                JSONObject objektCitat = new JSONObject();
                if(vrstica == indeksCitata) {
                    objektCitat.put("citat", vpisCitat);
                    objektCitat.put("knjiga", vpisKnjiga);
                    objektCitat.put("avtor", vpisAvtor);
                    objektCitat.put("leto", vpisLeto);
                    zapisovalec.write(objektCitat.toString());
                    zapisovalec.write("\n");
                } else {
                    objektCitat.put("citat", c.getCitat());
                    objektCitat.put("knjiga", c.getKnjiga());
                    objektCitat.put("avtor", c.getAvtor());
                    objektCitat.put("leto", c.getLetoToString());
                    zapisovalec.write(objektCitat.toString());
                    zapisovalec.write("\n");
                }
                vrstica++;
            }
            zapisovalec.close();

            Dialog obvestilo = new Dialog(this);
            obvestilo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            obvestilo.setContentView(R.layout.obvestilo);
            obvestilo.getWindow().setBackgroundDrawableResource(R.drawable.dialog_ozadje);
            //obvestilo.setCancelable(false);
            TextView naslov = obvestilo.findViewById(R.id.naslov_obvestila);
            naslov.setText(R.string.text_naslov_citat);
            TextView sporocilo = obvestilo.findViewById(R.id.sporocilo_obvestila);
            sporocilo.setText(R.string.text_sporocilo_citat);
            obvestilo.show();
            obvestilo.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.80),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            Button potrdi = obvestilo.findViewById(R.id.btn_obvestilo);
            potrdi.setOnClickListener(v -> {
                obvestilo.dismiss();
                finish();
            });
        } catch (Exception e) {
        }
    }

    public void zavrziSpremembe() {
        Dialog obvestilo = new Dialog(this);
        obvestilo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        obvestilo.setContentView(R.layout.obvestilo_shrani);
        obvestilo.getWindow().setBackgroundDrawableResource(R.drawable.dialog_ozadje);
        obvestilo.setCancelable(false);
        TextView naslov = obvestilo.findViewById(R.id.naslov_obvestila);
        naslov.setText(R.string.obvestilo_naslov_zavrzi);
        TextView sporocilo = obvestilo.findViewById(R.id.sporocilo_obvestila);
        sporocilo.setText(R.string.obvestilo_sporocilo_zavrzi);
        obvestilo.show();
        obvestilo.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.89),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        Button preklici = obvestilo.findViewById(R.id.btn_obvestilo_shrani_nazaj);
        Button domov = obvestilo.findViewById(R.id.btn_obvestilo_shrani_domov);
        domov.setText("");
        domov.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navigacijaOzadje)));
        domov.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.navigacijaOzadje)));
        domov.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        Button potrdi = obvestilo.findViewById(R.id.btn_obvestilo_shrani_citat);
        preklici.setText(R.string.navigacija_preklici);
        potrdi.setText(R.string.navigacija_potrdi);
        preklici.setOnClickListener(v -> {
            obvestilo.dismiss();
        });
        potrdi.setOnClickListener(v -> {
            obvestilo.dismiss();
            finish();
        });
    }

    public void nastaviPolja(int indeks) {
        Citat zapis = seznamCitatov.get(indeks);
        vnosnoPoljeCitat.setText(zapis.getCitat());
        vnosnoPoljeKnjiga.setText(zapis.getKnjiga());
        vnosnoPoljeAvtor.setText(zapis.getAvtor());
        vnosnoPoljeLeto.setText(zapis.getLetoToString());
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
}
