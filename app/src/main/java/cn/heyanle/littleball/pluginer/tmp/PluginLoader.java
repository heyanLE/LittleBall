package cn.heyanle.littleball.pluginer.tmp;

import android.content.Context;
import android.content.pm.PackageManager;

import cn.heyanle.littleball.pluginer.data.DataMonitor;

public class PluginLoader {

    Context context;
    PackageManager packageManager;

    public PluginLoader(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
        DataMonitor.init(context);
    }

//    public PluginLoader loadInfo() {
//        infoList.clear();
//        for (PluginData pluginData : DataMonitor.getInit().pluginDataS.dataList) {
//            try {
//                ApplicationInfo info = packageManager.getApplicationInfo(pluginData.packageName, PackageManager.GET_META_DATA);
//                PluginInfo pluginInfo = new PluginInfo();
//                pluginInfo.setApplicationInfo(info);
//                pluginInfo.setDescribe(pluginData.describe);
//                pluginInfo.setEntrance(pluginData.entrance);
//                pluginInfo.setIconDraw(info.loadIcon(packageManager));
//                pluginInfo.setMinVersion(pluginData.minVersion);
//                pluginInfo.setName(pluginData.name);
//                pluginInfo.setVersion(pluginData.version);
//                infoList.add(pluginInfo);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return this;
//    }


//    public PluginManager loadPlugin(IntLittleBall intLittleBall) {
//        PluginManager manager = new PluginManager();
//        for (PluginInfo info : infoList) {
//            try {
//                if (info.getEntrance().isEmpty()) continue;
//                String dexPath = info.getApplicationInfo().sourceDir;
//                String libPath = info.getApplicationInfo().nativeLibraryDir;
//                String dexOutputPath = context.getCodeCacheDir().getAbsolutePath();
//                DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOutputPath, libPath
//                        , this.getClass().getClassLoader());
//                Plugin plugin = (Plugin)
//                        dexClassLoader.loadClass(info.getEntrance()).newInstance();
//                Context context = this.context.createPackageContext(info.getPackageName(),
//                        Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
//                plugin.setResources(context.getResources());
//                plugin.setPluginInfo(info);
//                manager.pluginMap.put(plugin.getPluginInfo().getName(), plugin);
//            } catch (ClassNotFoundException | IllegalAccessException
//                    | InstantiationException | PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        manager.onLoad(intLittleBall);
//        return manager;
//    }


//    public PluginLoader checkPlugin() {
//        List<ApplicationInfo> appList
//                = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//        DataMonitor.getInit().pluginDataS.dataList.clear();
//        for (ApplicationInfo info : appList) {
//            if (info.metaData.getBoolean("LittleBallModule", false)) {
//                PluginData pluginData = new PluginData();
//                try {
//                    pluginData.name(info.name)
//                            .describe(info.metaData.getString("LittleBallDescribe"
//                                    , "这个作者很懒，没有提供描述"))
//                            .entrance(info.metaData.getString("LittleBallEntrance"
//                                    , ""))
//                            .minVersion(info.metaData.getFloat("LittleBallMinVersion"
//                                    , 1.0f))
//                            .version(packageManager.getPackageInfo(info.packageName
//                                    , 0).versionName);
//
//                    DataMonitor.getInit().pluginDataS.dataList.add(pluginData);
//
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
//
//        DataMonitor.getInit().apply();
//
//        return this;
//
//    }

}