package sw.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class swTcpServer implements Runnable 
{
    public static final int SERVERPORT=50001;
	private static final String TAG = "tcp server";
	Thread desktopServerThread;
	public Context ctx;
	
	public void l(String s)
	{
		Log.e(TAG,s);	
	}
	
	public swTcpServer(Context _ctx)
	{
		this.ctx=_ctx;
	}
	
    @Override
    public void run() 
    {
       try
       {
           ServerSocket serverSocket = new ServerSocket(SERVERPORT);
           l("服务器：正在连接... ");
           while(true)
           {
        	  
              Socket client = serverSocket.accept();
              l("服务器：正在接收...");
              (new Thread(new swClient(client,ctx))).start();
           }
       }catch(Exception e){
           l("服务器：出错！"+e.getMessage());
       }
       
    }
    
    public void start()
    {
       desktopServerThread = new Thread(new swTcpServer(ctx));
       desktopServerThread.start();
    }
    
	public void stop()
    {
    	try
    	{
    		desktopServerThread.interrupt();
    		desktopServerThread=null;
    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    
    
}



class swClient implements Runnable
{
	private static final String TAG = "tcp server each client";
	public Socket c;
	private clsDB db;
	private Context ctx;
	

	public void l(String s)
	{
		Log.e(TAG,s);
	}

	@SuppressLint("SdCardPath")
	public swClient(Socket client,Context _ctx)
	{
		l("开始一个新连接:"+client.getInetAddress().getHostName()+":"+client.getPort());
		db=new clsDB(((Appl)_ctx.getApplicationContext()).strDbPath);
		this.c=client;		
		this.ctx=_ctx;
		

	}
	
	//返回字符串B,C中间的部分
	public String getPart(String a,String b,String c)
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
	
	public double cdbl(String s)
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

	public int cint(String s)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	
	public String execCMD(String s)
	{
		if(s==null)return"";
		if(s.length()==0)return "";
		String cmd=getPart(s,"<CMD>","</CMD>").toUpperCase();
		String p1=getPart(s,"<p1>","</p1>");
		String p2=getPart(s,"<p2>","</p2>");
		String p3=getPart(s,"<p3>","</p3>");
		String p4=getPart(s,"<p4>","</p4>");
		String p5=getPart(s,"<p5>","</p5>");

		l(cmd+"("+p1+","+p2+","+p3+","+p4+","+p5+")");
		
		if(cmd.indexOf("COUNT")>-1)return "<data>"+db.sv("select count(*) from items")+"</data>\r\n";

		if(cmd.equals("ROWS"))
		{
			return cmd_rows(cint(p1),cint(p2));
		}

		if(cmd.equals("VER"))
		{
			return "<data>"+((Appl)ctx.getApplicationContext()).version+"</data>\r\n";
		}

		if(cmd.equals("SN"))
		{
			return "<data>"+((Appl)ctx.getApplicationContext()).getSN()+"</data>\r\n";
		}

		
		return "<data>command not found</data>";
	}
	
	private String cmd_rows(int offset,int limit)
	{
		if(offset<0)offset=0;
		if(limit<1)limit=1;
		db.query("select * from items order by id limit "+limit+" offset "+offset);
		String ret="<data>\r\n";
		ret+="<RecordCount>"+db.RecordCount+"</RecordCount>\r\n";
		int iIdx=0;
		while(!db.eof())
		{
			iIdx++;
			//StuNo,StuName,ItemCode,ItemName,ItemCj
			ret+="<row"+iIdx+"/>\r\n";
			db.next();
		}
		ret+="</data>\r\n";
		return ret;
	}
	
	
	@Override
	public void run() 
	{
		try
		{
			try
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
				BufferedWriter out=new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
			     while(true)
			      {
				      String str = in.readLine();
				      
				      out.write(execCMD(str));
				      out.flush();

				      if(str.toUpperCase().indexOf("EXIT")>-1)
				      {
				    	  l("客户端要求关闭");
				    	  break;
				      }


			      }

			}
			catch(Exception e)
			{
				l("tcp.error:"+e.getMessage());
			}
			finally
			{
				db.close();
				c.close();
			}
		}
		catch(Exception e1)
		{}
	}
}
