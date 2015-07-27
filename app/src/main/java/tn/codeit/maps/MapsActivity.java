package tn.codeit.maps;


        import android.os.AsyncTask;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.util.Log;

        import android.widget.Toast;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.UiSettings;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;

        import tn.codeit.maps.webservices.UserFunctions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private UiSettings mUiSettings;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public JSONObject data , allStations;
    public JSONArray stationsList;
    public ArrayList<HashMap<String, String>> listItem ;

    public   JSONObject station ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpMapIfNeeded();
    }
    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("mapReady", "mapReady");
        mMap = map;
        mMap.setMyLocationEnabled(true);
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        AsyncCallWS task = new AsyncCallWS();
        task.execute();

    }
    public void getBusStations(){
        Log.d("test","get bus station");
        UserFunctions uf=new UserFunctions();
        data = uf.getAllStations();
        //Log.d("data",String.valueOf(data));
        listItem = new ArrayList<HashMap<String, String>>();
        try {
            allStations = data.getJSONObject("data");
            stationsList =allStations.getJSONArray("nearstations");
            Log.d("result" , String.valueOf(stationsList));


        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insertmarker() throws JSONException

    {   mMap.clear();
        for (int i=0;i<stationsList.length();i++) {

            try {
                station = stationsList.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String streetName = station.getString("street_name");
            double latitude = Double.parseDouble(station.getString("lat"));
            double longitude = Double.parseDouble(station.getString("lon"));

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(streetName));
        }
    }



    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            // Log.i(TAG, "doInBackground");
            try {

                getBusStations();

            } catch (Exception e) {
                Log.d("error", e.getMessage() + "-RESULT");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            Log.d("done", "onPostExecute");


            try {
                insertmarker();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING STATIONS
//            layoutProgress.setVisibility(View.VISIBLE);
            //          textProgress.setText("Chargement des stations en cours...");
            //        Log.d("on pre execute", "onPreExecute");

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.d("done", "onProgressUpdate");
        }

    }




    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //mMap.addMarker(new MarkerOptions()
        //      .position(new LatLng(10, 10))
        //    .title("Hello world"));
    }
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, "Map is not ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}