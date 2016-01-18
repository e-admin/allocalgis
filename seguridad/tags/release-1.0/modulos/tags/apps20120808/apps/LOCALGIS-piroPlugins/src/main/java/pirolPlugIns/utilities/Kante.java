/*
 * Created on 05.01.2005
 *
 * CVS header information:
 *  $RCSfile: Kante.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Kante.java,v $s
 */
package pirolPlugIns.utilities;

import java.util.Vector;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jts.geom.Envelope;


/**
 * 
 * Class that describes a single line, specified by it's starting and
 * end point. If offers methods e.g. to find intersection points with other
 * Kante objects or to determine on which side of the Kante object a given
 * punkt object resides.
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 * @see pirolPlugIns.utilities.punkt
 * @see pirolPlugIns.utilities.Data2LayerConnector
 */
public class Kante {
    
    public static Kante KANTE_X0Y0ToX0Y1 = new Kante( punkt.NULLPUNKT, new punkt(new double[]{0,1}), true, true);
    public static Kante KANTE_X0Y0ToX1Y0 = new Kante( punkt.NULLPUNKT, new punkt(new double[]{1,0}), true, true);
    
    protected punkt anfang = null, ende = null;
    protected int punktIndexA = -1, punktIndexB = -1;
    protected boolean anfangUnbegrenzt = true, endeUnbegrenzt = true;
    protected boolean gueltig = true;
    
    protected static double infinityFaktor = Math.pow(10,30);
    
    protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
    public Kante(punkt anfang, punkt ende, boolean anfangUnbegrenzt, boolean endeUnbegrenzt){
        this.anfang = anfang;
        this.ende = ende;
        
        this.anfangUnbegrenzt = anfangUnbegrenzt;
        this.endeUnbegrenzt = endeUnbegrenzt;
    }
    
    public Kante(punkt anfang, punkt ende){
        this.anfang = anfang;
        this.ende = ende;
        
        this.anfangUnbegrenzt = false;
        this.endeUnbegrenzt = false;
    }
    
    public Kante(punkt anfang, double steigung, double laenge){
        this.anfang = anfang;
        
        double alpha = Math.atan(steigung);
        
        double dx = Math.cos(alpha) * laenge;
        double dy = Math.sin(alpha) * laenge;
        
        try {
            this.ende = new punkt( new double[]{anfang.getX()+dx, anfang.getY()+dy});
        } catch (Exception e) {
            // this should not happen!
            this.setGueltig(false);
            e.printStackTrace();
        }
        
        this.anfangUnbegrenzt = false;
        this.endeUnbegrenzt = false;
    }
    
    public static Kante kreiereKanteDurchPunktInnerhalbBegrenzung( punkt p, double steigung, Envelope begrenzung ) throws Exception {
        punkt p1 = null, p2 = null;
        if (steigung==0){
            p1 = new punkt(new double[]{begrenzung.getMinX(), p.getY()} );
            p2 = new punkt(new double[]{begrenzung.getMaxX(), p.getY()} );
        } else if (steigung==Double.POSITIVE_INFINITY) {
            p1 = new punkt(new double[]{p.getX(), begrenzung.getMinY()} );
            p2 = new punkt(new double[]{p.getX(), begrenzung.getMaxY()} );
        } else if (steigung==Double.NEGATIVE_INFINITY) {
            p2 = new punkt(new double[]{p.getX(), begrenzung.getMinY()} );
            p1 = new punkt(new double[]{p.getX(), begrenzung.getMaxY()} );
        } else {
            double minX = begrenzung.getMinX();
            double maxX = begrenzung.getMaxX();
            double minY = begrenzung.getMinY();
            double maxY = begrenzung.getMaxY();
            
            punkt upperLeft = new punkt(new double[]{minX, maxY});
            punkt lowerLeft = new punkt(new double[]{minX, minY});
            punkt upperRight = new punkt(new double[]{maxX, maxY});
            punkt lowerRight = new punkt(new double[]{maxX, minY});
            
            Kante top = new Kante(upperLeft,upperRight);
            Kante right = new Kante(upperRight,lowerRight);
            Kante bottom = new Kante(lowerRight,lowerLeft);
            Kante left = new Kante(lowerLeft,upperLeft);
            
            Kante toBeCut = new Kante(p, steigung, 5.0);
            toBeCut.setAnfangUnbegrenzt(true);
            toBeCut.setEndeUnbegrenzt(true);
            
            Kante[] kanten = new Kante[]{top, right, bottom, left};
            Vector schnittPunkte = new Vector();
            punkt sp;
            
            for (int i=0; i<kanten.length; i++){
                sp = toBeCut.getSchnittpunkt(kanten[i]);
                
                if (sp != null){
                    schnittPunkte.add(sp);
                }
            }
            
            if (schnittPunkte.size() == 2){
                // this should happen!!
                punkt tmp1 = (punkt)schnittPunkte.get(0);
                punkt tmp2 = (punkt)schnittPunkte.get(1);
                
                if (steigung > 0){
                    if (tmp1.getY()<tmp2.getY()){
                        p1 = tmp1;
                        p2 = tmp2;
                    } else {
                        p1 = tmp2;
                        p2 = tmp1;
                    }
                } else {
                    if (tmp1.getY()>tmp2.getY()){
                        p1 = tmp1;
                        p2 = tmp2;
                    } else {
                        p1 = tmp2;
                        p2 = tmp1;
                    }
                }
            }
        }
        
        return new Kante(p1, p2);
    }
    
    public boolean isParallelZu(Kante k) throws Exception{
        punkt sp = this.getSchnittpunkt(k);
        
        if (sp==null){
            return true;
        } 
        double thisLaenge = this.getAnfang().distanceTo(this.getEnde());
        double andereLaenge = k.getAnfang().distanceTo(k.getEnde());
        double laenge = (thisLaenge+andereLaenge) / 2.0;
        
        double infiniteDistance = laenge * infinityFaktor;
        
        if (this.getAnfang().distanceTo(sp) > infiniteDistance && this.getEnde().distanceTo(sp) > infiniteDistance){
            // as good as parallel
            return true;
        }

        return false;
    }
    
    public double getSteigung() throws Exception {
        double x1 = this.getAnfang().getX();
        double x2 = this.getEnde().getX();
        double y1 = this.getAnfang().getY();
        double y2 = this.getEnde().getY();
        
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        
        if (deltaX!=0){
            return deltaY/deltaX;
        } 
        if (deltaY>0)
            return Double.POSITIVE_INFINITY;
        else if (deltaY<0)
            return Double.NEGATIVE_INFINITY;
        else
            throw new Exception("wasn't able to calculate the increase of line (starting point == end point)");
        
    }
    
    public static punkt getSchnittpunkt( Kante k1, Kante k2 ) throws Exception {
        punkt a = k1.getAnfang();
        punkt ab = new punkt(new double[]{k1.getEnde().getX()-a.getX(), k1.getEnde().getY()-a.getY()});
        punkt c = k2.getAnfang();
        punkt cd = new punkt(new double[]{k2.getEnde().getX()-c.getX(), k2.getEnde().getY()-c.getY()});
        
        if (a.distanceTo(k1.getEnde())==0){
            throw new Exception("zero length!");
        }
        
        double m = 0.0;
        
        if (ab.getX()==0 && cd.getX()!=0){
            m = (a.getX()-c.getX())/cd.getX();
        } else if (ab.getX()==0 && cd.getX()==0){
            if ( a.getX() != c.getX() )
                return null;
            else if ( (c.getY() >= a.getY() && c.getY() <= a.getY() + ab.getY()) || (c.getY() <= a.getY() && c.getY() >= a.getY() + ab.getY()) )
                return new punkt( new double[]{a.getX(), c.getY()} );
            else if ( (a.getY() >= c.getY() && a.getY() <= c.getY() + cd.getY()) || (a.getY() <= c.getY() && a.getY() >= c.getY() + cd.getY()) )
                return new punkt( new double[]{a.getX(), a.getY()} );
            else
                return null;
        } else if ( ab.getY()==0 && cd.getY()!=0 ){
            m = (a.getY()-c.getY())/cd.getY();
        }
        /*
         else if ( ab.getY() && cd.getY()==0 ){
         	// sollte nicht auftreten
         	m = 0;
         }
        */
        else if ( ab.getX()!=0 && (cd.getX()*ab.getY()/ab.getX())!=cd.getY() ){
            m = ( a.getY()-c.getY()+((c.getX()-a.getX())*ab.getY())/ab.getX() )/( cd.getY()-(cd.getX()*ab.getY()/ab.getX()) );
        } else {
            return null;
        }
        
        if ( !k2.isAnfangUnbegrenzt() && m<=0 ){
            return null;
        }
        if ( !k2.isEndeUnbegrenzt() && m>=1 ){
            return null;
        }
        
        double n = 0.0;
        
        if (ab.getX()==0 && ab.getY()!=0 && cd.getX()!=0){
            n = (a.getY()-c.getY()-((a.getX()-c.getX())*cd.getY()/cd.getX()))/(-ab.getY());
        } else if (ab.getX()!=0) {
            n = (c.getX()+m*cd.getX()-a.getX())/ab.getX();
        } else if (cd.getY()==0 && ab.getY()!=0){
            n = (c.getY()-a.getY())/ab.getY();
        } else {
            return null;
        }
        
        if ( !k1.isAnfangUnbegrenzt() && n<=0 )
            return null;
        if ( !k1.isEndeUnbegrenzt() && n>=1 )
            return null;
        
        punkt schnittpunkt = new punkt( new double[]{a.getX() + n*ab.getX(), a.getY() + n*ab.getY()} );
        
        return schnittpunkt;
    }
    
    public punkt getSchnittpunkt( Kante k2 ) throws Exception{
        return Kante.getSchnittpunkt(this, k2);
    }
    
    public int vorzeichenDesNormalenFaktors( punkt pkt ) throws Exception{
        double fact = this.getNormalenFaktorZu(pkt);
        
        if (fact > 0) return 1;
        if (fact < 0) return -1;
        return 0;
    }
    
    public double getABFaktorZumNormalenFaktor( punkt pkt ) throws Exception{
        
        double normalenFaktor = this.getNormalenFaktorZu(pkt);
        
        punkt a = this.getAnfang();
        punkt b = this.getEnde();
        punkt c = pkt;
        
        punkt ab = new punkt( new double[]{b.getX()-a.getX(), b.getY()-a.getY()} );
        // N ist normale zu ab
        punkt N = new punkt( new double[]{ab.getY(), -ab.getX()} );
        // berechne faktor y mit dem N multipliziert werden muss, um punkt3 zu erreichen
        
        if (ab.getX()!=0){
            double Xx = ( c.getX() - a.getX() - normalenFaktor*N.getX() ) / ab.getX();
            return Xx;
        } else {
            double Yx = ( c.getY() - a.getY() - normalenFaktor*N.getY() ) / ab.getY();
            return Yx;
        }
    }
    
    public double getNormalenFaktorZu( punkt pkt ) throws Exception{
        punkt punkt1 = this.getAnfang();
        punkt punkt2 = this.getEnde();
        punkt punkt3 = pkt;
        /*
        #           (C)
        #          / |  \
        #        /   |     \
        #      /     |y*N     \
        #   (A)______|_________(B)
        # ab ist vektor punkt1 -> punkt2 = a -> b
        */
        punkt ab = punkt.createVector(punkt1,punkt2);
        // N ist normale zu ab
        punkt N = new punkt( new double[]{ab.getY(), -ab.getX()} );
        // berechne faktor y mit dem N multipliziert werden muss, um punkt3 zu erreichen

        double ergebnis = 0;
        
        try {
            if (ab.getX() != 0 && ab.getY() != 0)
                ergebnis = ((punkt1.getY()- punkt3.getY())-( punkt1.getX() - punkt3.getX()) * ab.getY() / ab.getX() )/ (N.getX() * ab.getY() / ab.getX() - N.getY());
            else if (ab.getX() == 0 )
                ergebnis = (punkt3.getX() - punkt1.getX()) / N.getX();
            else
                ergebnis = (punkt3.getY() - punkt1.getY()) / N.getY();
        } catch (Exception e) {
            // faengt division durch null fehler ab, die hier aber nicht auftauchen koennen sollten
            ergebnis = 1;
            logger.printError(e.getMessage());
        }
        
        /*
        if (Double.isNaN(ergebnis)){
            logger.printError("got Nan!");
            logger.printDebug(this.toString());
            logger.printDebug(pkt.toString());
            logger.printDebug(ab.toString());
            logger.printDebug("---");
        }
        */

        return ergebnis;
    }

    public Kante getNormalenKante( double laenge ) throws Exception{
        punkt punkt1 = this.getAnfang();
        punkt punkt2 = this.getEnde();

        punkt ab = new punkt( new double[]{punkt2.getX()-punkt1.getX(), punkt2.getY()-punkt1.getY()} );
        punkt N = new punkt( new double[]{ab.getY(), -ab.getX()} );

        punkt nullPunkt = punkt.NULLPUNKT;
        double nLaenge = nullPunkt.distanceTo( N );
        
        double faktor;
        
        try{
            faktor = laenge / nLaenge * -1.0;
        } catch ( Exception e ) {
            faktor = -1.0;
        }
        
        N.setX( N.getX()*faktor );
        N.setY( N.getY()*faktor );
        
        Kante nKante = new Kante( new punkt( new double[]{punkt2.getX(), punkt2.getY()} ), new punkt( new double[]{punkt2.getX()+N.getX(), punkt2.getY()+N.getY()} ), false, false );
        
        return nKante;
    }
    
    public String toString() {
        return "Kante<"+this.punktIndexA+","+this.punktIndexB+">["+this.getAnfang().toString()+","+this.getEnde().toString()+"]";
    }
    
    public double getLaenge() throws Exception{
        if (this.anfang!=null && this.ende!=null){
            if (this.anfangUnbegrenzt || this.isEndeUnbegrenzt()) return Double.POSITIVE_INFINITY;
            return anfang.distanceTo(ende);
        }
        throw new Exception(Kante.class.getName()+": Starting point or end point not specified!"); 
    }
    
    public punkt getAnfang() {
        return anfang;
    }
    public void setAnfang(punkt anfang) {
        this.anfang = anfang;
    }
    public boolean isAnfangUnbegrenzt() {
        return anfangUnbegrenzt;
    }
    public void setAnfangUnbegrenzt(boolean anfangUnbegrenzt) {
        this.anfangUnbegrenzt = anfangUnbegrenzt;
    }
    public punkt getEnde() {
        return ende;
    }
    public void setEnde(punkt ende) {
        this.ende = ende;
    }
    public boolean isEndeUnbegrenzt() {
        return endeUnbegrenzt;
    }
    public void setEndeUnbegrenzt(boolean endeUnbegrenzt) {
        this.endeUnbegrenzt = endeUnbegrenzt;
    }
    public boolean isGueltig() {
        return gueltig;
    }
    public void setGueltig(boolean gueltig) {
        this.gueltig = gueltig;
    }
    public int getPunktIndexA() {
        return punktIndexA;
    }
    public void setPunktIndexA(int punktIndexA) {
        this.punktIndexA = punktIndexA;
    }
    public int getPunktIndexB() {
        return punktIndexB;
    }
    public void setPunktIndexB(int punktIndexB) {
        this.punktIndexB = punktIndexB;
    }
}
