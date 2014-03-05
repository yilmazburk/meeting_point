package com.example.nerdebulusalim.overlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.nerdebulusalim.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class addressOverlay extends Overlay {

	private GeoPoint point;
	private Address address;
	private Context context;
	public addressOverlay(Address address,Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.context = context;
		if(address != null){
			setAddress(address);
			int latitude = (int)(address.getLatitude()*1000000);
			int longitude = (int)(address.getLongitude()*1000000);
			
			/* * * * * * * * Dont forget! * * * * * * * * *
			 * latitude and longitude infos in address should be converted the 
			 * latitudeSpan and longitudeSpan like getLatitudeSpan() and getLongitudeSpan()
			 * mapview functions.
			 * if you remember please check MapView doc in eclipse*/
			setGeographicPoint(new GeoPoint(latitude,longitude));//sets latitude and longtitude
		}
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		Point location = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(getGeoPoint(),location);
		Paint paint = new Paint();//need for paint an overlay(or circle) on map
		Bitmap pin = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.pin);
		paint.setAntiAlias(true);
		int xcoordinate = location.x;
		int ycoordinate = location.y;
		if(shadow){
			xcoordinate -= 25; //these 2s are used for draw shadow over the map
			ycoordinate -= 40; 
			paint.setARGB(90, 0, 0, 0);
			canvas.save();
			//canvas.skew(-0.9F, 0.0F);
			//canvas.scale(1.0F, 0.0F);
			//canvas.rotate(F);
			//canvas.rotate(SHADOW_Y_SCALE);
			//canvas.drawCircle(xcoordinate, ycoordinate, 6, paint);
			canvas.drawBitmap(pin, xcoordinate, ycoordinate,paint);
		}else{
			xcoordinate -= 25;
			ycoordinate -= 40;
			canvas.restore();
			//canvas.rotate(SHADOW_X_SKEW);
			//paint.setColor(Color.BLUE);
			//canvas.drawCircle(xcoordinate, ycoordinate, 6, paint);
			canvas.drawBitmap(pin, xcoordinate, ycoordinate,null);
			
		}
	}
	
	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		GeoPoint p = point;
		if(point!=null){
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
           try {
               List<Address> addresses = geoCoder.getFromLocation(
                  p.getLatitudeE6()  / 1E6, 
                  p.getLongitudeE6() / 1E6, 1);
              if(addresses!=null && addresses.size()>0){
   				List<Overlay> overlayList = mapView.getOverlays();
   				address = addresses.get(0);//address contains latitude and longitude inofmations
   				addressOverlay overlay = new addressOverlay(address,context);
   				overlayList.clear();
   				overlayList.add(overlay);
   				mapView.invalidate();//invokes draw method if view is visible
              }else{
            	  //nothing
              }
              String add = "";
             if (addresses.size() > 0) 
             {
                for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
                  add += addresses.get(0).getAddressLine(i) + "\n";
             }
                Toast.makeText(context, add, (int)3).show();
            }
            catch (IOException e) {                
                e.printStackTrace();
            }   
            return true;
        }
        else                
            return false;
	}
/*
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		if (event.getAction() == 1) {                
            GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());
            Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
               try {
                   List<Address> addresses = geoCoder.getFromLocation(
                      p.getLatitudeE6()  / 1E6, 
                      p.getLongitudeE6() / 1E6, 1);
                  if(addresses!=null && addresses.size()>0){
       				List<Overlay> overlayList = mapView.getOverlays();
       				address = addresses.get(0);//address contains latitude and longitude inofmations
       				addressOverlay overlay = new addressOverlay(address,context);
       				overlayList.clear();
       				overlayList.add(overlay);
       				mapView.invalidate();//invokes draw method if view is visible
                  }else{
                	  //nothing
                  }
                  String add = "";
                 if (addresses.size() > 0) 
                 {
                    for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
                      add += addresses.get(0).getAddressLine(i) + "\n";
                 }
                    Toast.makeText(context, add, (int)3).show();
                }
                catch (IOException e) {                
                    e.printStackTrace();
                }   
                return true;
            }
            else                
                return false;
	}
*/
	public GeoPoint getGeoPoint() {
		// TODO Auto-generated method stub
		return point;
	}

	private void setGeographicPoint(GeoPoint point) {
		// TODO Auto-generated method stub
		this.point = point;
	}

	private void setAddress(Address address) {
		// TODO Auto-generated method stub
		this.address = address;
	}

}
