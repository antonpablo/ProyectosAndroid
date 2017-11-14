package com.pablo.felicitacumple;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Sonido extends Activity {

	
	private int idd,sonido=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sonido);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		idd= getIntent().getIntExtra("idSonido",-5);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sonido, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(android.R.id.home == id)
      	{      	
      		startActivityAfterCleanup(AgregarUser.class);
            return true;
      	}
		if (id == R.id.action_new) {
			guardar();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
private void startActivityAfterCleanup(Class<?> cls) {
        
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("idModif",idd);
        startActivity(intent);
      }

public void son1(View v)
{
	sonido=R.raw.sonido1;
	
    MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.sonido1);                      
    try {
    mPlayer.prepare();                       
    } catch (IllegalStateException e) {       
        e.printStackTrace();   
       }
    catch (IOException e) {          
       e.printStackTrace();                    
    	}   
    mPlayer.start();
}

public void son2(View v)
{
	sonido=R.raw.sonido2;
	
    MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.sonido2);                       
    try {
    mPlayer.prepare();                       
    } catch (IllegalStateException e) {       
        e.printStackTrace();   
       }
    catch (IOException e) {          
       e.printStackTrace();                    
    	}   
    mPlayer.start();
}

public void son3(View v)
{
	sonido=R.raw.sonido3;
	
    MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.sonido3);                       
    try {
    mPlayer.prepare();                       
    } catch (IllegalStateException e) {       
        e.printStackTrace();   
       }
    catch (IOException e) {          
       e.printStackTrace();                    
    	}   
    mPlayer.start();
}

public void son4(View v)
{
	sonido=R.raw.sonido4;
    MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.sonido4);                       
    try {
    mPlayer.prepare();                       
    } catch (IllegalStateException e) {       
        e.printStackTrace();   
       }
    catch (IOException e) {          
       e.printStackTrace();                    
    	}   
    mPlayer.start();
}



public void guardar()
{
	    ActualizarSql();
	
	    Intent inte=new Intent(this,AgregarUser.class);
		inte.putExtra("idModif",idd);
		startActivity(inte);
}

public void ActualizarSql()
{
	
	UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
	SQLiteDatabase db= basedatos.getWritableDatabase();
	String consulta="UPDATE contactos SET ";
	       consulta+="alarma1="+sonido+" ";
	       consulta+="WHERE id='"+idd+"'";
   db.execSQL(consulta);
}

}
