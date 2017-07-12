package com.example.sample.myrssreader.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yoshitaka.fujisawa on 2017/07/12.
 */
public class HttpGet {

    // connection timeout msec
    private static final int CONNECT_TIMEOUT_MS = 3000;
    // reading timeout msec
    private static final int READ_TIMEOUT_MS = 5000;

    private String url;
    private int status;
    private InputStream in;

    public HttpGet(String url) {
        this.url = url;
    }

    public boolean get() {
        try {
            URL url = new URL(this.url);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(CONNECT_TIMEOUT_MS);
            con.setReadTimeout(READ_TIMEOUT_MS);
            con.setInstanceFollowRedirects(true); // Redirect permission

            con.connect();

            status = con.getResponseCode();
            if (status >= 200 && status < 300) {
                in = new BufferedInputStream(con.getInputStream());
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public InputStream getResponse() {
        return in;
    }

    public int getStatus() {
        return status;
    }

}
