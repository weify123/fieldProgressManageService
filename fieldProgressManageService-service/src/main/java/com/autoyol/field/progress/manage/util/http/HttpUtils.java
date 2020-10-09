package com.autoyol.field.progress.manage.util.http;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**  
 * @Author zg 
 * @Date 2014年7月5日
 * @Comments
 */
public class HttpUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	private static final String ENCODEING = "UTF-8";
	
	public static String get(String reqUrl) throws Exception {
		String resContent = null;
		
		HttpURLConnection conn = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);//是否自动处理重定向
			conn.setRequestMethod("GET");
			conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "AutoyolEs_console");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//传递参数使用 &链接的表单提交方式
            conn.connect();
			//接收返回数据
			int resCode = conn.getResponseCode();
			if(resCode == 200){
				in = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				resContent = reader.readLine();
			}else{
				resContent = "error:"+resCode;
				logger.info("服务器返回码："+resCode);
			}
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		} finally{
			if(reader != null){
				reader.close();
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		return resContent;
	}
	
	public static String post(String reqUrl, String reqContent) throws Exception {
		return sendPost(reqUrl, reqContent, "AutoyolEs_console");
	}

	public static String postInterface(String requestUrl, String reqestContent) throws Exception{
		return sendPost(requestUrl, reqestContent, "Autoyol-ServerCenter");
	}
	
	private static String sendPost(String reqUrl, String reqContent, String userAgent) throws Exception{
		String resContent = null;
		
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(60000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);//是否自动处理重定向
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//传递参数使用 &链接的表单提交方式
            conn.connect();
            out = conn.getOutputStream();
            //发送请求数据
			out.write(reqContent.getBytes(ENCODEING));
			out.flush();
			out.close();
			//接收返回数据
			int resCode = conn.getResponseCode();
			if(resCode == 200){
				in = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				resContent = reader.readLine();
			}else{
				resContent = "error:"+resCode;
				logger.info("服务器返回码："+resCode);
			}
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		} finally{
			if(reader != null){
				reader.close();
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		return resContent;
	}

	public static void main(String[] args) throws Exception {
		StringBuilder sb = new StringBuilder("accessKey=");
		sb.append("513ec047844d40bb8ff6ce940e0f38d6");
		sb.append("&mobile=");
		sb.append("18702138251");
		sb.append("187xxxxxxxx");
		sb.append("&content=");
		sb.append("测试短信");
		sb.append("&sender=");
		sb.append(4);
		sb.append("&message=");
		sb.append(4);
		sb.append("&type=");
		sb.append(4);

		
		System.out.println(post("http://10.0.3.84:8081/sms/mongateMsg", sb.toString()));
			
	}
	
	/**
	 * 
	 * @param str   json数据格式
	 * @param reqUrl
	 */
	public static String appPost(String str, String reqUrl) throws Exception{
		//发送数据
		HttpURLConnection conn = null;
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);//是否自动处理重定向
			conn.setRequestMethod("POST");
			//conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type","application/x-gzip");
			conn.setRequestProperty("User-Agent", "AutoyolEs_console");
			conn.connect();
			GZIPOutputStream out = new GZIPOutputStream(conn.getOutputStream());
			byte[] reqContent = str.getBytes();
//			System.out.println("testPost发送内容："+new String(reqContent,"UTF-8"));//{"name":"张三","age":25,"sex":"男","addr":["地址1","地址2"]}
			out.write(reqContent);
			out.flush();
			out.close();
			
			//接收返回数据
			InputStream in = conn.getInputStream();
			GZIPInputStream gzin = new GZIPInputStream(in);
			BufferedReader reader = new BufferedReader(new InputStreamReader(gzin,"UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {
//			    System.err.println("client received:"+line);
				sb.append(line);
			}
			reader.close();
			//conn.disconnect();       //{"resCode":"000000","resMsg":"success","data":{"serverTime":"20140804172747557"}}
			return sb.toString();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		} finally{
			if(conn != null){
				conn.disconnect();
			}
		}
	}
	
	 /**
     * 以&的方式提交
     * @param reqUrl
     * @param reqContent
     * @return
     */
    public static String postAnd(String reqUrl,String reqContent){
		String resContent = null;
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);//是否自动处理重定向
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.connect();
            out = conn.getOutputStream();
            //发送请求数据
            out.write(reqContent.getBytes(ENCODEING));
            out.flush();
            out.close();
            //接收返回数据
            int resCode = conn.getResponseCode();
            if(resCode == 200){
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODEING));
                resContent = reader.readLine();
            }else{
                System.out.println("服务器返回码："+resCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(reader != null){
                try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
            if(conn != null){
                conn.disconnect();
            }
        }
        return resContent;
	}
    
    public static String getAnd(String reqUrl,String reqParam) throws Exception {
		String resContent = null;
		
		HttpURLConnection conn = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(reqUrl+"?"+reqParam);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			conn.setDoInput(true);
			//conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);//是否自动处理重定向
			conn.setRequestMethod("GET");
			conn.setRequestProperty("connection", "Keep-Alive");
            //conn.setRequestProperty("User-Agent", "AutoyolEs_console");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//传递参数使用 &链接的表单提交方式
            conn.connect();
			//接收返回数据
			int resCode = conn.getResponseCode();
			if(resCode == 200){
				in = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				resContent = reader.readLine();
			}else{
				resContent = "error:"+resCode;
				logger.error("服务器返回码："+resCode);
			}
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		} finally{
			if(reader != null){
				reader.close();
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
			if(conn != null){
				conn.disconnect();
			}
		}
		return resContent;
	}
    
    public static InputStream getInputStreamAnd(String reqUrl) throws Exception {
		
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			URL url = new URL(reqUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			conn.setDoInput(true);
			//conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);//是否自动处理重定向
			conn.setRequestMethod("GET");
			conn.setRequestProperty("connection", "Keep-Alive");
            //conn.setRequestProperty("User-Agent", "AutoyolEs_console");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");//传递参数使用 &链接的表单提交方式
            conn.connect();
			//接收返回数据
			int resCode = conn.getResponseCode();
			if(resCode == 200){
				in = conn.getInputStream();
				return in;
			}else{
				logger.error("服务器返回码："+resCode);
			}
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		} finally{}
		return null;
	}
    
    /**
     * 以json的方式提交
     * @param reqUrl
     * @param reqContent
     * @return
     */
    public static String postByJson(String reqUrl,String reqContent){
		String resContent = null;
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);//是否自动处理重定向
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
            conn.connect();
            out = conn.getOutputStream();
            //发送请求数据
            out.write(reqContent.getBytes(ENCODEING));
            out.flush();
            out.close();
            //接收返回数据
            int resCode = conn.getResponseCode();
            if(resCode == 200){
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODEING));
                resContent = reader.readLine();
            }else{
                System.out.println("服务器返回码："+resCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(reader != null){
                try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
            if(conn != null){
                conn.disconnect();
            }
        }
        return resContent;
	}
    
    /**
     * 参数拼接
     * @param reqMap
     * @return
     */
    public static String MapToStr(Map<String,String> reqMap){
		String reqStr="";
		if(reqMap!=null && !reqMap.isEmpty()){
			for(Entry<String, String> entry:reqMap.entrySet()){
				String key=entry.getKey();
				String value=(String) entry.getValue();
				reqStr+=key+"="+value+"&";
			}
		}
		return reqStr;
	}
    
    /**
     * 参数拼接
     * @param reqMap
     * @return
     */
    public static String MapToStrUrlEncode(Map<String,String> reqMap){
		String reqStr="";
		if(reqMap!=null && !reqMap.isEmpty()){
			for(Entry<String, String> entry:reqMap.entrySet()){
				String key=entry.getKey();
				String value="";
				try {
					//urlencode
					value = URLEncoder.encode(entry.getValue().toString(),ENCODEING);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				reqStr+=key+"="+value+"&";
			}
		}
		return reqStr;
	}
    
    /**
     * 参数拼接
     * @param reqMap
     * @return
     */
    public static String mapToStrUrlEncode(Map<String,String> reqMap){
		String reqStr="";
		if(reqMap!=null && !reqMap.isEmpty()){
			for(Entry<String, String> entry:reqMap.entrySet()){
				String key=entry.getKey();
				String value="";
				try {
					//urlencode
					value = URLEncoder.encode(entry.getValue().toString(),ENCODEING);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				reqStr+=key+"="+value+"&";
			}
		}
		return reqStr;
	}


    
    /**
     * Map转化成Json
     * @return
     */
    public static String ObjectToJson(Object o){
    	ObjectMapper om=new ObjectMapper();
    	String json="[{}]";
		try {
			json = om.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return json;  
    }





}
 
