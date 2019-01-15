package com.example.education.Presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.education.Views.MapFragment;
import com.yandex.mapkit.directions.driving.DrivingArrivalPoint;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.RequestPoint;
import com.yandex.mapkit.directions.driving.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

public class MapPresenter {
    private MapObjectCollection mapObjects;
    private MapView mapView;
    private Point ROUTE_START_LOCATION = new Point(55.746963, 37.863387);
    private Point ROUTE_END_LOCATION = new Point(55.793288, 37.700819); //стромынка
    private DrivingSession drivingSession;
    private DrivingRouter drivingRouter;
    private MapFragment.OnFragmentInteractionListener mListener;

    public MapPresenter(DrivingRouter drivingRouter, MapView mapView){
        setDrivingRouter(drivingRouter);
        setMapView(mapView);
    }

    public void setROUTE_START_LOCATION(Point ROUTE_START_LOCATION1){
        ROUTE_START_LOCATION = ROUTE_START_LOCATION1;
    }
    public void setROUTE_END_LOCATION(Point ROUTE_END_LOCATION1){
        ROUTE_END_LOCATION = ROUTE_END_LOCATION1;
    }
    public void setDrivingRouter(DrivingRouter drivingRouter1) {
        drivingRouter = drivingRouter1;
    }
    public void setMapView(MapView mapView1) {
        mapView = mapView1;
    }



    public void submitRequest(Point ROUTE_START_LOCATION, Point ROUTE_END_LOCATION, DrivingSession drivingSession,
                                     DrivingRouter drivingRouter, DrivingSession.DrivingRouteListener drivingRouteListener) {
        DrivingOptions options = new DrivingOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(
                ROUTE_START_LOCATION,
                new ArrayList<Point>(),
                new ArrayList<DrivingArrivalPoint>(),
                RequestPointType.WAYPOINT));
        requestPoints.add(new RequestPoint(
                ROUTE_END_LOCATION,
                new ArrayList<Point>(),
                new ArrayList<DrivingArrivalPoint>(),
                RequestPointType.WAYPOINT));
        drivingSession = drivingRouter.requestRoutes(requestPoints, options, drivingRouteListener);
    }


    DrivingSession.DrivingRouteListener drivingRouteListener = new DrivingSession.DrivingRouteListener() {
        @Override
        public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
            for (DrivingRoute route : list) {
                mapObjects.addPolyline(route.getGeometry());
                break;
            }
        }

        @Override
        public void onDrivingRoutesError(@NonNull Error error) {
            String errorMessage = "unknown_error_message";
            if (error instanceof RemoteError) {
                errorMessage = "remote_error_message";
            } else if (error instanceof NetworkError) {
                errorMessage = "network_error_message";
            }
            Log.i("YANDEXMAP_ERORR", errorMessage);
        }
    };

    public void showRoute(){
        mapObjects = mapView.getMap().getMapObjects().addCollection();
        submitRequest(ROUTE_START_LOCATION, ROUTE_END_LOCATION, drivingSession, drivingRouter, drivingRouteListener);
    }

}

