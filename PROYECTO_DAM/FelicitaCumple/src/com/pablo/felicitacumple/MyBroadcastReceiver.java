package com.pablo.felicitacumple;


import java.io.IOException;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;
 
public class MyBroadcastReceiver extends BroadcastReceiver{
	
    private int alarma[],autosms[],telefono[],sms1[],email1[],fac1[],twi1[],wha1[],id[],año[];
    private int añoActual,mesActual,diaActual,horaActual,minActual,contSql=0;
    private String nombre[],email[],facebook[],twitter[];
    private String sms[],asunto[];
  
   public void onReceive(Context context, Intent intent){
	      
	//Toast.makeText(context,"ALRAMAAAAAA!!!!!", Toast.LENGTH_SHORT).show();
    //El vibrador del dispositivo
    Vibrator vibrator =(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    vibrator.vibrate(2000);
    
    //CALENDARIO
  
    Calendar ca=Calendar.getInstance();
    
    mesActual=ca.get(Calendar.MONTH)+1;
    diaActual=ca.get(Calendar.DAY_OF_MONTH);
    horaActual=ca.get(Calendar.HOUR_OF_DAY);
    minActual=ca.get(Calendar.MINUTE);
    
   
    leerSql(context);
    

	   //NOTIFICACION
	    Notification.Builder builder = new Notification.Builder(context);
	    
	    
	    Intent notIntent =
	    	    new Intent(context, CalendarioActivity.class);
	    	 
	    	PendingIntent contIntent =
	    	    PendingIntent.getActivity(
	    	        context, id[0], notIntent, 0);
	    	 
	    	builder.setContentIntent(contIntent);
	    	
	    	NotificationManager mNotificationManager =
	    		    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	    		 
	    		mNotificationManager.notify(id[0], getDefaultNotification(builder));
    
if(contSql>0)
{
 if (autosms[0]!=0)
   {
	   
	   Intent i = new Intent(context, Calendar2.class);
	   i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   
	   if(contSql>0)
	   {   
		  if (sms1[0]==1)
	   {
		  //enviar sms al movil del cumpleaños
		 try{
			   String tlf=String.format("%d", telefono[0]);
		       SmsManager smsManager = SmsManager.getDefault(); 
			   smsManager.sendTextMessage(tlf, null, sms[0], null, null);
			   Toast.makeText(context,"mensaje enviado", Toast.LENGTH_SHORT).show();
			   
		   } catch (android.content.ActivityNotFoundException ex) {   
			   Toast.makeText(context,"el mensaje a fallado"+ex,Toast.LENGTH_LONG).show();
			   ex.printStackTrace();
		   }
		  
		  
	   }
	   
	   if (email1[0]==1)
	   {
		   i.putExtra("idEmail", 1);
		   i.putExtra("SmsEmail",email[0]+","+asunto[0]+","+sms[0]);
	   }
	   
	  if(fac1[0]==1)
	   {
		   i.putExtra("idFac", 1);
		   i.putExtra("SmsFac",sms[0]);
	   }
	   
	   if(twi1[0]==1)
	   {
		   i.putExtra("idTwi", 1);
		   i.putExtra("SmsTwi",sms[0]);
	   }
	   
	   if(wha1[0]==1)
	   {
	       i.putExtra("idWha", 1);
		   i.putExtra("SmsWha",sms[0]);
	   }
	 }
	   
	   //iniciar calendar2 para enviar el mensaje a las plataformas escojidas
	   context.startActivity(i); 
	   
   }
   
   if (alarma[0]!=0)
   {
	   
	   //SONIDO DE LA ALARMA
	    MediaPlayer mPlayer = MediaPlayer.create(context, alarma[0]);           
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
}  
  
  }
 
	
	public void leerSql(Context cc)
	{
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (cc,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		
		if (db!=null)
		{
			consulta+="SELECT id,nombre,telefono,email,facebook,twitter,sms1,alarma1,año,mes,dia,hora,min FROM contactos WHERE  ";
			consulta+="mes="+mesActual+" AND dia="+diaActual+" AND hora="+horaActual+" AND min="+minActual;
		    
			Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				id=new int[c.getCount()];
				autosms=new int[c.getCount()];
				telefono=new int[c.getCount()];
				alarma=new int[c.getCount()];
				año=new int[c.getCount()];
				email=new String[c.getCount()];
				facebook=new String[c.getCount()];
				twitter=new String[c.getCount()];	
				nombre=new String[c.getCount()];
				c.moveToFirst();

			    for (int i = 0; i < c.getCount(); i++)
			      {
			    	     id[i]=c.getInt(0);
			    	     nombre[i]=c.getString(1);
			        	 telefono[i]=c.getInt(2);
			        	 email[i]=c.getString(3);
			        	 facebook[i]=c.getString(4);
			        	 twitter[i]=c.getString(5);
			        	 autosms[i]=c.getInt(6);
			        	 alarma[i]=c.getInt(7);
			        	 año[i]=c.getInt(8);
			      }
			  
			    c.close();
			}
		}
		
		MensajeSQLiterHelper basedatos2=new MensajeSQLiterHelper (cc,"mensaje",null,1);
		SQLiteDatabase db2= basedatos2.getReadableDatabase();
		
		
		if (db2!=null)
		{
			
			consulta="SELECT idSms,sms,asunto,smss,ema,fac,twi,wha  FROM  mensaje  WHERE  idSms="+id[0];
		    Cursor c =db2.rawQuery(consulta,null);
			
			if (c!=null)
			{
				contSql=c.getCount();
				sms=new String[c.getCount()];
				asunto=new String[c.getCount()];
				sms1=new int[c.getCount()];
				email1=new int[c.getCount()];
				fac1=new int[c.getCount()];
				twi1=new int[c.getCount()];
				wha1=new int[c.getCount()];
				c.moveToFirst();

			    for (int i = 0; i < c.getCount(); i++)
			      {
			        	
			          sms[i]=c.getString(1);
			          asunto[i]=c.getString(2);
			          sms1[i]=c.getInt(3);
			          email1[i]=c.getInt(4);
			          fac1[i]=c.getInt(5);
			          twi1[i]=c.getInt(6);
			          wha1[i]=c.getInt(7);
			          
			      }
			  
			    c.close();
			   
			    
			}
		}
	}
	


	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) private Notification getDefaultNotification(Notification.Builder builder) {
        
		int cumple=añoActual-año[0];
		
		String mensaje="";
		
		if (contSql==0)
		{
			mensaje+="NOT message";
		}
		else
		{
			mensaje+=sms[0];
		}
		
		
		
		builder
        .setSmallIcon(R.drawable.ic_launcher)
        .setWhen(System.currentTimeMillis())
        .setContentTitle("Happy birthday from "+nombre[0])
        .setContentText("Your friends are "+cumple+ " year old")
        .setContentInfo("Message sent : "+mensaje);

		
	return new Notification.BigTextStyle(builder)
    .bigText("Message sent : "+mensaje)
    .setBigContentTitle("Happy birthday from "+nombre[0])
    .build();
    }
	

}