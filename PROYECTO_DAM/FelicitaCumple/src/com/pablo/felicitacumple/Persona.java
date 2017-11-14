package com.pablo.felicitacumple;

import android.net.Uri;

public class Persona {
	
	private String nombre;
	private String cumple;
	private Uri drawableImageID;

	public Persona(String nombre,String cumple, Uri drawableImageID) {
		this.nombre = nombre;
		this.cumple=cumple;
		this.drawableImageID = drawableImageID;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCumple() {
		return cumple;
	}

	public void setCumple(String cumple) {
		this.cumple = cumple;
	}

    public Uri getDrawableImageID() {
		return drawableImageID;
	}

	public void setDrawableImageID(Uri drawableImageID) {
		this.drawableImageID = drawableImageID;
	}


}
