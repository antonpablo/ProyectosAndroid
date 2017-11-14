package com.pablo.felicitacumple;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class MensajeSQLiterHelper extends SQLiteOpenHelper{
	
	String  sqlCreate="CREATE TABLE mensaje(idSms INT,sms TEXT,asunto TEXT,smss INT,ema INT,fac INT,twi INT,wha INT)";

    
    
public MensajeSQLiterHelper(Context contexto,String nombre,
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
db.execSQL("DROP TABLE IF EXISTS mensaje");
onCreate(db);;
}

}
