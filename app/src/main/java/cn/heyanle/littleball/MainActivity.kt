package cn.heyanle.littleball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.heyanle.littleball.plugin.PluginMaster

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pluginMaster = PluginMaster(this)
        pluginMaster.checkPlugin(this)
        pluginMaster.initPlugin()
        pluginMaster.loadPlugin()
    }
}
