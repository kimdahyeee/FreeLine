package com.perples.recosample.Http;

import java.net.URLEncoder;

/**
 * Created by gwangyonglee on 16. 9. 19..
 */
public class encode_string {
    private static final String TAG = "FreeLine_log";


    StringBuffer buffer = new StringBuffer();

    public encode_string(String... params){
        for(int i=0; i<params.length; i++){
            try{
                //보낼 값 인코딩
                //URLEncoder URL에 있는 문자를 코드화 하는 것
                String msg = URLEncoder.encode("params"+i, "UTF-8") + "=" + URLEncoder.encode(params[i], "UTF-8");
                buffer.append(msg).append("&");
            }catch (Exception e){
                //Log.d(TAG,"encode_string error!");
            }
        }
    }

    public String getParams(){
        return buffer.toString();
    }
}
