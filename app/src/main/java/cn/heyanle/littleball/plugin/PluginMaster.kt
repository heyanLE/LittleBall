package cn.heyanle.littleball.plugin

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.widget.Toast
import dalvik.system.DexClassLoader
import java.util.*

class PluginMaster(private val masterContext: Context) {

    private val applicationInfoList = LinkedList<ApplicationInfo>()
    private val pluginMap = HashMap<String, Plugin>()
    private val packageManager: PackageManager = masterContext.packageManager

    fun checkPlugin(context: Context) {
        val applicationInfoList = packageManager.getInstalledApplications(
                PackageManager.GET_META_DATA)
        for (applicationInfo in applicationInfoList) {
//            var bundle = applicationInfo.metaData
//            if (bundle == null) {
//                Toast.makeText(masterContext, "NULL!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show()
//                continue
//            }
            if (applicationInfo.metaData.getBoolean("LittleBallModule", false)) {
                applicationInfoList.add(applicationInfo)
            }
        }
    }

    fun initPlugin() {
        for (applicationInfo in applicationInfoList) {
            val packageManager = masterContext.packageManager
            val packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, 0)
            val pluginEnter = applicationInfo.metaData.getString("LittleBallEnter") ?: continue
            applicationInfo.sourceDir
            val dexClassLoader = DexClassLoader(applicationInfo.sourceDir,
                    masterContext.codeCacheDir.absolutePath,
                    applicationInfo.nativeLibraryDir,
                    ClassLoader.getSystemClassLoader())
            val plugin = dexClassLoader.loadClass(pluginEnter).newInstance() as Plugin
            plugin.packageInfo = packageInfo
            pluginMap[plugin.name] = plugin
        }
    }

    fun loadPlugin() {
        for (plugin in pluginMap.values) {
            plugin.onLoad()
        }
    }

    fun getPlugin(pluginName: String): Plugin? {
        return if (pluginMap.containsKey(pluginName)) {
            pluginMap[pluginName]
        } else null
    }

}