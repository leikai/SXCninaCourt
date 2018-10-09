package org.sxchinacourt.util.network;

import android.annotation.SuppressLint;
import android.content.Context;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by baggio on 2017/2/22.
 */

public class SSLRequest implements X509TrustManager {
    private static final String KEY_STORE_TYPE_BKS = "bks";//证书类型 固定值
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型 固定值

    private static final String KEY_STORE_CLIENT_PATH = "client.p12";//客户端要给服务器端认证的证书
    private static final String KEY_STORE_TRUST_PATH = "client.truststore";//客户端验证服务器端的证书库
    private static final String KEY_STORE_PASSWORD = "courtoa";// 客户端证书密码
    private static final String KEY_STORE_TRUST_PASSWORD = "courtoa";//客户端证书库密码
    private static TrustManager[] trustManagers;
    private static final X509Certificate[] _AcceptedIssuers = new
            X509Certificate[]{};

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String
            authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String
            authType) throws CertificateException {
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }

    @SuppressLint("TrulyRandom")
    public static void allowAllSSL(String url, Context mContext) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }

        });


//        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            // uwca.crt 打包在 asset 中，该证书可以从https://itconnect.uw.edu/security/securing-computer/install/safari-os-x/下载
//            InputStream caInput = new BufferedInputStream(mContext.getAssets().open("client.cer"));
//            Certificate ca;
//            PublicKey publicKey;
//            ca = cf.generateCertificate(caInput);
////            publicKey = ca.getPublicKey();
//
//
//            // Create a KeyStore containing our trusted CAs
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(caInput, "courtoa".toCharArray());
////            keyStore.setCertificateEntry("client", ca);
//
//            // Create a TrustManager that trusts the CAs in our KeyStore
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//            // Create an SSLContext that uses our TrustManager
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, tmf.getTrustManagers(), new SecureRandom());
//            caInput.close();
//
//            KeyStore ks = KeyStore.getInstance("PKCS12");
//            InputStream fis = mContext.getAssets().open("client.p12");
//            char[] pwd = "courtoa".toCharArray();
//            ks.load(fis, pwd);
//
//            tmf.init(keyStore);
//
//            // Create an SSLContext that uses our TrustManager
//            context.init(null, tmf.getTrustManagers(), new SecureRandom());
//            fis.close();
////            Enumeration enume = ks.aliases();
////            String keyAlias = null;
////            if (enume.hasMoreElements()) {
////                keyAlias = (String) enume.nextElement();
////            }
//
//            final HttpsURLConnection urlConnection =
//                    (HttpsURLConnection) new URL(url).openConnection();
//            urlConnection.setSSLSocketFactory(context.getSocketFactory());
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    InputStream in = null;
//                    try {
//                        in = urlConnection.getInputStream();
//                        copyInputStreamToOutputStream(in, System.out);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
        SSLContext context = null;
        InputStream inputStream1 = null;
        InputStream inputStream2 = null;
        InputStream inputStream3 = null;
        List<InputStream> inputStreamList = new ArrayList<>();
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new SSLRequest()};
        }

        try {
            context = SSLContext.getInstance("TLS");
                context.init(null, trustManagers, new SecureRandom());
                try {
                //自签名证书client.cer放在assets文件夹下
                inputStream1 = mContext.getAssets().open("client.cer");
//                inputStream2 = mContext.getAssets().open("client.p12");
//                inputStream3 = mContext.getAssets().open("client.truststore");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SSLSocketFactory socketFactory = SSLUtil.getSSLSocketFactory(inputStream1);
            if (socketFactory != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
            }

            /*
             * 使用此方法并配合HttpsURLConnection connection设置（见下面）则可以越过证书验证
             */
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        /*
         * 使用此方法则可以越过证书验证
         */
        HttpsURLConnection connection;
        try {
            connection = (HttpsURLConnection) new URL(url).openConnection();
            ((HttpsURLConnection) connection).setHostnameVerifier(new AllowAllHostnameVerifier());

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
        }catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


//        try {
//            // 服务器端需要验证的客户端证书
//            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
//            // 客户端信任的服务器端证书
//            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
//
//            InputStream ksIn = mContext.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);
//            InputStream tsIn = mContext.getResources().getAssets().open(KEY_STORE_TRUST_PATH);
//            try {
//                keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
//                trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    ksIn.close();
//                } catch (Exception ignore) {
//                }
//                try {
//                    tsIn.close();
//                } catch (Exception ignore) {
//                }
//            }
//            SSLSocketFactory sslSocketFactory
////                    = new SSLSocketFactory(keyStore, KEY_STORE_PASSWORD,
////                    trustStore);
//            if (sslSocketFactory != null) {
//                HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void copyInputStreamToOutputStream(InputStream in, PrintStream out) throws
            IOException {
        byte[] buffer = new byte[1024];
        int c = 0;
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
    }
}
