package com.gta.gaodeguide

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

class MapActivity : ComponentActivity(), AMapLocationListener {
    
    private var mapView: MapView? = null
    private var aMap: AMap? = null
    private var locationClient: AMapLocationClient? = null
    
    // 权限请求启动器
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            initLocation()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建MapView
        mapView = MapView(this)
        mapView?.onCreate(savedInstanceState)
        setContentView(mapView)
        
        // 初始化地图
        initMap()
        
        // 检查并请求权限
        checkAndRequestPermissions()
    }
    
    private fun initMap() {
        aMap = mapView?.map
        aMap?.let { map ->
            // 设置地图类型
            map.mapType = AMap.MAP_TYPE_NORMAL
            
            // 设置缩放控件
            map.uiSettings.isZoomControlsEnabled = true
            
            // 设置指南针
            map.uiSettings.isCompassEnabled = true
            
            // 设置比例尺
            map.uiSettings.isScaleControlsEnabled = true
            
            // 设置默认位置（北京）
            val beijing = LatLng(39.906901, 116.397972)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(beijing, 10f))
        }
    }
    
    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        val needRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        
        if (needRequest.isEmpty()) {
            initLocation()
        } else {
            requestPermissionLauncher.launch(needRequest.toTypedArray())
        }
    }
    
    private fun initLocation() {
        try {
            locationClient = AMapLocationClient(applicationContext)
            locationClient?.setLocationListener(this)
            
            val option = AMapLocationClientOption().apply {
                locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                isOnceLocation = true
                isNeedAddress = true
                httpTimeOut = 20000
                locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
            }
            
            locationClient?.setLocationOption(option)
            locationClient?.startLocation()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onLocationChanged(location: AMapLocation?) {
        location?.let {
            if (it.errorCode == 0) {
                // 定位成功
                val latLng = LatLng(it.latitude, it.longitude)
                aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                
                // 显示我的位置
                aMap?.isMyLocationEnabled = true
            } else {
                // 定位失败
                println("定位失败: ${it.errorInfo}")
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }
    
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
        locationClient?.stopLocation()
        locationClient?.onDestroy()
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}