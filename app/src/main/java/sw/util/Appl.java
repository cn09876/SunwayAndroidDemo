package sw.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Appl extends Application
{
	public clsDB db;
	public Handler hDownload=new Handler();
	public final float version=(float) 6.01;
	private static final String TAG = Appl.class.getSimpleName();
	public Context frm;
	public String sn="";
	public String broadcast_filter=Appl.class.getSimpleName();
	public String path="";
	public String strDbPath="";
	public swTcpServer objTcp;
	public static String strBaseDir="/sdcard/_ry1/";


	@Override
	public void onCreate() 
	{
		path="/mnt/extsd/";
        Log.e(TAG,"path+"+path);

        objTcp=new swTcpServer(this);
        objTcp.start();

        File f=new File(strBaseDir);
        f.mkdir();
                
		super.onCreate();
	}

	public void ini(String k,String v)
	{
		SharedPreferences mySharedPreferences= getSharedPreferences("sun", Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		editor.putString(k, v); 
		editor.commit(); 
	}
	
	public String ini(String k)
	{
		SharedPreferences mySharedPreferences= getSharedPreferences("sun", Activity.MODE_PRIVATE); 
		return mySharedPreferences.getString(k,"");
	}
	
	//生成硬件唯一序列号
	public String getSN()
	{
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		String m_szImei = TelephonyMgr.getDeviceId();

		String m_szAndroidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
		
		String m_szDevIDShort = "086" + 
		Build.BOARD + Build.BRAND+ Build.CPU_ABI+ Build.DEVICE + Build.DISPLAY + Build.HOST + Build.ID + 
		Build.MANUFACTURER + Build.MODEL + Build.PRODUCT + Build.TAGS + Build.TYPE + Build.USER; //13 digits
		
		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE); 
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter      
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      
		String m_szBTMAC = "";
		try{m_BluetoothAdapter.getAddress();}catch(Exception e){}
		
		Log.w(TAG,"m_szImei="+m_szImei);
		Log.w(TAG,"m_szDevIDShort="+m_szDevIDShort);
		Log.w(TAG,"m_szAndroidID="+m_szAndroidID);
		Log.w(TAG,"m_szAndroidID="+m_szWLANMAC);
		Log.w(TAG,"m_szBTMAC="+m_szBTMAC);
		
		String m_szLongID = m_szImei + m_szDevIDShort  + m_szAndroidID+ m_szWLANMAC + m_szBTMAC;  
		String ret=util.getInfoMD5(m_szLongID).toUpperCase();
		Log.w(TAG,"jni md5="+ret);
		return ret;
	}

	public void l(String s)
	{
		Log.e(TAG,s);
	}
	
	public boolean downloadFile(String url,String strDownloadTo)
	{		
		l("尝试下载文件:"+url);
		l("To:"+strDownloadTo);
		try
		{
			URL Url = new URL(url);
			URLConnection conn = Url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			int fileSize = conn.getContentLength();// 根据响应获取文件大小
			if (fileSize <= 0) return false;
			if (is == null)  return false;			
			@SuppressWarnings("resource")
			FileOutputStream FOS = new FileOutputStream(strDownloadTo); // 创建写入文件内存流，
			byte buf[] = new byte[1024];
			int numread;
			
			while ((numread = is.read(buf)) != -1) 
			{
				FOS.write(buf, 0, numread);
			}
			try {is.close();} catch (Exception ex) {}
		}
		catch(Exception e)
		{
			l("下载文件失败: "+e.getMessage());
			return false;
		}
		return true;
	}
	
	public String sendHttpPost(String url) 
	{
		try 
		{
			l("http.send="+url);
			HttpGet request = new HttpGet(url); // 创建Http请求
			l("uri="+request.getURI().toString());
			HttpResponse httpResponse = new DefaultHttpClient().execute(request); // 发送请求并获取反馈
			// 解析返回的内容
			if (httpResponse.getStatusLine().getStatusCode() != 404) 
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				l(result);
				return result;
			}
		}
		catch (Exception e) 
		{
			l("sendhttppost.error="+e.getMessage());
			return "";
		}
		return "";
	}

	public String sendHttpPost(String url,List<BasicNameValuePair> lst) 
	{
		try {
			HttpPost request = new HttpPost(url); // 创建Http请求
			request.setEntity(new UrlEncodedFormEntity(lst, "GBK")); // 设置参数的编码
			HttpResponse httpResponse = new DefaultHttpClient().execute(request); // 发送请求并获取反馈
			// 解析返回的内容
			if (httpResponse.getStatusLine().getStatusCode() != 404) 
			{
				String result = EntityUtils.toString(httpResponse.getEntity());
				l(result);
				return result;
			}
		}
		catch (Exception e) 
		{
			l("http.post.error= "+e.getMessage());
			return "";
		}
		return "";
	}

	public void mkdirs(String p)
	{
		try
		{
			l("检查目录："+p);
			if(p.length()-p.lastIndexOf(".")<5)
			{
				p=p.substring(0, p.lastIndexOf("/"));
				l("它是一个文件，取目录:"+p);
			}
			File f=new File(p);
			if(!f.exists())f.mkdirs();
		}
		catch(Exception e)
		{
			l("无法创建目录: "+e.getMessage());	
		}
	}
	
	public void msgbox(String msg)
	{
		new AlertDialog.Builder(this).setTitle("提示").setMessage(msg)
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

}
