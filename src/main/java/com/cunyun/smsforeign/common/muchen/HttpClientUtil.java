package com.cunyun.smsforeign.common.muchen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


/**
 * @author administrator
 *
 * Http Client工具类
 * 发起http client 请求
 *
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

public class HttpClientUtil {

    private static Logger LOGGER = Logger.getLogger(HttpClientUtil.class);
    private static final String TYPE_STRING = "string";

    private static final String TYPE_BYTEARRAY = "byte[]";

    private static final String TYPE_STREAM = "stream";

    private static final int CONNECT_TIMEOUT = 10000;  //连接超时1s
    private static final int READ_TIMEOUT = 100000;     //读取超时2s

    private static HttpClientUtil instance = null;

    private HttpClientUtil(){}

    /**
     * 使用默认的页面请求编码：UTF-8
     * @return
     */
    public static HttpClientUtil getInstance(){
        return getInstance("UTF-8");
    }

    public static HttpClientUtil getInstance(String urlCharset){
        if(instance == null){
            instance = new HttpClientUtil();
        }
        //设置默认的url编码
        instance.setUrlCharset(urlCharset);
        return instance;
    }

    /**
     * 请求编码，默认使用UTF-8
     */
    private String urlCharset = "UTF-8";

    /**
     * @param urlCharset 要设置的 urlCharset。
     */
    public void setUrlCharset(String urlCharset) {
        this.urlCharset = urlCharset;
    }
    /**
     * 获取字符串型返回结果，通过发起http post请求
     * @param targetUrl
     * @param String requestContent
     * @return
     * @throws Exception
     */
    public String getResponseBodyAsString(String targetUrl,String requestContent)throws Exception{

        return (String)setPostRequest(targetUrl,TYPE_STRING,requestContent);
    }

    /**
     * 获取字符串型返回结果，通过发起http post请求
     * @param targetUrl
     * @param String requestContent
     * @return
     * @throws Exception
     */
    public String getResponseBodyAsString(String targetUrl,String requestContent,Map<String,String> headers)throws Exception{

        return (String)setPostRequest(targetUrl,TYPE_STRING,requestContent,headers);
    }

    /**
     * 获取字符数组型返回结果，通过发起http post请求
     * @param targetUrl
     * @param String requestContent
     * @return
     * @throws Exception
     */
    public byte[] getResponseBodyAsByteArray(String targetUrl,String requestContent)throws Exception{

        return (byte[])setPostRequest(targetUrl,TYPE_BYTEARRAY,requestContent);
    }

    /**
     * 获取字符串型返回结果，通过发起http post请求
     * @param targetUrl
     * @param params
     * @return
     * @throws Exception
     */
    public String getResponseBodyAsStringWithMap(String targetUrl,Map params)throws Exception{

        return (String)setPostRequestWithMap(targetUrl,TYPE_STRING,params);
    }

    public String getResponseBodyAsStringWithMap_httpget(String targetUrl,Map params)throws Exception{

        return (String)setGetRequestWithMap(targetUrl,TYPE_STRING,params);
    }

    public String getResponseBodyAsStringWithMap_httpget(String targetUrl,Map params,String charset)throws Exception{

        return (String)setGetRequestWithMap(targetUrl,TYPE_STRING,params,charset);
    }

    public String getResponseBodyAsStringWithMap_httpget(String targetUrl,Map params,String charset,Map<String, String> headers)throws Exception{

        return (String)setGetRequestWithMap(targetUrl,TYPE_STRING,params,charset,headers);
    }

    /**
     * 获取字符数组型返回结果，通过发起http post请求
     * @param targetUrl
     * @param params
     * @return
     * @throws Exception
     */
    public byte[] getResponseBodyAsByteArrayWithMap(String targetUrl,Map params)throws Exception{

        return (byte[])setPostRequestWithMap(targetUrl,TYPE_BYTEARRAY,params);
    }

    /**
     * 将response的返回流写到outputStream中，通过发起http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @param outputStream              输出流
     * @throws Exception

    public void getResponseBodyAsStream(String targetUrl,Map params,OutputStream outputStream)throws Exception{
    if(outputStream == null){
    throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
    }

    //response 的返回结果写到输出流
    setPostRequest(targetUrl,params,TYPE_STREAM,outputStream);
    }
     */


    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @return  Object                  返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    private Object setPostRequest(String targetUrl,String responseType,String requestContent,Map<String,String> headers)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        Object responseResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();;
        CloseableHttpResponse response = null;

        try{
            LOGGER.info("Request url:" + targetUrl +" data:" + requestContent);

            HttpPost httppost = new HttpPost(targetUrl);
            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);

            StringEntity entity = new StringEntity(requestContent,"UTF-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            //entity.setContentType("application/json");
            httppost.setEntity(entity);


//        	HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }

            Header h1 = httppost.getFirstHeader("Authorization");
            System.out.println(h1);
            Header h2 = httppost.getLastHeader("Accept");
            System.out.println(h2);

            response = httpClient.execute(httppost);
            //StatusLine state = response.getStatusLine();
            //if (state.getStatusCode() == HttpStatus.SC_OK){
            //}

            //根据指定类型进行返回
            if(StringUtils.equals(TYPE_STRING,responseType)){
                responseResult = EntityUtils.toString(response.getEntity(),"UTF-8");

                LOGGER.info("resp data:" + (String)responseResult);
            }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                responseResult = EntityUtils.toByteArray(response.getEntity());
                LOGGER.info("resp data:" + new String((byte[])responseResult));
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("setPostRequest Exception:",e);
        }finally{
            //释放链接
            response.close();
            httpClient.close();

        }

        return responseResult;
    }

    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @return  Object                  返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    private Object setPostRequest(String targetUrl,String responseType,String requestContent)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        Object responseResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();;
        CloseableHttpResponse response = null;

        try{
//        	LOGGER.info("Request url:" + targetUrl +" data:" + requestContent);

            HttpPost httppost = new HttpPost(targetUrl);
            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);

            StringEntity entity = new StringEntity(requestContent,"UTF-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            //entity.setContentType("application/json");
            httppost.setEntity(entity);
//            httpClient.getParams().setParameter("http.socket.timeout", new Integer(30000));
            response = httpClient.execute(httppost);

//        	HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
//		    response.addHeader("Set-Cookie", "c1=a; path=/; domain=yeetrack.com");
//		    response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"yeetrack.com\"");
//
//		    HeaderIterator it = response.headerIterator("Set-Cookie"); while (it.hasNext()) { System.out.println(it.next()); }


            //StatusLine state = response.getStatusLine();
            //if (state.getStatusCode() == HttpStatus.SC_OK){
            //}

            //根据指定类型进行返回
            if(StringUtils.equals(TYPE_STRING,responseType)){
                responseResult = EntityUtils.toString(response.getEntity(),"UTF-8");

                LOGGER.info("resp data:" + (String)responseResult);
            }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                responseResult = EntityUtils.toByteArray(response.getEntity());
                LOGGER.info("resp data:" + new String((byte[])responseResult));
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("setPostRequest Exception:",e);
        }finally{
            //释放链接
            response.close();
            httpClient.close();

        }

        return responseResult;
    }

    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @return  Object                  返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    private Object setPostRequestWithMap(String targetUrl,String responseType,Map params)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        Object responseResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();;


        CloseableHttpResponse response = null;

        NameValuePair[] nameValuePairArr = null;
        UrlEncodedFormEntity entity = null;
        try{
            //LOGGER.info("Request url:" + targetUrl +" data:" + requestContent);

            HttpPost httppost = new HttpPost(targetUrl);
            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();//设置请求和传输超时时间
            httppost.setConfig(requestConfig);
            //StringEntity entity = new StringEntity(requestContent,"UTF-8");//解决中文乱码问题
            //entity.setContentEncoding("UTF-8");
            //entity.setContentType("application/json");
            //httppost.setEntity(entity);

            if(params != null){

                List <NameValuePair> nvps = new ArrayList <NameValuePair>();

                Set key = params.keySet();
                Iterator keyIte = key.iterator();
                while(keyIte.hasNext()){
                    Object paramName = keyIte.next();
                    Object paramValue = params.get(paramName);
                    if(paramName instanceof String && paramValue instanceof String){
                        nvps.add(new BasicNameValuePair((String)paramName,(String)paramValue));
                    }
                }

                entity = new UrlEncodedFormEntity(nvps);
                entity.setContentEncoding("UTF-8");
                httppost.setEntity(entity);
            }

            LOGGER.info("Request url:" + targetUrl +" data:" + entity.toString());
            response = httpClient.execute(httppost);

            //StatusLine state = response.getStatusLine();
            //if (state.getStatusCode() == HttpStatus.SC_OK){
            //}

            // 请求结束，返回结果
            //responseResult = EntityUtils.toString(response.getEntity());



            //根据指定类型进行返回
            if(StringUtils.equals(TYPE_STRING,responseType)){
                responseResult = EntityUtils.toString(response.getEntity(),"UTF-8");

                LOGGER.info("resp data:" + (String)responseResult);
            }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                responseResult = EntityUtils.toByteArray(response.getEntity());

                LOGGER.info("resp data:" + new String((byte[])responseResult));
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("setPostRequestWithMap Exception:",e);
        }finally{
            //释放链接
            response.close();
            httpClient.close();

        }

        return responseResult;
    }

    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @return  Object                  返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    private Object setGetRequestWithMap(String targetUrl,String responseType,Map<String,String> params)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        Object responseResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();;
        CloseableHttpResponse response = null;

        NameValuePair[] nameValuePairArr = null;
        UrlEncodedFormEntity entity = null;
        HttpGet httpget = null;
        String url = targetUrl ;
        try{
            //LOGGER.info("Request url:" + targetUrl +" data:" + requestContent);


            //StringEntity entity = new StringEntity(requestContent,"UTF-8");//解决中文乱码问题
            //entity.setContentEncoding("UTF-8");
            //entity.setContentType("application/json");
            //httppost.setEntity(entity);

            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, "UTF-8"));
            }

            /*
            if(params != null){

                List <NameValuePair> nvps = new ArrayList <NameValuePair>();

                Set key = params.keySet();
                Iterator keyIte = key.iterator();
                while(keyIte.hasNext()){
                    Object paramName = keyIte.next();
                    Object paramValue = params.get(paramName);
                    if(paramName instanceof String && paramValue instanceof String){
                    	nvps.add(new BasicNameValuePair((String)paramName,(String)paramValue));
                    }
                }
                LOGGER.info("nvps:" + nvps.size() +  "  data:" +nvps.toString());
                entity = new UrlEncodedFormEntity(nvps);
                entity.setContentEncoding("UTF-8");
             */



            //}

            httpget  = new HttpGet(url);

            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();//设置请求和传输超时时间
            httpget.setConfig(requestConfig);
            //LOGGER.info("Request url:" + targetUrl +" data:" + entity.toString());
            LOGGER.info("Request url:" + url );
            response = httpClient.execute(httpget);

            //StatusLine state = response.getStatusLine();
            //if (state.getStatusCode() == HttpStatus.SC_OK){
            //}

            // 请求结束，返回结果
            //responseResult = EntityUtils.toString(response.getEntity());



            //根据指定类型进行返回
            if(StringUtils.equals(TYPE_STRING,responseType)){
                responseResult = EntityUtils.toString(response.getEntity(),"UTF-8");

                LOGGER.info("resp data:" + (String)responseResult);
            }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                responseResult = EntityUtils.toByteArray(response.getEntity());

                LOGGER.info("resp data:" + new String((byte[])responseResult));
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("setGetRequestWithMap Exception:",e);
        }finally{
            //释放链接
            response.close();
            httpClient.close();

        }

        return responseResult;
    }


    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @return  Object                  返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    private Object setGetRequestWithMap(String targetUrl,String responseType,Map<String,String> params,String charset)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        Object responseResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();;
        CloseableHttpResponse response = null;

        NameValuePair[] nameValuePairArr = null;
        UrlEncodedFormEntity entity = null;
        HttpGet httpget = null;
        String url = targetUrl ;
        try{
            //LOGGER.info("Request url:" + targetUrl +" data:" + requestContent);


            //StringEntity entity = new StringEntity(requestContent,"UTF-8");//解决中文乱码问题
            //entity.setContentEncoding("UTF-8");
            //entity.setContentType("application/json");
            //httppost.setEntity(entity);

            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }

            /*
            if(params != null){

                List <NameValuePair> nvps = new ArrayList <NameValuePair>();

                Set key = params.keySet();
                Iterator keyIte = key.iterator();
                while(keyIte.hasNext()){
                    Object paramName = keyIte.next();
                    Object paramValue = params.get(paramName);
                    if(paramName instanceof String && paramValue instanceof String){
                    	nvps.add(new BasicNameValuePair((String)paramName,(String)paramValue));
                    }
                }
                LOGGER.info("nvps:" + nvps.size() +  "  data:" +nvps.toString());
                entity = new UrlEncodedFormEntity(nvps);
                entity.setContentEncoding("UTF-8");
             */



            //}

            httpget  = new HttpGet(url);

            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();//设置请求和传输超时时间
            httpget.setConfig(requestConfig);
            //LOGGER.info("Request url:" + targetUrl +" data:" + entity.toString());
            LOGGER.info("Request url:" + url );
            response = httpClient.execute(httpget);

            //StatusLine state = response.getStatusLine();
            //if (state.getStatusCode() == HttpStatus.SC_OK){
            //}

            // 请求结束，返回结果
            //responseResult = EntityUtils.toString(response.getEntity());



            //根据指定类型进行返回
            if(StringUtils.equals(TYPE_STRING,responseType)){
                responseResult = EntityUtils.toString(response.getEntity(),charset);

                LOGGER.info("resp data:" + (String)responseResult);
            }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                responseResult = EntityUtils.toByteArray(response.getEntity());

                LOGGER.info("resp data:" + new String((byte[])responseResult));
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("setGetRequestWithMap Exception:",e);
        }finally{
            //释放链接
            response.close();
            httpClient.close();

        }

        return responseResult;
    }


    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @param headers
     * @return  Object                  返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    private Object setGetRequestWithMap(String targetUrl,String responseType,Map<String,String> params,String charset,Map<String, String> headers)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }

        Object responseResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();;
        CloseableHttpResponse response = null;

        NameValuePair[] nameValuePairArr = null;
        UrlEncodedFormEntity entity = null;
        HttpGet httpget = null;
        String url = targetUrl ;
        try{
            //LOGGER.info("Request url:" + targetUrl +" data:" + requestContent);


            //StringEntity entity = new StringEntity(requestContent,"UTF-8");//解决中文乱码问题
            //entity.setContentEncoding("UTF-8");
            //entity.setContentType("application/json");
            //httppost.setEntity(entity);

            if(params != null && !params.isEmpty()){
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(Map.Entry<String,String> entry : params.entrySet()){
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }

            /*
            if(params != null){

                List <NameValuePair> nvps = new ArrayList <NameValuePair>();

                Set key = params.keySet();
                Iterator keyIte = key.iterator();
                while(keyIte.hasNext()){
                    Object paramName = keyIte.next();
                    Object paramValue = params.get(paramName);
                    if(paramName instanceof String && paramValue instanceof String){
                    	nvps.add(new BasicNameValuePair((String)paramName,(String)paramValue));
                    }
                }
                LOGGER.info("nvps:" + nvps.size() +  "  data:" +nvps.toString());
                entity = new UrlEncodedFormEntity(nvps);
                entity.setContentEncoding("UTF-8");
             */



            //}

            httpget  = new HttpGet(url);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                System.out.println("key = " + entry.getKey() + " and value = " + entry.getValue());
                httpget.addHeader(entry.getKey(), entry.getValue());
            }

            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(READ_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();//设置请求和传输超时时间
            httpget.setConfig(requestConfig);
            //LOGGER.info("Request url:" + targetUrl +" data:" + entity.toString());
            LOGGER.info("Request url:" + url );


            response = httpClient.execute(httpget);

            //StatusLine state = response.getStatusLine();
            //if (state.getStatusCode() == HttpStatus.SC_OK){
            //}

            // 请求结束，返回结果
            //responseResult = EntityUtils.toString(response.getEntity());



            //根据指定类型进行返回
            if(StringUtils.equals(TYPE_STRING,responseType)){
                responseResult = EntityUtils.toString(response.getEntity(),charset);

                LOGGER.info("resp data:" + (String)responseResult);
            }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                responseResult = EntityUtils.toByteArray(response.getEntity());

                LOGGER.info("resp data:" + new String((byte[])responseResult));
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("setGetRequestWithMap Exception:",e);
        }finally{
            //释放链接
            response.close();
            httpClient.close();

        }

        return responseResult;
    }

}
