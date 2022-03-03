package com.example.assignment.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapResponse {
    @SerializedName("results")
    public List<Results> results;
    @SerializedName("status")
    public String status;

    public static class Results {
        @SerializedName("address_components")
        public List<AddressComponents> addressComponents;
        @SerializedName("formatted_address")
        public String formattedAddress;
        @SerializedName("geometry")
        public Geometry geometry;
        @SerializedName("place_id")
        public String placeId;
        @SerializedName("types")
        public List<String> types;

        public static class Geometry {
            @SerializedName("bounds")
            public Bounds bounds;
            @SerializedName("location")
            public Location location;
            @SerializedName("location_type")
            public String locationType;
            @SerializedName("viewport")
            public Viewport viewport;

            public static class Bounds {
                @SerializedName("northeast")
                public Northeast northeast;
                @SerializedName("southwest")
                public Southwest southwest;


                public static class Northeast {
                    @SerializedName("lat")
                    public Double lat;
                    @SerializedName("lng")
                    public Double lng;
                }


                public static class Southwest {
                    @SerializedName("lat")
                    public Double lat;
                    @SerializedName("lng")
                    public Double lng;
                }
            }

            public static class Location {
                @SerializedName("lat")
                public Double lat;
                @SerializedName("lng")
                public Double lng;
            }


            public static class Viewport {
                @SerializedName("northeast")
                public Northeast northeast;
                @SerializedName("southwest")
                public Southwest southwest;


                public static class Northeast {
                    @SerializedName("lat")
                    public Double lat;
                    @SerializedName("lng")
                    public Double lng;
                }


                public static class Southwest {
                    @SerializedName("lat")
                    public Double lat;
                    @SerializedName("lng")
                    public Double lng;
                }
            }
        }


        public static class AddressComponents {
            @SerializedName("long_name")
            public String longName;
            @SerializedName("short_name")
            public String shortName;
            @SerializedName("types")
            public List<String> types;
        }
    }
}
