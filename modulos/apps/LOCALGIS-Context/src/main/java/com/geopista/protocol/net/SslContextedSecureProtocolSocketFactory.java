/**
 * SslContextedSecureProtocolSocketFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

public class SslContextedSecureProtocolSocketFactory  implements SecureProtocolSocketFactory
{
  private SSLContext sslContext;
  private boolean verifyHostname = true;

  public SslContextedSecureProtocolSocketFactory(SSLContext sslContext, boolean verifyHostname)
  {
    this.sslContext = sslContext;
    this.verifyHostname = verifyHostname;
  }

  public SslContextedSecureProtocolSocketFactory(SSLContext sslContext)
  {
    this(sslContext, true);
  }

  public SslContextedSecureProtocolSocketFactory(boolean verifyHostname)
  {
    this(null, verifyHostname);
  }

  public SslContextedSecureProtocolSocketFactory()
  {
    this(null, true);
  }

  public synchronized void setHostnameVerification(boolean verifyHostname)
  {
    this.verifyHostname = verifyHostname;
  }

  public synchronized boolean getHostnameVerification()
  {
    return this.verifyHostname;
  }

  public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
    throws IOException, UnknownHostException
  {
    SSLSocketFactory sf = getSslSocketFactory();
    SSLSocket sslSocket = (SSLSocket)sf.createSocket(host, port, clientHost, clientPort);

    verifyHostname(sslSocket);

    return sslSocket;
  }

  public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
    throws IOException, UnknownHostException, ConnectTimeoutException
  {
    if (params == null) {
      throw new IllegalArgumentException("Parameters may not be null");
    }
    int timeout = params.getConnectionTimeout();
    Socket socket = null;

    SocketFactory socketfactory = getSslSocketFactory();
    if (timeout == 0) {
      socket = socketfactory.createSocket(host, port, localAddress, localPort);
    }
    else {
      socket = socketfactory.createSocket();
      SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);

      SocketAddress remoteaddr = new InetSocketAddress(host, port);
      socket.bind(localaddr);
      socket.connect(remoteaddr, timeout);
    }
    verifyHostname((SSLSocket)socket);
    return socket;
  }

  public Socket createSocket(String host, int port)
    throws IOException, UnknownHostException
  {
    SSLSocketFactory sf = getSslSocketFactory();
    SSLSocket sslSocket = (SSLSocket)sf.createSocket(host, port);
    verifyHostname(sslSocket);

    return sslSocket;
  }

  public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
    throws IOException, UnknownHostException
  {
    SSLSocketFactory sf = getSslSocketFactory();
    SSLSocket sslSocket = (SSLSocket)sf.createSocket(socket, host, port, autoClose);

    verifyHostname(sslSocket);

    return sslSocket;
  }

  private void verifyHostname(SSLSocket socket)
    throws SSLPeerUnverifiedException, UnknownHostException
  {
    synchronized (this) {
      if (!this.verifyHostname) {
        return;
      }
    }
    SSLSession session = socket.getSession();
    String hostname = session.getPeerHost();
    try {
      InetAddress.getByName(hostname);
    } catch (UnknownHostException uhe) {
      throw new UnknownHostException("Could not resolve SSL sessions server hostname: " + hostname);
    }

    X509Certificate[] certs = (X509Certificate[])session.getPeerCertificates();

    if ((certs == null) || (certs.length == 0)) {
      throw new SSLPeerUnverifiedException("No server certificates found!");
    }

    X500Principal subjectDN = certs[0].getSubjectX500Principal();

    List<String> cns = getCNs(subjectDN);
    boolean foundHostName = false;
    for (String cn : cns) {
      if (hostname.equalsIgnoreCase(cn)) {
        foundHostName = true;
        break;
      }
    }
    if (!foundHostName)
      throw new SSLPeerUnverifiedException("HTTPS hostname invalid: expected '" + hostname + "', received '" + cns + "'");
  }

  private List<String> getCNs(X500Principal subjectDN)
  {
    List cns = new ArrayList();

    StringTokenizer st = new StringTokenizer(subjectDN.getName(), ",");
    while (st.hasMoreTokens()) {
      String cnField = st.nextToken();
      if (cnField.startsWith("CN=")) {
        cns.add(cnField.substring(3));
      }
    }
    return cns;
  }

  protected SSLSocketFactory getSslSocketFactory()
  {
    SSLSocketFactory sslSocketFactory = null;
    synchronized (this) {
      if (this.sslContext != null) {
        sslSocketFactory = this.sslContext.getSocketFactory();
      }
    }
    if (sslSocketFactory == null) {
      sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    }
    return sslSocketFactory;
  }

  public synchronized void setSSLContext(SSLContext sslContext)
  {
    this.sslContext = sslContext;
  }
}
