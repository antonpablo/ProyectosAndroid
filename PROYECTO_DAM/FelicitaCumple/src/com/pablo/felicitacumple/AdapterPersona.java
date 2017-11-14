package com.pablo.felicitacumple;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPersona extends ArrayAdapter<Persona>{
	
	private Context context;
	private ArrayList<Persona> datos;

	/**
	 * Constructor del Adapter.
	 * 
	 * @param context
	 *            context de la Activity que hace uso del Adapter.
	 * @param datos
	 *            Datos que se desean visualizar en el ListView.
	 */
	public AdapterPersona(Context context, ArrayList<Persona> datos) {
		super(context, R.layout.listview_item, datos);
		// Guardamos los parámetros en variables de clase.
		this.context = context;
		this.datos = datos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// En primer lugar "inflamos" una nueva vista, que será la que se
		// mostrará en la celda del ListView.
		View item = LayoutInflater.from(context).inflate(
				R.layout.listview_item, null);

		// A partir de la vista, recogeremos los controles que contiene para
		// poder manipularlos.
		// Recogemos el ImageView y le asignamos una foto.
		ImageView imagen = (ImageView) item.findViewById(R.id.imgAnimal);
		imagen.setImageURI(datos.get(position).getDrawableImageID());

		// Recogemos el TextView para mostrar el nombre y establecemos el
		// nombre.
		TextView nombre = (TextView) item.findViewById(R.id.tvNombre);
		nombre.setText(datos.get(position).getNombre());

		// Recogemos el TextView para mostrar el número de celda y lo
		// establecemos.
		TextView numCelda = (TextView) item.findViewById(R.id.tvCumple);
		numCelda.setText(datos.get(position).getCumple());

		// Devolvemos la vista para que se muestre en el ListView.
		return item;
	}

}
