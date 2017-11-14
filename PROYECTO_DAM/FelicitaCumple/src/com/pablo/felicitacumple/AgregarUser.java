package com.pablo.felicitacumple;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

public class AgregarUser extends Activity {

	
	private String cor,nom,tlf,date="";
	private int year,month,day,hour,minutes,idd=-5;
	private int sms1=0,alar1=0;
	private boolean swException=false,swException2=false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_user);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		 
       
	    idd= getIntent().getIntExtra("idModif",-5);
		
		if (idd>=0)
		    sqlLecturaModificado();
		
		if (getIntent().getIntExtra("idBuCa",0)==1)
		{
			String date[]=getIntent().getStringExtra("buFecha").split("-");
			DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
			dp.updateDate(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));

		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.agregar_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_new) {
			agregar();
			return true;
		}
		if(android.R.id.home == id)
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

	
  //BOTON VOLVER FISICO DEL MOViL
 
  @Override
  public void onBackPressed() {
    Log.d("CDA", "onBackPressed Called");
    Intent inte=new Intent(AgregarUser.this,Contactos.class);
    startActivity(inte); 
    return;
 }  

	public void autosms(View v)
	{
		CheckBox sms=(CheckBox)findViewById(R.id.autosms);
		sms.setChecked(true);
		guardar();
		
		if (swException==true)
			
		{
			lecturaId();
			
			Intent inte=new Intent(v.getContext(),AutoMensaje.class);
			inte.putExtra("idAutoSms",idd);
			v.getContext().startActivity(inte);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "excepcion", Toast.LENGTH_LONG).show();
		}
		
		
	}
	

	public void agregar()
	{
         guardar();
         
		
		if (swException==true)
		{	
			Intent inte=new Intent(AgregarUser.this,Contactos.class);
			startActivity(inte);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "excepcion", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	
	public void alarma(View v)
	{
		CheckBox alarma=(CheckBox)findViewById(R.id.alarma);
		alarma.setChecked(true);
		guardar();	
		
		if (swException==true)
		{	
			lecturaId();
			Intent inte=new Intent(v.getContext(),Sonido.class);
			inte.putExtra("idSonido",idd);
			v.getContext().startActivity(inte);
		}
		else
		{
			TOAST: Toast.makeText(getApplicationContext(), "excepcion", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	
	public void guardar ()
	{  
		try{
		
		String con="";
	    
		EditText nombre=(EditText)findViewById(R.id.editText1);
		nom=nombre.getText().toString();
		
		tlf="";
		EditText telefono=(EditText)findViewById(R.id.editText3);
		tlf=telefono.getText().toString();
		
        EditText correo=(EditText)findViewById(R.id.editText4);
	    cor=correo.getText().toString();
		
		String fac="";
		
		String twi="";
		
        //Excepcion por si no introduce ningun nombre o ningun tlf.
		if (nom.equals(con))
		{
			Toast.makeText(getApplicationContext(), "el nombre no puede quedar en blanco", Toast.LENGTH_LONG).show();
		    throw new Exception("nombre vacio");
		}
        
		CheckBox sms=(CheckBox)findViewById(R.id.autosms);
		if (sms.isChecked()==true)
		   sms1=1;
		
		else 
		   sms1=0;
		
		//TRATAMIENTO DE LA FECHA DE CUMPLEAÑOS
		
		DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
		
		year =dp.getYear();
        month = dp.getMonth()+1;
        day = dp.getDayOfMonth(); 
        
	        
	      
	    //TRATAMIENTO DE LA HORA DE CUMPLEAÑOS
	        
	    TimePicker time=(TimePicker)findViewById(R.id.timePicker1);
	        
	    hour=time.getCurrentHour();
        minutes=time.getCurrentMinute();
	       

		if (idd<0)
		{
			//Excepcion por si introducen un nombre igual.
			ExceptionNombre();
			if (swException2==true)
			{
				swException2=false;
				Toast.makeText(getApplicationContext(), "el nombre esta repetido", Toast.LENGTH_LONG).show();
		        throw new Exception("nombre repetido");
			}
			//Escepcion como maximo 4 numeros igual
			ExceptionTelefono();
			if (swException2==true)
			{
				swException2=false;
				Toast.makeText(getApplicationContext(), "maximo 4 telefonos iguales", Toast.LENGTH_LONG).show();
		        throw new Exception("max 4 tlf iguales");
			}
			sqlEscritura(nom,tlf,cor,fac,twi,sms1,year,month,day,hour,minutes);
			//AgregarEvento(year,month,day,hour,minutes,nom,cor);
		}
		    
		else
		   sqlActualizar(nom,tlf,cor,fac,twi,sms1,year,month,day,hour,minutes);
		
		
		swException=true;
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void ExceptionNombre()
	{
		
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		int cuenta=0;
		boolean sw=false;
		if (db!=null)
		{
			consulta+="SELECT nombre FROM contactos WHERE nombre='"+nom+"'";
		    Cursor c =db.rawQuery(consulta,null);
			if (c!=null)
			   cuenta=c.getCount();
		}
		
		if (cuenta>0)
			swException2=true;
	}
	
	public void ExceptionTelefono()
	{
		
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		int cuenta=0;
		boolean sw=false;
		if (db!=null)
		{
			consulta+="SELECT telefono FROM contactos WHERE telefono='"+tlf+"'";
		    Cursor c =db.rawQuery(consulta,null);
			if (c!=null)
			   cuenta=c.getCount();
		}
		
		if (cuenta>4)
			swException2=true;
	}
	
	public void AgregarEvento(int año,int mes,int dia,int hora, int min,String nombre,String email)
	{
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(año, mes, dia, hora, min);
		
		hora+=1;
		Calendar endTime = Calendar.getInstance();
		endTime.set(año, mes, dia, hora, min);
		Intent intent = new Intent(Intent.ACTION_INSERT)
		        .setData(Events.CONTENT_URI)
		        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
		        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
		        .putExtra(Events.TITLE, "Cumpleaños de "+nombre)
		        .putExtra(Events.DESCRIPTION, "Group class")
		        .putExtra(Events.EVENT_LOCATION, "The word")
		        .putExtra(Events.HAS_ALARM,1)
		        .putExtra(Events.LAST_DATE,86400000) //cada dia
		        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
		        .putExtra(Intent.EXTRA_EMAIL, email);
		startActivity(intent);
		
		
		Toast.makeText(getApplicationContext(), "evento bien", Toast.LENGTH_SHORT).show(); 
	}
	
	

	public void sqlActualizar(String nom,String tlf,String cor,String fac,String twi,int sms,int año,int mes,int dia,int hora,int min)
	{
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getWritableDatabase();
		String consulta="UPDATE contactos SET ";
		       consulta+="nombre='"+nom+"',";
		       consulta+="telefono="+tlf+",";
		       consulta+="email='"+cor+"',";
		       consulta+="facebook='"+fac+"',";
		       consulta+="twitter='"+twi+"',";
		       consulta+="sms1="+sms+",";
		       consulta+="año="+año+",";
		       consulta+="mes="+mes+",";
		       consulta+="dia="+dia+",";
		       consulta+="hora="+hora+",";
		       consulta+="min="+min+" ";
		       consulta+="WHERE id='"+idd+"'";
		       
		db.execSQL(consulta);
	}
	
	public void sqlEscritura(String nom,String tlf,String cor,String fac,String twi,int sms,int año,int mes,int dia,int hora,int min)
	{
		
       UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
	   SQLiteDatabase db= basedatos.getWritableDatabase();
	   
	   String imagen="android.resource://com.pablo.felicitacumple/drawable/regalo1";
		
       String consulta="INSERT INTO contactos(nombre,telefono,email,facebook,twitter,sms1,alarma1,año,mes,dia,hora,min,uri,sonido)\n VALUES ";
		
	   consulta+="('"+nom+"','"+tlf+"','"+cor+"','"+fac+"','"+twi+"',"+sms+",0,"+año+","+mes+","+dia+","+hora+","+min+",'"+imagen+"','sonido')";
		 
		db.execSQL(consulta);
		db.close(); 
	}
	
	
	//MODIFICAR REGISTROS
 public void sqlLecturaModificado()
 {
	UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
	SQLiteDatabase db= basedatos.getReadableDatabase();
	String consulta="";
	
	if (db!=null)
	{
		consulta+="SELECT nombre,telefono,email,facebook,twitter,sms1,alarma1,año,mes,dia,hora,min FROM contactos WHERE id="+idd+";";
	    
		Cursor c =db.rawQuery(consulta,null);
		
		if (c!=null)
		{
			
			String []vector=new String[c.getCount()];
			int  []tiempo=new int[c.getCount()];
		    c.moveToFirst();

		    for (int i = 0; i < c.getCount(); i++)
		      {
		        	 vector[i]=c.getString(0);
		        	 EditText nombre=(EditText)findViewById(R.id.editText1);
		        	 nombre.setText(vector[i]);
		        	 
		        	 vector[i]=c.getString(1);
		        	 EditText tlf=(EditText)findViewById(R.id.editText3);
		        	 tlf.setText(vector[i]);
		        	 
		        	 vector[i]=c.getString(2);
		        	 EditText correo=(EditText)findViewById(R.id.editText4);
		        	 correo.setText(vector[i]);
		        	 
		             vector[i]=c.getString(5);
		        	 CheckBox sms=(CheckBox)findViewById(R.id.autosms);
		        	 if(Integer.parseInt(vector[i])==1)
		        	 {
		        		sms.setChecked(true); 
		        	 }
		        	 
		        	 vector[i]=c.getString(6);
		        	 CheckBox alarma=(CheckBox)findViewById(R.id.alarma);
		        	 if(Integer.parseInt(vector[i])!=0)
		        	 {
		        		alarma.setChecked(true);  
		        	 }
		        	
		        	 //TRATAMIENTO DE LA FECHA DE CUMPLEAÑOS
		        	 
		        	 vector[i]=c.getString(7)+"-";
		        	 vector[i]+=c.getString(8)+"-";
		        	 vector[i]+=c.getString(9);
		        	 String[] dateArr=vector[i].split("-");
		        	 DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
		             dp.updateDate(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1])-1, Integer.parseInt(dateArr[2]));
		     		
		 	     
		 	        
		 	    //TRATAMIENTO DE LA HORA DE CUMPLEAÑOS
		 	        
		 	        TimePicker time=(TimePicker)findViewById(R.id.timePicker1);
		 	       tiempo[i]=c.getInt(10);
		 	       time.setCurrentHour(tiempo[i]);
		 	      
		 	       tiempo[i]=c.getInt(11);
				   time.setCurrentMinute(tiempo[i]);
		        	 
		        	 c.moveToNext();
		      }
		 
			 
			 c.close();
		}
	}
  }
 
 public void lecturaId()
 {  UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
	SQLiteDatabase db= basedatos.getReadableDatabase();
	String consulta="";
	
	if (db!=null)
	{
		consulta+="SELECT id,nombre FROM contactos WHERE nombre='"+nom+"'";
	    
		Cursor c =db.rawQuery(consulta,null);
		
		if (c!=null)
		{
			int  []ids=new int[c.getCount()];
		    c.moveToFirst();

		    for (int i = 0; i < c.getCount(); i++)
		      {
		        	ids[i]=c.getInt(0);
		        	
		      }
		  
		    c.close();
		    idd=ids[0];
		}
	}
 }



 
 public void galeria(View v)
 {
	 guardar();
	 
	 if (swException==true)
			
		{
		   lecturaId();
           Intent inte=new Intent(AgregarUser.this,Galeria.class);
		   inte.putExtra("idGaleria",idd);
		   v.getContext().startActivity(inte);
		}
		else
		{
			 Toast.makeText(getApplicationContext(), "excepcion", Toast.LENGTH_LONG).show();
		}
 }
 
 public void facebook(View v)
 {
    
 }

 public void twitter(View v)
 {
	//  guardar();	
	 Twitter twitter = TwitterUtil.getInstance().getTwitter();
	  
      long cursor = -1;
      IDs ids;
      System.out.println("Listing followers's ids.");
      try{
      do {
              ids = twitter.getFollowersIDs("username", cursor);
          for (long id : ids.getIDs()) {
              System.out.println(id);
              User user = twitter.showUser(id);
              Toast.makeText(this,"user:"+user.getName(), Toast.LENGTH_SHORT).show();
             
          }
      } while ((cursor = ids.getNextCursor()) != 0);
      }catch(Exception e){}
 }
 



 public void volver(View v)
	{
		/*Intent inte=new Intent(v.getContext(),Contactos.class);
		v.getContext().startActivity(inte);*/
	}
}
