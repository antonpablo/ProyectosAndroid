package com.pablo.felicitacumple;

import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
public class UsuariosSQLiteHelper extends SQLiteOpenHelper{
	

	String sqlCreate="CREATE TABLE contactos(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT,telefono INT,email TEXT,facebook TEXT,twitter TEXT,sms1 INT,alarma1 INT,año INT,mes INT,dia INT,hora INT,min INT,uri TEXT,sonido TEXT) ";
			
			          
	     
	public UsuariosSQLiteHelper(Context contexto,String nombre,
			                    CursorFactory factory,int version)
	{
		super(contexto, nombre, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db,int versionAnterior,int VersionNueva)
	{
	    Log.w(UsuariosSQLiteHelper.class.getName(),
	            "Upgrading database from version " + versionAnterior + " to "
	                + VersionNueva + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS contactos");
	        onCreate(db);;
	}
 

}
