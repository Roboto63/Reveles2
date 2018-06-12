package com.example.roberto.reveles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Directions.DirectionFinder;
import Directions.DirectionFinderListener;
import Directions.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    private List<Marker> markers = new ArrayList<>();
    private List<LatLng> latLngs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //sendRequest();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng iglesiaAsunsion = new LatLng(19.385917, -96.972556);
        LatLng parqueRevolucion = new LatLng(19.385972, -96.972917);
        LatLng hotelReal = new LatLng(19.386306, -96.976694);
        LatLng rinconcito = new LatLng(19.393972, -96.979667);
        LatLng cascadaTexolo = new LatLng(19.393648, -96.979141);
        LatLng museoComunitario = new LatLng(19.387250, -96.978833);
        LatLng cafebeneficio = new LatLng(19.384381, -96.972623);
        LatLng plazaArtesanias = new LatLng(19.386222, -96.972556);
        LatLng nevado = new LatLng(19.385194, -96.973250);
        LatLng acuatica = new LatLng(19.383917, -96.973583);
        LatLng amorescafe = new LatLng(19.383917, -96.973583);
        LatLng yotecielo = new LatLng(19.382528, -96.976694);
        LatLng diostigre = new LatLng(19.386250, -96.971167);

        String[] titulos = {"Iglesia de la Asunción", "Parque Revolución", "Hotel Real Teocelo",
                "El Rinconcito", "Mirador Cascada de Texolo", "Museo Comunitario \"Antigua Estación Ferroviaria\"",
                "Cafe \"El Beneficio\"", "Plaza de artesanias", "El nevado", "Acuatica \"Los Delfines\"",
                "Cafeteria \"Amorescafe\"", "Cafeteria \"Yo te cielo\"", "Hotel Dios Tigre"};

        latLngs.add(iglesiaAsunsion);
        latLngs.add(parqueRevolucion);
        latLngs.add(hotelReal);
        latLngs.add(rinconcito);
        latLngs.add(cascadaTexolo);
        latLngs.add(museoComunitario);
        latLngs.add(cafebeneficio);
        latLngs.add(plazaArtesanias);
        latLngs.add(nevado);
        latLngs.add(acuatica);
        latLngs.add(amorescafe);
        latLngs.add(yotecielo);
        latLngs.add(diostigre);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 20));

        for (int i = 0; i < latLngs.size(); i++) {
            markers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(titulos[i])
                    .position(latLngs.get(i))));
        }

        markers.add(mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(titulos[4])
                .position(latLngs.get(4))));

        markers.add(mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title(titulos[6])
                .position(latLngs.get(6))));
        /*mMap.addPolyline(new PolylineOptions().add(
           finca,
           new LatLng(19.390052, -96.979869),
           new LatLng(19.388686, -96.977763),
           cafe
        ).width(10).color(Color.RED)
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLng(finca));*/


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < markers.size(); i++) {
                    if (marker.equals(markers.get(i))) {
                        Toast.makeText(
                                MapsActivity.this,
                                "Lugar:\n" +
                                        marker.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        //placeInfo();
                    }
                }

                if (marker.equals(markers.get(6))) {
                    Toast.makeText(
                            MapsActivity.this,
                            "Lugar:\n" +
                                    marker.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    placeInfo();
                }

                if (marker.equals(markers.get(4))) {
                    Toast.makeText(
                            MapsActivity.this,
                            "Lugar:\n" +
                                    marker.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    placeInfo2();
                }

                return true;
            }
        });
    }

    private void sendRequest() {
        String origin = "19.390052,-96.979869";
        String destination = "19.388686,-96.977763";
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    public void placeInfo() {
        Intent i = new Intent(MapsActivity.this, LugarActivity.class);
        startActivity(i);
        finish();
    }

    public void placeInfo2() {
        Intent i = new Intent(MapsActivity.this, LugarActivity2.class);
        startActivity(i);
        finish();
    }

    private String getPhotoPlace(String photo_reference, int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth=" + maxWidth);
        url.append("&photoreference=" + photo_reference);
        url.append("&key=AIzaSyCinE0jhv2MO0u1xwvFqaVjH9XqCRepGsY");

        return url.toString();
    }
}
