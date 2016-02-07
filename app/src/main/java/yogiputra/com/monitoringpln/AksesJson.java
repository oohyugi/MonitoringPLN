package yogiputra.com.monitoringpln;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by starks on 01/05/15.
 */
public class AksesJson extends Application {
    private RequestQueue mRequestQueue;
    private static AksesJson mInstance;
    private ImageLoader mImageLoader;

    public void onCreate(){
        super.onCreate();
        mInstance=this;

    }
    public static synchronized AksesJson getIntance(){
        return mInstance;
    }
    public RequestQueue getReqQueue(){
        if (mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(getApplicationContext());

        }
        return mRequestQueue;

    }
    public <T>void addToReqQueue(Request<T> req,String tag){
        getReqQueue().add(req);


    }
    public <T>void addToReqQueue(Request<T> req){
        getReqQueue().add(req);


    }


    public void cancelPendingReq(Objectnya tag){
        if (mRequestQueue !=null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
