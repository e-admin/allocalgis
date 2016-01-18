package com.geopista.app.catastro.intercambio.importacion.xml;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.geopista.app.catastro.intercambio.importacion.xml.handlers.SituacionXMLHandler;

public class TestEntrada
{
    ArrayList instancias = new ArrayList ();
    String cad = "<elemf>  <fincacef>   <fincaf>    <idfcat> <pc>  <pc1>54003A0</pc1>  <pc2>0100004</pc2> </pc> <locat>  <cd>54</cd>  <cmc>003</cmc> </locat>    </idfcat>    <dt> <loine>  <cp>36</cp>  <cm>003</cm> </loine> <cmc>003</cmc> <np>PONTEVEDRA</np> <nm>BAIONA</nm> <locs>  <lous>   <lourb>    <dir>     <cv>00004</cv>     <tv>LG</tv>     <nv>CRUCEIRO-BAHINA</nv>     <pnp>0002</pnp>    </dir>    <dp>36308</dp>    <dm>02</dm>   </lourb>  </lous> </locs>    </dt>    <dff> <ssf>  <ss>0000100</ss>  <sct>0000200</sct>  <ssr>0000200</ssr>  <sbr>0000100</sbr>  <sc>0000100</sc> </ssf>    </dff>   </fincaf>   <movf>    <tmov>F</tmov>   </movf>  </fincacef>  <lsuelof>   <suelocf>    <suelof> <idsucat>  <rsu>   <pc1>54003A0</pc1>   <pc2>0100004</pc2>   <subp>0001</subp>  </rsu>  <locat>   <cd>54</cd>   <cmc>003</cmc>  </locat> </idsucat> <dfsu>  <fach>   <tipof>FA</tipof>  </fach>  <sup>0000141</sup> </dfsu> <dvs>  <fpon>   <cvpv>00034</cvpv>   <ctvpv>003</ctvpv>  </fpon>  <zu>14</zu>  <ctv>2</ctv>  <ccvs>   <nfach>3</nfach>   <ilf>N</ilf>   <ifir>N</ifir>   <ide>N</ide>   <isdm>N</isdm>   <iit>N</iit>   <ivpp>N</ivpp>  </ccvs>  <cccsc>   <vccad>1,00</vccad>   <iadf>N</iadf>   <vcccs>1,00</vcccs>   <icce>N</icce>   <iccunl>N</iccunl>  </cccsc> </dvs>    </suelof>    <movf> <tmov>F</tmov>    </movf>   </suelocf>  </lsuelof>  <lucf>   <uccf>    <ucf> <iducat>  <ruc>   <pc1>54003A0</pc1>   <pc2>0100004</pc2>   <cuc>0001</cuc>  </ruc>  <cn>UR</cn>  <locat>   <cd>03</cd>   <cmc>003</cmc>  </locat> </iducat> <dfuc>  <ac>1996</ac>  <iacons>+</iacons>  <so>200</so> </dfuc> <dvuc>  <ccvsuc>   <nf>2</nf>   <ilf>S</ilf>  </ccvsuc>  <ccvcuc>   <ccec>1</ccec>  </ccvcuc>  <ccvscuc>   <iccdf>S</iccdf>   <vcccs>0,57</vcccs>   <iccsce>N</iccsce>   <iccunl>N</iccunl>  </ccvscuc> </dvuc>    </ucf>    <movf> <tmov>F</tmov>    </movf>   </uccf>  </lucf>  <lbicenf>   <bicf>    <bi> <idcat>  <cn>UR</cn>  <rc>   <pc1>54003A0</pc1>   <pc2>0100004</pc2>   <car>0001</car>   <cc1>F</cc1>   <cc2>G</cc2>  </rc>  <locat>   <cd>54</cd>   <cmc>003</cmc>  </locat> </idcat> <dt>  <loine>   <cp>36</cp>   <cm>003</cm>  </loine>  <cmc>003</cmc>  <np>PONTEVEDRA</np>  <nm>BAIONA</nm>  <locs>   <lors>    <lorus>     <cma>202</cma>     <czc>02</czc>     <cpp>  <cpo>110</cpo>  <cpa>20204</cpa>     </cpp>     <npa>P</npa>     <cpaj>20205</cpaj>    </lorus>   </lors>  </locs> </dt>    </bi>    <mov> <tmov>F</tmov>    </mov>    <lsf> <ltit>  <tit>   <der>    <cdr>PR</cdr>    <pct>050,00</pct>   </der>   <ord>1</ord>   <idpa>    <nif>88888888J</nif>    <nom>ESPANNIOL ESPANNIOL FRANCISCO</nom>   </idpa>   <df>    <loine>     <cp>36</cp>     <cm>003</cm>    </loine>    <cmc>202</cmc>    <np>PONTEVEDRA</np>    <nm>PONTEVEDRA</nm>    <dir>     <tv>LG</tv>     <nv>BAHINA CRUCERO</nv>     <pnp>0050</pnp>    </dir>    <pos>     <dp>36300</dp>    </pos>   </df>  </tit>  <tit>   <der>    <cdr>PR</cdr>    <pct>050,00</pct>   </der>   <ord>1</ord>   <idpa>    <nif>12345678N</nif>    <nom>ESPANNIOL ESPANNIOL PEDRO</nom>   </idpa>   <df>    <loine>     <cp>36</cp>     <cm>003</cm>    </loine>    <cmc>202</cmc>    <np>PONTEVEDRA</np>    <nm>PONTEVEDRA</nm>    <dir>     <tv>LG</tv>     <nv>BAHINA CRUCERO</nv>     <pnp>0050</pnp>    </dir>    <loint>     <pu>06</pu>    </loint>    <pos>     <dp>36300</dp>    </pos>   </df>  </tit> </ltit>    </lsf>   </bicf>  </lbicenf>  <lconsf>   <conscf>    <consf> <idconscat>  <rcons>   <pc1>54003A0</pc1>   <pc2>0100004</pc2>   <noec>0001</noec>  </rcons>  <locat>   <cd>03</cd>   <cmc>003</cmc>  </locat> </idconscat> <dfcons>  <cuc>0001</cuc>  <car>0001</car>  <cdes>112</cdes>  <stl>0000001</stl> </dfcons> <dvcons>  <tip>2</tip>  <usop>1</usop>  <cat>3</cat>  <ctv>1</ctv>  <ccvscc>   <vccad>0,79</vccad>   <iacli>N</iacli>  </ccvscc> </dvcons>    </consf>    <movf> <tmov>F</tmov>    </movf>   </conscf>  </lconsf>  <lreparf>   <reparcf>    <reparf> <idrepcat>  <pc>   <pc1>54003A0</pc1>   <pc2>0100004</pc2>  </pc>  <ner>   <noec>0001</noec>  </ner>  <locat>   <cd>03</cd>   <cmc>003</cmc>  </locat> </idrepcat>    </reparf>    <movf> <tmov>F</tmov>    </movf>   </reparcf>  </lreparf> </elemf>";
    
    
    
    public TestEntrada()
    {
    }
    
    public void procesarFichero ()
    {
        try
        {
            XMLReader parser = new SAXParser();
            
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setFeature("http://xml.org/sax/features/validation", true);
            
            parser.setContentHandler(new SituacionXMLHandler(parser, instancias, null));
            //parser.parse("D:/Trabajos/2006/GeoCatastro/Descargados de Catastro/fin/catastro-finentrada-elemf.xml");
            parser.parse(new InputSource(new ByteArrayInputStream(cad.getBytes())));            
        }
        catch (Exception e)
        {
            System.out.println ("Error al procesar el fichero de catastro: " 
                    + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args)
    {
        TestEntrada test = new TestEntrada();
        
        test.procesarFichero();
        System.out.println("");
    }
}
