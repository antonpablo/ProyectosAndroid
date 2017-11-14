package com.pablo.felicitacumple;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class CalendarioActivity extends Activity {

	
	private final int Tiempo = 2000;

    @Override
    
		protected void onCreate(Bundle savedInstanceState) {

			    super.onCreate(savedInstanceState);     

			    //Oculta la barra de título de la aplicación.       
        		
				requestWindowFeature(Window.FEATURE_NO_TITLE);  

				// Oculta la barra de notificaciones.
         
				   this.getWindow().setFlags(
		       				                 WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		       				                 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        
				// Tiempo 
				
				Handler handler = new Handler();

				handler.postDelayed(IniciarMenu(), Tiempo);
	
				setContentView(R.layout.activity_calendario);
		}
	
		private Runnable IniciarMenu()
		{
			
			Runnable res = new Runnable()
			{
				public void run()
				{
					Intent intent = new Intent(CalendarioActivity.this, Calendar2.class);
					startActivity(intent);
					finish();
				}
			};
			return res;
		}
}
