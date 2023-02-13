package com.julienhammer.go4lunch.models;

import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class PlacesResponse {

    public class Geometry{
        public Location location;
        public Viewport viewport;
    }

    public class Location{
        public double lat;
        public double lng;
    }

    public class Northeast{
        public double lat;
        public double lng;
    }

    public class OpeningHours{
        public boolean open_now;
    }

    public class Photo{
        public int height;
        public ArrayList<String> html_attributions;
        public String photo_reference;
        public int width;
    }

    public class PlusCode{
        public String compound_code;
        public String global_code;
    }

    public class Result{
        public String business_status;
        public String formatted_address;
        public Geometry geometry;
        public String icon;
        public String icon_background_color;
        public String icon_mask_base_uri;
        public String name;
        public OpeningHours opening_hours;
        public ArrayList<Photo> photos;
        public String place_id;
        public PlusCode plus_code;
        public int price_level;
        public double rating;
        public String reference;
        public ArrayList<String> types;
        public int user_ratings_total;
    }

    public static class Root{
        public ArrayList<Object> html_attributions;
        public String next_page_token;
        public ArrayList<Result> results;
        public String status;

        public Root(ArrayList<Object> html_attributions, String next_page_token, ArrayList<Result> results, String status) {
            this.html_attributions = html_attributions;
            this.next_page_token = next_page_token;
            this.results = results;
            this.status = status;
        }

    }

    public class Southwest{
        public double lat;
        public double lng;
    }

    public class Viewport{
        public Northeast northeast;
        public Southwest southwest;
    }

}
