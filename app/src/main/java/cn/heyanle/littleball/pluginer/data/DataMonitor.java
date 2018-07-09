package cn.heyanle.littleball.pluginer.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static android.content.Context.MODE_PRIVATE;

public class DataMonitor {


    private SharedPreferences preferences ;
    private SharedPreferences.Editor editor;


    private static DataMonitor init;
    private Context context;

    public PluginDataS pluginDataS = new PluginDataS();

    private DataMonitor(){}

    public static DataMonitor init(Context context){



        init = new DataMonitor();

        init.context = context;

        init.preferences = context.getSharedPreferences("plugin_data",MODE_PRIVATE);
        init.editor = init.preferences.edit();

        return init;

    }

    public static DataMonitor getInit(){

        return init;

    }


    public void load(){

        String dataJson = preferences.getString("plugin_data","");

        if (!dataJson.isEmpty()){

            try {
                pluginDataS = new ObjectMapper().readValue(dataJson,PluginDataS.class);
                return;
            }catch (IOException e){
                e.printStackTrace();
            }

        }

        pluginDataS = new PluginDataS();

    }

    public void apply(){

        String dataJson = "";

        try{
            dataJson = new ObjectMapper().writeValueAsString(pluginDataS);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }

        editor.putString("plugin_data",dataJson);

        editor.apply();

    }


    public class PluginDataS{

        public List<PluginData> dataList = new ArrayList<>();

    }


}
