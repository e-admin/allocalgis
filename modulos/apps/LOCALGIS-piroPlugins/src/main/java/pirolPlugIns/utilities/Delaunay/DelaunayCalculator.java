/**
 * DelaunayCalculator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 04.01.2005
 *
 * CVS header information:
 *  $RCSfile: DelaunayCalculator.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Delaunay/DelaunayCalculator.java,v $
 */
package pirolPlugIns.utilities.Delaunay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.CollectionsTools;
import pirolPlugIns.utilities.CoordinateComparator;
import pirolPlugIns.utilities.Kante;
import pirolPlugIns.utilities.punkt;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Class to calculate a Delaunay diagram from a given points (DelaunayPunkt) array.
 * 
 * This class is from a time when we were not sure if we should use german or english language in our classes - parts of this one are german as you see... 
 * Sorry about that! 
 * 
 * <pre>
 * german,         english:
 * -----------------------------
 * Punkt           point
 * Nachbar (von)   neighbor (of)
 * Kante           line
 * Dreieck         triangle
 * gehört zu       is part of
 * </pre>
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 * @see DelaunayPunkt
 */
public final class DelaunayCalculator extends Thread {
    
    private int sortiertNach = -1;
    private boolean sortiert = false;

    private double minimalerAbstand = Double.MAX_VALUE;
    
    private DelaunayPunkt[] pointsArray = null;
    private int numPoints = 0;
    private int kantenCreated = 0;
    private int estimatedLineNum = 0;
    
    private DelaunayPunkt punkt1, punkt2;
    
    private static final double pi = Math.PI;
    //private static final double umrechnungsFaktor = 1.0/(2.0*DelaunayCalculator.pi)*360.0;
    private static final double standardGrenzWinkel = DelaunayCalculator.pi/18.0;
    protected double grenzWinkel = DelaunayCalculator.standardGrenzWinkel;
    
    private boolean berechnungAbgeschlossen = false;
    private boolean errorOccured = false;
    private TaskMonitorDialog monitor = null;
    private ErrorHandler errHandler = null;
    
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
    
    int numTriangles = 0;
    
    public DelaunayCalculator(List punktefeld) {
        super();
        this.pointsArray = (DelaunayPunkt[])punktefeld.toArray(new DelaunayPunkt[0]);
    }
    
    public DelaunayCalculator(List punktefeld, TaskMonitorDialog monitor, ErrorHandler errHandler) {
        super();
        this.pointsArray = (DelaunayPunkt[])punktefeld.toArray(new DelaunayPunkt[0]);
        this.numPoints = this.pointsArray.length;
        this.monitor = monitor;
        this.errHandler = errHandler;
    }
    
    public DelaunayCalculator(DelaunayPunkt[] punktefeld, TaskMonitorDialog monitor, ErrorHandler errHandler) {
        super();
        this.pointsArray = punktefeld;
        this.numPoints = this.pointsArray.length;
        this.monitor = monitor;
        this.errHandler = errHandler;
    }
    
    public boolean hasErrorOccured() {
        return errorOccured;
    }
    
    public DelaunayPunkt getPunkt( int index ){
        DelaunayPunkt p;
        
        for ( int i=0; i<this.numPoints; i++ ){
            p = this.pointsArray[i];
            if (p.getIndex()==index)
                return p;
        }
        return null;
    }
    
    public boolean isBerechnungAbgeschlossen() {
        return berechnungAbgeschlossen;
    }
    public boolean isSortiert() {
        return sortiert;
    }
    protected void setSortiert(boolean sortiert, int sortFor) {
        this.sortiert = sortiert;
        this.sortiertNach = sortFor;
    }
    
    protected punkt[] findNextNeighbors() throws Exception{

        if (!this.isSortiert() || this.sortiertNach < 0){
            CoordinateComparator cComp = new CoordinateComparator(CoordinateComparator.SORTFOR_X);
            List tmp = new ArrayList();
            CollectionsTools.addArrayToList(tmp, this.pointsArray);
            Collections.sort(tmp, cComp);
            this.pointsArray = (DelaunayPunkt[])tmp.toArray(new DelaunayPunkt[0]);
            this.setSortiert( true, CoordinateComparator.SORTFOR_X );
        }
        this.findNextNeighborsRecursive(0, this.pointsArray.length-1);
        
        return new punkt[]{this.punkt1, punkt2, this.punkt2};
    }
    
    protected void findNextNeighborsRecursive( int von, int bis ) throws Exception {
        if (von < bis){
            int trenner = (int)(Math.floor( (bis + von)/2 ));
            this.findNextNeighborsRecursive( von, trenner );
            this.findNextNeighborsRecursive( trenner+1, bis );
            
            double trennenderXWert  = ((punkt)this.pointsArray[trenner]).getX();
            
            int untererGrenzIndex = trenner;
            int obererGrenzIndex = trenner;
            boolean gefunden = false;
            
            while ( !gefunden && untererGrenzIndex > von ){
                if ( Math.abs(trennenderXWert - ((punkt)this.pointsArray[untererGrenzIndex]).getX()) > this.minimalerAbstand) {
                    gefunden = true;
                    break;
                }
                untererGrenzIndex--;
            }
            
            gefunden = false;
            while (!gefunden && obererGrenzIndex < bis ){
                if ( Math.abs(trennenderXWert - ((punkt)this.pointsArray[obererGrenzIndex]).getX()) > this.minimalerAbstand) {
                    gefunden = true;
                    break;
                }
                obererGrenzIndex++;
            }
            
            ArrayList grenzPunkte = new ArrayList();
            
            for ( int i=untererGrenzIndex; i<=obererGrenzIndex; i++){
                grenzPunkte.add(this.pointsArray[i]);
            }
            
            CoordinateComparator cComp = new CoordinateComparator(CoordinateComparator.SORTFOR_Y);
            Collections.sort(grenzPunkte, cComp);
            
            DelaunayPunkt iPunkt = null, jPunkt;
            double tmpAbstand;
            
            for (int i=0; i<grenzPunkte.size(); i++){
                try {
                    iPunkt = ((DelaunayPunkt)grenzPunkte.get(i));
                } catch (Exception e){
                    this.logger.printError(e.getMessage() + ", " + grenzPunkte.get(i).getClass().getName() + " vs. DelaunayPunkt");
                    e.printStackTrace();
                }
                for (int j=0; j<grenzPunkte.size(); j++){
                    if ( i==j ) continue;
                    jPunkt = ((DelaunayPunkt)grenzPunkte.get(j));
                    if (iPunkt.istNachbarVon(jPunkt)) continue;
                    
                    if ( Math.abs(iPunkt.getY()-jPunkt.getY()) < this.minimalerAbstand ){
                        tmpAbstand = iPunkt.distanceTo(jPunkt);
                        if ( tmpAbstand < this.minimalerAbstand ){
                            if (tmpAbstand==0){
                                this.eliminatePoint(jPunkt);
                                this.minimalerAbstand = Double.MAX_VALUE;
                                this.punkt1 = null;
                                this.punkt2 = null;
                                throw new IllegalArgumentException(PirolPlugInMessages.getString("two-points-with-identical-coordinates"));
                            }
                            this.minimalerAbstand = tmpAbstand;
                            this.punkt1 = iPunkt;
                            this.punkt2 = jPunkt;
                        }
                    } else if (j>i) {
                        // die Abstaende sind bereits groesser, da der index j im nach y-koordinaten
                        // geordneten feld groesser ist...
                        break;
                    }

                }
            }
        }     
    }
    
    protected void eliminatePoint(DelaunayPunkt pkt){
        DelaunayPunkt[] newPointArray = new DelaunayPunkt[this.pointsArray.length-1];
        
        for (int i=0, j=0; i<this.pointsArray.length; i++){
            if (this.pointsArray[i] != pkt){
                newPointArray[j] = this.pointsArray[i];
                j++;
            }
        }
        this.pointsArray = newPointArray;
        this.numPoints = newPointArray.length;
    }
    
    protected void calculateDiagramIterativ( DelaunayPunkt p1, DelaunayPunkt p2, DelaunayPunkt alterPunkt, int dreieckZaehler ) throws Exception{
        int nummer = dreieckZaehler;
        boolean sucheFortsetzen = true;
        
        List aktuelleListe = new ArrayList();
        aktuelleListe.add( new DelaunayLoopItem( p1, p2, alterPunkt, nummer ) );
        
        List neueListe, neueErgebnisse;
        
        DelaunayLoopItem item;

        while ( !aktuelleListe.isEmpty() ){
            neueListe = new ArrayList();
            sucheFortsetzen = true;
            
            while (sucheFortsetzen){
                sucheFortsetzen = false;
                
                Iterator iter = aktuelleListe.iterator();
                
                while (iter.hasNext()){
                    nummer ++;
                    item = (DelaunayLoopItem) iter.next();
                    neueErgebnisse = this.buildTriangles( item.getPunkt1(), item.getPunkt2(), item.getAlterPunkt(), nummer );
                    neueListe.addAll(neueErgebnisse);
                }
                
                if ( neueListe.isEmpty() && this.kantenCreated==1 && this.grenzWinkel > Double.MIN_VALUE ){
                    this.grenzWinkel -= 0.017;
                    sucheFortsetzen = true;
                } else if ( this.grenzWinkel != DelaunayCalculator.standardGrenzWinkel ){
                    this.grenzWinkel = DelaunayCalculator.standardGrenzWinkel;
                }
                
                
            }
            
            this.monitor.report(this.kantenCreated, this.estimatedLineNum, PirolPlugInMessages.getString("done"));
            
            aktuelleListe.clear();
            aktuelleListe = neueListe;
        }

    }
    
    protected List buildTriangles(DelaunayPunkt p1, DelaunayPunkt p2, DelaunayPunkt alterPunkt, int nummerDreieck) throws Exception{
        List resultat = new ArrayList();
        Kante k;

        DelaunayPunkt ap = null;
        
        Kante k_tmp = new Kante( p1, p2 );
        
        
        if ( p1.gehoertZuMehrAlsEinemDreieckMit( p2 ) ){
            return resultat;
        }

        double maximalerWinkel = 0;
        double aktuellerWinkel = 0;

        int index3 = -1;
        double normFaktor = 0;

        double c = p1.distanceTo( p2 );
        double cSq = c*c;

        if (alterPunkt != null){
            ap = alterPunkt;
            normFaktor = k_tmp.getNormalenFaktorZu( ap );
        }

        DelaunayPunkt aktItem, p3 = null;
        double a, b, cosGamma, aktuellerNormFaktor = 0;
        
        for ( int j=0; j<this.numPoints; j++ ){
            aktItem = this.pointsArray[j];

            if ( aktItem.getIndex() == p1.getIndex() || aktItem.getIndex() == p2.getIndex() || (ap!=null && aktItem.getIndex() == ap.getIndex()) ) continue;

            aktuellerNormFaktor = k_tmp.getNormalenFaktorZu( aktItem );
            if (aktuellerNormFaktor == 0) continue;
            else if (normFaktor>0 && aktuellerNormFaktor>0) continue;
            else if (normFaktor<0 && aktuellerNormFaktor<0) continue;
            

/*
            #           (C)
            #          / |  \
            #       b/   |     \a
            #      /     |h       \
            #   (A)______|_________(B)
            #               c
*/
            aktuellerWinkel = 0;
            b = p1.distanceTo( aktItem );
            a = p2.distanceTo( aktItem );

            // a^2=b^2+c^2-2*b*c*cos( Gamma ) (Kosinussatz).
            // => gamma = acos( (a^2+b^2-c^2)/(2ab) )
            cosGamma = (a*a+b*b-cSq)/(2.0*b*a);
            
            if ( cosGamma > 1.0 && cosGamma - 1.0 < 0.000000000000001){
                cosGamma = 1;
            }
            
            if (cosGamma >= -1.0 && cosGamma <= 1.0){
                aktuellerWinkel = Math.acos( cosGamma );
            } else {
                this.logger.printWarning("schrott winkel: " + cosGamma + ", " + a + ", " + b + ", " + c);
                continue;
            }

            if (aktuellerWinkel > maximalerWinkel){
                maximalerWinkel = aktuellerWinkel;
                index3 = aktItem.getIndex();
                p3 = aktItem;
            }

        }
        
        if (index3 > -1 && maximalerWinkel >= this.grenzWinkel) {
            
            boolean isNeighborOfP1 = p3.istNachbarVon(p1);
            boolean isNeighborOfP2 = p3.istNachbarVon(p2);
            
            if (!isNeighborOfP1 || !isNeighborOfP2) {

                p1.setZugehoerigZu(nummerDreieck);
                p2.setZugehoerigZu(nummerDreieck);
                p3.setZugehoerigZu(nummerDreieck);
                this.numTriangles++;

                if (!isNeighborOfP1) {
                    // if p3 and p1 are not already connected, they are now
                    p1.setNachbarVon(p3);
                    p3.setNachbarVon(p1);
                    k = new Kante(p1, p3);
                    p1.bildetKante(k);
                    p3.bildetKante(k);
                    this.kantenCreated++;
                    resultat.add(new DelaunayLoopItem(p1, p3, p2));
                }

                if (!isNeighborOfP2) {
                    // if p3 and p2 are not already connected, they are now
                    p2.setNachbarVon(p3);
                    p3.setNachbarVon(p2);
                    k = new Kante(p2, p3);
                    p2.bildetKante(k);
                    p3.bildetKante(k);
                    this.kantenCreated++;
                    resultat.add(new DelaunayLoopItem(p2, p3, p1));
                }

            } else {
                // Löcher im 3d modell weg!
                if (!p3.bildetDreieckMit(p1,p2) ) {
                    this.logger.printDebug("stopfe loch!");
                    p1.setZugehoerigZu(nummerDreieck);
                    p2.setZugehoerigZu(nummerDreieck);
                    p3.setZugehoerigZu(nummerDreieck);
                    this.numTriangles++;
                }
            }
        }
        return resultat;
    }
    
    public void run() {
        super.run();
        this.estimatedLineNum = this.numPoints * 3;
        try {
            this.erstelleDelaunayNetz();
            this.compilePoints();
        } catch (Exception e) {
            errorOccured = true;
            if (this.errHandler!=null){
                this.errHandler.handleThrowable(e);
            } else {
                e.printStackTrace();
            }
        } finally {
            this.quitExecution();
        }
    }
    
    protected void compilePoints(){
        int numPoints = this.pointsArray.length;
        
        for (int i=0; i<numPoints; i++){
            this.pointsArray[i].compile();
        }
    }
    
    protected void quitExecution(){
        if (this.monitor!=null){
            this.monitor.setVisible(false);
            this.monitor.dispose();
        }
        this.berechnungAbgeschlossen = true;        
    }
    public int getNumPoints() {
        return numPoints;
    }
    public DelaunayPunkt[] getPointsArray() {
        return pointsArray;
    }
    protected boolean erstelleDelaunayNetz() throws Exception{
        if (this.pointsArray.length == 0){
            this.berechnungAbgeschlossen = true;
            return false;
        } 
        
        boolean neighborsFound = false;
        int numPts = this.numPoints;
        while (!neighborsFound){
            try {
                numPts = this.numPoints;
                this.findNextNeighbors();
                neighborsFound = true;
            } catch (IllegalArgumentException e) {
                neighborsFound = (numPts==this.numPoints);
                logger.printWarning( (numPts-this.numPoints) + " point(s) was/were kicked out");
            }
            numPts = this.numPoints;
        }

        // das punktefeld ist nun nach x koordinaten geordnet
        // rendern der ersten Basislinie
        
        
        if (this.punkt1 != null && this.punkt2 != null ){
            Kante k = new Kante(this.punkt1, this.punkt2);
            this.punkt1.bildetKante(k);
            this.punkt2.bildetKante(k);
            this.kantenCreated++;

            this.punkt1.setNachbarVon(this.punkt2);
            this.punkt2.setNachbarVon(this.punkt1);
        } else {
            throw new Exception("No next neighbours found!");
        }

        this.calculateDiagramIterativ( this.punkt1, this.punkt2, null, 0 );
        this.berechnungAbgeschlossen = true;
        return true;
   
    }
    
    
    public int getNumLines(){
        return this.kantenCreated;
    }
    
    public int getNumTriangles() {
        return numTriangles;
    }
}
