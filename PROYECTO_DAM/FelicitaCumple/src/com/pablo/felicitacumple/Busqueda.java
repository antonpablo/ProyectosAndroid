package com.pablo.felicitacumple;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.os.Build;

public class Busqueda extends Activity {
	
	private String nombre,vector[],imagen[],cumple[],date;
	private int idCalendar=0,mes,dia,contSqlCa,ids[],idd=-5;
	
    private ListView lv;
	private ArrayList<Persona> personas;
	private AdapterPersona adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		idCalendar=getIntent().getIntExtra("idCalendar",0);
		if (idCalendar==1)
		{
			//SACAR LA LISTA CON LOS CUMPLEAÑOS QUE TENGAS IGUAL DIA MES Y AÑO
			date= getIntent().getStringExtra("date");
			
			String res[]=date.split("-");
			mes=Integer.parseInt(res[1]);
			dia=Integer.parseInt(res[2]);
			
		//	Toast.makeText(this,"mes:"+mes+" dia:"+dia, Toast.LENGTH_SHORT).show();
			
			leerSqlCalendar();
			
			if (contSqlCa==0)
			{
				Intent inte=new Intent(Busqueda.this,AgregarUser.class);
				inte.putExtra("idModif",-5);
				inte.putExtra("idBuCa",1);
				inte.putExtra("buFecha", date);
				startActivity(inte);
			}else if (contSqlCa==1)
			{
				
				Intent inte=new Intent(Busqueda.this,AgregarUser.class);
				inte.putExtra("idModif",ids[0]);
				startActivity(inte);
			}
			
		}
		else
		{
			nombre= getIntent().getStringExtra("buscar");
			leersqlNombre();
		}
		
		
		
        personas = new ArrayList<Persona>();
		
        rellenarArrayList();
        
        adapter = new AdapterPersona(this, personas);
        lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			 
		    @Override
		    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		        idd=ids[position];
	        	modificar();
		 
		    }
		 
		}); 
	
		
	
		
	    lv.setOnItemLongClickListener(new OnItemLongClickListener() {
	      @Override
	       public boolean onItemLongClick(AdapterView<?> arg0,View v,
	                int posicion, long id) {
	    	    
	        	idd=ids[posicion];
	        	registerForContextMenu(lv);
	        	
	        	return false;
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busqueda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(android.R.id.home == item.getItemId())
      	{      	
      		startActivityAfterCleanup(Contactos.class);
            return true;
      	}
		return super.onOptionsItemSelected(item);
	}
	

    private void startActivityAfterCleanup(Class<?> cls) {
        
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo)
	{
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_ctx, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	 
	    switch (item.getItemId()) {
	        case R.id.mod:
	        	modificar();
	            return true;
	        case R.id.eli:
	        	eliminar();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
    //BOTON VOLVER FISICO DEL MOViL
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent inte=new Intent(Busqueda.this,Contactos.class);
        startActivity(inte); 
        return;
    }   
	
	public void modificar()
	{
		Intent inte=new Intent(Busqueda.this,AgregarUser.class);
		inte.putExtra("idModif",idd);
		startActivity(inte);
	}
	
	public void eliminar()
	{
		
        UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		
	   if (db!=null)
		{
			consulta+="DELETE FROM contactos where id="+idd;
			db.execSQL(consulta);
			
		}
		
		db.close();
		
		
		MensajeSQLiterHelper bb=new  MensajeSQLiterHelper (this,"mensaje",null,1);
		SQLiteDatabase db2= bb.getReadableDatabase();
		 
		if (db2!=null)
			{
				consulta="DELETE FROM mensaje where idSms="+idd;
				db2.execSQL(consulta);
				
			}
		db2.close();
		
		personas = new ArrayList<Persona>();
		rellenarArrayList();
         adapter = new AdapterPersona(this, personas);
        lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		return;

	}
	
	
	public void adaptador()
	{

		lv=(ListView)findViewById(R.id.listView1);
	     
		
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,vector);
	    adaptador.setDropDownViewResource(android.R.layout.simple_list_item_1); 
		lv.setAdapter(adaptador); 
	}


	public void leersqlNombre()
	{

		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		
		if (db!=null)
		{
			consulta+="SELECT id,nombre,año,mes,dia,uri FROM contactos WHERE nombre like '"+nombre+"%'";
		    Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				vector=new String[c.getCount()];
			    ids=new int[c.getCount()];
			    cumple=new String[c.getCount()];
			    imagen=new String[c.getCount()];
				c.moveToFirst();

			     for (int i = 0; i < c.getCount(); i++) {
			        	   
			    	 ids[i]=c.getInt(0);
		        	 vector[i]=c.getString(1);
		        	 cumple[i]=c.getString(4)+"-";
		        	 cumple[i]+=c.getString(3)+"-";
		        	 cumple[i]+=c.getString(2);
		        	 imagen[i]=c.getString(5);
		        	 c.moveToNext();
			        }
			     c.close();
			}
			else
			{
				Toast.makeText(this,"este nombre no se encuentra en la lista", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(Busqueda.this, Contactos.class);
		        startActivity(i);

			}	
		}
	}
	
	private void rellenarArrayList() {
		
		Uri urimg;
		
		for (int i=0;i<ids.length;i++)
		{
			urimg=Uri.parse(imagen[i]);
			personas.add(new Persona("Nombre :                       "+vector[i],"Fecha Cumpleños :   "+cumple[i], urimg));
		}
	}
	
	
	public void leerSqlCalendar()
	{

		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		
		if (db!=null)
		{
			consulta+="SELECT id,nombre,año,mes,dia,uri FROM contactos WHERE mes="+mes+" AND dia="+dia;
		    Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				contSqlCa=c.getCount();
				vector=new String[c.getCount()];
			    ids=new int[c.getCount()];
			    cumple=new String[c.getCount()];
			    imagen=new String[c.getCount()];
				c.moveToFirst();

			     for (int i = 0; i < c.getCount(); i++) {
			        	   
			    	 ids[i]=c.getInt(0);
		        	 vector[i]=c.getString(1);
		        	 cumple[i]=c.getString(4)+"-";
		        	 cumple[i]+=c.getString(3)+"-";
		        	 cumple[i]+=c.getString(2);
		        	 imagen[i]=c.getString(5);
		        	 c.moveToNext();
			        }
			     c.close();
			}
			else
			{
				Toast.makeText(this,"este nombre no se encuentra en la lista", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(Busqueda.this, Contactos.class);
		        startActivity(i);

			}	
		}
	}


}
