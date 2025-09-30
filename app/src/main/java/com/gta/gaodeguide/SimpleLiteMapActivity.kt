package com.gta.gaodeguide

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * 轻量版SDK简化地图示例
 * 基于高德轻量版SDK的基本地图显示
 */
class SimpleLiteMapActivity : ComponentActivity() {
    
    private var mapView: Any? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // 尝试创建MapView - 使用反射以避免编译错误
            val mapViewClass = Class.forName("com.amap.api.maps.MapView")
            val constructor = mapViewClass.getConstructor(android.content.Context::class.java)
            mapView = constructor.newInstance(this)
            
            // 调用onCreate方法
            val onCreateMethod = mapViewClass.getMethod("onCreate", Bundle::class.java)
            onCreateMethod.invoke(mapView, savedInstanceState)
            
            // 设置为内容视图
            setContentView(mapView as android.view.View)
            
            // 初始化地图
            initMap()
            
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果SDK不可用，显示错误信息
            setContentView(createErrorView())
        }
    }
    
    private fun createErrorView(): android.view.View {
        val textView = android.widget.TextView(this)
        textView.text = "地图SDK初始化失败"
        textView.textSize = 16f
        textView.setPadding(32, 32, 32, 32)
        return textView
    }
    
    private fun initMap() {
        try {
            // 获取地图对象并进行基本设置
            val mapViewClass = mapView?.javaClass
            val getMapMethod = mapViewClass?.getMethod("getMap")
            val aMap = getMapMethod?.invoke(mapView)
            
            // 如果成功获取地图对象，可以进行进一步配置
            aMap?.let {
                println("地图初始化成功")
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onResume() {
        super.onResume()
        try {
            val mapViewClass = mapView?.javaClass
            val onResumeMethod = mapViewClass?.getMethod("onResume")
            onResumeMethod?.invoke(mapView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onPause() {
        super.onPause()
        try {
            val mapViewClass = mapView?.javaClass
            val onPauseMethod = mapViewClass?.getMethod("onPause")
            onPauseMethod?.invoke(mapView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        try {
            val mapViewClass = mapView?.javaClass
            val onDestroyMethod = mapViewClass?.getMethod("onDestroy")
            onDestroyMethod?.invoke(mapView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        try {
            val mapViewClass = mapView?.javaClass
            val onSaveInstanceStateMethod = mapViewClass?.getMethod("onSaveInstanceState", Bundle::class.java)
            onSaveInstanceStateMethod?.invoke(mapView, outState)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}