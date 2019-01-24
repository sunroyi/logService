package com.inesa.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class RestUtil {

	public static String getRest(String url){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		// 创建默认的httpClient实例.
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpClient httpclient = createSSLClientDefault();
		// 创建httpGet
		HttpGet httpGet = new HttpGet(url);
		  
		try{
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			String json= EntityUtils.toString(entity, "UTF-8");
			int code= response.getStatusLine().getStatusCode();
			if(code==200 ||code ==204){
				return json;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		  
		return "";
	}

	public static CloseableHttpClient createSSLClientDefault(){
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
/*                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }*/

				@Override
				public boolean isTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return  HttpClients.createDefault();
	}
}