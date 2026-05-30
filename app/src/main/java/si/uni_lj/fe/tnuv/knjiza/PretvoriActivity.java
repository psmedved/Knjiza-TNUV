package si.uni_lj.fe.tnuv.knjiza;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//ML-kit
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PretvoriActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pretvori);
        poimenujStran(R.string.text_pretvori);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        Toolbar topAppMenu = findViewById(R.id.top_app_bar_menu);
        setSupportActionBar(topAppMenu);
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        bottomAppMenu.setOnItemSelectedListener(this::obKlikuSpodnjeNavigacijskeVrstice);
        //Prikaz zajete slike
        byte[] bitniTok = getIntent().getByteArrayExtra("slika");
        Bitmap slika = BitmapFactory.decodeByteArray(bitniTok, 0, bitniTok.length);
        ImageView prikazSlike = findViewById(R.id.polje_slike_pretvori);
        prikazSlike.setImageBitmap(slika);
        //String besedilo = pretvoriSlikoVBesedilo(slika, bitniTok);
        pretvoriSlikoVBesedilo(slika, new OZPPovratniKlic() {
            @Override
            public void onOZPRezultat(String besedilo) {
                Log.d("OCR", "Rezultat: " + besedilo);
                pretvorjenoBesedilo = besedilo;
                TextView prikazBesedila = findViewById(R.id.polje_besedilo_pretvori);
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
    public void obKlikuNaprej(){
        BottomNavigationView bottomAppMenu = findViewById(R.id.bottom_app_bar_menu);
        boolean gumbOmogocen = bottomAppMenu.getMenu().findItem(R.id.btn_n_naprej).isEnabled();
        if(gumbOmogocen) {
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

    private String pretvorjenoBesedilo = "";
    //public String pretvoriSlikoVBesedilo(Bitmap slika, byte[] bitniTok) {
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