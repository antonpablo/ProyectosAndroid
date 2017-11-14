package com.pablo.felicitacumple;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.AlarmClock;
import android.widget.Toast;

public class ClassFecha {
	
    private int mesActual,diaActual,mesCumple[],diaCumple[],diaTotal[],horaActual,minActual;
	private int horaCumple[],minCumple[],segundoTotal[];
	private ArrayList<PendingIntent> intentArray ;
	private AlarmManager AlarmManager;

	
	public void ClassFecha(Context context)
	{
		 
	    UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (context,"contactos",null,1);
	 	SQLiteDatabase db= basedatos.getReadableDatabase();
	 	String consulta="";
	 	
	 	if (db!=null)
	 	{
	 		consulta="SELECT mes,dia,hora,min FROM contactos ";		 	    
	 		Cursor c =db.rawQuery(consulta,null);
	 		
	 		if (c!=null)
	 		{
	 			 mesCumple=new int[c.getCount()];
	 			 diaCumple=new int[c.getCount()];
	 			 horaCumple=new int[c.getCount()];
	 			 minCumple=new int[c.getCount()];
	 			 
	 			 diaTotal=new int[c.getCount()];
	 			 segundoTotal=new int[c.getCount()];
	 			c.moveToFirst();

	 		    for (int i = 0; i < c.getCount(); i++)
	 		      {
	 		    	     mesCumple[i]=c.getInt(0);
	 		    	     diaCumple[i]=c.getInt(1);
	 		    	     horaCumple[i]=c.getInt(2);
	 		    	     minCumple[i]=c.getInt(3);
	 		    	     c.moveToNext();
	 		      }
	 		  
	 		    c.close();
	 		    
	 		}
	 		
	 	}
	 	
	 	
	}
	
	public int[] getSegundoTotal()
	{
		
		
		 Calendar ca=Calendar.getInstance();
		    
	     mesActual=ca.get(Calendar.MONTH)+1;
	     diaActual=ca.get(Calendar.DAY_OF_MONTH);
	     horaActual=ca.get(Calendar.HOUR_OF_DAY);
	     minActual=ca.get(Calendar.MINUTE);
	   int segundoActual=ca.get(Calendar.SECOND);
	     
	     //cojer de la base de datos
	  
	     
	 	for (int i=0;i<mesCumple.length;i++)
 		{
	 		
	     if (mesCumple[i]>=mesActual)
	     {
	      for(int j=mesActual;j<mesCumple[i];j++)
	       {
	    	 
	    	 if (j==1)
	    	 {
	    		 diaTotal[i]+=31;
	    	 }
	    	 else if (j==2)
	    	 {
	    		 diaTotal[i]+=28;
	    	 }
	    	 else if (j==3)
	    	 {
	    		 diaTotal[i]+=31;
	    	 }
	    	 else if (j==4)
	    	 {
	    		 diaTotal[i]+=30;
	    	 }
	    	 else if (j==5)
	    	 {
	    		 diaTotal[i]+=31;
	    	 }
	    	 else if (j==6)
	    	 {
	    		 diaTotal[i]+=30;
	    	 }
	    	 else if (j==7)
	    	 {
	    		 diaTotal[i]+=31;
	    	 }
	    	 else if (j==8)
	    	 {
	    		 diaTotal[i]+=31;
	    	 }
	    	 else if (j==9)
	    	 {
	    		 diaTotal[i]+=30;
	    	 }
	    	 else if (j==10)
	    	 {
	    		 diaTotal[i]+=31;
	    	 }
	    	 else if (j==11)
	    	 {
	    		 diaTotal[i]+=30;
	    	 }
	    	 else if (j==12)
	    	 {
	    		 diaTotal[i]+=31;
	    	 }
	     }
	    
	     
	     
	     }
	     
	     else if (mesCumple[i]<mesActual)
	     {
	    	 
	    	 for(int j=mesCumple[i];j<mesActual;j++)
		       {
		    	 
		    	 if (j==1)
		    	 {
		    		 diaTotal[i]-=31;
		    	 }
		    	 else if (j==2)
		    	 {
		    		 diaTotal[i]-=29;
		    	 }
		    	 else if (j==3)
		    	 {
		    		 diaTotal[i]-=31;
		    	 }
		    	 else if (j==4)
		    	 {
		    		 diaTotal[i]-=30;
		    	 }
		    	 else if (j==5)
		    	 {
		    		 diaTotal[i]-=31;
		    	 }
		    	 else if (j==6)
		    	 {
		    		 diaTotal[i]-=30;
		    	 }
		    	 else if (j==7)
		    	 {
		    		 diaTotal[i]-=31;
		    	 }
		    	 else if (j==8)
		    	 {
		    		 diaTotal[i]-=31;
		    	 }
		    	 else if (j==9)
		    	 {
		    		 diaTotal[i]-=30;
		    	 }
		    	 else if (j==10)
		    	 {
		    		 diaTotal[i]-=31;
		    	 }
		    	 else if (j==11)
		    	 {
		    		 diaTotal[i]-=30;
		    	 }
		    	 else if (j==12)
		    	 {
		    		 diaTotal[i]-=31;
		    	 }
		     }
	     }
	     
	     diaTotal[i]+=diaCumple[i]-diaActual;
	     
	     segundoTotal[i]=(minCumple[i]-minActual)*60;
	     segundoTotal[i]+=(horaCumple[i]-horaActual)*3600;
	     segundoTotal[i]+=diaTotal[i]*86400;
	     segundoTotal[i]-=segundoActual;
	    
	}
	 	
	 	return segundoTotal;
  }
	


}
