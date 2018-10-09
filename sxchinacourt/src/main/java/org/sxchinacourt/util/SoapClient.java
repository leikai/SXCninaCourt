package org.sxchinacourt.util;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;


/**
 * Email：312607360@qq.com
 * Created by 温瑞壮 on 2017/9/14.
 * Description：
 */
public class SoapClient {
    /**
     * 上下文.
     */
    private static Context mContext;

    /**
     * 线程执行器.
     */
    public static Executor mExecutorService = null;

    /**
     * WebService dotNet属性.
     */
    private boolean mDotNet = false;

    /**
     * soap参数.
     */
    private SoapParams mParams = null;

    /**
     * 超时时间.
     */
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;

    /**
     * 超时时间.
     */
    private int mTimeout = DEFAULT_SOCKET_TIMEOUT;
    /**
     * 命名空间.
     */
    private String mNameSpace;

    /**
     * 初始化.
     *
     * @param context the context
     */
    public SoapClient(Context context, String nameSpace)
    {
        mContext = context;
        mNameSpace = nameSpace;
        mExecutorService = ThreadFactory.getExecutorService();
    }

    /**
     * Call.
     *
     * @param url               the url
     * @param methodName        the method name
     * @param Params            the params
     * @param iSoapUtilCallback the listener
     */
    public void call(final String url, final String methodName, SoapParams Params, final String level, final ISoapUtilCallback iSoapUtilCallback)
    {
        this.mParams = Params;

        if (!NetUtils.isNetworkAvailable(mContext))
        {
            Toast.makeText(mContext, "无网络，请稍后重试!", Toast.LENGTH_SHORT).show();
            iSoapUtilCallback.onFailure(new NetworkErrorException());
            return;
        }


        mExecutorService.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    doCall(url, methodName, mParams, level,iSoapUtilCallback);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private Thread thread;

    /**
     *
     * @param url
     * @param methodName
     * @param Params
     * @param iSoapUtilCallback
     */
    public void callSynchro(final String url, final String methodName, SoapParams Params, final String level, final ISoapUtilCallback iSoapUtilCallback)
    {
        this.mParams = Params;

        if (!NetUtils.isNetworkAvailable(mContext))
        {
            Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
            iSoapUtilCallback.onFailure(new NetworkErrorException());
            return;
        }
        thread =new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    doCall(url, methodName, mParams, level, iSoapUtilCallback);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try
        {
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }






    /**
     * Do call.
     *
     * @param url               the url
     * @param methodName        the method name
     * @param params            the params
     * @param iSoapUtilCallback the listener
     */
    public void doCall(String url, String methodName, SoapParams params, String level, ISoapUtilCallback iSoapUtilCallback)
    {
        try
        {

            // 指定WebService的命名空间和调用的方法名
            SoapObject request = new SoapObject(mNameSpace, methodName);
            // 传递参数
            LinkedHashMap<String,Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String,Object>> iter=paramsList.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry<String, Object> entry =  iter.next();
                Log.e("ksoap","请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }

            // 生成调用WebService方法的SOAP请求信息，并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);

            envelope.dotNet = mDotNet;
            HttpTransportSE httpTransportSE = new HttpTransportSE(url, mTimeout);
//            httpTransportSE.debug = true;
//            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
//            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(mNameSpace+level+"/"+methodName,envelope);
//            httpTransportSE.call(null, envelope, headerPropertyArrayList);
//            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("ksoap","返回结果--->" + envelope.getResponse().toString());

        }
        catch (Exception e)
        {
            iSoapUtilCallback.onFailure(e);
            Log.e("ksoap", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
    }

    /**
     * 描述：设置连接超时时间.
     *
     * @param timeout 毫秒
     */
    public void setTimeout(int timeout)
    {
        this.mTimeout = timeout;
    }

    /**
     * Checks if is dot net.
     *
     * @return true, if is dot net
     */
    public boolean isDotNet()
    {
        return mDotNet;
    }

    /**
     * Sets the dot net.
     *
     * @param dotNet the new dot net
     */
    public void setDotNet(boolean dotNet)
    {
        this.mDotNet = dotNet;
    }

    public interface ISoapUtilCallback
    {
        void onSuccess(SoapSerializationEnvelope envelope) throws Exception;

        void onFailure(Exception e);
    }

}
