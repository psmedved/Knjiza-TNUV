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

        findViewById(R.id.btn_s_shrani).setOnClickListener(v -> shraniCitat());
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
            vnosnoPoljeKnjiga.setText("");
            vnosnoPoljeKnjiga.setText("");
            vnosnoPoljeAvtor.setText("");
            vnosnoPoljeLeto.setText("");

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
}
