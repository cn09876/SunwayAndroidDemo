package sww;

import com.example.cn09876.swwext.R;

/**
 * Created by cn09876 on 15/7/6.
 */
public class util
{
    static
    {
        System.loadLibrary("swwext");
    }

    public native  static  int get_enc();

    public static String get_util_info()
    {
        return "123";
    }
}
