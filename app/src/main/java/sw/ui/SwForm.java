package sw.ui;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import sw.util.Appl;
import sw.util.clsDB;

public class SwForm extends Activity 
{
	public String TAG = "SwForm";
	public Appl app;
	public clsDB db;

    
	public void setFullScreen()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}
	
	public void show_frm(Class<?> cls)
    {
		SwForm.this.startActivity(new Intent(SwForm.this,cls));
    }

	public void l(String s)
	{
		Log.e(TAG,s);
	}
	
	public boolean FileExists(String fs)
	{
		File f=new File(fs);
		return f.exists();
	}
	
    public void msgbox(String msg) 
	{
		new AlertDialog.Builder(this).setTitle("提示").setMessage(msg).setCancelable(false)
		.setNeutralButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		app=(Appl)getApplication();
		app.frm=this;
        db=new clsDB(app.strDbPath);            
		super.onCreate(savedInstanceState);
	}
	
	
}
