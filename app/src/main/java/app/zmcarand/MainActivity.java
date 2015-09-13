package app.zmcarand;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {
	// Log tag
	private static final String TAG = MainActivity.class.getSimpleName();


	private static final String url = "https://zoomcar.0x10.info/api/zoomcar?type=json&query=list_cars";
    private static final String url_api_hits_2 = "https://zoomcar.0x10.info/api/zoomcar?type=json&query=api_hits";

	private ProgressDialog pDialog;
	private List<Car> carList = new ArrayList<Car>();
	private ListView listView;
	private CustomListAdapter adapter;
    private JSONArray jarr;
    private String api_hits_result,result;
    private Button home,priceHrBt,ratingBt;
    private Activity activity;

    private TextView totCars,api_hits_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        home = (Button) findViewById(R.id.home);
        priceHrBt = (Button) findViewById(R.id.priceHrBt);
        ratingBt = (Button) findViewById(R.id.ratingBt);

        totCars = (TextView) findViewById(R.id.totCars);
        api_hits_tv = (TextView) findViewById(R.id.apihits);

		listView = (ListView) findViewById(R.id.list);
		adapter = new CustomListAdapter(this, carList);
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
		pDialog.setMessage("Loading...");
		pDialog.show();
		final DatbaseHandler dbh = new DatbaseHandler(getApplicationContext());

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pDialog.isShowing())
                    pDialog.show();
                List<Car> tempcarlist = dbh.getAllCars();
                adapter = new CustomListAdapter(activity,tempcarlist);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(pDialog.isShowing())
                    pDialog.dismiss();
            }
        });

        priceHrBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pDialog.isShowing())
                    pDialog.show();
                List<Car> tempcarlist = dbh.getAllPriceHrSortedCars();
                adapter = new CustomListAdapter(activity,tempcarlist);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Sorted By Price/Hr Ascending",Toast.LENGTH_SHORT).show();
                if(pDialog.isShowing())
                    pDialog.dismiss();
            }
        });

        ratingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pDialog.isShowing())
                    pDialog.show();
                List<Car> tempcarlist = dbh.getAllRatingSortedCars();
                adapter = new CustomListAdapter(activity,tempcarlist);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Sorted By Rating Descending",Toast.LENGTH_SHORT).show();
                if(pDialog.isShowing())
                    pDialog.dismiss();
            }
        });

		// changing action bar color
		//getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));

		// Creating volley request obj

		try{
			if(dbh.getCarCount() > 0)
			{
                totCars.setText("Total Cars: "+dbh.getCarCount());
				Log.d("ZMC","Getting from Database");
				List<Car> tempcarlist = dbh.getAllCars();
				adapter = new CustomListAdapter(this,tempcarlist);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				Log.d("DBSTORAGE", "Retrieved from Database");
				pDialog.dismiss();
                new GetAPIHits().execute();
			}else {
				Log.d("DBSTORAGE","Cant Retrieved from Database = 0");
				Log.d("DBSTORAGE","Cannot Retrieved from Database");
				//pDialog.setMessage("Loading from Internet");
				//pDialog.show();
				setupCars(dbh);

			}
		}catch (Exception ex){
			Log.d("DBSTORAGE","Cannot Retrieved from Database");
			//pDialog.setMessage("Loading from Internet");
			//pDialog.show();
            setupCars(dbh);

		}

	}

	public void setupCars(final DatbaseHandler dbh){

		Log.d("ZMC","Getting from Internet");

		JsonObjectRequest movieReq = new JsonObjectRequest(url,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
                        pDialog.dismiss();

						try {
							jarr = response.getJSONArray("cars");
						}catch (JSONException ex){
							Log.d("Err",ex.toString());
						}

						// Parsing json
						for (int i = 0; i < jarr.length(); i++) {
							try {

								JSONObject obj = jarr.getJSONObject(i);
								Car car = new Car();
								car.setName(obj.getString("name"));
								car.setImage(obj.getString("image"));
								car.setType(obj.getString("type"));
								car.setHourlyRate(obj.getString("hourly_rate"));
								car.setRating(obj.getString("rating"));
								car.setSeater(obj.getString("seater"));
								car.setAc(obj.getString("ac"));

								JSONObject locationObj = obj.getJSONObject("location");

								car.setLat(locationObj.getString("latitude"));
								car.setLong(locationObj.getString("longitude"));
								// Genre is json array
								dbh.addCarItem(car);
								// adding car to movies array
								carList.add(car);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
                        totCars.setText("Total Cars: "+dbh.getCarCount());
                        new GetAPIHits().execute();
						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());

			}
		});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(movieReq);


	}

    public class GetAPIHits extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            if(!pDialog.isShowing())
                pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL(url_api_hits_2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedReader reader =new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String webPage = "",data="";
                while ((data = reader.readLine()) != null){
                    webPage += data + "\n";
                }
                try {
                    JSONObject obj = new JSONObject(webPage);
                    api_hits_result = obj.getString("api_hits");
                }catch (Exception ex){

                }
            }catch (Exception ex){

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            api_hits_tv.setText("API Hits : " + api_hits_result);
            if(pDialog.isShowing())
                pDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
