package org.sxchinacourt.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.sxchinacourt.bean.DepartmentBean;
import org.sxchinacourt.bean.DepartmentNewBean;
import org.sxchinacourt.bean.MenuBean;
import org.sxchinacourt.bean.NewsContentPattsRoot;
import org.sxchinacourt.bean.OBDataBean;
import org.sxchinacourt.bean.ProcessOpinion;
import org.sxchinacourt.bean.RoleBean;
import org.sxchinacourt.bean.TaskAssignedToMeBean;
import org.sxchinacourt.bean.TaskBean;
import org.sxchinacourt.bean.TaskICreatedBean;
import org.sxchinacourt.bean.UserBean;
import org.sxchinacourt.bean.UserInfoBean;
import org.sxchinacourt.bean.UserNewBean;
import org.sxchinacourt.bean.ViewComponents;
import org.sxchinacourt.cache.CSharedPreferences;
import org.sxchinacourt.dataparser.ComponentParser;
import org.sxchinacourt.dataparser.DataParser;
import org.sxchinacourt.dataparser.UserParser;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baggio on 2017/2/4.
 */

public class WebServiceUtil {
    public static String RESULT_PARAM_TASKSCOUNT = "tasksCount";
    public static String RESULT_PARAM_DATASCOUNT = "datasCount";
    public static String RESULT_PARAM_TASKS = "tasks";
    public static String RESULT_PARAM_DATAS = "datas";

    //---------------------------------------------Formal-------------------------------//
    public static String BASE_SERVER_URL = "http://111.53.181.200:6688/mserver/services/awsSvc?wsdl"; // http
    public static String BASE_SERVER_URL_NEWS = "http://124.167.17.5:6688/mserver/services/awsSvc?wsdl"; // http 192.168.1.14:6688
    public static String BASE_DOWNLOAD_URL = "http://111.53.181.200:6688/mserver";
    public static String BASE_SERVER_URL_NEW = "http://192.168.1.145:6688/mserver/services/awsSvc?wsdl";
    public static String BASE_CABINET_URLDEPOSITINFO = "http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";
    public static String BASE_CABINET_URLPICKUP="http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";
    public static String BASE_CABINET_URL ="http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";//云柜
    public static String BASE_CABINET_URLCOURT="http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";
    public static String BASE_CABINET_URLEMPASSIGN="http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";
    public static String BASE_CABINET_URLEMPLOYEE="http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";
    public static String BASE_MESSAGE_URL ="http://111.53.181.200:8087/Cloud/CloudAppPort?wsdl";//留言机

    //----------------------------------------------newOA-------------------------------------//
    public static String BASE_SERVER_URL_TOKEN = "http://172.16.3.48:6641/mserver/services/bwsSvc?wsdl"; // http
    //---------------------------------------------finish------------------------------------//

//    //----------------------------------------------newOA——测试-------------------------------------//
//    public static String BASE_SERVER_URL_TOKEN = "http://192.168.1.114:40/mserver/services/bwsSvc?wsdl"; // http
//    //---------------------------------------------finish------------------------------------//





    private static final boolean IS_HTTP = true;
    // port 6688
    private static WebServiceUtil mInstance;

    private static final String NAMESPACE_TOKEN = "http://bws.courtoa.zt.com";
    private final String NAMESPACE = "http://maws.courtoa.zt.com";
    private final String NAMESPACECABINET = "http://action.cloud/";
    private final String NAMESPACECABINETORIGINAL = "http://tempuri.org/";
    //    public static final String BASE_SERVER_URL = BASE_URL + "/services/";
//    public static final String AWSSVC = "awsSvc?wsdl";
//    private final String AWSORGSVC = "awsOrgSvc";

    //---------------------Formal----------------------//
    private String mHost = "111.53.181.200";//223.12.199.236
    //----------------------finish--------------------//


//    //-----------------ceshi---------------------//
//    private String mHost = "192.168.1.145";//223.12.199.236
//    //------------------finish----------------//


    private String mCompany = "132.24.3.40";   //
    private int mPort = 7443;
    private final String WS_OPS = "/mserver/services/awsSvc?wsdl";
    private final int TIMEOUT = 20000;
    private final boolean TEST = false;





    private WebServiceUtil() {

    }

    public synchronized static WebServiceUtil getInstance() {
        if (mInstance == null) {
            mInstance = new WebServiceUtil();
            mInstance.initServerInfo();
        }
        return mInstance;
    }
    //网络请求的地址
    public void initServerInfo() {
        try {
            JSONObject json = CSharedPreferences.getInstance().getServerInfo();
            if (json != null) {
                if (json.has("port")) {
                    mPort = json.getInt("port");
                }
                if (json.has("host")) {
                    mHost = json.getString("host");
                    if (IS_HTTP) {
                        BASE_SERVER_URL = "http://" + mHost + ":"+mPort+"/mserver/services/awsSvc?wsdl";
                        BASE_DOWNLOAD_URL = "http://" + mHost + ":"+mPort+"/mserver";//6688
                    } else {
                        BASE_DOWNLOAD_URL = "https://" + mHost + ":"+mPort+"/mserver";//7445
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录获取
     *
     * @param user
     * @return
     */
    public  String createSession(UserNewBean user) {
        String methodName = "createSession";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userPwd", user.getUserPassword());
            json.put("userLoginName", user.getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 手势登录获取
     *
     * @param
     * @return
     */
    public  String createSession(String userLoginName, String userHand) {
        String methodName = "createSession";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userLoginName", userLoginName);
            json.put("userHand", userHand);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean refreshSession(String sessionId) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "refreshSession";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("sessionId", sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject detail = (SoapObject) result.getProperty("return");
                System.out.println("result:[" + detail.getPropertyCount() + "]");
                if (detail.getPropertyCount() > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public UserBean getUserInfo(UserBean user) {
        String methodName = "getUserInfo";
        UserBean newUser = null;
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();//18129925301   分管院领导签批  院领导手写签批
        try {
            json.put("userId", user.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    newUser = (UserBean) DataParser.factory(DataParser.PARSER_TYPE_USER).parser
                            (new
                                    JSONObject(resultStr));
                    user.copyUserInfo(newUser);
                    return newUser;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }




    /**
     * 获取个人详情页
     *  @param username
     * @param userOid
     * @return
     */
    public UserNewBean getUserInfo(String username, String userOid) {
        UserNewBean user = null;
        String methodName = "getUserInfo";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (username != null){
                json.put("userName", username);
            }
            if (userOid != null){
                json.put("userOid", userOid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                Log.e("resultStr",""+resultStr);
                user = JSON.parseObject(resultStr,UserNewBean.class);
                user.copyUserInfo(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }




    public boolean getUserDepartmentInfo(UserBean user) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getUserDepartmentInfo";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", user.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    DepartmentBean departmentBean = (DepartmentBean) DataParser.factory(DataParser
                            .PARSER_TYPE_DEPARTMENT).parser(new JSONObject(resultStr));
                    user.setDepartment(departmentBean);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean closeSession(UserBean user) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "closeSession";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("sessionId", user.getSessionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTaskCount(String userId, int taskType, String workflowGroupName, String
            workflowDefUUID) {
        int taskCount = -1;
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getTaskCount";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("taskType", String.valueOf(taskType));
            json.put("workflowGroupName", workflowGroupName);
            json.put("workflowDefUUID", workflowDefUUID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject detail = (SoapObject) result.getProperty("return");
                System.out.println("result:[" + detail.getPropertyCount() + "]");
                if (detail.getPropertyCount() > 0) {
                    taskCount = 10;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskCount;
    }


    /**
     * 获取待办数量
     *
     * @param token
     * @return
     */
    public String getTaskCount(String token) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getTaskCount";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Hashtable<String, Object> getTaskList(String userId, int taskType, String
            workflowGroupName, String
                                                         workflowDefUUID, int start, int limit) {
        Hashtable<String, Object> hash = new Hashtable<>();
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getTaskList";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("taskType", taskType);
            if (!TextUtils.isEmpty(workflowGroupName)) {
                json.put("workflowGroupName", workflowGroupName);
            } else {
                json.put("workflowGroupName", "");
            }
            if (!TextUtils.isEmpty(workflowDefUUID)) {
                json.put("workflowDefUUID", workflowDefUUID);
            } else {
                json.put("workflowDefUUID", "");
            }
            json.put("start", start);
            json.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            String resultStr = null;
            if (TEST) {
                resultStr = TestData.DATA_TODO_TASK;
            } else {
                ht.call(NAMESPACE + methodName, envelope);
                if (envelope.getResponse() != null) {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    resultStr = result.getPropertyAsString("return");
                }
            }
            if (resultIsNotEmpty(resultStr)) {
                JSONObject jsonObject = new JSONObject(resultStr);
                if (jsonObject.has(RESULT_PARAM_TASKSCOUNT)) {
                    int tasksCount = jsonObject.getInt(RESULT_PARAM_TASKSCOUNT);
                    hash.put(RESULT_PARAM_TASKSCOUNT, new Integer(tasksCount));
                }
                if (jsonObject.has(RESULT_PARAM_TASKS)) {
                    List<TaskBean> tasks = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray(RESULT_PARAM_TASKS);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject childJson = jsonArray.getJSONObject(i);
                        TaskBean task = (TaskBean) DataParser.factory(DataParser
                                .PARSER_TYPE_TASK).parser(childJson);
                        tasks.add(task);
                    }
                    hash.put(RESULT_PARAM_TASKS, tasks);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }


    public Hashtable<String, Object> getHistoryTaskList(String userId, int taskType, String
            workflowGroupName, String
                                                                workflowDefUUID, int start, int limit) {
        Hashtable<String, Object> hash = new Hashtable<>();
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getHistoryTaskList";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("taskType", taskType);
            if (!TextUtils.isEmpty(workflowGroupName)) {
                json.put("workflowGroupName", workflowGroupName);
            } else {
                json.put("workflowGroupName", "");
            }
            if (!TextUtils.isEmpty(workflowDefUUID)) {
                json.put("workflowDefUUID", workflowDefUUID);
            } else {
                json.put("workflowDefUUID", "");
            }
            json.put("start", start);
            json.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            String resultStr = null;
            if (TEST) {
                resultStr = TestData.DATA_TODO_TASK;
            } else {
                ht.call(NAMESPACE + methodName, envelope);
                if (envelope.getResponse() != null) {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    resultStr = result.getPropertyAsString("return");
                }
            }
            if (resultIsNotEmpty(resultStr)) {
                JSONObject jsonObject = new JSONObject(resultStr);
                if (jsonObject.has(RESULT_PARAM_TASKSCOUNT)) {
                    int tasksCount = jsonObject.getInt(RESULT_PARAM_TASKSCOUNT);
                    hash.put(RESULT_PARAM_TASKSCOUNT, new Integer(tasksCount));
                }
                if (jsonObject.has(RESULT_PARAM_TASKS)) {
                    List<TaskBean> tasks = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray(RESULT_PARAM_TASKS);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject childJson = jsonArray.getJSONObject(i);
                        TaskBean task = (TaskBean) DataParser.factory(DataParser
                                .PARSER_TYPE_TASK).parser(childJson);
                        tasks.add(task);
                    }
                    hash.put(RESULT_PARAM_TASKS, tasks);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    //得到炎黄通讯录的数据 2017-8-23
    public List<UserBean> getUserList(String userName, String departmentId) {
        List<UserBean> users = null;
        Hashtable<String, Object> hash = new Hashtable<>();
        String methodName = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(userName)) {
                json.put("userName", userName);
            }
            if (!TextUtils.isEmpty(departmentId)) {
                json.put("deptId", departmentId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONArray array = new JSONArray(resultStr);
                    users = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObj = array.getJSONObject(i);
                        UserBean user = (UserBean) DataParser.factory(DataParser.PARSER_TYPE_USER)
                                .parser(jsonObj);
                        users.add(user);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 新OA通讯录人员信息
     * @param token
     * @param qdeptid
     * @return
     */
    public List<UserNewBean> getNewUserList(String token, String qdeptid) {
        List<UserBean> users = null;
        Hashtable<String, Object> hash = new Hashtable<>();
        String methodName = "getUserList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(token)) {
                json.put("token", token);
            }
            if (!TextUtils.isEmpty(qdeptid)) {
                json.put("qdeptid", qdeptid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN,3600*1000);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {

                    List<UserNewBean> resps=new ArrayList<UserNewBean>(com.alibaba.fastjson.JSONArray.parseArray(resultStr,UserNewBean.class));
                    Log.e("人员数据",""+resps.get(0));

                    return resps;
//                    JSONArray array = new JSONArray(resultStr);
//                    users = new ArrayList<>();
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject jsonObj = array.getJSONObject(i);
//                        UserBean user = (UserBean) DataParser.factory(DataParser.PARSER_TYPE_USER)
//                                .parser(jsonObj);
//                        users.add(user);
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean changeUserPWD(String userId, String oldPassword, String newPassword) {
        String methodName = "changeUserPWD";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("oldPassword", oldPassword);
            json.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr.equals("true");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 修改手势密码
     *
     * @param
     * @return
     */
    public  String changeUserPWD(String userLoginName, String userHand) {
        String methodName = "changeUserPWD";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userLoginName", userLoginName);
            json.put("userHand", userHand);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




/*
    public UserBean getUserInfo(String userId) {
        UserBean user = null;
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getUserInfo";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    user = (UserBean) UserParser.factory(DataParser.PARSER_TYPE_USER).parser(new
                            JSONObject(resultStr));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    */


//    /**
//     * 获取个人详情页
//     *  @param username
//     * @param userOid
//     * @return
//     */
//    public UserNewBean getUserInfo(String username, String userOid) {
//        UserNewBean user = null;
//        String methodName = "getUserInfo";
//        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
//        JSONObject json = new JSONObject();
//        try {
//            if (username != null){
//                json.put("userName", username);
//            }
//            if (userOid != null){
//                json.put("userOid", userOid);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        request.addProperty("jsonstr", json.toString());
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.bodyOut = request;
//        envelope.dotNet = true;
//        HttpTransportSE ht;
//        if (IS_HTTP) {
//            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
//        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
//        }
//        try {
//            ht.call(NAMESPACE_TOKEN + methodName, envelope);
//            if (envelope.getResponse() != null) {
//                SoapObject result = (SoapObject) envelope.bodyIn;
//                String resultStr = result.getPropertyAsString("return");
//                Log.e("resultStr",""+resultStr);
//                user = JSON.parseObject(resultStr,UserNewBean.class);
//                user.copyUserInfo(user);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return user;
//    }


    public DepartmentBean getUserDepartmentInfo(String userId) {
        DepartmentBean department = null;
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getUserDepartmentInfo";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject detail = (SoapObject) result.getProperty("return");
                System.out.println("result:[" + detail.getPropertyCount() + "]");
                if (detail.getPropertyCount() > 0) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return department;
    }

    public boolean getUserRoleInfo(UserBean user) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getUserRoleInfo";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", user.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    RoleBean role = (RoleBean) DataParser.factory(DataParser.PARSER_TYPE_ROLE)
                            .parser(new JSONObject(resultStr));
                    user.setRole(role);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //请求联系人分组
    public List<DepartmentBean> getDepartmentList(String departmentNo) {
        List<DepartmentBean> departments = null;
        String methodName = "getDepartmentList";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(departmentNo)) {
                json.put("departmentNo", departmentNo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    departments = new ArrayList<>();
                    JSONArray array = new JSONArray(resultStr);
                    for (int i = 0; i < array.length(); i++) {
                        DepartmentBean department = (DepartmentBean) DataParser.factory(DataParser
                                .PARSER_TYPE_DEPARTMENT).parser(array.getJSONObject(i));
                        departments.add(department);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departments;
    }

    /**
     * 修改后的通讯录
     * @param token
     * @param qorgid
     * @return
     */
    public List<DepartmentNewBean> getDepartmentList(String token, String qorgid) {
        List<DepartmentNewBean> departments = null;
        String methodName = "getDepartmentList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(qorgid)) {
                json.put("token", token);
                json.put("qorgid", qorgid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {


                    List<DepartmentNewBean> resps=new ArrayList<DepartmentNewBean>(com.alibaba.fastjson.JSONArray.parseArray(resultStr,DepartmentNewBean.class));
                    Log.e("部门数据",""+resps.get(0));

                    return resps;
//                    departments = new ArrayList<>();
//                    JSONArray array = new JSONArray(resultStr);
//                    for (int i = 0; i < array.length(); i++) {
//                        DepartmentBean department = (DepartmentBean) DataParser.factory(DataParser
//                                .PARSER_TYPE_DEPARTMENT).parser(array.getJSONObject(i));
//                        departments.add(department);
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departments;
    }


    public Hashtable<String, Object> getHistoryTaskCount(String userId, int taskType, String
            workflowGroupName, String workflowDefUUID, int start, int limit) {
        Hashtable<String, Object> hash = new Hashtable<>();
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getHistoryTaskCount";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("taskType", taskType);
            if (!TextUtils.isEmpty(workflowGroupName)) {
                json.put("workflowGroupName", workflowGroupName);
            } else {
                json.put("workflowGroupName", "");
            }
            if (!TextUtils.isEmpty(workflowDefUUID)) {
                json.put("workflowDefUUID", workflowDefUUID);
            } else {
                json.put("workflowDefUUID", "");
            }
            json.put("start", start);
            json.put("limit", limit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            String resultStr = null;
            if (TEST) {
                resultStr = TestData.DATA_TODO_TASK;
            } else {
                ht.call(NAMESPACE + methodName, envelope);
                if (envelope.getResponse() != null) {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    resultStr = result.getPropertyAsString("return");
                }
            }
            if (resultIsNotEmpty(resultStr)) {
                JSONObject jsonObject = new JSONObject(resultStr);
                if (jsonObject.has(RESULT_PARAM_TASKSCOUNT)) {
                    int tasksCount = jsonObject.getInt(RESULT_PARAM_TASKSCOUNT);
                    hash.put(RESULT_PARAM_TASKSCOUNT, new Integer(tasksCount));
                }
                if (jsonObject.has(RESULT_PARAM_TASKS)) {
                    List<TaskBean> tasks = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray(RESULT_PARAM_TASKS);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject childJson = jsonArray.getJSONObject(i);
                        TaskBean task = (TaskBean) DataParser.factory(DataParser
                                .PARSER_TYPE_TASK).parser(childJson);
                        tasks.add(task);
                    }
                    hash.put(RESULT_PARAM_TASKS, tasks);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    //banner请求
    public String getWebImgs(String nums){
        String dataWebImgs = null;
        String methodName = "getWebImgs";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        /* JSONObject json = new JSONObject();*/
      /*  try{
            json.put("nums", nums);
        }catch (JSONException e){
            e.printStackTrace();
        }*/
        request.addProperty("jsonstr", nums);//json.toString()
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP){
            ht = new HttpTransportSE(BASE_SERVER_URL);
            Log.e("我是ht1","hhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        }else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
            Log.e("我是ht2","tttttttttttttttttttttttttttttttt");
        }try{
            String resultStr = null;
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                resultStr = result.getPropertyAsString("return");
            }
            if (resultIsNotEmpty(resultStr)) {
                dataWebImgs = resultStr;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataWebImgs;
    }
    public String getDataBasePhote(int bindId, int metaId, String imageInfo){
        String dataBasePhoto = null;
        String methodName = "uploadSignImg";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("bindid", bindId);
            json.put("metaid", metaId);
            json.put("imginfo" ,imageInfo);
        }catch (JSONException e){
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }try {
            String resultStr = null;
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                resultStr = result.getPropertyAsString("return");
            }
            if (resultIsNotEmpty(resultStr)) {
                dataBasePhoto = resultStr;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return dataBasePhoto;
    }

    public int getNextStepNo(String userId, int bindId, int taskId, String menuName, String
            opinion, int menuType) {
        int nextStepNo = -1;
        String methodName = "getNextStepNo";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("taskId", taskId);
            json.put("bindid", bindId);
            if (menuName == null) {
                menuName = "";
            }
            if (opinion == null) {
                opinion = "";
            }
            json.put("menuName", menuName);
            json.put("opinion", opinion);
            json.put("menuType", menuType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            String resultStr = null;
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                resultStr = result.getPropertyAsString("return");
            }
            if (resultIsNotEmpty(resultStr)) {
                return Integer.parseInt(resultStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextStepNo;
    }

    public String getNextParticipants(String userId, int bindId, int taskId, String menuName, String
            opinion, int menuType) {
        String participants = null;
        String methodName = "getNextParticipants";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("bindid", bindId);
            json.put("taskId", taskId);
            if (menuName == null) {
                menuName = "";
            }
            if (opinion == null) {
                opinion = "";
            }
            json.put("menuName", menuName);
            json.put("opinion", opinion);
            json.put("menuType", menuType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    participants = resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return participants;
    }


    public ViewComponents getBOData(String userId, int taskId, int processInstanceId, String
            boTableName, boolean isHistoryTask) {
        ViewComponents components = null;
        String methodName = "getBOData";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(userId)) {
                json.put("userId", userId);
            }
            if (!isHistoryTask && taskId > -1) {
                json.put("taskId", taskId);
            }
            json.put("processInstanceId", processInstanceId);
            if (boTableName == null) {
                boTableName = "";
            }
            json.put("boTableName", boTableName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            String resultStr = null;
            if (TEST) {
                resultStr = TestData.TEST_SELECTVIEWDATA;
            } else {
                ht.call(NAMESPACE + methodName, envelope);
                if (envelope.getResponse() != null) {
                    SoapObject result = (SoapObject) envelope.bodyIn;
                    resultStr = result.getPropertyAsString("return");

                }
            }
            if (resultIsNotEmpty(resultStr)) {
                JSONArray jsonArray = new JSONArray(resultStr);
                components = new ViewComponents();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ComponentParser.parser(components, jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return components;
    }

    public List<OBDataBean> getBODatas(int processInstanceId, String boTableName) {
        List<OBDataBean> OBDatas = null;
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getBODatas";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("processInstanceId", processInstanceId);
            if (boTableName == null) {
                boTableName = "";
            }
            json.put("boTableName", boTableName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONArray jsonArray = new JSONArray(resultStr);
                    OBDatas = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        OBDataBean OBData = (OBDataBean) DataParser.factory(DataParser
                                .PARSER_TYPE_OBDATA).parser(jsonObject);
                        OBDatas.add(OBData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OBDatas;
    }

    public String downloadFileByFiled(String boTableName, String fileName, int boid) {
        String url = null;
        String methodName = "downloadFileByFiled";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("boid", boid);
            if (boTableName == null) {
                boTableName = "";
            }
            json.put("boTableName", boTableName);
            json.put("fileName", fileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    url = resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public List<ProcessOpinion> getProcessOpinionList(int bindid) {
        List<ProcessOpinion> processOpinionList = null;
        String methodName = "getProcessOpinionList";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("bindid", bindid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONArray jsonArray = new JSONArray(resultStr);
                    if (jsonArray.length() > 0) {
                        processOpinionList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ProcessOpinion processOpinion = (ProcessOpinion) DataParser.factory
                                    (DataParser.PARSER_TYPE_PROCESSOPINION).parser(jsonObject);
                            processOpinionList.add(processOpinion);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return processOpinionList;
    }

    public List<MenuBean> getAuditMenus(String userId, int taskId) {
        List<MenuBean> menus = null;
        String methodName = "getAuditMenus";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("taskId", taskId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONArray jsonArray = new JSONArray(resultStr);
                    if (jsonArray.length() > 0) {
                        menus = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MenuBean menu = (MenuBean) DataParser.factory
                                    (DataParser.PARSER_TYPE_MENU).parser(jsonObject);
                            menus.add(menu);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menus;
    }


    public String createTheOaWorkMng(String json) {
        String OID = null;
        String methodName = "createTheOaWorkMng";
        SoapObject request = new SoapObject(NAMESPACE, methodName);

        request.addProperty("jsonstr", json);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    OID = resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OID;
    }


    public Hashtable<String, Object> getOaWorkMngs(String brzid, int brzoaid, String type, int start,
                                                   int limit)
            throws
            JSONException {
        Hashtable<String, Object> hash = new Hashtable<>();
        String methodName = "getOaWorkMngs";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        json.put("brzid", brzid);
        json.put("brzoaid", String.valueOf(brzoaid));
        json.put("type", type);
        json.put("start", start);
        json.put("limit", limit);
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    if (jsonObject.has(RESULT_PARAM_DATASCOUNT)) {
                        int datascount = jsonObject.getInt(RESULT_PARAM_DATASCOUNT);
                        hash.put(RESULT_PARAM_DATASCOUNT, new Integer(datascount));
                    }
                    if (jsonObject.has(RESULT_PARAM_DATAS)) {
                        List<TaskICreatedBean> tasks = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray(RESULT_PARAM_DATAS);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject childJson = jsonArray.getJSONObject(i);
                            TaskICreatedBean task = (TaskICreatedBean) DataParser.factory(DataParser
                                    .PARSER_TYPE_TASKICREATED).parser(childJson);
                            tasks.add(task);
                        }
                        hash.put(RESULT_PARAM_DATAS, tasks);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public Hashtable<String, Object> getOaWorkMngDetailss(String brzid, int brzoaid, String type, int start,
                                                          int limit)
            throws
            JSONException {
        Hashtable<String, Object> hash = new Hashtable<>();
        String methodName = "getOaWorkMngDetailss";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        json.put("zxrid", brzid);
        json.put("zxroaid", String.valueOf(brzoaid));
        json.put("type", type);
        json.put("start", start);
        json.put("limit", limit);
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    if (jsonObject.has(RESULT_PARAM_DATASCOUNT)) {
                        int datascount = jsonObject.getInt(RESULT_PARAM_DATASCOUNT);
                        hash.put(RESULT_PARAM_DATASCOUNT, new Integer(datascount));
                    }
                    if (jsonObject.has(RESULT_PARAM_DATAS)) {
                        List<TaskAssignedToMeBean> tasks = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray(RESULT_PARAM_DATAS);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject childJson = jsonArray.getJSONObject(i);
                            TaskAssignedToMeBean task = (TaskAssignedToMeBean) DataParser.factory(DataParser
                                    .PARSER_TYPE_TASKASSIGNEDTOME).parser(childJson);
                            tasks.add(task);
                        }
                        hash.put(RESULT_PARAM_DATAS, tasks);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }


    public ViewComponents getTheOaWorkMng(String type, String OID) {
        ViewComponents components = null;
        String methodName = "getTheOaWorkMng";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("fid", OID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONArray jsonArray = new JSONArray(resultStr);
                    components = new ViewComponents();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ComponentParser.parser(components, jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return components;
    }

    public ViewComponents getTheOaWorkMngDetails(String type, String OID) {
        ViewComponents components = null;
        String methodName = "getTheOaWorkMngDetails";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("fid", OID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONArray jsonArray = new JSONArray(resultStr);
                    components = new ViewComponents();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ComponentParser.parser(components, jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return components;
    }

    public String createTheOaWorkMngInfos(String json) {
        String OID = null;
        String methodName = "createTheOaWorkMngInfos";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("jsonstr", json);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    OID = resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OID;
    }

    public String createNextWorkflowTask(String userId, int bindid, int taskId, String taskTitle,
                                         String menuName, String opinion, int menuType, String
                                                 joinPerson)
            throws JSONException {
        String methodName = "createNextWorkflowTask";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("bindid", bindid);
        json.put("taskId", taskId);
        if (menuName == null) {
            menuName = "";
        }
        if (opinion == null) {
            opinion = "";
        }
        json.put("menuName", menuName);
        json.put("menuType", menuType);
        json.put("opinion", opinion);
        if (!TextUtils.isEmpty(joinPerson)) {
            json.put("joinPerson", joinPerson);
        }
        if (!TextUtils.isEmpty(taskTitle)) {
            json.put("taskTitle", taskTitle);
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String downloadOaWorkMngFile(String attname, String realname) throws JSONException {
        String methodName = "downloadOaWorkMngFile";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        if (attname == null) {
            attname = "";
        }
        if (realname == null) {
            realname = "";
        }
        json.put("attname", attname);
        json.put("realname", realname);
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public int updateBOData(String boTableName, int boId, String recordData) throws JSONException {
        String methodName = "updateBOData";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        json.put("boTableName", boTableName);
        json.put("boId", boId);
        json.put("recordData", recordData);
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return Integer.parseInt(resultStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取服务器上apk 的地址
    public String getAndroidVersion(int versionCode) {
        String path = null;
        String methodName = "getAndroidVersion";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("version", String.valueOf(versionCode));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    JSONObject jsonResult = new JSONObject(resultStr);
                    if (jsonResult.has("download") && jsonResult.getString("download").equals
                            ("true")) {
                        path = jsonResult.getString("path");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    public String downloadSignImg(String signValue) {
        String signUrl = null;
        String methodName = "downloadSignImg";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        request.addProperty("jsonstr", signValue);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    signUrl = resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signUrl;
    }
    public String getWebInfos1(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "getWebInfos1";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_SERVER_URL, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACE + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String getWebInfos(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "getWebInfos";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_SERVER_URL, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACE + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String GetEmployeeDeposit(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetEmployeeDeposit";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLDEPOSITINFO, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINETORIGINAL + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String GetEmployeePickup(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetEmployeePickup";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLPICKUP, 8 * 1000);
            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());
        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }

    public String GetQRCode(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetQRCode";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URL, 8 * 1000);
//            //-------------------------------ceshi------------------------//
//            HttpTransportSE httpTransportSE = new HttpTransportSE("http://192.168.2.110:8080/Cloud/CloudAppPort?wsdl", 8 * 1000);
//            //--------------------------------------//
//            httpTransportSE.call(null, envelope);
            //-------------------------------ceshi------------------------//
            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);
            //--------------------------------------//
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());

        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());
        }
        return null;
    }
    public String GetDepartment(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetDepartment";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLCOURT, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String FileStateInReverse(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "FileStateInReverse";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLEMPASSIGN, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());
        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String GetTelByEmployeeID(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetTelByEmployeeID";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLEMPASSIGN, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());


        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String sendSmsInfo(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "sendSmsInfo";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE("http://111.53.181.200:6688/mserver/services/awsSvc?wsdl", 8 * 1000);
            httpTransportSE.call(null, envelope);iSoapUtilCallback.onSuccess(envelope);

            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());

        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());
        }
        return null;
    }
    public String sendAndrNotByAlias(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "sendAndrNotByAlias";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLEMPASSIGN, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());


        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String GetEmployee(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetEmployee";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;

            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLEMPLOYEE, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }
    public String GetCourtInfo(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "GetCourtInfo";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
//            envelope.dotNet = true;
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_CABINET_URLCOURT, 8 * 1000);
            //            httpTransportSE.debug = true;
            //            ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
            //            headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

            httpTransportSE.call(NAMESPACECABINET + methodName, envelope);


            //            httpTransportSE.call(null, envelope, headerPropertyArrayList);
            //            System.setProperty("http.keepAlive", "false");
            // SoapObject bodyIn = (SoapObject) envelope.bodyIn;
            // result = bodyIn.getProperty(0).toString();
            //            LogUtil.i("SoapClient", envelope.getResponse().toString());
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());



        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());

        }
        return null;
    }

    public String getOaMessageList(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "getOaMessageList";
            SoapObject request = new SoapObject(NAMESPACECABINET, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.bodyOut = request;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_MESSAGE_URL, 8 * 1000);
            httpTransportSE.call(null, envelope);
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());

        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());
        }
        return null;
    }

    /**
     * 请假申请
     * @param params
     * @param iSoapUtilCallback
     * @return
     */

    public String getBoTableName(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "getBoTableName";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            envelope.dotNet = true;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_SERVER_URL, 8 * 1000);
            httpTransportSE.call(null, envelope);
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());

        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());
        }
        return null;
    }
    /**
     * 创建请假申请的流程
     * @param params
     * @param iSoapUtilCallback
     * @return
     */

    public String createInstances(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
        try {
            String dataDynamicCode = null;
            String methodName = "createInstances";
            SoapObject request = new SoapObject(NAMESPACE, methodName);
            // 传递参数
            LinkedHashMap<String, Object> paramsList = params.getParamsList();
            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
                request.addProperty(entry.getKey(), entry.getValue());
            }
            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = request;
            envelope.dotNet = true;
            // 等价于envelope.bodyOut=rpc;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_SERVER_URL, 8 * 1000);
            httpTransportSE.call(null, envelope);
            iSoapUtilCallback.onSuccess(envelope);
            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());

        } catch (Exception e) {
            iSoapUtilCallback.onFailure(e);
            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());
        }
        return null;
    }
    /**
     * 请假申请
     *
     * @param name
     * @return
     */
    public String  getBoTableName(String  name) {
        String methodName = "getBoTableName";
        SoapObject request = new SoapObject(NAMESPACE, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.addProperty("name", name);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                String resultStr = result.getPropertyAsString("return");
                return resultStr;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }



//    public String getOaAnswerMessages(SoapParams params, SoapClient.ISoapUtilCallback iSoapUtilCallback) {
//        try {
//            String dataDynamicCode = null;
//            String methodName = "getOaAnswerMessages";
//            SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
//            // 传递参数
//            LinkedHashMap<String, Object> paramsList = params.getParamsList();
//            Iterator<Map.Entry<String, Object>> iter = paramsList.entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry<String, Object> entry = iter.next();
//                Log.e("wrz", "请求参数--->Key:" + entry.getKey() + ",Value:" + entry.getValue());
//                request.addProperty(entry.getKey(), entry.getValue());
//            }
//            //生成调用WebService方法的SOAP请求信息，并制定SOAP的版本
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            //envelope.bodyOut = request;
//            // 等价于envelope.bodyOut=rpc;
//            envelope.setOutputSoapObject(request);
//            HttpTransportSE httpTransportSE = new HttpTransportSE(BASE_SERVER_URL_TOKEN, 8 * 1000);
//            httpTransportSE.call(null, envelope);
//            iSoapUtilCallback.onSuccess(envelope);
//            Log.e("wrz", "返回结果--->" + envelope.getResponse().toString());
//
//        } catch (Exception e) {
//            iSoapUtilCallback.onFailure(e);
//            Log.e("wrz error", params.getParamsList().get("action") + "报错--->" + e.toString());
//        }
//        return null;
//    }
    /**
     * 获取留言信息列表
     *
     * @param userOid
     * @return
     */
    public  String getOaAnswerMessages(String userOid) {
        String methodName = "getOaAnswerMessages";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
//        JSONObject json = new JSONObject();
//        try {
//            json.put("judge_id", userOid);
//            json.put("del_flag","0");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        request.addProperty("jsonstr", userOid.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取留言内容详情
     *
     * @param oid
     * @return
     */
    public  String getTheOaAnswerMessage(String oid) {
        String methodName = "getTheOaAnswerMessage";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("oid", oid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 帐号切换
     *
     * @param
     * @return
     */
    public  String getUsersByRelation(String relation) {
        String methodName = "getUsersByRelation";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("relation", relation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
//            ht = new HttpsTransportSE(mHost, mPort,
//                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 登录获取
     *
     * @param token
     * @return
     */
    public String getNoticeCount(String token, String state) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getNoticeCount";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
            json.put("qisread", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 登录获取
     *
     * @param token
     * @return
     */
    public String getNoticeList(String token, String state) {
//        String serviceUrl = BASE_SERVER_URL + AWSSVC;
        String methodName = "getNoticeList";
        SoapObject request = new SoapObject(NAMESPACE_TOKEN, methodName);
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
            json.put("qisread", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addProperty("jsonstr", json.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = request;
        envelope.dotNet = true;
        HttpTransportSE ht;
        if (IS_HTTP) {
            ht = new HttpTransportSE(BASE_SERVER_URL_TOKEN);
        } else {
            ht = new HttpsTransportSE(mHost, mPort,
                    WS_OPS, TIMEOUT);
        }
        try {
            ht.call(NAMESPACE_TOKEN + methodName, envelope);
            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;

                String resultStr = result.getPropertyAsString("return");
                if (resultIsNotEmpty(resultStr)) {
                    return resultStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    private boolean resultIsNotEmpty(String resultStr) {
        return !TextUtils.isEmpty(resultStr) && !resultStr.equals("anyType{}");
    }

}
