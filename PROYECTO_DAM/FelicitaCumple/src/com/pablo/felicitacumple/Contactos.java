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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class Contactos extends Activity {
	
	    private String nombre[],cumple[],imagen[]; 
		private int post;
		private int ids[],idd=-5;
		private ListView lv;
		private ArrayList<Persona> personas;
		private AdapterPersona adapter;
		private ArrayList<PendingIntent> intentArray ;
		private  AlarmManager alarmManager;
		private int segundoTotal[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactos);
        getActionBar().setDisplayHomeAsUpEnabled(true);
		 
		//CLASS FECHA

	    ClassFecha fecha=new ClassFecha();
	    fecha.ClassFecha(this.getApplicationContext());
	    segundoTotal= fecha.getSegundoTotal();
		
        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		intentArray = new ArrayList<PendingIntent>(segundoTotal.length);
		cancelAlarma();
		startAlert();
		
		leersql();
		
		personas = new ArrayList<Persona>();
		rellenarArrayList();
         adapter = new AdapterPersona(this, personas);
        lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			 
		    @Override
		    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		        
		    	Intent inte=new Intent(v.getContext(),AutoMensaje.class);
		    	post=position;
	        	idd=ids[post];
				inte.putExtra("idAutoSms",idd);
				v.getContext().startActivity(inte);
		 
		    }
		 
		}); 
	
	
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
	      @Override
	       public boolean onItemLongClick(AdapterView<?> arg0,View v,
	                int posicion, long id) {
	    	    post=posicion;
	        	idd=ids[post];
	        	
	        	ListView lv=(ListView)findViewById(R.id.listView1);
	        	registerForContextMenu(lv);
	        	
	        	return false;
	      }
	    });
		
	 
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contactos, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();  
		if(android.R.id.home == id)
	      	{      	
	      		startActivityAfterCleanup(Calendar2.class);
	            return true;
	      	}
	      	if(R.id.action_new== id)
	      	{
	      		aniadirActionBar();
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

	private void aniadirActionBar() {
        Intent i = new Intent(Contactos.this, AgregarUser.class);
        String s="-8";
        i.putExtra("idModif",s);
        startActivity(i);
    }

	   
    //BOTON VOLVER FISICO DEL MOViL
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent inte=new Intent(Contactos.this,Calendar2.class);
        startActivity(inte); 
        return;
    }   
	public void modificar()
	{
		Intent inte=new Intent(Contactos.this,AgregarUser.class);
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
		
		//CLASS FECHA

	    ClassFecha fecha=new ClassFecha();
	    fecha.ClassFecha(this.getApplicationContext());
	    segundoTotal= fecha.getSegundoTotal();
		
        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		intentArray = new ArrayList<PendingIntent>(segundoTotal.length);
		cancelAlarma();
		startAlert();
		
        leersql();
		
		personas = new ArrayList<Persona>();
		rellenarArrayList();
         adapter = new AdapterPersona(this, personas);
        lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);

	}
	
	public void leersql()
	{
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		
		if (db!=null)
		{
			consulta+="SELECT id,nombre,año,mes,dia,uri FROM contactos ";
		    Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				nombre=new String[c.getCount()];
			    ids=new int[c.getCount()];
			    cumple=new String[c.getCount()];
			    imagen=new String[c.getCount()];
			    
				c.moveToFirst();

			     for (int i = 0; i < c.getCount(); i++) {
			        	   
			        	 ids[i]=c.getInt(0);
			        	 nombre[i]=c.getString(1);
			        	 cumple[i]=c.getString(4)+"-";
			        	 cumple[i]+=c.getString(3)+"-";
			        	 cumple[i]+=c.getString(2);
			        	 imagen[i]=c.getString(5);
			        	 c.moveToNext();
			        }
			     c.close();
			     
			    
			}
		}
	}
	
	private void rellenarArrayList() {
		
		Uri urimg;
		
		for (int i=0;i<ids.length;i++)
		{
			
			urimg=Uri.parse(imagen[i]);
			personas.add(new Persona("Nombre :               "+nombre[i],"Fecha Cumpleños :   "+cumple[i], urimg));
			
		}
				
		
	}
	
	 public void startAlert(){

		 //ALARMA MANAGERRRR!!!!!
	     
		for(int i = 0; i < segundoTotal.length; ++i)
		  {
			
			   //añadir 1 año a los segundo negaticvos
			 
			  if(segundoTotal[i]<0)
			  {
				  segundoTotal[i]=(segundoTotal[i]*10)+315569251;  
			  }
			  else if(segundoTotal[i]>0)
			  {
				  segundoTotal[i]*=10;
			  }
			  
			  
			   Intent intent = new Intent(this, MyBroadcastReceiver.class);
			     
			   PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), i, intent, 0);
			   long año=315569251; //faltan 100 para llegar al año en mill
			  
			   alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + segundoTotal[i]*100, pendingIntent);
		  //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + segundoTotal[i]*100, 86400000, pendingIntent);
			  
			   intentArray.add(pendingIntent);
			   Toast.makeText(this,"segundos:"+ segundoTotal[i]/10, Toast.LENGTH_SHORT).show();
		  }
		}

		 
		 public void cancelAlarma()
		 {
	         if(intentArray.size()>0){
			       for(int i=0; i<intentArray.size(); i++){
			    	   alarmManager.cancel(intentArray.get(i));
			      }
			       intentArray.clear();
			    }
		 }
		 
	

	
	public void buscar(View v)
	{   EditText nombre=(EditText)findViewById(R.id.editText1);
		String nom=nombre.getText().toString();
        Intent inte=new Intent(v.getContext(),Busqueda.class);
		inte.putExtra("buscar",nom);
		v.getContext().startActivity(inte);
		
	}
	
}
