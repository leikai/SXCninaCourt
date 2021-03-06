package org.sxchinacourt.util;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Email：312607360@qq.com
 * Created by 温瑞壮 on 2017/9/14.
 * Description：请求参数
 */
public class SoapParams {

    /** 参数. */
    protected LinkedHashMap<String, Object> params;

    /**
     * Instantiates a new ab soap params.
     */
    public SoapParams() {
        init();
    }

    /**
     * Instantiates a new ab soap params.
     *
     * @param obj
     *            the obj
     */
    public SoapParams(Object obj) {
        try {
            init();
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                field.setAccessible(true);
                Object fieldValue =  field.get(obj);
                params.put(fieldName, fieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用一个map构造请求参数.
     *
     * @param source
     *            the source
     */
    public SoapParams(Map<String, Object> source) {
        init();

        for (Map.Entry<String, Object> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 初始化.
     */
    private void init() {
        params = new LinkedHashMap<String, Object>();
    }

    /**
     * 增加一对请求参数.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public SoapParams put(String key, Object value) {
        if (key != null && value != null) {
            params.put(key, value);
        }
        return this;
    }

    /**
     * 删除一个请求参数.
     *
     * @param key
     *            the key
     */
    public void remove(String key) {
        params.remove(key);
    }

    /**
     * 获取参数列表.
     *
     * @return the params list
     */
    public LinkedHashMap<String,Object> getParamsList() {
        LinkedHashMap<String,Object> paramsList = new LinkedHashMap<String,Object>();
        return params;
    }

    @Override
    public String toString() {
        return "SoapParams{" +
                "params=" + params +
                '}';
    }
}
