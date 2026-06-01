package si.uni_lj.fe.tnuv.knjiza;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PretvoriActivity extends MainActivity {

    private static final String TAG = PretvoriActivity.class.getSimpleName();
    private byte[] bitniTok;
    private String pretvorjenoBesedilo = "";
    private ImageView prikazSlike;
    private TextView prikazBesedila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pretvori);
        poimenujStran(R.string.text_pretvori);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(true);

        findViewById(R.id.btn_p_nadaljuj).setOnClickListener(v -> {
            if(pretvorjenoBesedilo.trim().isEmpty()) {
                obvestiloONapaki(getString(R.string.obvestilo_sporocilo_napaka_citat_prazno_izberi));
                return;
            }
            Intent odpriIzberi = new Intent(PretvoriActivity.this, IzberiActivity.class);
            odpriIzberi.putExtra("Besedilo", pretvorjenoBesedilo);
            startActivity(odpriIzberi);
        });
        //Prikaz zajete slike
        bitniTok = getIntent().getByteArrayExtra("slika");
        Bitmap slika = BitmapFactory.decodeByteArray(bitniTok, 0, bitniTok.length);
        prikazSlike = findViewById(R.id.polje_slike_pretvori);
        prikazBesedila = findViewById(R.id.polje_besedilo_pretvori);
        prikazSlike.setImageBitmap(slika);
        pretvoriSlikoVBesedilo(slika, new OZPPovratniKlic() {
            @Override
            public void onOZPRezultat(String besedilo) {
                Log.d("OCR", "Rezultat: " + besedilo);
                pretvorjenoBesedilo = besedilo;
                prikazBesedila.setText(besedilo);
                BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
                //bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(true);
            }
            @Override
            public void onOZPNapaka(Exception e) {
                Log.e("OCR", "Napaka: ", e);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle izhodnoStanje) {
        super.onSaveInstanceState(izhodnoStanje);
        izhodnoStanje.putString("pretvorjenoBesedilo", pretvorjenoBesedilo);
        izhodnoStanje.putByteArray("slika", bitniTok);
    }

    @Override
    protected void onRestoreInstanceState(Bundle shranjenoStanje) {
        super.onRestoreInstanceState(shranjenoStanje);
        pretvorjenoBesedilo = shranjenoStanje.getString("pretvorjenoBesedilo", "");
        prikazBesedila.setText(pretvorjenoBesedilo);
        if (!pretvorjenoBesedilo.isEmpty()) {
            BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
            bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).setEnabled(true);
        }
        bitniTok = shranjenoStanje.getByteArray("slika");
        if (bitniTok != null) {
            Bitmap slika = BitmapFactory.decodeByteArray(bitniTok, 0, bitniTok.length);
            prikazSlike.setImageBitmap(slika);
        }
    }

    @Override
    public void obKlikuNaprej(){
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        boolean gumbOmogocen = bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).isEnabled();
        if(gumbOmogocen) {
            if(pretvorjenoBesedilo.trim().isEmpty()) {
                obvestiloONapaki(getString(R.string.obvestilo_sporocilo_napaka_citat_prazno_izberi));
                return;
            }
            Intent odpriIzberi = new Intent(PretvoriActivity.this, IzberiActivity.class);
            odpriIzberi.putExtra("Besedilo", pretvorjenoBesedilo);
            startActivity(odpriIzberi);
        } else {
            Toast.makeText(this, "Ponovno slikaj.", Toast.LENGTH_LONG).show();
        }
    }

    public interface OZPPovratniKlic {
        //OCR (Optical Character Recognition) - OZP
        void onOZPRezultat(String besedilo);
        void onOZPNapaka(Exception e);
    }

    public void pretvoriSlikoVBesedilo(Bitmap slika, OZPPovratniKlic povratniKlic) {
        //ML-kit: https://developers.google.com/ml-kit/vision/text-recognition/v2/android#java
        TextRecognizer prepoznavaBesedila = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage slikaZaPrepoznavo = InputImage.fromBitmap(slika, 0);
        Task<Text> rezultatPrepoznave = prepoznavaBesedila.process(slikaZaPrepoznavo)
                .addOnSuccessListener(text -> {String rezultat = text.getText(); povratniKlic.onOZPRezultat(rezultat);
                    //Log.d("Obvestilo","Uspešno");
                })
                .addOnFailureListener(e -> {povratniKlic.onOZPNapaka(e);
                            //Log.d("Obvestilo","Neuspešno");
                });
    }
}