package com.pplic.android.sample.doublecasseandroidlibproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import doubleca.security.gmssl.provider.DoubleCASSE;
import doubleca.security.provider.DoubleCA;

public class MainActivity extends AppCompatActivity
{
    TextView textview;
    StringBuffer outMess = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textView1);
    }

    public void button1_click(View view)
    {
        try
        {
            new Thread() {
                @Override
                public void run() {
                    //这里写入子线程需要做的工作
                    try
                    {
                        DoubleCASSE dcsse = new DoubleCASSE();
                        Security.addProvider(new DoubleCA());
                        Security.addProvider(dcsse);
                        // 生成终端授权请求编码，用于请求授权数据使用
//                        StringBuffer buffer = new StringBuffer();
//                        dcsse.generateLicRequest(buffer);
//                        System.out.printf(buffer.toString());
                        // oppo授权数据
                        // String licData = "AXq2iC8OfW5cwAABBHE9WIecEeq8ZMNhLPRhgDpWWw6bml28PVjXlySFT+NuIid\n" + "D9qU1vSgFKsapjWaW++XY8jU2BcnbcGoOdcqziLayUj1bmABPW7wuf2tVRfynZU\n" + "rGhKTHzkc6MDBPu5C2O4q9SJpQCVJkew9weoEmJW0WAvxEiDKrvgwFnZcqPjEgg\n" + "lMM+PjMSAO3XhZEzdGsr/rb5Ef32qsNv8aZJHswJlGiGfgAx8bMg/8bk9xgIkb1\n" + "sjWGpmbkGccMw7oYzpgNvAcp6fb9V1M7m8I76NVJYvF0spIJCTORRjWKFfKfYah\n" + "XMA69PPb5LaCNWsVNQ5mtXROpwWUh7cA7PP9/KwJ8hsLQAPHHuQMwO5rCwNG0Mb\n" + "mbkMpy7lqyvYndDhS3YjX2iGJDeedf9dru6iYs2uN/PvvUFEr26/DBSwAibhjYT\n" + "vRVwVhnULb0HmXqBwBYQP/YeK/OA0BFfYR+hs2R673voym+KT6pSfWBVoY8Tr/f\n" + "SEUdnb59GXUdLP1P+N15UKnyM3H4kQQn1ha7F6ZouGEzPCF/701o9Z40atnbVn1\n" + "JtBQYzvRjhJKau/vuDlEdcvw8sdcb7Np4nS3qLvoOb1Wv7iilIzmSpflg9LmuEh\n" + "m8M6JmASA=";
                        // 模拟器授权数据
                        String licData = "AQwOmWzY14M3wAABTcPBj9G8nVSpuyR0xkfZ7vfo9hey8Hn9wHaijx7n6U7f8gQVjOsXVfbiV5tw91JZN4st8rOHvXXPGZWrUvzWsaqYQi3mlV5tOdt6crpf2on1LTx79wReyZmp16KSncENbyFgNpQVVHKWXuM3+Niy9gTkgLPh5uE7Y1f4UtZO9obTtaUPBIn7F76kBnWCSts/XjSLXG42a/YpxvFUzD+Gu3zi0Yb5WQknjahOoaNDhLNsWN3MHh7zyOuvucEl4zZ1NwTfrMDGsldNrZzfZKofC2xbaCnwBOCWbvJjvbE6tHUN+jnqN7EEMoLbYta4rxZF6+4yHnYVhqNPLGWQIe1JlMLwACdgYQsirB2zgiMRcDdiWS/066FbY/o9nnSxXm9MOgjHHzVtxW6FaTdO6fGdPJ7xmuYcbjEw1xon/V6/1OAbVIlVoLFgdD/dXl4TOL8RMEfiNHXs1eWRFk/vXc/uGRjtJaUILV4b5rtqOKmk5vJqtlyulROz/V9NP/7nO97zHlWcRqq/0cM6XO6y3C/JcU35hGMXxfHevN3VM88hlbs/yyfKv2J7eSHPh+ax6RkXujn8i53bPxEZhsxT5LLvU093NWojitT/UPoUMcBBG0FdYj5H39C+E1wP3tBm/amQT6KCYqF4QAKm4vhMhG+/ZlU9oQ==";
                        dcsse.setLicData(licData);
                        Date endTime = dcsse.getLicEndTime();
//                        String endTimeStr = endTime.toString();
                        // 密钥管理器
                        KeyStore sm2ClientKeyStore = KeyStore.getInstance("DCKS");
                        sm2ClientKeyStore.load(getAssets().open("CLIENT.dcks"), "DoubleCA".toCharArray());

                        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509", DoubleCASSE.PROVIDER_NAME);
                        kmf.init(sm2ClientKeyStore, "DoubleCA".toCharArray());

                        // 信任管理器
                        KeyStore sm2TrustKeyStore = KeyStore.getInstance("DCKS");
                        sm2TrustKeyStore.load(getAssets().open("CLIENT.dcks"), "DoubleCA".toCharArray());

                        // SSL上下文
                        SSLContext sslContext = SSLContext.getInstance("GMSSLv1.1", DoubleCASSE.PROVIDER_NAME);
                        // 实现一个X509TrustManager接口，用于绕过验证
                        X509TrustManager trustManager = new X509TrustManager()
                        {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException
                            {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException
                            {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers()
                            {
                                return null;
                            }
                        };
                        // 单向认证时
//                        sslContext.init(null, new TrustManager[]{trustManager}, null);
                        // 双向认证时
                        sslContext.init(kmf.getKeyManagers(), new TrustManager[]{trustManager}, null);
                        SSLSocketFactory sslcntFactory = (SSLSocketFactory) sslContext.getSocketFactory();
                        //
                        outMess.setLength(0);
                        SSLSocket sslSocket = (SSLSocket)sslcntFactory.createSocket();
                        // 本示例代码没有对HTTP协议进行处理，所以会有HTTP协议错误请求的返回，但国密SSL通信是成功的
                        sslSocket.connect(new InetSocketAddress("10.0.2.2", 8443), 1000);
                        sslSocket.startHandshake();
                        Certificate[] serverCerts = sslSocket.getSession().getPeerCertificates();
                        outMess.append("服务端身份信息： ");
                        for (int i = 0; i < serverCerts.length; i++)
                        {
                            outMess.append("\r\n" + ((X509Certificate)serverCerts[i]).getSubjectDN().getName());
                        }
                        String[] supported = sslSocket.getSupportedCipherSuites();
                        sslSocket.setEnabledCipherSuites(supported);
                        outMess.append("\r\n\r\n启用的加密套件： " + Arrays.asList(supported));
                        // 发送
                        InputStream in = sslSocket.getInputStream();// 输入流
                        OutputStream out = sslSocket.getOutputStream();
                        out.write("大宝CA 国密数字证书\r\n".getBytes());
                        out.flush();
                        out.write("Android 国密SSL解决方案\r\n".getBytes());
                        out.write(new byte[]{0});
                        out.flush();
                        byte[] buffer = new byte[1024];
                        int a = in.read(buffer);
                        // 循环检查是否有消息到达
                        outMess.append("\r\n\r\n来自于服务端信息： \r\n");
                        while (a > 0)
                        {
                            outMess.append(new String(buffer).trim());
                            if (buffer[a - 1] != 0)
                            {
                                buffer = new byte[1024];
                                a = in.read(buffer);
                            }
                            else
                            {
                                a = 0;
                            }
                        }
                        sslSocket.close();
                        textview.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                textview.setText(outMess.toString());
                            }
                        });
                    }
                    catch(Exception ex)
                    {
                        outMess.setLength(0);
                        outMess.append(ex.getLocalizedMessage());
                        textview.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                textview.setText(outMess.toString());
                            }
                        });
                    }
                }
            }.start();
        }
        catch(Exception ex)
        {
            outMess.setLength(0);
            outMess.append(ex.getLocalizedMessage());
            textview.post(new Runnable()
            {
                @Override
                public void run()
                {
                    textview.setText(outMess.toString());
                }
            });
        }
    }
}
