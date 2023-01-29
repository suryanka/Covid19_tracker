package com.ghosh.covid_19_tracker

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ghosh.covid_19_tracker.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var covidList=ArrayList<covidRvModel>()
    lateinit var mainBinding:ActivityMainBinding
    private lateinit var covidRvAdapter: CovidRvAdapter
    var permissionCode=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view=mainBinding.root
        setContentView(view)

        loadRv()

        covidRvAdapter=CovidRvAdapter(this@MainActivity,covidList)
        mainBinding.covidRecyclerView.adapter=covidRvAdapter

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET),permissionCode)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==permissionCode && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this,"Permission Granted", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this,"Please grant the permissions", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    fun loadRv() {
        var url:String="https://api.rootnet.in/covid19-in/stats/latest"
        var requestQueue:RequestQueue=Volley.newRequestQueue(this@MainActivity)

        val jsonObjectRequest:JsonObjectRequest= JsonObjectRequest(Request.Method.GET, url, null, {response->

            covidList.clear()
            try{
                var caseSummary:JSONObject=response.getJSONObject("data").getJSONObject("summary")
                mainBinding.idCasesIndia.text=caseSummary.getInt("total").toString()
                mainBinding.idRecoveredCasesIndia.text=caseSummary.getInt("discharged").toString()
                mainBinding.idDeathsIndia.text=caseSummary.getInt("deaths").toString()

                var ufo_summary:JSONObject=response.getJSONObject("data").getJSONArray("unofficial-summary").getJSONObject(0)
                mainBinding.idCasesCo.text=ufo_summary.getInt("total").toString()
                mainBinding.idRecoveredCasesCo.text=ufo_summary.getInt("recovered").toString()
                mainBinding.idDeathsCo.text=ufo_summary.getInt("deaths").toString()

                var regionalArray:JSONArray=response.getJSONObject("data").getJSONArray("regional")

                for(i in 0..regionalArray.length())
                {
                    var stateObj:JSONObject=regionalArray.getJSONObject(i)
                    var cases:String=stateObj.getInt("totalConfirmed").toString()
                    var recovered:String=stateObj.getInt("discharged").toString()
                    var deaths:String=stateObj.getInt("deaths").toString()
                    var state:String=stateObj.getString("loc")

                    covidList.add(covidRvModel(cases,recovered,deaths,state))
                }

                covidRvAdapter.notifyDataSetChanged()

            }
            catch(e:JSONException)
            {
                e.printStackTrace()
            }
        },
        {
            Toast.makeText(this@MainActivity,"Please enter valid search content.", Toast.LENGTH_LONG).show()
        })

        requestQueue.add(jsonObjectRequest)
    }

}