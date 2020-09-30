package com.daffodil.assignment.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.daffodil.assignment.R;
import com.daffodil.assignment.googlemaps.api.GoogleMapServiceManager;
import com.daffodil.assignment.googlemaps.api.model.GoogleApiResponse;
import com.daffodil.assignment.googlemaps.api.model.PlaceModel;
import com.daffodil.assignment.googlemaps.api.model.PlaceStructuredFormat;
import com.daffodil.assignment.googlemaps.repo.location.ILocationRepository;
import com.daffodil.assignment.googlemaps.repo.location.LocationRepositiory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LocationPickerAdapter extends ArrayAdapter<PlaceModel> {

    private final ILocationRepository locationRepository;
    private List<PlaceModel> searchPlaceList = new ArrayList<>();
    private List<PlaceModel> filterSearchList = new ArrayList<>();
    private ArrayList<PlaceModel> recentSearchPlaceList = new ArrayList<>();
    private Context context;
    private LatLng currentLocation;

    private boolean cameraMove = false;

    public LocationPickerAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        this.context = context;
        locationRepository = new LocationRepositiory(context);
        HashMap<String, PlaceModel> placesList = locationRepository.readRecentPlacesList();
        recentSearchPlaceList.addAll(placesList.values());

        searchPlaceList.addAll(recentSearchPlaceList);
        filterSearchList.addAll(searchPlaceList);


        if (filterSearchList == null || filterSearchList.size()<=0) {
            filterSearchList = new ArrayList<>();
        }

    }

    public void setCurrentLocation(LatLng currentLocation){
        if (currentLocation != null) {
            this.currentLocation = currentLocation;
        }
    }


    @Override
    public int getCount() {
        if (filterSearchList == null || filterSearchList.size() <= 0) {
            return 0;
        } else {
            return filterSearchList.size();
        }
    }

    @Nullable
    @Override
    public PlaceModel getItem(int position) {
        if (filterSearchList == null || filterSearchList.size() <=0) {
            return null;
        } else {
            return filterSearchList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.common_map_location_view, parent, false);
        }

        AppCompatTextView address = convertView.findViewById(R.id.txt_address);
        AppCompatTextView addressDetail = convertView.findViewById(R.id.txt_address_detail);

        PlaceModel model = filterSearchList.get(position);
        address.setText(model.getPlaceStructuredFormat().getMain_text());
        addressDetail.setText(model.getPlaceStructuredFormat().getSecondary_text());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final List<PlaceModel> filtered = new ArrayList<>();
                filterSearchList.clear();
                if (didCameraMove()) {
                    setCameraMove(false);
                    return null;
                }
                if (constraint.toString().trim().isEmpty()) {
                    filterSearchList.addAll(recentSearchPlaceList);
                } else {
                    if (searchPlaceList.size() > 0) {
                        for (int i = 0; i < searchPlaceList.size(); i++) {
                            if (searchPlaceList.get(i).getDescription().toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                                filterSearchList.add(searchPlaceList.get(i));
                            }
                        }
                    }
                }
                if (filterSearchList.size() > 0) {
                    filtered.addAll(filterSearchList);
                } else if (currentLocation!=null) {
                    locationRepository.autoCompletePlacesApiCall(context, constraint.toString().trim(), currentLocation.latitude + "," + currentLocation.longitude, new GoogleMapServiceManager.IMapServiceListener() {
                        @Override
                        public void onSuccess(GoogleApiResponse routes) {
                            filterSearchList.clear();
                            filterSearchList.addAll(routes.getPlaceModelList());

                            FilterResults results = new FilterResults();
                            results.values = filtered;
                            results.count = filtered.size();
                            publishResults(constraint, results);
                        }

                        @Override
                        public void onFailure(int no_route_found) {
                            if (filtered.isEmpty()) {
                                filtered.addAll(getErrorView());
                            }
                            FilterResults results = new FilterResults();
                            results.values = filtered;
                            results.count = filtered.size();
                            publishResults(constraint, results);
                        }
                    });
                    if (filtered.isEmpty()) {
                        filtered.addAll(getErrorView());
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                if (results != null && results.count > 0) {
                    addAll((Collection<? extends PlaceModel>) results.values);
                }
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                if (resultValue instanceof PlaceModel) {
                    PlaceModel model = (PlaceModel) resultValue;
                    return model.getPlaceStructuredFormat().getMain_text() +" "+model.getPlaceStructuredFormat().getSecondary_text();
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }


    private ArrayList<PlaceModel> getErrorView() {
        ArrayList<PlaceModel> suggestionList = new ArrayList<>();
        PlaceStructuredFormat format = new PlaceStructuredFormat();
        format.setMain_text(context.getResources().getString(R.string.error_location_not_found));
        PlaceModel model = new PlaceModel();
        model.setPlaceStructuredFormat(format);
        suggestionList.add(model);
        return suggestionList;
    }


    public void setCameraMove(boolean didCameraMove) {
        this.cameraMove = didCameraMove;
    }

    public boolean didCameraMove() {
        return cameraMove;
    }
}
