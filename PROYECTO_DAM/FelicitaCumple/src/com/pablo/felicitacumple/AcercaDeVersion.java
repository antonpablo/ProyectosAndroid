
package com.pablo.felicitacumple;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class AcercaDeVersion extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acerca_de_version);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		try
		{
			PackageInfo pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
			String versionName = pinfo.versionName;
			
			TextView version = (TextView)findViewById(R.id.textView1);			
			String ver ="Versión "+ versionName +"\nDesarrollada por Pablo Antón Estrella\n\n";
			version.setText(ver.toString());
			
			
			
			TextView email = (TextView) findViewById(R.id.textView2);
			email.setText(Html.fromHtml("Email de contacto<br /><a href='mailto:antonpablo_91@hotmail.es'>antonpablo_91@hotmail.es</a><br /><br />"));
			email.setMovementMethod(LinkMovementMethod.getInstance());
			
			
			
			TextView redessoc = (TextView) findViewById(R.id.textView3);
			redessoc.setText(Html.fromHtml("<b>Redes sociales:</b><br /><br />"
					+ "Twitter: <a href='https://twitter.com/Antonpablo91'>@Antonpablo91</a><br />"
					+"Facebook:  <a href='https://www.facebook.com/pablo.anton.75'>Pablo Antón Estrella</a>"));
			redessoc.setMovementMethod(LinkMovementMethod.getInstance());
		}		
		catch(Exception e)
		{
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acerca_de_version, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(android.R.id.home == item.getItemId())
      	{      	
      		startActivityAfterCleanup(Calendar2.class);
            return true;
      	}
		return super.onOptionsItemSelected(item);
	}
	  private void startActivityAfterCleanup(Class<?> cls) {
	        
	        Intent intent = new Intent(getApplicationContext(), cls);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
	      }
}
