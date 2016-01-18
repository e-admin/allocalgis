package es.satec.localgismobile.fw.net.communications;

import iaik.me.security.CryptoException;
import iaik.me.security.MessageDigest;

import java.util.StringTokenizer;
/**
 * Soporte para  HTTP DigestAuthentication
 * RFC 2617.
 * @todo: No soporta  qop con   auth-int, ni  'auth' y 'auth-int' en  WWW-Authenticate
 * @todo: Tampoco soporta slate
 */

public class DigestAuthen
{
    String user;
    String pass;
    String method;
    String file;

    private String realm;
    private String nonce;
    private String count;
    private String cnonce;
    private String qop;
    private String opaque;
    private String algor;
    private boolean stale;
    private int ncount;

    MessageDigest MD;

    /**
     * Constructor
     *
     * @param u Usuario
     * @param p Password
     * @param m HTTP metodo
     * @param f URL path
     */

    public DigestAuthen(String u, String p, String m, String f)
    {
        user = u;
        pass = p;
        method = m.toUpperCase();
        file = f;
        // Algoritmo por defecto
        algor = "MD5";
        try {
            MD = MessageDigest.getInstance("MD5");
        } catch (CryptoException e) {
            MD=null;
        }

    }


    public void setAuthentication(String u, String p)
    {
        user = u;
        pass = p;
    }

    public void setQop(String q)
    {
        if(q.toLowerCase().compareTo("auth-info") == 0) return;
        qop = q;
    }

    public void setCNonce(String c)
    {
        cnonce = c;
    }

    public void setNonce(String n)
    {
        String c;
        if(nonce == null) ncount = 1;
        else if(n.compareTo(nonce) != 0) ncount = 1;
        else ncount++;
        nonce = n;
        c = Integer.toHexString(ncount);
        count = new String("00000000").substring(c.length()) + c;
    }

    public void setMethod(String meth)
    {
        method = meth.toUpperCase();
    }

    public void setURI(String uri)
    {
        file = uri;
    }

    public void setRealm(String r)
    {
        realm = r;
    }

    public boolean setAlgorithm(String algorithm)
    {
        if(algorithm == null) algor = "MD5";

        else if(algorithm.toUpperCase().compareTo("MD5") != 0 &&
             algorithm.toUpperCase().compareTo("MD5-SESS") != 0)
            return false;

        else algor = algorithm.toUpperCase();
        return true;
    }

    /**
     * Parses the specified WWW-Authenticate header from the last
     * request and sets the appropriate internal values.
     *
     * @param header WWW-Authenticate header to parse
     */

    public void parseHeader(String header)
    {
        StringTokenizer ST = new StringTokenizer(header.trim(), " ");
        String R = ST.nextToken();      // "Digest"

        while(ST.hasMoreTokens())
        {
            R = ST.nextToken(",").trim();

            StringTokenizer T = new StringTokenizer(R, "=");
            String S = T.nextToken().toLowerCase();

            T.nextToken("\"");

            if(S.compareTo("qop") == 0)
                qop = T.nextToken("\"");

            else if(S.compareTo("nonce") == 0)
                setNonce(T.nextToken("\""));

            else if(S.compareTo("realm") == 0)
                realm = T.nextToken("\"");

            else if(S.compareTo("opaque") == 0)
                opaque = T.nextToken("\"");

            else if(S.compareTo("algorithm") == 0)
                algor = T.nextToken("\"");
        }
    }

    /**
     * Crea un Authorization header
     *
     * @return Authorization header
     */

    public String createHeader()
    {
        String Header = "Digest ";

        if(user != null)
            Header = Header + "username=\"" + user + "\", ";

        if(realm != null)
            Header = Header + "realm=\"" + realm + "\", ";

        if(nonce != null)
            Header = Header + "nonce=\"" + nonce + "\", ";

        if(file != null)
            Header = Header + "uri=\"" + file + "\", ";

        if(qop != null)
            Header = Header + "qop=\"" + qop + "\", ";

        if(count != null)
            Header = Header + "nc=" + count + ", ";

        if(algor != null)
            Header = Header + "algorithm=\"" + algor + "\", ";
        else
            Header = Header + "algorithm=\"MD5\", ";

        if(cnonce != null)
            Header = Header + "cnonce=\"" + cnonce + "\", ";

        if(opaque != null)
            Header = Header + "opaque=\"" + opaque + "\", ";

        Header = Header + "response=\"" + Digest() + "\"";

        return Header;
    }

    /**
     * Digest creado siguiendo casi copiando el algoritmo de  RFC 2617.
     *
     * @return Digest
     */

    private String Digest()
    {
        String Digest = new String();
        String A1, A2;

        if(user.length() == 0 || pass.length() == 0 || realm.length() == 0 ||
           file.length() == 0 || nonce.length() == 0 || cnonce.length() == 0 ||
           method.length() == 0 ) return Digest;

        A1 = getA1();
        A2 = getA2();

        MD.reset();

        if(qop == null)
            Digest = A1 + ":" + nonce + ":" + A2;
        else
            Digest = A1 + ":" + nonce + ":" + count + ":" + cnonce + ":" +
                qop + ":" + A2;

        MD.update(Digest.getBytes());

        Digest = HexString.convert(MD.digest(), 16);

        return Digest;
    }

    /**
     * 'A1' valor del algoritmo de 'digest'
     *
     * @return A1
     */

    private String getA1()
    {
        String Digest;

        MD.reset();

        Digest = user + ":" + realm + ":" + pass;

        MD.update(Digest.getBytes());

        Digest = HexString.convert(MD.digest(), 16);

        if(algor.toLowerCase().compareTo("md5-sess") != 0) return Digest;

        Digest += ":" + nonce + ":" + cnonce;

        MD.update(Digest.getBytes());

        Digest = HexString.convert(MD.digest(), 16);
        return Digest;
    }

    /**
     *'A2' valor del algoritmo de 'digest'
     *
     * @return A2
     */

    private String getA2()
    {
        String Digest;

        MD.reset();

        Digest = method + ":" + file;

        MD.update(Digest.getBytes());

        Digest = HexString.convert(MD.digest(), 16);
        return Digest;
    }
}
