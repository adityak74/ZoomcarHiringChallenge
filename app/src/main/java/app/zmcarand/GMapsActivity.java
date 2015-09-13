package app.zmcarand;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMapsActivity extends ActionBarActivity {


    //static final LatLng CAR_LOCATION = new LatLng(18.520430 , 73.856744);
    private GoogleMap googleMap;
    private NetworkImageView thb;
    private TextView nameTv,ratingTv,hrateTv,seaterTv,acTv;
    private LatLng CAR_LOCATION;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmaps);

        thb = (NetworkImageView) findViewById(R.id.thumbnail);
        nameTv = (TextView) findViewById(R.id.title);
        ratingTv = (TextView) findViewById(R.id.rating);
        hrateTv = (TextView) findViewById(R.id.hourlyrate);
        seaterTv = (TextView) findViewById(R.id.seater);
        acTv = (TextView) findViewById(R.id.ac);

        thb.setImageUrl(getIntent().getStringExtra("image"), imageLoader);
        nameTv.setText(getIntent().getStringExtra("name"));
        ratingTv.setText("Rating : " + getIntent().getStringExtra("rating"));
        hrateTv.setText(getIntent().getStringExtra("hrate") + " per hr");
        seaterTv.setText("Seater : "  + String.valueOf(Integer.parseInt(getIntent().getStringExtra("seater")) - 1) + " + 1");
        if(Integer.parseInt(getIntent().getStringExtra("ac"))==1){
            acTv.setText("AC: Yes");
        }else{
            acTv.setText("AC: No");
        }

        Double lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        Double lng = Double.parseDouble(getIntent().getStringExtra("lng"));

        CAR_LOCATION = new LatLng(lat , lng);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CAR_LOCATION, 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(CAR_LOCATION).title(getIntent().getStringExtra("name")));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gmaps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
