package com.example.pornhubsearch

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.net.URLEncoder
import java.util.Locale

class MainActivity : AppCompatActivity() {
 private lateinit var web: WebView; private lateinit var locationText: TextView; private var place=""
 override fun onCreate(b: Bundle?) { super.onCreate(b); setContentView(R.layout.activity_main)
  locationText=findViewById(R.id.locationText); web=findViewById(R.id.web); web.webViewClient=WebViewClient(); web.settings.javaScriptEnabled=true
  val dates=arrayOf("Mọi thời gian","24 giờ","7 ngày","30 ngày","365 ngày")
  val durations=arrayOf("Mọi thời lượng","Dưới 5 phút","5–15 phút","15–30 phút","Trên 30 phút")
  findViewById<Spinner>(R.id.dateFilter).adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,dates)
  findViewById<Spinner>(R.id.durationFilter).adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,durations)
  findViewById<Button>(R.id.search).setOnClickListener { search() }
  findViewById<Button>(R.id.imageSearch).setOnClickListener { startActivity(Intent("android.intent.action.VIEW", android.net.Uri.parse("https://lens.google.com/"))) }
  if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),7) else resolveLocation()
 }
 private fun resolveLocation(){ try { val lm=getSystemService(LOCATION_SERVICE) as android.location.LocationManager; val p=lm.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER); if(p!=null){ val a=Geocoder(this,Locale.getDefault()).getFromLocation(p.latitude,p.longitude,1)?.firstOrNull(); place=listOfNotNull(a?.locality,a?.adminArea,a?.countryName).joinToString(", "); locationText.text="📍 Vị trí: ${if(place.isBlank()) "khu vực hiện tại" else place}" } } catch(_:Exception){} }
 override fun onRequestPermissionsResult(r:Int,p:Array<out String>,g:IntArray){ super.onRequestPermissionsResult(r,p,g); if(r==7&&g.isNotEmpty()&&g[0]==PackageManager.PERMISSION_GRANTED) resolveLocation() }
 private fun search(){
  val q=findViewById<EditText>(R.id.query).text.toString().trim(); val area=findViewById<EditText>(R.id.area).text.toString().trim(); val tags=findViewById<EditText>(R.id.tags).text.toString().trim(); val ex=findViewById<EditText>(R.id.exclude).text.toString().trim(); val boost=findViewById<CheckBox>(R.id.locationBoost).isChecked; val hd=findViewById<CheckBox>(R.id.hd).isChecked
  val terms=mutableListOf<String>(); if(q.isNotBlank()) terms+=q; if(tags.isNotBlank()) terms+=tags.split(",").map{it.trim()}; if(hd) terms+="HD 4K"; if(boost&&place.isNotBlank()) terms+=place; else if(area.isNotBlank()) terms+=area
  val minus=ex.split(",").map{it.trim()}.filter{it.isNotBlank()}.joinToString(" "){ "-$it" }
  val query=URLEncoder.encode((terms.joinToString(" ")+" "+minus).trim(),"UTF-8")
  web.loadUrl("https://www.pornhub.com/video/search?search=$query")
 }
}
