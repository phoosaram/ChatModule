package test.demo.com.chatmodule.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by phoosaram on 3/7/2017.
 */

public class NetworkRequest<T> extends Request<T> {
    private Response.Listener successListener;
    private int socketTimeout = 50000;
    private Map<String, String> params;
    private Map<String, String> headers;
    private Class<T> responseTypeClass;
    private Gson gson = new Gson();

    /**
     * @param method          for post 1 and for get 0
     * @param url             api url
     * @param errorListener   error listener reference
     * @param successListener successListener reference
     */
    public NetworkRequest(int method, String url, Response.ErrorListener errorListener, Response.Listener successListener, Map<String, String> params, Map<String, String> headers, Class<T> responseTypeClass) {
        super(method, url, errorListener);
        this.successListener = successListener;
        this.params = params;
        this.headers = headers;
        this.responseTypeClass = responseTypeClass;
        setRetryPolicy();
    }

    private void setRetryPolicy() {
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        super.setRetryPolicy(policy);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, responseTypeClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (successListener != null) {
            successListener.onResponse(response);
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }
}
