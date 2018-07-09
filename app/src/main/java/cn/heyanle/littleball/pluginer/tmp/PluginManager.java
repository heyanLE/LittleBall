package cn.heyanle.littleball.pluginer.tmp;

import java.util.HashMap;
import java.util.Map;

import cn.heyanle.littleball.plugin.IntLittleBall;
import cn.heyanle.littleball.plugin.tmp.Plugin;

public class PluginManager {

    Map<String, Plugin> pluginMap = new HashMap<>();

    /**
     * 在Service OnDestroy时候调用
     */
    public void onPluginDestroy() {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onPluginDestroy();
        }
    }

    /**
     * 在OnCreate后加载插件时候调用（可以在这里设置WindowsView对象）
     *
     * @param intLittleBall 主apk接口
     */
    public void onLoad(IntLittleBall intLittleBall) {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onLoad(intLittleBall);
        }
    }

    /**
     * 当窗口关闭时候调用
     */
    public void onWindowClose() {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onWindowClose();
        }
    }

    /**
     * 当窗口显示时候调用
     */
    public void onWindowShow() {
        for (Plugin plugin : pluginMap.values()) {
            plugin.onWindowShow();
        }
    }

    /**
     * 当窗口的该插件页面显示的时候（如果窗口显示后默认就是该页面 也会执行）
     */
    public void onVisibility(Plugin plugin, boolean isFirst) {
        plugin.onVisibility(isFirst);
    }

    /**
     * 当窗口的该插件页面不显示的时候（滑到别的页面 如果窗口关闭时候就是该页面 也会执行）
     */
    public void onInVisibility(Plugin plugin) {
        plugin.onInVisibility();
    }

}