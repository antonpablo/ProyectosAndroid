package com.pablo.felicitacumple;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Galeria extends Activity {
	
	private String URIMG;
	private static int RESULT_LOAD_IMAGE = 1;
	private int idd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_galeria);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		idd= getIntent().getIntExtra("idGaleria",-5);
		
		
		GridView gridview = (GridView) findViewById(R.id.gridView1);
	    gridview.setAdapter(new AdapterGalery(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	
	        	//guardar la uri de la imagen selecinada de la galeria de la app
	        	URIMG="android.resource://com.pablo.felicitacumple/drawable/regalo"+(position+1);
	        	guardarSQL();

	        }
	    });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		
		getMenuInflater().inflate(R.menu.galeria, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(android.R.id.home == id)
      	{      	
      		startActivityAfterCleanup(AgregarUser.class);
            return true;
      	}
		if (id == R.id.action_new) {
			galeriaInterna();
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
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (resultCode == RESULT_OK) {
	           
            Uri selectedImageUri = data.getData();
            URIMG=selectedImageUri .toString();
            guardarSQL();
            
        }
    }
	
	public void galeriaInterna ()
	{
		//ENTREDA A LA GALERIA INTERNA DE IMAGENES DEL TLF
		Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         
        startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  savedInstanceState.putInt("MyInt", idd);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  idd = savedInstanceState.getInt("MyInt");
	}
	
	
	public void guardarSQL()
	{
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getWritableDatabase();
		String consulta="UPDATE contactos SET ";
		       consulta+="uri='"+URIMG+"' ";
		       consulta+="WHERE id='"+idd+"'";
	   db.execSQL(consulta);
	  
	   Intent inte=new Intent(Galeria.this,AgregarUser.class);
		inte.putExtra("idModif",idd);
		startActivity(inte);
	}

}
