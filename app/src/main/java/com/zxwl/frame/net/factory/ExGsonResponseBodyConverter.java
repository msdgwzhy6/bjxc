package com.zxwl.frame.net.factory;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.zxwl.frame.bean.DataList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * 作者：gaoyin
 * 电话：18810474975
 * 邮箱：18810474975@163.com
 * 版本号：1.0
 * 类描述：
 * 备注消息：
 * 修改时间：2016/12/12 上午10:26
 **/
public class ExGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    ExGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 进行解析预处理操作
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String value = responseBody.string();
        //[text={"message":"查询失败!","result":"error"}]
        DataList httpResponse = new DataList();
        try {
            JSONObject response = new JSONObject(value);
            if(response.has("result")){
                String error = response.getString("result");
                //不等于0代表异常
                if (!TextUtils.isEmpty(error)) {
                    httpResponse.result ="error";
                    return (T) gson.fromJson(value, httpResponse.getClass());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gson.fromJson(value, type);
    }
}
