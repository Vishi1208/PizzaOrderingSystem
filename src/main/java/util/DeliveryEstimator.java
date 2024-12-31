package util;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class DeliveryEstimator {
    private static final String API_KEY = "5b3ce3597851110001cf62484d3b13c847f6486ba49e7465bc78c438";
    private static final String MATRIX_API_URL = "https://api.openrouteservice.org/v2/matrix/driving-car";
    private static final String GEOCODING_API_URL = "https://api.openrouteservice.org/geocode/search";

    /**
     * Get the estimated delivery time between the shop and the destination.
     */
    public static String getDeliveryEstimate(double[] destinationCoordinates) {
        OkHttpClient client = new OkHttpClient();

        // Coordinates for the shop (Horana, Sri Lanka)
        double[] shopCoordinates = {80.0589, 6.7563};

        try {
            JSONObject requestBody = new JSONObject();
            JSONArray locations = new JSONArray();

            JSONArray origin = new JSONArray();
            origin.put(shopCoordinates[0]); // Longitude
            origin.put(shopCoordinates[1]); // Latitude
            locations.put(origin);

            JSONArray destination = new JSONArray();
            destination.put(destinationCoordinates[0]); // Longitude
            destination.put(destinationCoordinates[1]); // Latitude
            locations.put(destination);

            requestBody.put("locations", locations);

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(MATRIX_API_URL)
                    .post(body)
                    .addHeader("Authorization", API_KEY)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return "HTTP Error: " + response.code() + " - " + response.message();
            }

            String responseBody = response.body().string();
            return parseDeliveryEstimate(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to fetch delivery estimate. Please try again later.";
        }
    }

    /**
     * Parse the API response to extract the delivery estimate.
     */
    private static String parseDeliveryEstimate(String responseBody) {
        try {
            JSONObject json = new JSONObject(responseBody);

            JSONArray durations = json.optJSONArray("durations");
            if (durations != null && durations.length() > 0) {
                JSONArray firstDurationArray = durations.getJSONArray(0);
                if (firstDurationArray.length() > 1) {
                    double durationInSeconds = firstDurationArray.getDouble(1);
                    int durationInMinutes = (int) Math.ceil(durationInSeconds / 60);
                    return "Estimated delivery time: " + durationInMinutes + " minutes";
                }
            }
            return "No delivery information available.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing delivery estimate.";
        }
    }

    /**
     * Converts an address to coordinates using the OpenRouteService Geocoding API.
     */
    public static double[] getCoordinates(String address) {
        OkHttpClient client = new OkHttpClient();

        try {
            String url = GEOCODING_API_URL + "?api_key=" + API_KEY + "&text=" + address.replace(" ", "%20");

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                System.err.println("HTTP Error: " + response.code());
                return new double[]{0.0, 0.0};
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);

            JSONArray features = json.optJSONArray("features");
            if (features != null && features.length() > 0) {
                JSONObject firstFeature = features.getJSONObject(0);
                JSONObject geometry = firstFeature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");

                return new double[]{coordinates.getDouble(0), coordinates.getDouble(1)};
            } else {
                System.err.println("No features found for the given address.");
                return new double[]{0.0, 0.0};
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new double[]{0.0, 0.0};
        }
    }
}
