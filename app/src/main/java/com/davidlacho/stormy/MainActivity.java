package com.davidlacho.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

  private CurrentWeather currentWeather;

  public static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String apiKey = getString(R.string.darksky_api_key);
    Double latitude = 37.8267;
    Double longitude = -122.4233;

    String forecastURL = "https://api.darksky.net/forecast/"
        + apiKey
        + "/"
        + latitude
        + ","
        + longitude;

    if (isNetworkAvailable()){

    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder().url(forecastURL).build();

    Call call = client.newCall(request);

    call.enqueue(new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
      }
      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        try {

          if (response.isSuccessful()) {
            String jsonData =  response.body().string();
            currentWeather = getCurrentDetails(jsonData);
            Log.v(TAG, jsonData);

          }
          else {
            alertUserAboutError();
          }
        }
        catch (IOException e) {
          Log.e(TAG, getString(R.string.IO_exception_message), e);
        } catch (JSONException e) {
          Log.e(TAG, getString(R.string.JSON_exception_message), e);
        }
      }
    });
    } else {
      Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
    }
  }

  private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
    CurrentWeather currentWeather = new CurrentWeather();
    JSONObject forecast = new JSONObject(jsonData);
    String timezone = forecast.getString("timezone");
    JSONObject currently = forecast.getJSONObject("currently");

    currentWeather.setHumidity(currently.getDouble("humidity"));
    currentWeather.setTime(currently.getLong("time"));
    currentWeather.setIcon(currently.getString("icon"));
    currentWeather.setLocationLabel("Somewhere");
    currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
    currentWeather.setSummary(currently.getString("summary"));
    currentWeather.setTemperature(currently.getDouble("temperature"));
    return currentWeather;
  }


  private boolean isNetworkAvailable() {
    ConnectivityManager manager =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }

  private void alertUserAboutError() {
    AlertDialogFragment dialog = new AlertDialogFragment();
    dialog.show(getSupportFragmentManager(), "error_dialog");
  }

}
