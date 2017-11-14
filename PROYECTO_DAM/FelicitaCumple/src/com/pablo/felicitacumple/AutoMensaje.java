package com.pablo.felicitacumple;


import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class AutoMensaje extends Activity {
	
    private int idd;
	private int cont=0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_mensaje);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
        idd= getIntent().getIntExtra("idAutoSms",-5);
        LeerSql();
		
		if (cont>0)
		    LeerActualizarSql();
		else
			LeerSqlNombreFechaCumple();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.auto_mensaje, menu);
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
      		startActivityAfterCleanup(AgregarUser.class);
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
	

	public void agregar(){
		
		CheckBox sms=(CheckBox)findViewById(R.id.autosms);
		int sms1=0;
		
		//SI ESTA MARCADO ES UN 1.
		
		if (sms.isChecked())
		{
			sms1=1;
		}
		else
		{
			sms1=0;
		}
		
		CheckBox ema=(CheckBox)findViewById(R.id.email);
		int email=0; 
		
		if (ema.isChecked())
		{
			email=1;
		}
		else
		{
			email=0;
		}
		
		CheckBox wha=(CheckBox)findViewById(R.id.wha);
		int whatsapp=0; 
		
		if (wha.isChecked())
		{
			whatsapp=1;
		}
		else
		{
			whatsapp=0;
		}
		
		CheckBox fac=(CheckBox)findViewById(R.id.fac);
		int facebook=0; 
		
		
		if (fac.isChecked())
		{
			facebook=1;
		}
		else
		{
			facebook=0;
		}
		
		
		CheckBox twi=(CheckBox)findViewById(R.id.twi);
		int twitter=0; 
		
		
		if (twi.isChecked())
		{
			twitter=1;
		}
		else
		{
			twitter=0;
		}
		
		
		EditText men=(EditText)findViewById(R.id.editText1);
		String mensaje="";
		mensaje=men.getText().toString();
		
		EditText asu=(EditText)findViewById(R.id.editText2);
		String asunto="";
		asunto=asu.getText().toString();
		
		if (cont>0)
		{
			 ActualizarSql(sms1,email,whatsapp,facebook,twitter,mensaje,asunto);
		}
		else
		{
		    InsertarSql(sms1,email,whatsapp,facebook,twitter,mensaje,asunto);
		}
		
	
	 
	    Intent inte=new Intent(AutoMensaje.this,AgregarUser.class);
	    inte.putExtra("idModif",idd);
		startActivity(inte);
		
		
	}
	
	public void LeerSql()
	{
		MensajeSQLiterHelper basedatos=new MensajeSQLiterHelper (this,"mensaje",null,1);
	    SQLiteDatabase db= basedatos.getReadableDatabase();
	    String consulta="";
	    
	    if (db!=null)
		{
	    	consulta="SELECT idSms FROM mensaje WHERE idSms="+idd;
		    Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				cont=c.getCount();
				 c.close();
			}
		}
}
	
	public void  LeerActualizarSql()
	{
		MensajeSQLiterHelper basedatos=new MensajeSQLiterHelper (this,"mensaje",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		
		if (db!=null)
		{
			consulta="SELECT sms,asunto,smss,ema,fac,twi,wha FROM mensaje WHERE idSms="+idd+";";
		    
			Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				String []vector=new String[c.getCount()];
			    
				c.moveToFirst();

			    for (int i = 0; i < c.getCount(); i++)
			      {
			        	 vector[i]=c.getString(0);
			        	 EditText autosms=(EditText)findViewById(R.id.editText1);
			        	 autosms.setText(vector[i]);
			        	 
			        	 vector[i]=c.getString(1);
			        	 EditText asunto=(EditText)findViewById(R.id.editText2);
			        	 asunto.setText(vector[i]);
			        	 
			        	 vector[i]=c.getString(2);
			        	 CheckBox sms=(CheckBox)findViewById(R.id.autosms);
			        	 if(Integer.parseInt(vector[i])==1)
			        	 {
			        		sms.setChecked(true);
			        	 }
			        	 
			        	 vector[i]=c.getString(3);
			        	 CheckBox ema=(CheckBox)findViewById(R.id.email);
			        	 if(Integer.parseInt(vector[i])==1)
			        	 {
			        		 ema.setChecked(true);
			        	 }
			        	 
			        	 vector[i]=c.getString(4);
			        	 CheckBox fac=(CheckBox)findViewById(R.id.fac);
			        	 if(Integer.parseInt(vector[i])==1)
			        	 {
			        		 fac.setChecked(true); 
			        	 }
			        	 
			        	 vector[i]=c.getString(5);
			        	 CheckBox twi=(CheckBox)findViewById(R.id.twi);
			        	 if(Integer.parseInt(vector[i])==1)
			        	 {
			        		 twi.setChecked(true); 
			        	 }
			        	 
			        	 vector[i]=c.getString(6);
			        	 CheckBox wha=(CheckBox)findViewById(R.id.wha);
			        	 if(Integer.parseInt(vector[i])==1)
			        	 {
			        		 wha.setChecked(true);
			        	 }
			        	
			        c.moveToNext();
			      }
			  
			    c.close();
			}
		}
	}
	
	
	public void ActualizarSql(int sms1,int email1,int wha1,int fac1,int twi1,String sms,String asunto)
	{
        
		MensajeSQLiterHelper basedatos=new MensajeSQLiterHelper (this,"mensaje",null,1);
		SQLiteDatabase db= basedatos.getWritableDatabase();
		String consulta="";
	
				   consulta="UPDATE mensaje SET ";
				   consulta+="sms='"+sms+"',";
				   consulta+="asunto='"+asunto+"',";
			       consulta+="smss="+sms1+",";
			       consulta+="ema="+email1+",";
			       consulta+="wha="+wha1+",";
			       consulta+="fac="+fac1+",";
			       consulta+="twi="+twi1+" ";
			       consulta+="WHERE idSms="+idd+"";
			       
			db.execSQL(consulta);
		    db.close();
	
	}
	
	public void InsertarSql(int sms1,int email1,int wha1,int fac1,int twi1,String sms,String asunto)
	{
		MensajeSQLiterHelper basedatos=new MensajeSQLiterHelper (this,"mensaje",null,1);
		SQLiteDatabase db= basedatos.getWritableDatabase();
		String consulta="";
		
		consulta="INSERT INTO mensaje(idSms,sms,asunto,smss,ema,fac,twi,wha)\n VALUES ";
		consulta+="("+idd+",'"+sms+"','"+asunto+"',"+sms1+","+email1+","+fac1+","+twi1+","+wha1+")";
		db.execSQL(consulta);
	}
	
	public void LeerSqlNombreFechaCumple()
	{
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		String res="";
		
		if (db!=null)
		{
			consulta+="SELECT id,nombre,año FROM contactos WHERE id="+idd;
		    
			Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				String []nombres=new String[c.getCount()];
				int  []años=new int[c.getCount()];
			    c.moveToFirst();

			    for (int i = 0; i < c.getCount(); i++)
			      {
			    	    nombres[i]=c.getString(1);
			        	años[i]=c.getInt(2);
			        	c.moveToNext();
			        	
			      }
			  
			    c.close();
			    
			    Calendar cc=Calendar.getInstance();
			    
			    int añoActual=cc.get(Calendar.YEAR);
			    
			    int añoCumple=añoActual-años[0];
			    
			    res="Your friend "+nombres[0]+" turns "+añoCumple;
			    
			    EditText autosms=(EditText)findViewById(R.id.editText1);
	        	autosms.setText(res);
			    
			}
		}
	  }
}
