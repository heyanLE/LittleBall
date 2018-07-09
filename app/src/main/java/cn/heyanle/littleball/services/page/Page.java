package cn.heyanle.littleball.services.page;

import android.widget.FrameLayout;

import cn.heyanle.littleball.plugin.LittleBallPlugin;

public class Page {

    LittleBallPlugin plugin;

    FrameLayout rootView ;

    boolean isFirstVisibility = false;

    public Page(LittleBallPlugin plugin){
        this.plugin = plugin;
        rootView = plugin.getRootView();
    }

    public FrameLayout getRootView(){
        return rootView;
    }

    public void onVisibility(){

        if (isFirstVisibility){
            isFirstVisibility = false;
            plugin.onVisibility(true);
            rootView.addView(plugin.getView());
        }else {
            plugin.onVisibility(false);
        }

    }

    public void onFirstVisibility() {
        plugin.onVisibility(true);
        rootView.addView(plugin.getView());
    }
    public void onInVisibility() {
        plugin.onInVisibility();
    }

}
