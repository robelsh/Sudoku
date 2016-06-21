package etiennejezequel.smartsudoku;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Score extends AppCompatActivity {
    private TextView text;
    private TextView scoreTxt;
    private Intent intent;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text = (TextView) findViewById(R.id.scoreTitle);
        scoreTxt = (TextView) findViewById(R.id.scoreTxt);
        playerName = this.getSharedPreferences("name", Context.MODE_PRIVATE).getString("name", "");
        String textString = text.getText().toString()+playerName;
        String score = this.getSharedPreferences("score:" + playerName, Context.MODE_PRIVATE).getString("score:"+playerName,"");
        scoreTxt.setText(score);
        text.setText(textString);
    }

    public void goBack(View view){
        intent = new Intent(Score.this, JeuActivity.class);
        startActivity(intent);
        finish();
    }

    public void resetScore(View view){
        SharedPreferences prefs = this.getSharedPreferences("score:"+playerName,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove("score:" + playerName);
        edit.commit();
        Snackbar snackbar = Snackbar.make(view, "Score réinitialisé.", Snackbar.LENGTH_LONG);
        snackbar.show();
        resetScore();
    }

    public void resetScore(){
        scoreTxt.setText("");
    }

}
