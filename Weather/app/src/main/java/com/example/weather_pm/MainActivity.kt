package com.example.weather_pm

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Json
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import kotlin.math.roundToInt
import io.reactivex.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class WeatherResponse(
    @field:Json(name = "timezone") var timezone: String,
    @field:Json(name = "current") var current: Current,
    @field:Json(name = "daily") var daily: List<Daily>
)

class Current(
    @field:Json(name = "humidity") var humidity: Int,
    @field:Json(name = "temp") var temp: Float,
    @field:Json(name = "clouds") var clouds: Int,
    @field:Json(name = "wind_speed") var wind_speed: Int,
    @field:Json(name = "weather") var weather: List<WeatherDescription>
)

class Daily(
    @field:Json(name = "temp") var temp: Temp,
    @field:Json(name = "weather") var weather: List<WeatherDescription>
)

class Temp(
    @field:Json(name = "day") var day: Float
)

class WeatherDescription(
    @field:Json(name = "main") var main: String,
    @field:Json(name = "icon") var icon: String
)



@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class MainActivity : AppCompatActivity() {
    companion object{
        const val URL = "https://api.openweathermap.org/data/2.5/"
    }

    interface OpenWeatherMapServiceRX {
        @GET("onecall?lat=59.937500&lon=30.308611&exclude=minutely,hourly,alerts&units=metric&appid=508af3c89d5fdcb4b9f030cea26e964e")
        fun listWeather(): Single<WeatherResponse>
    }

    private var flagTheme = false
    private val retrofit =
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    private val service: OpenWeatherMapServiceRX = retrofit.create(OpenWeatherMapServiceRX::class.java)

    private var rxResponse: Single<WeatherResponse>? = service.listWeather()

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("storage", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        fun setViewInfo() {
            textView.text = prefs.getString("textView", "")!!
            textViewWindFLow.text = prefs.getString("textViewWindFLow", "")!!
            textViewCloudy.text = prefs.getString("textViewCloudy", "")!!
            textViewHumidity.text = prefs.getString("textViewHumidity", "")!!
            textViewTempPN.text = prefs.getString("textViewTempPN", "")!!
            textViewTempVT.text = prefs.getString("textViewTempVT", "")!!
            textViewTempSR.text = prefs.getString("textViewTempSR", "")!!
            textViewTempCT.text = prefs.getString("textViewTempCT", "")!!
            textViewTempPT.text = prefs.getString("textViewTempPT", "")!!
            textViewTempSB.text = prefs.getString("textViewTempSB", "")!!
            textViewTempVS.text = prefs.getString("textViewTempVS", "")!!
        }

        rxResponse
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : DisposableSingleObserver<WeatherResponse>() {
                override fun onSuccess(weatherResponse: WeatherResponse) {
//                    TODO("Not yet implemented")
                    Log.d("SUCCESS", "${weatherResponse.current.weather}")
                    editor.putInt("textView", weatherResponse.current.temp.roundToInt()).apply()
                    val t = weatherResponse.current.temp.roundToInt()
                    if (t > 0) {
                        editor.putString("textView", "+${t}°C").apply()
                    } else {
                        editor.putString("textView", "${t}°C").apply()
                    }
                    editor.putString(
                        "textView2",
                        weatherResponse.timezone + "\n" + weatherResponse.current.weather[0].main
                    ).apply()
                    editor.putString(
                        "textViewWindFLow",
                        weatherResponse.current.wind_speed.toString()
                    ).apply()
                    editor.putString("textViewCloudy", weatherResponse.current.clouds.toString())
                        .apply()
                    editor.putString(
                        "textViewHumidity",
                        weatherResponse.current.humidity.toString()
                    ).apply()
                    val icon1 = weatherResponse.current.weather[0].icon
                    Picasso.get()
                        .load("https://openweathermap.org/img/wn/$icon1@2x.png")
                        .fit().centerCrop()
                        .into(icon)
                    fun genSaveEveryDay(days: List<Daily>) {
                        val textViewTempDay = listOf(
                            "textViewTempPN",
                            "textViewTempVT",
                            "textViewTempSR",
                            "textViewTempCT",
                            "textViewTempPT",
                            "textViewTempSB",
                            "textViewTempVS"
                        )
                        days.forEachIndexed { index, day ->
                            if (index != 0) {
                                val tempNow = day.temp.day.roundToInt()
                                if (tempNow >= 0) {
                                    editor.putString(textViewTempDay[index - 1], "+$tempNow")
                                        .apply()
                                } else {
                                    editor.putString(textViewTempDay[index - 1], "$tempNow").apply()
                                }
                            }
                        }
                    }
                    genSaveEveryDay(weatherResponse.daily)
                    setViewInfo()
                }


                override fun onError(e: Throwable) {
                    setViewInfo()
                    Log.d("Error!!!", "onError $e")
                }
            })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("flagTheme", flagTheme)
    }

    override fun onDestroy() {
        super.onDestroy()
        rxResponse = null
    }

}