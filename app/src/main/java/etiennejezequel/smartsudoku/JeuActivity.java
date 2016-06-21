package etiennejezequel.smartsudoku;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class JeuActivity extends AppCompatActivity {
    private int CHOIX_NUM_FENETRE = 0;
    private String playerName;
    private Grille grille;
    private Intent intent;
    private TextView timerText;
    private ImageView imgWin;
    private Animation animation;
    private TextView welcome;
    private int timerMin = 0;
    private int timerSec = 0;
    private int timerHour = 0;
    private int score = 0;
    private int x = 0;
    private int y = 0;
    private Thread t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        this.imgWin = (ImageView) findViewById(R.id.imgWin);
        this.imgWin.setVisibility(View.INVISIBLE);
        this.welcome = (TextView) findViewById(R.id.welcome);
        this.grille = (Grille) findViewById(R.id.grid);
        this.timerText = (TextView) findViewById(R.id.timer);
        this.playerName = this.getSharedPreferences("name", Context.MODE_PRIVATE).getString("name","");
        this.welcome.setText(welcome.getText().toString()+" "+playerName);
        this.animation= AnimationUtils.loadAnimation(this,R.anim.transition);

        //Déclaration du timer
        this.t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTimer();
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        t.start();

        // Associer la grille de l'interface graphique
        grille.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                x = grille.getXFromMatrix((int) event.getX());
                y = grille.getYFromMatrix((int) event.getY());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    intent = new Intent(JeuActivity.this, ChoixActivity.class);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    //Si l'élément du sudoku n'est pas fix on change d'activité.
                    if (x < 9 && y < 9 && grille.isNotFix(y, x))
                        startActivityForResult(intent, CHOIX_NUM_FENETRE);
                }
                return true;
            }
        });
    }

    //Méthode appelé chaque seconde pour augmenter le timer. Et convertis les secondes en minutes et heures.
    public void updateTimer() {
        this.score += 1;
        this.timerSec += 1;
        if(timerSec == 60){
            this.timerMin += 1;
            this.timerSec = 0;
        }

        if(timerMin == 60){
            this.timerHour += 1;
            this.timerMin = 0;
            this.timerSec = 0;
        }
        //ajoute le temps à l'objet (TextView) timerText
        this.timerText.setText(this.timerHour + "h" + this.timerMin + "m" + this.timerSec + "s");
    }

    //Test la grille si elle est valide quand on appuie sur le bouton valider.
    public void valider(View v) {
        Snackbar snackbar;
        if (grille.isValid()) {
            //Sauvegarde le score personnel du joueur
            SharedPreferences prefs = this.getSharedPreferences("score:"+playerName,Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            if(prefs.getString("score:" + playerName,"").length() == 0){
                edit.putString("score:" + playerName, Integer.toString(score));
                edit.commit();
            }
            else if(Integer.parseInt(prefs.getString("score:" + playerName,"")) > score){
                edit.putString("score:" + playerName, Integer.toString(score));
                edit.commit();
            }

            //affiche le cas de victoire sur la grille
            snackbar = Snackbar.make(v, "Gagné ! en " + this.timerHour + "h" + this.timerMin + "m" + this.timerSec + "s, Bravo : "+this.playerName, Snackbar.LENGTH_LONG);
            this.imgWin.setVisibility(View.VISIBLE);
            this.imgWin.setAnimation(this.animation);
            this.welcome.setVisibility(View.INVISIBLE);
            this.grille.setWin(true);
            this.grille.invalidate();
            findViewById(R.id.valider).setEnabled(false);
        }
        else {
            snackbar = Snackbar.make(v, "Perdu, essayez encore.", Snackbar.LENGTH_LONG);
        }
        snackbar.show();
    }

    //Affiche le choix dans la grille du jeu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            this.grille.set(x, y, data.getIntExtra("result", 0));
        }
    }

    //Appelé quand on clic sur Retour. Retour à l'accueil.
    public void goBack(View view){
        intent = new Intent(JeuActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //Appelé quand on clic sur Score. Affiche le meilleur score du joueur dans une autre activitées.
    public void displayScore(View view){
        intent = new Intent(JeuActivity.this, Score.class);
        startActivity(intent);
        finish();
    }

    //Appelé quand on clic sur reset. Reset la grille.
    public void reset(View view){
        intent = new Intent(JeuActivity.this, JeuActivity.class);
        startActivity(intent);
        finish();
    }
}
