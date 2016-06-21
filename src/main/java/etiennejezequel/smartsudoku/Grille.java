package etiennejezequel.smartsudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class Grille extends View {

    private Canvas canvas;
    private Network network;
	private int n;
	private boolean win = false;
	private Paint paint1;
	private Paint paint2;
	private Paint paint3;
	private Paint paint4;


	private int[][] matrix = new int[9][9];
	private boolean[][] fixIdx = new boolean[9][9];

	public Grille(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Grille(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Grille(Context context) {
		super(context);
		init();
	}

	public void setWin(boolean win){
		this.win = win;
	}

    public void resetGrille(){
        network.execute(this);
    }

	private void init() {
		//set une grille par défaut
        set("672145398145983672389762451263574819958621743714398526597236184426817935831459267");
        network = new Network();
        resetGrille();
		paint1 = new Paint();
		paint1.setTextSize(30);
		paint1.setAntiAlias(true);
		paint1.setColor(Color.BLACK);
		paint2 = new Paint();
		paint2.setTextSize(30);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.RED);
		paint3 = new Paint();
		paint3.setAntiAlias(true);
		paint3.setStyle(Paint.Style.FILL);
		paint3.setColor(Color.LTGRAY);
		paint4 = new Paint();
		paint4.setAntiAlias(true);
		paint4.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
		int screenWidth = getWidth();
		int screenHeight = getHeight();
		int x = Math.min(screenWidth, screenHeight);
		switch (n = (x / 9) - (1 - (x % 2))) {
		}
		this.canvas.drawColor(Color.WHITE);
		String s;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				s = "" + (matrix[j][i] == 0 ? "" : matrix[j][i]);
				if (fixIdx[j][i]) {
					this.canvas.drawRect(new Rect(i*x/9, j*x/9, i*x/9+x/9, j*x/9+x/9),paint3);
					this.canvas.drawText(s, i * n + (n / 2) - (n / 10), j * n
							+ (n / 2) + (n / 10), paint2);
				}
				else {
					this.canvas.drawText(s, i * n + (n / 2) - (n / 10), j * n
							+ (n / 2) + (n / 10), paint1);
				}
			}
		}

		//Dessine les lignes et collones
		float colSize = x/9;
		for(int i=0;i<=9;++i){
			if(i != 3 && i != 6) {
				this.canvas.drawLine(i*colSize,0,i*colSize,x,paint1);
				this.canvas.drawLine(i*colSize+1,0,i*colSize+1,x,paint1);
				this.canvas.drawLine(0,i*colSize+1,x,i*colSize+1,paint1);
				this.canvas.drawLine(i*colSize-1,0,i*colSize+1,x,paint1);
				this.canvas.drawLine(0,i*colSize-1,x,i*colSize+1,paint1);
				this.canvas.drawLine(0,i*colSize,x,i*colSize,paint1);
			}
			else {
				this.canvas.drawLine(i*colSize,0,i*colSize,x,paint2);
				this.canvas.drawLine(i*colSize+1,0,i*colSize+1,x,paint2);
				this.canvas.drawLine(0,i*colSize+1,x,i*colSize+1,paint2);
				this.canvas.drawLine(i*colSize-1,0,i*colSize+1,x,paint2);
				this.canvas.drawLine(0,i*colSize-1,x,i*colSize+1,paint2);
				this.canvas.drawLine(0,i*colSize,x,i*colSize,paint2);
			}
		}

		if(win){
			this.paint4.setColor(Color.GREEN);
		}
		else{
			this.paint4.setColor(Color.RED);
		}
		this.canvas.drawRect(new Rect(0, x, x, x+50),paint4);
	}

	public int getXFromMatrix(int x) {
		// Renvoie l'indice d'une case à partir du pixel x de sa position h
		return (x / n);
	}

	public int getYFromMatrix(int y) {
		// Renvoie l'indice d'une case à partir du pixel y de sa position v
		return (y / n);
	}

	public void set(String s, int i) {
		// Remplir la ième ligne de la matrice matrix avec un vecteur String s
		int v;
		for (int j = 0; j < 9; j++) {
			v = s.charAt(j) - '0';
			matrix[i][j] = v;
			if (v == 0)
				fixIdx[i][j] = false;
			else
				fixIdx[i][j] = true;
		}
	}

	public void set(String s) {
		// Remplir la matrice matrix à partir d'un vecteur String s
		for (int i = 0; i < 9; i++) {
			set(s.substring(i * 9, i * 9 + 9), i);
		}
	}

	public void set(int x, int y, int v) {
		// Affecter la valeur v à la case (y,x)
		matrix[y][x] = v;
		isValid();
	}

	//renvois true si la case n'est pas fixe.
	public boolean isNotFix(int x, int y) {
		// Renvoie si la case (y,x) n'est pas fixe
		if(fixIdx[x][y]==false){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isValid() {
		// 1. Vérifier l'existence de chaque numéro (de 1 à 9) dans chaque
		// ligne et chaque colonne
		boolean[] rl = { true, true, true, true, true, true, true, true, true };
		boolean[] rc = { true, true, true, true, true, true, true, true, true };
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (matrix[i][j] == 0)
					return false;
				if (rl[j] && rc[j])
					rl[j] = rc[j] = false;
				else
					return false;
			}
			for (int j = 0; j < 9; j++) {
				rl[matrix[i][j] - 1] = true;
				rc[matrix[i][j] - 1] = true;
			}
		}
		// ------
		// 2. Vérifier l'existence de chaque numéro (de 1 à 9) dans chacun
		// des 9 carrés
		boolean[] r = { true, true, true, true, true, true, true, true, true };
		int w;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				w = 0;
				for (int k = i * 3; k < i * 3 + 3; k++) {
					for (int l = j * 3; l < j * 3 + 3; l++) {
						if (matrix[k][l] == 0)
							return false;
						if (r[w])
							r[w++] = false;
						else
							return false;
					}
				}
				for (int k = i * 3; k < i * 3 + 3; k++) {
					for (int l = j * 3; l < j * 3 + 3; l++) {
						r[matrix[k][l] - 1] = true;
					}
				}
			}
		}
		// ------
		// Gagné

		return true;
	}
}
