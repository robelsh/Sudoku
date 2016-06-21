package etiennejezequel.smartsudoku;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Snackbar snackbar;
    private Intent intent;
    private Button playButton;
    private Button aboutButton;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.name = (EditText) findViewById(R.id.name);
        this.playButton = (Button) findViewById(R.id.play);
        this.playButton.animate().translationY(200);
        this.aboutButton = (Button) findViewById(R.id.about);
        this.aboutButton.animate().translationY(200);

        if(!this.getSharedPreferences("name", Context.MODE_PRIVATE).getString("name","").isEmpty()){
            name.setText(this.getSharedPreferences("name", Context.MODE_PRIVATE).getString("name",""));
        }
    }

    //Appelé quand on clic sur Jouer. Récupère et stoque le nom du joueur et lance le jeu.
    public void startJeu(View view) {
        //si il n'y a pas de nom, une SnackBar s'affiche.
        if(name.getText().length()==0){
            this.snackbar = Snackbar.make(view, "Entrez votre nom", Snackbar.LENGTH_LONG);
            this.snackbar.show();
        }
        else{
            this.intent = new Intent(MainActivity.this,JeuActivity.class);
            SharedPreferences prefs = this.getSharedPreferences("name",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("name", name.getText().toString());
            edit.commit();
            startActivity(intent);
        }
    }

    //Appelé quand on clic sur About. Affiche l'activité about.
    public void about(View view) {
        this.intent = new Intent(MainActivity.this,About.class);
        startActivity(intent);
    }
}
