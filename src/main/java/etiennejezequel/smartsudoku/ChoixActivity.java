package etiennejezequel.smartsudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChoixActivity extends AppCompatActivity {
    private ListView list;
    private Intent intent;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);
        String [] liste = {"1","2","3","4","5","6","7","8","9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, liste);
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

        //écoute sur la liste. Renvois l'élément sélectionné à l'activité Jeu.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                intent = new Intent();
                result = position +1;
                intent.putExtra("result",result);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }
}
