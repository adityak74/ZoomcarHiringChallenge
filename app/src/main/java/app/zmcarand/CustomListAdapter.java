package app.zmcarand;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Car> carItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Car> carItems) {
		this.activity = activity;
		this.carItems = carItems;
	}

	@Override
	public int getCount() {
		return carItems.size();
	}

	@Override
	public Object getItem(int location) {
		return carItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView rating = (TextView) convertView.findViewById(R.id.rating);
		TextView hourlyrate = (TextView) convertView.findViewById(R.id.hourlyrate);
		View clickView = convertView.findViewById(R.id.clickView);

		// getting movie data for the row
		Car c = carItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(c.getImage(), imageLoader);
		
		// title
		title.setText(c.getName());
		
		// rating
		rating.setText("Rating: " + c.getRating());

		// hourly
		hourlyrate.setText(c.getHourlyRate() + " per hr");

        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car clickedCar = carItems.get(position);
                Intent i = new Intent(activity, GMapsActivity.class);
                i.putExtra("name", clickedCar.getName());
                i.putExtra("image", clickedCar.getImage());
                i.putExtra("type", clickedCar.getType());
                i.putExtra("hrate", clickedCar.getHourlyRate());
                i.putExtra("rating", clickedCar.getRating());
                i.putExtra("seater", clickedCar.getSeater());
                i.putExtra("ac", clickedCar.getAc());
                i.putExtra("lat", clickedCar.getLat());
                i.putExtra("lng", clickedCar.getLong());
                activity.startActivity(i);
                //Toast.makeText(activity, "Clicked on : " + clickedCar.getName(), Toast.LENGTH_LONG).show();
            }
        });

		return convertView;
	}

}