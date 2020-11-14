package com.example.weather_pm

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.moshi.Json
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class WeatherResponse(
    @field:Json(name = "lat") var lat: Float,
    @field:Json(name = "lon") var lon: Float,
    @field:Json(name = "timezone") var timezone: String,
    @field:Json(name = "current") var current: Current
)

class Current(
    @field:Json(name = "humidity") var humidity: Int,
    @field:Json(name = "temp") var temp: Float,
    @field:Json(name = "clouds") var clouds: Int,
    @field:Json(name = "wind_speed") var wind_speed: Int,
    @field:Json(name = "pressure") var pressure: Int

)



@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class MainActivity : Activity() {
    companion object{
        const val URL = "https://api.openweathermap.org/data/2.5/"
        const val lat = "59.937500"
        const val lon = "30.308611"
        const val exclude = "minutely,hourly,daily,alerts"
        const val API_KEY = "6f102fd88763d69f0b8347b981501367"
    }

    public interface OpenWeatherMapService {
        @GET("onecall?lat=59.937500&lon=30.308611&exclude=minutely,hourly,alerts&units=metric&appid=6f102fd88763d69f0b8347b981501367")
        fun listWeather(): Call<WeatherResponse>
    }

    private var flagTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofit =
            Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        val service = retrofit.create(OpenWeatherMapService::class.java)
        val call: Call<WeatherResponse> = service.listWeather()
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder = "lat: " +
                            weatherResponse.lat.toString() +
                            "\n"

                    Log.d("MSG-ff", stringBuilder)
                    temperature.text = weatherResponse.current.temp.toString()
                    pressure.text = weatherResponse.current.pressure.toString()
                    pressure.text = weatherResponse.current.pressure.toString()
                    WindFLow.text = weatherResponse.current.wind_speed.toString()
                    humidity.text = weatherResponse.current.humidity.toString()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("MSG-ff", "ERROR")
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("flagTheme", flagTheme)
    }

}