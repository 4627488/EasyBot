package me.ed333.easyBot.utils;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLContext;
import java.io.IOException;

public class HttpRequest {
    public static String doGet(String url, String param) {
        String geturl = url + "?" + param;
        String responseContent = null;
        HttpGet get = new HttpGet(geturl);
        CloseableHttpResponse response = null;
        SSLContext sslContext = null;
        
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CloseableHttpClient client = HttpClients.custom().setSslcontext(sslContext).
                setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        try {
            response = client.execute(get);
            HttpEntity entity = response.getEntity();// 响应体
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {// 正确返回
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) { e.printStackTrace(); }

        finally {
            try {
                if (response != null) response.close();
                if (client != null) client.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
        return responseContent;
    }

    public static @Nullable String doPost(String url, @NotNull JSONObject json) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(json.toString(), "UTF-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                return EntityUtils.toString(responseEntity);
            }
            else{
                System.out.println("请求返回:"+state+"("+url+")");
            }
        }
        finally {
            try {
                if (response != null) response.close();
                httpclient.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
        return null;
    }
}