package com.gta.gaodeguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng

/**
 * 轻量版SDK简化地图示例
 * 基于高德轻量版SDK的基本地图显示
 */
class SimpleLiteMapActivity : ComponentActivity() {
    
    private var mapView: MapView? = null
    private var aMap: AMap? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建MapView
        mapView = MapView(this)
        mapView?.onCreate(savedInstanceState)
        setContentView(mapView)
        
        // 初始化地图
        initMap()
    }
    
    private fun initMap() {
        aMap = mapView?.map
        aMap?.let { map ->
            // 设置地图类型为普通地图
            map.mapType = AMap.MAP_TYPE_NORMAL
            
            // 设置默认位置（北京天安门）
            val beijing = LatLng(39.906901, 116.397972)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(beijing, 12f))
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
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}