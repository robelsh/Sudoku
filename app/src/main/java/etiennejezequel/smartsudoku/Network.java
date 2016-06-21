package etiennejezequel.smartsudoku;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network extends AsyncTask<Grille, Void, String> {
    private Grille grid;
    private String grille;

    @Override
    protected String doInBackground(Grille... params) {
        try {
            URL url = new URL("http://sudoku.rcdinfo.fr");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String readStream = readStream(con.getInputStream());
            this.grid = params[0];
            this.grille = readStream;
            return readStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //S'exécute une fois la tâche finis, il set la grille si il y a un résultat
    protected void onPostExecute(String result) {
        if(this.grille.length() != 0){
            this.grid.set(this.grille);
        }
    }

    //Li le flux de donnée
    private static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] parts = sb.toString().split(":");
        String[] result = parts[parts.length-1].split("\"");

        return result[1];
    }
}
