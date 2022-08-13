package com.jdd.android.jdd.requests;

import android.os.Bundle;
import android.os.Environment;
import com.jdd.android.jdd.constants.CacheKey;
import com.jdd.android.jdd.utils.HttpRequest;
import com.jdd.android.jdd.utils.ProjectUtil;
import com.thinkive.adf.core.CallBack;
import com.thinkive.adf.core.MessageAction;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.core.cache.DataCache;
import com.thinkive.adf.core.cache.MemberCache;
import com.thinkive.adf.invocation.http.EasySSLSocketFactory;
import com.thinkive.adf.invocation.http.ResponseAction;
import com.thinkive.adf.log.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * 描述：上传文件请求接口
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2015-06-18
 * @since 1.0
 */
public class UploadFileRequest implements CallBack.SchedulerCallBack {
    private MemberCache mCache = DataCache.getInstance().getCache();
    private Parameter mParameter;
    private ResponseAction mAction;
    private int mTaskId;
    private String mCacheKey;

    public UploadFileRequest(int taskId, Parameter parameter, ResponseAction action) {
        mParameter = parameter;
        mAction = action;
        mTaskId = taskId;
        ProjectUtil.addPlatformFlag(parameter);
    }

    @Override
    public void handler(MessageAction messageAction) {
        //TODO 服务器原因必须加端口，否则上传不了
        String url = "http://112.74.29.151:8080/uploadfile.do?type=T1";

        Bundle bundle = new Bundle();
        bundle.putInt("taskId", mTaskId);

        HttpClient httpclient;
        HttpPost httpPost = null;
        try {
            httpclient = getHttpClient();

            httpPost = new HttpPost(url);
            httpPost.setHeader("Cookie", "JSESSIONID=" + HttpRequest.sJSessionId);
            MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            mulentity.addPart("param", new FileBody(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "temp.png")));
            for (Iterator<String> itera = mParameter.keys(); itera.hasNext(); ) {
                String key = itera.next();
                Object value = mParameter.getObject(key);
                if (value instanceof String) {
                    mulentity.addPart(key, new StringBody(value.toString().trim()));
                } else {
                    FileBody filebody = (FileBody) value;
                    mulentity.addPart(key, filebody);
                }
            }
            httpPost.setEntity(mulentity);
            HttpResponse response = httpclient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                messageAction.transferAction(ResponseAction.RESULT_OK, bundle, mAction);
            } else {
                messageAction.transferAction(ResponseAction.RESULT_SERVER_ERROR, bundle, mAction);
            }
            System.out.println("UploadFile: "+readStream(response.getEntity().getContent(), "UTF-8"));
            httpPost.abort();
        } catch (Exception e) {
            messageAction.transferAction(ResponseAction.RESULT_INTERNAL_ERROR, bundle, mAction);
        }
    }

    public HttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        // 设置一些基本参数
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
        // 超时设置
        /* 从连接池中取连接的超时时间 */
        ConnManagerParams.setTimeout(params, 1000);
        /** 连接超时 */
        HttpConnectionParams.setConnectionTimeout(params, 1000 * 10);
        /** 请求超时 */
        HttpConnectionParams.setSoTimeout(params, 1000 * 50);

        // 设置我们的HttpClient支持HTTP和HTTPS两种模式
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        schReg.register(new Scheme("https", new EasySSLSocketFactory(), 443));
        // 使用线程安全的连接管理来创建HttpClient
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
        HttpClient customerHttpClient = new DefaultHttpClient(conMgr, params);
        return customerHttpClient;
    }

    private static String readStream(InputStream inputStream, String charset) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
        return null;
    }
}
