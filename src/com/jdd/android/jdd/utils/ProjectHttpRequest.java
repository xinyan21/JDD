package com.jdd.android.jdd.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.thinkive.adf.core.Parameter;
import com.thinkive.adf.invocation.http.HttpFactory;
import com.thinkive.adf.invocation.http.ResponseCallBack;
import com.thinkive.adf.log.Logger;
import com.thinkive.adf.tools.ConfigStore;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;


public class ProjectHttpRequest {
    private String charset = "UTF-8";
    private int responseCode = -1;
    private DefaultHttpClient httpClient = null;

    public ProjectHttpRequest() {
        this.charset = ConfigStore.getConfigValue("system", "CHARSET");
        this.httpClient = (DefaultHttpClient) HttpFactory.getHttpClient(this.charset);
    }

    public byte[] post(String url, Parameter params) {
        HttpPost request = new HttpPost(url);
        byte[] result = null;

        try {
            if (params != null && !params.isEmpty()) {
                ArrayList e = new ArrayList();
                Iterator e1 = params.keys();

                while (e1.hasNext()) {
                    String key = (String) e1.next();
                    e.add(new BasicNameValuePair(key, params.getString(key)));
                }

                request.setEntity(new UrlEncodedFormEntity(e, "UTF-8"));
            }

            HttpResponse e1 = this.httpClient.execute(request);
            this.responseCode = e1.getStatusLine().getStatusCode();
            if (this.responseCode == 200) {
                HttpEntity e11 = e1.getEntity();
                result = EntityUtils.toByteArray(e11);
                e11.consumeContent();
            } else {
                request.abort();
            }
        } catch (SocketException var11) {
            try {
                result = "{\"error_info\":\"连接被重置,数据无法返回（SocketException）\",\"error_no\":\"-110\"}".getBytes(this.charset);
            } catch (UnsupportedEncodingException var10) {
            }

            request.abort();
        } catch (SocketTimeoutException var12) {
            try {
                result = "{\"error_info\":\"请求数据超时,请检查网络或服务是否运行正常.\",\"error_no\":\"-111\"}".getBytes(this.charset);
            } catch (UnsupportedEncodingException var9) {
            }

            request.abort();
        } catch (Exception var13) {
            try {
                result = "{\"error_info\":\"框架内部错误!.\",\"error_no\":\"-119\"}".getBytes(this.charset);
            } catch (UnsupportedEncodingException var8) {
            }

            request.abort();
        }

        return result;
    }

    public byte[] get(String url, Parameter params) {
        HttpGet request = null;
        byte[] result = null;

        try {
            if (params != null && !params.isEmpty()) {
                ArrayList e = new ArrayList();
                Iterator e1 = params.keys();

                while (e1.hasNext()) {
                    String key = (String) e1.next();
                    e.add(new BasicNameValuePair(key, params.getString(key)));
                }

                String e11 = URLEncodedUtils.format(e, this.charset);
                request = new HttpGet(url + "?" + e11);
            } else {
                request = new HttpGet(url);
            }

            HttpResponse e1 = this.httpClient.execute(request);
            this.responseCode = e1.getStatusLine().getStatusCode();
            if (this.responseCode == 200) {
                HttpEntity e12 = e1.getEntity();
                result = EntityUtils.toByteArray(e12);
                e12.consumeContent();
            } else {
                request.abort();
            }
        } catch (SocketException var11) {
            try {
                result = "{\"error_info\":\"连接被重置,数据无法返回（SocketException）\",\"error_no\":\"-110\"}".getBytes(this.charset);
            } catch (UnsupportedEncodingException var10) {
            }

            request.abort();
        } catch (SocketTimeoutException var12) {
            try {
                result = "{\"error_info\":\"请求数据超时,请检查网络或服务是否运行正常.\",\"error_no\":\"-111\"}".getBytes(this.charset);
            } catch (UnsupportedEncodingException var9) {
            }

            request.abort();
        } catch (Exception var13) {
            try {
                result = "{\"error_info\":\"框架内部错误!.\",\"error_no\":\"-119\"}".getBytes(this.charset);
            } catch (UnsupportedEncodingException var8) {
            }

            request.abort();
        }

        return result;
    }

    public void post(String url, Parameter params, ResponseCallBack callBack) {
        byte[] result = this.post(url, params);
        if (this.responseCode == 200) {
            callBack.handler(new Object[]{result});
        } else {
            callBack.exception(this.responseCode, new Object[]{result});
        }

    }

    public void get(String url, Parameter params, ResponseCallBack callBack) {
        byte[] result = this.get(url, params);
        if (this.responseCode == 200) {
            callBack.handler(new Object[]{result});
        } else {
            callBack.exception(this.responseCode, new Object[]{result});
        }

    }

    public String uploadTo(String url, Parameter args, ResponseCallBack callBack) {
        HttpPost httpPost = null;
        String result = "{\"error_info\":\"文件上传成功!\",\"error_no\":\"0\"}";

        try {
            HttpClient httpclient = HttpFactory.getHttpClient(this.charset);
            httpPost = new HttpPost(url);
            MultipartEntity e = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            Iterator response = args.keys();

            Object buffer;
            while (response.hasNext()) {
                String code = (String) response.next();
                buffer = args.getObject(code);
                if (buffer instanceof String) {
                    e.addPart(code, new StringBody(buffer.toString().trim()));
                } else {
                    FileBody filebody = (FileBody) buffer;
                    e.addPart(code, filebody);
                }
            }

            httpPost.setEntity(e);
            HttpResponse response1 = httpclient.execute(httpPost);
            int code1 = response1.getStatusLine().getStatusCode();
            buffer = null;
            byte[] buffer1 = EntityUtils.toByteArray(response1.getEntity());
            if (callBack != null) {
                if (code1 == 200) {
                    callBack.handler(new Object[]{buffer1});
                } else {
                    callBack.exception(code1, new Object[]{buffer1});
                }
            }

            httpPost.abort();
        } catch (Exception var12) {
            result = "{\"error_info\":\"" + Log.getStackTraceString(var12) + "\",\"error_no\":\"-119\"}";
            httpPost.abort();
        }

        return result;
    }

    public Bitmap getBitmapFromUrl(String imgUrl) {
        HttpPost request = new HttpPost(imgUrl);
        InputStream is = null;
        Bitmap bitmap = null;

        try {
            HttpResponse e = this.httpClient.execute(request);
            this.responseCode = e.getStatusLine().getStatusCode();
            if (this.responseCode == 200) {
                is = e.getEntity().getContent();
                bitmap = BitmapFactory.decodeStream(is);
            } else {
                request.abort();
            }
        } catch (Exception var14) {
            request.abort();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if (bitmap != null && bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            } catch (Exception var13) {
            }

        }

        return bitmap;
    }

    private byte[] getResult(InputStream inStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        boolean len = true;

        int len1;
        while ((len1 = inStream.read(buf)) != -1) {
            baos.write(buf, 0, len1);
        }

        return baos.toByteArray();
    }
}
