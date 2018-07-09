package cn.heyanle.littleball.pluginer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.littleball.plugin.IntLittleBall;
import cn.heyanle.littleball.plugin.LittleBallPlugin;
import cn.heyanle.littleball.plugin.PluginInfo;
import cn.heyanle.littleball.pluginer.data.DataMonitor;
import cn.heyanle.littleball.pluginer.data.PluginData;
import dalvik.system.DexClassLoader;

public class PluginLoader {

    Context context ;
    PackageManager packageManager ;

    List<PluginInfo> infoList = new ArrayList<>();

    public PluginLoader(Context context){
        this.context = context;
        packageManager = context.getPackageManager();
        DataMonitor.init(context);
    }


    public PluginLoader loadInfo(){

        infoList.clear();

        for(PluginData pluginData : DataMonitor.getInit().pluginDataS.dataList){

            try{

                ApplicationInfo info
                        = packageManager
                        .getApplicationInfo(pluginData.packageName,PackageManager.GET_META_DATA);


                PluginInfo pluginInfo = new PluginInfo()
                        .applicationInfo(info)
                        .describe(pluginData.describe)
                        .entrance(pluginData.entrance)
                        .iconDraw(info.loadIcon(packageManager))
                        .minVersion(pluginData.minVersion)
                        .name(pluginData.name)
                        .version(pluginData.version);

                infoList.add(pluginInfo);

            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }



        }

        return this;

    }


    public PluginManager loadPlugin(IntLittleBall intLittleBall){

        PluginManager manager = new PluginManager();

        for (PluginInfo info : infoList){

            try {
                if (info.entrance.isEmpty()) continue;

                String dexPath = info.applicationInfo.sourceDir;
                String libPath = info.applicationInfo.nativeLibraryDir;
                String dexOutputPath = context.getCodeCacheDir().getAbsolutePath();

                DexClassLoader dexClassLoader = new DexClassLoader(dexPath,dexOutputPath,libPath
                        ,this.getClass().getClassLoader());

                LittleBallPlugin plugin =(LittleBallPlugin)
                        dexClassLoader.loadClass(info.entrance).newInstance();

                Context context = this.context.createPackageContext(info.packageName,
                        Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);
                plugin.setResources(context.getResources());
                plugin.setPluginInfo(info);

                manager.pluginList.add(plugin);

            }catch (ClassNotFoundException | IllegalAccessException
                    | InstantiationException | PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }



        }

        manager.onLoad(intLittleBall);

        return manager;

    }


    public PluginLoader checkPlugin(){

        List<ApplicationInfo> appList
                =  packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        DataMonitor.getInit().pluginDataS.dataList.clear();

        for(ApplicationInfo info : appList){

            if(info.metaData.getBoolean("LittleBallModule",false)){

                PluginData pluginData = new PluginData();

                try {

                    pluginData.name(info.name)
                            .describe(info.metaData.getString("LittleBallDescribe"
                                    , "这个作者很懒，没有提供描述"))
                            .entrance(info.metaData.getString("LittleBallEntrance"
                                    , ""))
                            .minVersion(info.metaData.getFloat("LittleBallMinVersion"
                                    , 1.0f))
                            .version(packageManager.getPackageInfo(info.packageName
                                    , 0).versionName);

                    DataMonitor.getInit().pluginDataS.dataList.add(pluginData);

                }catch(PackageManager.NameNotFoundException e){
                    e.printStackTrace();
                }

            }

        }

        DataMonitor.getInit().apply();

        return this;

    }



}
