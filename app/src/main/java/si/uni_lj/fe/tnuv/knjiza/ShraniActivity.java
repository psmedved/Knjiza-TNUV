package si.uni_lj.fe.tnuv.knjiza;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ShraniActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shrani);
        poimenujStran(R.string.text_shrani);
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        //findViewById(R.id.btn_s_shrani).setOnClickListener(v -> {startActivity(new Intent(ShraniActivity.this, MainActivity.class));});
        findViewById(R.id.btn_s_shrani).setOnClickListener(v -> shraniCitat());
        Log.d("DEBUG", getString(R.string.datoteka_citati));


        String prebranoBesedilo = getIntent().getStringExtra("Besedilo");
        TextView prikazBesedila = findViewById(R.id.polje_besedilo_shrani);
        prikazBesedila.setText(prebranoBesedilo);
    }

    public void shraniCitat() {
        TextView vnosnoPoljeCitat = findViewById(R.id.polje_besedilo_shrani);
        String vpisCitat = vnosnoPoljeCitat.getText().toString();
        EditText vnosnoPoljeKnjiga = findViewById(R.id.polje_knjiga_shrani);
        String vpisKnjiga = vnosnoPoljeKnjiga.getText().toString();
        EditText vnosnoPoljeAvtor = findViewById(R.id.polje_avtor_shrani);
        String vpisAvtor = vnosnoPoljeAvtor.getText().toString();
        EditText vnosnoPoljeLeto = findViewById(R.id.polje_leto_shrani);
        String vpisLeto = vnosnoPoljeLeto.getText().toString();
        /*FileOutputStream datoteka = null;
        try {
            datoteka = openFileOutput(String.valueOf(R.string.datoteka_citati), MODE_APPEND);
        } catch (FileNotFoundException e) {}

        JsonWriter novVnos = new JsonWriter(new OutputStreamWriter(datoteka, StandardCharsets.UTF_8));
        novVnos.beginObject();
        novVnos.name("citat") = vpisCitat;
        novVnos.name("knjiga") = vpisKnjiga;
        novVnos.name("avtor") = vpisAvtor;
        novVnos.name("leto") = vpisLeto;*/

        try {
            FileOutputStream datoteka = openFileOutput(getString(R.string.datoteka_citati), MODE_APPEND);
            /*Zapisovanje posamezni objektov-potrebuje prepisovanje dokumenta
            JsonWriter novVnos = new JsonWriter(new OutputStreamWriter(datoteka, StandardCharsets.UTF_8));
            novVnos.beginObject();
            novVnos.name("citat").value(vpisCitat);
            novVnos.name("knjiga").value(vpisKnjiga);
            novVnos.name("avtor").value(vpisAvtor);
            novVnos.name("leto").value(vpisLeto);
            novVnos.value("\n");
            novVnos.endObject();
            novVnos.close();*/
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
            //Log.d("DEBUG", "Izpis v datoteko");
            //Pobrisi vnosna polja
            vnosnoPoljeKnjiga.setText("");
            vnosnoPoljeKnjiga.setText("");
            vnosnoPoljeAvtor.setText("");
            vnosnoPoljeLeto.setText("");

            new AlertDialog.Builder(this)
                    .setTitle(R.string.text_naslov_citat)
                    .setMessage(R.string.text_sporocilo_citat)
                    .setCancelable(false)
                    .setPositiveButton(R.string.navigacija_nov_citat, (dialog, which) -> {
                        Intent odpriSlikaj = new Intent(this, SlikajActivity.class);
                        odpriSlikaj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(odpriSlikaj);
                        finish();
                    })
                    .setNegativeButton(R.string.navigacija_domov, (dialog, which) -> {
                        Intent odpriDomov = new Intent(this, MainActivity.class);
                        odpriDomov.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(odpriDomov);
                        finish();
                    })
                    .setNeutralButton(R.string.navigacija_nazaj, (dialog, which) -> {
                        Intent odpriShrani = new Intent(this, ShraniActivity.class);
                        odpriShrani.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(odpriShrani);
                        finish();
                        dialog.dismiss();
                    })
                    .show();


        } catch (Exception e) {
        }
    }
}
