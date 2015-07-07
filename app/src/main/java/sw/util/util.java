package sw.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.Settings.Secure;
import android.util.Log;

public class util 
{
	public static String TAG="Sunway_Util";
	
	public static void sunway_jjm()
	{
		//Log.e(TAG,jjm("sunway",99));
	}
	
	public static String guid()
	{
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	
	public static boolean FileExists(String s)
	{
		File f=new File(s);
		return f.exists();
	}
	
	public static void mem()
	{
		int maxMemory = ((int) Runtime.getRuntime().maxMemory())/1024/1024;  
        //应用程序已获得内存  
        long totalMemory = ((int) Runtime.getRuntime().totalMemory())/1024/1024;  
        //应用程序已获得内存中未使用内存  
        long freeMemory = ((int) Runtime.getRuntime().freeMemory())/1024/1024;  
        l("---> Max="+maxMemory+"M,Total="+totalMemory+"M,Free="+freeMemory+"M"); 
	}
	
	public static void write_file_by_bytes(byte[] b, String outputFile) {
		if(b==null)return;
		
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}

	}
	
	//把png图片数组组合成bitmap并转换为1024宽的图片并保存(李丹2015-06-04日要求修改)
	public static void write_file_by_bytes_resize_to_1024(byte[] img,String outputFile)
	{
		Bitmap b=BitmapFactory.decodeByteArray(img, 0, img.length);
		Matrix matrix = new Matrix(); 
		int width = b.getWidth(); 
		int height = b.getHeight(); 
		int newWidth=1024;
		int newHeight=(newWidth*height)/width;
		l("w="+width+",h="+height);
		float scaleWidth = ((float) newWidth) / width; 
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight); 
		Bitmap resizedBitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);
		try 
		{
			File fx = new File(outputFile);
			if (fx.exists()) fx.delete();
			FileOutputStream out = new FileOutputStream(fx);
			resizedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} 
		catch(Exception e)
		{
			l("resize.1024.error="+e.getMessage());
		}
		b.recycle();
		resizedBitmap.recycle();

	}

	public static String get_file_kmg(double size) 
	{
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
 
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
	
	//返回字符串B,C中间的部分
	public static String getPart(String a,String b,String c)
	{
		String ret="";
		int i1=a.indexOf(b);
		int i2=a.indexOf(c);
		if(i1<0)return "";
		if(i2<0)return "";
		if(i2<i1)return "";
		ret=a.substring(i1+b.length(),i2);
		return ret;
	}

	public static List<String> getPartArr(String a,String b,String c)
	{
		List<String> l=new ArrayList<String>();
		int iCnt=0;
		boolean bOver=false;
		String source=a;
		
		while(true)
		{
			iCnt++;
			if(iCnt>100)break;
			l("getpartarr idx="+iCnt+" bOver="+bOver);
			int i1=source.indexOf(b);
			int i2=source.indexOf(c);
			if(i1>0 && i2>0 && i2>i1)
			{
				String ret=source.substring(i1+b.length(),i2);
				l.add(ret);
				source=source.substring(i2+c.length());
			}
			else
			{
				break;
			}
		}
		return l;
	}

	private static void l(String string) {
		Log.e("util",string);
	}

	public static double cdbl(String s)
	{
		try
		{
			return Double.parseDouble(s);
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	public static int cint(String s) 
	{
		try
		{
			return Integer.valueOf(s);
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	public static String getInfoMD5(String s)
	{
		return getMD5(s);
	}

	public static String getMD5(String s)
	{
		// compute md5     
		MessageDigest m = null;   
		try 
		{
			m = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();   
		} 
		m.update(s.getBytes(),0,s.length());   
		
		// get md5 bytes   
		byte p_md5Data[] = m.digest();   
		// create a hex string   
		String m_szUniqueID = new String();   
		for (int i=0;i<p_md5Data.length;i++) 
		{   
		     int b =  (0xFF & p_md5Data[i]);    
		     // if it is a single digit, make sure it have 0 in front (proper padding)    
		     if (b <= 0xF) m_szUniqueID+="0";    
		     // add number to string    
		     m_szUniqueID+=Integer.toHexString(b); 
		}
		return m_szUniqueID.toUpperCase();

	}
	
	public static String getSN()
	{
		String ret="";
		
		return ret;
	}
	
	//public native static String jjm(String s1,int i1);
	//public native static String getInfoMD5(String s1);
	
	static
	{
		//System.loadLibrary("SunwayEx");
	}
}
