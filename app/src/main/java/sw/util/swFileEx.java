package sw.util;


import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import android.annotation.SuppressLint;
import android.content.ContentProvider;  
import android.content.ContentValues;  
import android.content.res.AssetFileDescriptor;  
import android.content.res.AssetManager;  
import android.database.Cursor;  
import android.net.Uri;  
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.util.Log;


public class swFileEx extends ContentProvider 
{
	private static final String URI_PREFIX = "content://sw.sf";  
	private static final String TAG = "sw.swFileEx";
	
	private void l(String s)
	{
		Log.e(TAG,s);
	}
	
    public static String constructUri(String url) 
    {  
        Uri uri = Uri.parse(url);
        return uri.isAbsolute() ? url : URI_PREFIX + url;
    }
    
	@SuppressLint("NewApi") 
	public ParcelFileDescriptor openFile(Uri uri, String mode)	throws FileNotFoundException {
		String path = uri.getPath().substring(1); 
		//l("openFile "+path+" mode="+mode);
		//l("uri "+uri);

		ParcelFileDescriptor[] pipe = null;
		
		try 
		{
			pipe=ParcelFileDescriptor.createPipe();
			final ParcelFileDescriptor write = pipe[1];  
			OutputStream outs=new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1]);
			new RequestHandler(path,outs).start();
		} 
		catch (Exception e1) 
		{
			l("create pipe error "+e1.getMessage());
		}
		
		return pipe[0];
	}
	
    @Override  
    public boolean onCreate() 
    {  
    	return true;  
    }  
  
    @Override  
    public int delete(Uri uri, String s, String[] as) 
    {  
    	l("delete uri="+uri+" s="+s+" as="+as);
    	return 1;
    }  
  
    @Override  
    public String getType(Uri uri) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    }  
  
    @Override  
    public Uri insert(Uri uri, ContentValues contentvalues) {  
        throw new UnsupportedOperationException("Not supported by this provider ");  
    }  
  
    @Override  
    public Cursor query(Uri uri, String[] as, String s, String[] as1, String s1) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    }  
  
    @Override  
    public int update(Uri uri, ContentValues contentvalues, String s, String[] as) {  
        throw new UnsupportedOperationException("Not supported by this provider");  
    }  
}

class RequestHandler extends Thread {  

	private static final String TAG = "sw.swFileEx.req.handler ";
	String kk;
	OutputStream mOutput;  
    
    public RequestHandler(String k, OutputStream out) 
    {  
    	kk=k;
    	mOutput = out;  
    }  
  
    @Override  
    public void run() {  
        super.run();  
        //try 
        {  
        	clsDB db=new clsDB("/sdcard/picex.db");
        	String sql="";
        	kk=URLDecoder.decode(kk);
        	if(kk.indexOf("sv/")>=0)sql=kk.replace("sv/", "");        	
        	Log.e(TAG,"SQL="+sql);
        	byte[] x=db.sv_blob(sql);        	
        	if(x!=null)
        	{
        		l("pic len="+x.length);
        		try
        		{
        			mOutput.write(x,0,x.length);
        			mOutput.flush();
        			mOutput.close();
        		}catch(Exception e){}
        		l("send ok");
        	}
        	else
        	{
        		l("no pic      ");
        		try
        		{
        			mOutput.close();
        		}catch(Exception e){}
        	}
        	
        } 
        /*
        catch (Exception e) 
        {  
            l("Failed to handle request: "+e.getMessage());  
        } 
        */
    }

	private void l(String string) {
		Log.e(TAG,string);
		
	}  
  
}
