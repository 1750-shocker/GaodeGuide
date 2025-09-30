package com.gta.gaodeguide

import android.app.Application

class GaodeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 轻量版SDK不需要特殊初始化
        // API Key在AndroidManifest.xml中配置即可
    }
}