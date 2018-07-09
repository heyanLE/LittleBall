package cn.heyanle.littleball.pluginer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.heyanle.littleball.plugin.IntLittleBall;
import cn.heyanle.littleball.plugin.Plugin;
import cn.heyanle.littleball.plugin.base.BasePlugin;
import dalvik.system.DexClassLoader;

public class PluginManager {

    private Context context;
    private List<BasePlugin> basePluginList = new ArrayList<>();
    private Map<String, Plugin> pluginMap = new HashMap<>();
    private PackageManager packageManager;

    public PluginManager(Context context) {
        packageManager = (this.context = context).getPackageManager();
    }

    public void checkPlugin() {
        List<ApplicationInfo> applicationInfoList = packageManager.
                getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            Bundle bundle = applicationInfo.metaData;
            if (bundle.getBoolean("LittleBallModule", false)) {
                try {
                    BasePlugin basePlugin = new Plugin();
                    basePlugin.setName(applicationInfo.name);
                    basePlugin.setDescribe(bundle.getString("LittleBallDescribe"));
                    basePlugin.setEntrance(bundle.getString("LittleBallEntrance"));
                    basePlugin.setMinVersion(bundle.getFloat("LittleBallMinVersion"));
                    basePlugin.setVersion(packageManager.getPackageInfo(
                            applicationInfo.packageName, 0).versionName);
                    basePlugin.setResources(context.getResources());
                    basePluginList.add(basePlugin);
                    Toast.makeText(context, basePlugin.getName(), Toast.LENGTH_LONG).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadPlugin() {
        for (BasePlugin basePlugin : basePluginList) {
            try {
                if (basePlugin.getEntrance().isEmpty()) continue;
                ApplicationInfo applicationInfo = basePlugin.getApplicationInfo();
                String sourceDir = applicationInfo.sourceDir;
                String nativeLibraryDir = applicationInfo.nativeLibraryDir;
                String codeCacheDir = context.getCodeCacheDir().getAbsolutePath();
                DexClassLoader dexClassLoader = new DexClassLoader(sourceDir, codeCacheDir,
                        nativeLibraryDir, this.getClass().getClassLoader());
                Plugin plugin = (Plugin) dexClassLoader.loadClass(basePlugin.getEntrance()).
                        newInstance();
                Context context = this.context.createPackageContext(basePlugin.getPackageName(),
                        Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
                plugin.setName(basePlugin.getName());
                plugin.setDescribe(basePlugin.getDescribe());
                plugin.setEntrance(basePlugin.getEntrance());
                plugin.setMinVersion(basePlugin.getMinVersion());
                plugin.setVersion(basePlugin.getVersion());
                plugin.setResources(basePlugin.getResources());
                pluginMap.put(plugin.getName(), plugin);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
                    PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Plugin getPlugin(String pluginName) {
        if (pluginMap.containsKey(pluginName)) {
            return pluginMap.get(pluginName);
        }
        return null;
    }

    public void onPluginDestroy() {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onPluginDestroy();
        }
    }

    public void onLoad(IntLittleBall intLittleBall) {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onLoad(intLittleBall);
        }
    }

    public void onWindowClose() {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onWindowClose();
        }
    }

    public void onWindowShow() {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onWindowShow();
        }
    }

    public void onVisibility(Plugin plugin, boolean isFirst) {
        plugin.onVisibility(isFirst);
    }

    public void onInVisibility(Plugin plugin) {
        plugin.onInVisibility();
    }

}