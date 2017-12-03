package com.woodpecker.util;

import org.json.JSONObject;

/**
 * status: 1 操作成功
 * status: 0 操作失败(无需退出)
 * status: -1 内部异常(强制退出)
 */
public class JSONResult {
    public static String fillResultString(Integer status, String message, Object result){
        JSONObject jsonObject = new JSONObject(){{
            put("status", status);
            put("message", message);
            put("result", result);
        }};
        return jsonObject.toString();
    }
}
