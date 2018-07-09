package cn.heyanle.littleball.pluginer.data;

public class PluginData{

    public String packageName = "";//包名

    public String name = "";//名字
    public String version = "";//版本
    public String describe = "";//描述
    public String entrance = "";//入口
    public float minVersion = 1.1f;//最低支持版本



    public PluginData name(String name){
        this.name = name;
        return this;
    }

    public PluginData version(String version){
        this.version = version;
        return this;
    }

    public PluginData describe(String describe){
        this.describe = describe;
        return this;
    }

    public PluginData entrance(String entrance){
        this.entrance = entrance;
        return this;
    }

    public PluginData minVersion(float minVersion){
        this.minVersion = minVersion;
        return this;
    }

}