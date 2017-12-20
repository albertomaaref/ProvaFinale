package it.dsgroup.provafinale;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private List<Polyline> polylines;



    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        polylines = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        LatLng milano = new LatLng(45.4652317, 9.1876072);

        // Add a marker in Sydney and move the camera
        LatLng positionA = geocoding("via meda, milano");
        mMap.addMarker(new MarkerOptions().position(positionA).title("inizio"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionA,15));
        LatLng positionB = geocoding("via pompeo mariani, milano");
        mMap.addMarker(new MarkerOptions().position(positionB).title("inizio"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(milano,12));

        // routing

        Routing routing = new Routing.Builder()

                .travelMode(AbstractRouting.TravelMode.DRIVING)

                .withListener(this)

                .alternativeRoutes(false)

                .waypoints(positionA, positionB)

                .build();

        routing.execute();
    }

    public LatLng geocoding(String indirizzo){



        Geocoder geocoder;

        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());



        try {

            addresses = geocoder.getFromLocationName(indirizzo + "Milano", 1);

            double latitude =  addresses.get(0).getLatitude();

            double longitude =  addresses.get(0).getLongitude();

            return new LatLng(latitude,longitude);



        } catch (IOException e) {

            Toast.makeText(getApplicationContext(), "Indirizzo non trovato",Toast.LENGTH_SHORT).show();

            e.printStackTrace();

        }

        return null;

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {

            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }else {

            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {

            for (Polyline poly : polylines) {

                poly.remove();

            }

        }
        polylines = new ArrayList<>();

        //add route(s) to the map.

        for (int i = 0; i <route.size(); i++) {



            //In case of more than 5 alternative routes

            int colorIndex = i % COLORS.length;



            PolylineOptions polyOptions = new PolylineOptions();

            polyOptions.color(getResources().getColor(COLORS[colorIndex]));

            polyOptions.width(10 + i * 3);

            polyOptions.addAll(route.get(i).getPoints());

            Polyline polyline = mMap.addPolyline(polyOptions);

            polylines.add(polyline);



            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}
