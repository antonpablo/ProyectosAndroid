package com.pablo.felicitacumple;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.hintdesk.core.activities.AlertMessageBox;
import com.hintdesk.core.util.OSUtil;
import com.hintdesk.core.util.StringUtil;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.preference.PreferenceManager;
import twitter4j.Twitter;

public class Calendar2 extends FragmentActivity {
	
	  
	private int mesActual,dia[];
	private String nombre[],imagen[];
	public Calendar month;
	public CalendarAdapter adapter;
	public Handler handler;
	public ArrayList<String> items;
    private ViewGroup controlsContainer;
	private final String PENDING_ACTION_BUNDLE_KEY = "com.pablo.felicitacumple:PendingAction";
	private LoginButton loginButton;
    private UiLifecycleHelper uiHelper;
    private PendingAction pendingAction = PendingAction.NONE;
    private GraphUser user;
    private boolean canPresentShareDialog;
    SharedPreferences sharedPreferences;
    private List<GraphUser> tags;
    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
   private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		 // Add code to print out the key hash
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.pablo.felicitacumple", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }

		//twitter
		 initControl();

	    uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
      

        setContentView(R.layout.activity_calendar2);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setPublishPermissions(Arrays.asList("publish_actions"));
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
            	 Calendar2.this.user = user;
            	 updateUI();
            }
        });
	
		int idEmail= getIntent().getIntExtra("idEmail", 0);
		int idWha= getIntent().getIntExtra("idWha", 0);
		int idTiwtter=getIntent().getIntExtra("idTwi", 0);
		int idFacebook=getIntent().getIntExtra("idFac", 0);
		if (idEmail==1)
		{
			emailSend ();
		}
		if (idTiwtter==1)
		{
			twitterStatus();
		}
		if(idFacebook==1)
		{
			updateUI() ;
			postStatusUpdate();
		}
		
		if (idWha==1)
		{
			smsMultiplataforma();
		}
		
		//CALENDARIO

		   month = Calendar.getInstance();
		   mesActual=month.get(Calendar.MONTH)+1;
		    
		   items = new ArrayList<String>();
		   adapter = new CalendarAdapter(this, month);
		    
		    GridView gridview = (GridView) findViewById(R.id.gridview);
		    gridview.setAdapter(adapter);
		    
		    handler = new Handler();
		    handler.post(calendarUpdater);
		    
		    TextView title  = (TextView) findViewById(R.id.title);
		    title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
		    
		    TextView previous  = (TextView) findViewById(R.id.previous);
		    previous.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {				
						month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
					} else {
						month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
					}
					mesActual-=1;
					refreshCalendar();
				}
			});
		    
		    TextView next  = (TextView) findViewById(R.id.next);
		    next.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {				
						month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
					} else {
						month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
					}
					mesActual+=1;
					refreshCalendar();
					
				}
			});
		    
			gridview.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			    	TextView date = (TextView)v.findViewById(R.id.date);
			        if(date instanceof TextView && !date.getText().equals("")) {
			        	
			        	Intent intent = new Intent(v.getContext(),Busqueda.class);
			        	String day = date.getText().toString();
			        	if(day.length()==1) {
			        		day = "0"+day;
			        	}
			        	
			        	intent.putExtra("idCalendar",1);
			        	String res=month+"-"+day;
			        	intent.putExtra("date",res);
			     		v.getContext().startActivity(intent);
			        	// return chosen date as string format 
			        	intent.putExtra("date", android.text.format.DateFormat.format("yyyy-MM", month)+"-"+day);
			        	v.getContext().startActivity(intent);
			        }
			        
			    }
			});
	
	}
	
	public void refreshCalendar()
	{
		TextView title  = (TextView) findViewById(R.id.title);
		
		adapter.refreshDays();
		adapter.notifyDataSetChanged();				
		handler.post(calendarUpdater); // generate some random calendar items				
		
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}
	
	
	public Runnable calendarUpdater = new Runnable() {
		
		@Override
		public void run() {
			
			items.clear();
			//lectura sql
			leersql();
			
			for(int i=0;i<dia.length;i++) {
				
		    /*ImageView date_ico=(ImageView )findViewById(R.id.date_icon);
			Uri myUri = Uri.parse(imagen[i]);
			date_ico.setImageURI(myUri);*/
			items.add(Integer.toString(dia[i]));
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
			return;
		}
	};
	
	
	public void leersql()
	{
		UsuariosSQLiteHelper basedatos=new UsuariosSQLiteHelper (this,"contactos",null,1);
		SQLiteDatabase db= basedatos.getReadableDatabase();
		String consulta="";
		
		if (db!=null)
		{
			consulta+="SELECT nombre,mes,dia,uri FROM contactos WHERE mes="+mesActual;
		    Cursor c =db.rawQuery(consulta,null);
			
			if (c!=null)
			{
				nombre=new String[c.getCount()];
				dia=new int[c.getCount()];
			    imagen=new String[c.getCount()];
			    
				c.moveToFirst();

			     for (int i = 0; i < c.getCount(); i++) {
			        	 nombre[i]=c.getString(0);
			        	 dia[i]=c.getInt(2);
			        	 imagen[i]=c.getString(3);
			        	 c.moveToNext();
			        }
			     c.close();  
			}
		}
	}
	
	 //BOTON VOLVER FISICO DEL MOViL
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Toast.makeText(this,"Application finish", Toast.LENGTH_SHORT).show();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent); 
        finish();
        return;
    }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.calendar2, menu);
		return true;
	}

	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	       
	
		  if(android.R.id.home == item.getItemId())
	      	{      	
			    onBackPressed() ;
	      	}
		  
		  if (R.id.mod ==item.getItemId())
	    	{
			  //modificar mensajes   
	    		return true;
	    	}
		  if (R.id.man ==item.getItemId())
	    	{
			  //Manual de usuario    
	    		return true;
	    	}
		  if (R.id.ayu ==item.getItemId())
	    	{
			  //Ayuda     
	    		return true;
	    	}
		  if (R.id.ver ==item.getItemId())
	    	{
			  //Version de la app 
	    	  Intent inte=new Intent(Calendar2.this,AcercaDeVersion.class);
	  		  startActivity(inte);
	    		return true;
	    	}
	   
	    	return super.onOptionsItemSelected(item); 
	    }
	  
	

	  @Override
	    protected void onResume() {
	        super.onResume();
	        uiHelper.onResume();

	        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
	        // the onResume methods of the primary Activities that an app may be launched into.
	        AppEventsLogger.activateApp(this);
	        
	        updateUI();

	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        uiHelper.onSaveInstanceState(outState);
	        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        uiHelper.onPause();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        uiHelper.onDestroy();
	    }

	    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	        if (pendingAction != PendingAction.NONE &&
	                (exception instanceof FacebookOperationCanceledException ||
	                exception instanceof FacebookAuthorizationException)) {
	                new AlertDialog.Builder(Calendar2.this)
	                    .setTitle(R.string.cancelled)
	                    .setMessage(R.string.permission_not_granted)
	                    .setPositiveButton(R.string.ok, null)
	                    .show();
	            pendingAction = PendingAction.NONE;
	        } 
	        updateUI();
	       
	    }
	    
	    private void updateUI() {
	        Session session = Session.getActiveSession();
	        boolean enableButtons = (session != null && session.isOpened());

	       
	    }

	 
	/*  private void onClickPickFriends() {
		  updateUI();
		  
		  
		  final FriendPickerFragment fragment = new FriendPickerFragment();
	       
	        setFriendPickerListeners(fragment);

	        
	       showPickerFragment(fragment);
	    }

	    private void showPickerFragment(PickerFragment<?> fragment) {
	        fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
	            @Override
	            public void onError(PickerFragment<?> pickerFragment, FacebookException error) {
	                String text = getString(R.string.exception, error.getMessage());
	                Toast toast = Toast.makeText(Calendar2.this, text, Toast.LENGTH_SHORT);
	                toast.show();
	            }
	        });

	     
	        FragmentManager fm = getSupportFragmentManager();
	        fm.beginTransaction()
	                .replace(R.id.fragment_container, fragment)
	                .addToBackStack(null)
	                .commit();

	        
	        
	        fm.executePendingTransactions();

	        fragment.loadData(true);
	        

	    }
	
	   
	    private void setFriendPickerListeners(final FriendPickerFragment fragment) {
	        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
	            @Override
	            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
	            	onFriendPickerDone(fragment);
	            }
	        });
	    }

	    private void onFriendPickerDone(FriendPickerFragment fragment) {
	        FragmentManager fm = getSupportFragmentManager();
	        fm.popBackStack();

	        String results = "";

	        List<GraphUser> selection = fragment.getSelection();
	        tags = selection;
	        if (selection != null && selection.size() > 0) {
	            ArrayList<String> names = new ArrayList<String>();
	            for (GraphUser user : selection) {
	                names.add(user.getName());
	            }
	            results = TextUtils.join(", ", names);
	        } else {
	            results = getString(R.string.no_friends_selected);
	        }

	        showAlert(getString(R.string.you_picked), results);
	    }


	    private void showAlert(String title, String message) {
	        new AlertDialog.Builder(this)
	                .setTitle(title)
	                .setMessage(message)
	                .setPositiveButton(R.string.ok, null)
	                .show();
	    }*/

	    
	    //SEND STATUS FACEBOOK!!!!!!
	    
	    
	   
	    
	    private interface GraphObjectWithId extends GraphObject {
	        String getId();
	    }

	   private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
	        String title = null;
	        String alertMessage = null;
	        if (error == null) {
	            title = getString(R.string.success);
	            String id = result.cast(GraphObjectWithId.class).getId();
	            alertMessage = getString(R.string.successfully_posted_post, message, id);
	        } else {
	            title = getString(R.string.error);
	            alertMessage = error.getErrorMessage();
	        }

	        new AlertDialog.Builder(this)
	                .setTitle(title)
	                .setMessage(alertMessage)
	                .setPositiveButton(R.string.ok, null)
	                .show();
	    }


	    private void postStatusUpdate() {
	        
	        	
	    	final String message=getIntent().getStringExtra("SmsFac");
	         
	        Request request = Request
	                    .newStatusUpdateRequest(Session.getActiveSession(), message, null, tags, new Request.Callback() {
	                        @Override
	                        public void onCompleted(Response response) {
	                            showPublishResult(message, response.getGraphObject(), response.getError());
	                        }
	                        
	                    });
	            request.executeAsync();
	         
	    }
	    
	    //TWITTTER!!!!
	   
	  public void twitterStatus()
	  {
		  
		  
		  String res=getIntent().getStringExtra("SmsTwi");
          if (!StringUtil.isNullOrWhitespace(res)) {
              new TwitterUpdateStatusTask().execute(res);
          } else {
              Toast.makeText(getApplicationContext(), "Please enter a status", Toast.LENGTH_SHORT).show();
          }
	  }
	  
	    private void initControl() {
	        Uri uri = getIntent().getData();
	        if (uri != null && uri.toString().startsWith(ConstantValues.TWITTER_CALLBACK_URL)) {
	            String verifier = uri.getQueryParameter(ConstantValues.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
	            new TwitterGetAccessTokenTask().execute(verifier);
	        } else
	            new TwitterGetAccessTokenTask().execute("");
	    }
	    
	    class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

	        @Override
	        protected void onPostExecute(String userName) {
	        	 Toast.makeText(getApplicationContext(), "Welcome  :"+userName, Toast.LENGTH_SHORT).show();
	          
	        }

	        @Override
	        protected String doInBackground(String... params) {

	            Twitter twitter = TwitterUtil.getInstance().getTwitter();
	            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
	            if (!StringUtil.isNullOrWhitespace(params[0])) {
	                try {

	                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
	                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	                    SharedPreferences.Editor editor = sharedPreferences.edit();
	                    editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
	                    editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
	                    editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
	                    editor.commit();
	                    return twitter.showUser(accessToken.getUserId()).getName();
	                    
	                    
	                } catch (TwitterException e) {
	                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	                }
	            } else {
	                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	                String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
	                String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
	                AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
	                try {
	                    TwitterUtil.getInstance().setTwitterFactory(accessToken);
	                    return TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getName();
	                } catch (TwitterException e) {
	                    e.printStackTrace();  
	                }
	            }
	           
	            return null;  
	        }
	    }

	    class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

	        @Override
	        protected void onPostExecute(Boolean result) {
	            if (result==true)
	                Toast.makeText(getApplicationContext(), "Tweet successfully", Toast.LENGTH_SHORT).show();
	            else
	                Toast.makeText(getApplicationContext(), "Tweet failed", Toast.LENGTH_SHORT).show();
	        }

	        @Override
	        protected Boolean doInBackground(String... params) {
	            try {
	            	
	                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	                String accessTokenString = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
	                String accessTokenSecret = sharedPreferences.getString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
	             
	                if (!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)) {
	                    AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
	                   
	                    twitter4j.Status status = TwitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
	                    return true;
	                }

	            } catch (TwitterException e) {
	                e.printStackTrace(); 
	            }
	            return false; 
	       }
   }



	public void emailSend ()
	{
		String res=getIntent().getStringExtra("SmsEmail");
		
		String vector[]=res.split(",");
		String email=vector[0];
		String asunto=vector[1];
		String sms=vector[2];

		
		//cojer del tlf estos datos si se puede
		String fromEmail = "91antonpablo91@gmail.com";
		String fromPassword = "Opel7063";
		
		List<String> toEmailList = Arrays.asList(email
				.split("\\s*,\\s*"));
		Log.i("SendMailActivity", "To List: " + toEmailList);
		
		new SendMailTask(Calendar2.this).execute(fromEmail,
				fromPassword, toEmailList, asunto, sms);   

	}
	
	public void smsMultiplataforma()
	{
		     //TODAS LAS PLATAFORMAS
		 String res=getIntent().getStringExtra("SmsWha");
		 
	   	Intent shareIntent = new Intent();
	      shareIntent.setAction(Intent.ACTION_SEND);
		  shareIntent.setType("text/plain");
		  shareIntent.putExtra(Intent.EXTRA_TEXT, res);
		  startActivity(Intent.createChooser(shareIntent, "Share your thoughts"));
	}
	public void agenda(View v)
	 {
	 		Intent inte=new Intent(v.getContext(),Contactos.class);
	 		v.getContext().startActivity(inte);
	}

	
	
	
	//CODIGO DEL LOGIN DE TWITTER
	public void twitter(View v)
	{
		if (!OSUtil.IsNetworkAvailable(getApplicationContext())) {
            AlertMessageBox.Show(Calendar2.this, "Internet connection", "A valid internet connection can't be established", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }

        if (StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_KEY) || StringUtil.isNullOrWhitespace(ConstantValues.TWITTER_CONSUMER_SECRET)) {
            AlertMessageBox.Show(Calendar2.this, "Twitter oAuth infos", "Please set your twitter consumer key and consumer secret", AlertMessageBox.AlertMessageBoxIcon.Info);
            return;
        }

        logIn();
    
	}

	
    private void logIn() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            new TwitterAuthenticateTask().execute();
        }
        else
        {
        	Toast.makeText(this,"login twitter sucessfull", Toast.LENGTH_SHORT).show();
        }
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
            startActivity(intent);
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }
    
 

}
