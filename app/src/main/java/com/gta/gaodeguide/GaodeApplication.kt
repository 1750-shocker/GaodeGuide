package com.gta.gaodeguide

import android.app.Application
import com.amap.api.maps.MapsInitializer
import com.amap.api.location.AMapLocationClient

class GaodeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化高德地图SDK
        try {
            // 设置隐私政策同意状态（必须）
            MapsInitializer.updatePrivacyShow(this, true, true)
            MapsInitializer.updatePrivacyAgree(this, true)
            
            // 初始化定位SDK
            AMapLocationClient.updatePrivacyShow(this, true, true)
            AMapLocationClient.updatePrivacyAgree(this, true)
            
            // 设置API Key（也可以在AndroidManifest.xml中设置）
            // MapsInitializer.setApiKey("YOUR_API_KEY_HERE")
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}