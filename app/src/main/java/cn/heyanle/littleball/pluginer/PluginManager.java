package cn.heyanle.littleball.pluginer;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.heyanle.littleball.plugin.IntLittleBall;
import cn.heyanle.littleball.plugin.LittleBallPlugin;

public class PluginManager {

    List<LittleBallPlugin> pluginList = new ArrayList<>();

    //插件生命周期

    /**
     * 在Service OnDestroy时候调用
     */
    public void onPluginDestroy(){
        for (LittleBallPlugin plugin:pluginList){
            plugin.onPluginDestroy();
        }
    }

    /**
     * 在OnCreate后加载插件时候调用（可以在这里设置WindowsView对象）
     * @param intLittleBall 主apk接口
     */
    public void onLoad(IntLittleBall intLittleBall){
        for (LittleBallPlugin plugin:pluginList){
            plugin.onLoad(intLittleBall);
        }
    }

    /**
     * 当窗口关闭时候调用
     */
    public void onWindowClose(){
        for (LittleBallPlugin plugin:pluginList){
            plugin.onWindowClose();
        }
    }

    /**
     * 当窗口显示时候调用
     */
    public void onWindowShow(){
        for (LittleBallPlugin plugin:pluginList){
            plugin.onWindowShow();
        }
    }

    /**
     * 当窗口的该插件页面显示的时候（如果窗口显示后默认就是该页面 也会执行）
     */
    public void onVisibility(LittleBallPlugin plugin,boolean isFirst){
        plugin.onVisibility(isFirst);
    }

    /**
     * 当窗口的该插件页面不显示的时候（滑到别的页面 如果窗口关闭时候就是该页面 也会执行）
     */
    public void onInVisibility(LittleBallPlugin plugin){
        plugin.onInVisibility();
    }


}
