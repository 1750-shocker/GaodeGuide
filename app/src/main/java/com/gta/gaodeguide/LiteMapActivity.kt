package com.gta.gaodeguide

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch

/**
 * 高德轻量版SDK完整功能示例
 * 包含地图显示、定位、搜索、标记等功能
 */
class LiteMapActivity : ComponentActivity(), AMapLocationListener, 
    GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener {
    
    private var mapView: MapView? = null
    private var aMap: AMap? = null
    private var locationClient: AMapLocationClient? = null
    private var geocodeSearch: GeocodeSearch? = null
    private var poiSearch: PoiSearch? = null
    private var currentLocationMarker: Marker? = null
    
    // 权限请求
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            initLocation()
        } else {
            Toast.makeText(this, "需要定位权限才能使用定位功能", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建MapView
        mapView = MapView(this)
        mapView?.onCreate(savedInstanceState)
        setContentView(mapView)
        
        // 初始化各个组件
        initMap()
        initSearch()
        checkAndRequestPermissions()
    }
    
    private fun initMap() {
        aMap = mapView?.map
        aMap?.let { map ->
            // 设置地图类型
            map.mapType = AMap.MAP_TYPE_NORMAL
            
            // 设置UI控件
            map.uiSettings?.apply {
                isZoomControlsEnabled = true
                isCompassEnabled = true
                isScaleControlsEnabled = true
                isMyLocationButtonEnabled = true
            }
            
            // 设置地图点击事件
            map.setOnMapClickListener { latLng ->
                onMapClick(latLng)
            }
            
            // 设置标记点击事件
            map.setOnMarkerClickListener { marker ->
                onMarkerClick(marker)
                true
            }
            
            // 设置默认位置（北京）
            val beijing = LatLng(39.906901, 116.397972)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(beijing, 10f))
        }
    }
    
    private fun initSearch() {
        try {
            // 初始化地理编码搜索
            geocodeSearch = GeocodeSearch(this)
            geocodeSearch?.setOnGeocodeSearchListener(this)
            
            // 初始化POI搜索
            poiSearch = PoiSearch(this, null)
            poiSearch?.setOnPoiSearchListener(this)
            
        } catch (e: AMapException) {
            e.printStackTrace()
            Toast.makeText(this, "搜索功能初始化失败", Toast.LENGTH_SHORT).show()
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
                isOnceLocation = false  // 连续定位
                interval = 5000  // 定位间隔5秒
                isNeedAddress = true  // 需要地址信息
                httpTimeOut = 20000
                locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
            }
            
            locationClient?.setLocationOption(option)
            locationClient?.startLocation()
            
            // 启用我的位置显示
            aMap?.isMyLocationEnabled = true
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "定位初始化失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    // 地图点击事件
    private fun onMapClick(latLng: LatLng) {
        // 添加标记点
        addMarker(latLng, "点击位置")
        
        // 逆地理编码 - 获取点击位置的地址
        val latLonPoint = LatLonPoint(latLng.latitude, latLng.longitude)
        val query = RegeocodeQuery(latLonPoint, 200f, GeocodeSearch.AMAP)
        geocodeSearch?.getFromLocationAsyn(query)
    }
    
    // 标记点击事件
    private fun onMarkerClick(marker: Marker): Boolean {
        val title = marker.title ?: "未知位置"
        val snippet = marker.snippet ?: ""
        Toast.makeText(this, "$title\n$snippet", Toast.LENGTH_LONG).show()
        return true
    }
    
    // 添加标记点
    private fun addMarker(latLng: LatLng, title: String, snippet: String = "") {
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(title)
            .snippet(snippet)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        
        aMap?.addMarker(markerOptions)
    }
    
    // 搜索附近POI
    private fun searchNearbyPoi(latLng: LatLng, keyword: String = "餐厅") {
        try {
            val query = PoiSearch.Query(keyword, "", "")
            query.pageSize = 10
            query.pageNum = 1
            
            poiSearch?.query = query
            poiSearch?.bound = PoiSearch.SearchBound(
                LatLonPoint(latLng.latitude, latLng.longitude), 1000
            )
            poiSearch?.searchPOIAsyn()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // 定位回调
    override fun onLocationChanged(location: AMapLocation?) {
        location?.let {
            if (it.errorCode == 0) {
                val latLng = LatLng(it.latitude, it.longitude)
                
                // 移动地图到当前位置
                aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                
                // 更新当前位置标记
                currentLocationMarker?.remove()
                currentLocationMarker = aMap?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("我的位置")
                        .snippet("${it.address}\n精度: ${it.accuracy}米")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )
                
                Toast.makeText(this, "定位成功: ${it.address}", Toast.LENGTH_SHORT).show()
                
            } else {
                Toast.makeText(this, "定位失败: ${it.errorInfo}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    // 地理编码回调
    override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS && result?.regeocodeAddress != null) {
            val address = result.regeocodeAddress.formatAddress
            Toast.makeText(this, "地址: $address", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onGeocodeSearched(result: GeocodeResult?, rCode: Int) {
        // 地理编码结果（地址转坐标）
    }
    
    // POI搜索回调
    override fun onPoiSearched(result: PoiResult?, rCode: Int) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS && result != null) {
            result.pois?.forEach { poi ->
                val latLng = LatLng(poi.latLonPoint.latitude, poi.latLonPoint.longitude)
                addMarker(latLng, poi.title, poi.snippet)
            }
            Toast.makeText(this, "找到 ${result.pois?.size} 个相关地点", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onPoiItemSearched(p0: com.amap.api.services.poisearch.PoiItem?, p1: Int) {
        // POI详情搜索回调
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