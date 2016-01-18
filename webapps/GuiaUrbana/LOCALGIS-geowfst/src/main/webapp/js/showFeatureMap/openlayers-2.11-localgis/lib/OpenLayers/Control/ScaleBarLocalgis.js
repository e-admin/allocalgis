/**
 * @fileoverview JavaScript Scale Bar for Dynamic Web Maps
 * @author Tim Schaub
 * @version 2.0
 */

// @requires OpenLayers/Control.js

/** 
* @class
*/
OpenLayers.Control.ScaleBarLocalgis  = 
  OpenLayers.Class(OpenLayers.Control, {

    /** For div.id
     * @type String */
    id:  "ScaleBarLocalgis",

    /** @type DOMElement */
    element: null,
    
    /**
     * Scale denominator (1 / X) - set on update
     * @type Float
     */    
    scaleDenominator: 1,

    /**
     * Display system for scale bar - metric or english supported
     * @type String
     */
    displaySystem: 'metric',

    /**
     * Minimum width of the scale bar in pixels
     * @type Int
     */
    minWidth: 140,

    /**
     * Maximum width of the scale bar in pixels
     * @type Int
     */
    maxWidth: 150, // pixels

    /**
     * Number of major divisions for the scale bar
     * @type Int
     */
    divisions: 2,

    /**
     * Number of subdivisions per major division
     * @type Int
     */
    subdivisions: 2,

    /**
     * Show measures for subdivisions
     * @type Boolean
     */
    showMinorMeasures: false,

    /**
     * Show abbreviated measurement unit (ft, km)
     * @type Boolean
     */
    abbreviateLabel: false,

    /**
     * Display scale bar length and unit after scale bar
     * @type Boolean
     */
    singleLine: false,

    /**
     * Determines how scale bar will be aligned within the element -
     * left, center, or right supported
     * @type String
     */
    align: 'center',
    
    labelSpan: '',

    /**
     * Holds display units, abbreviations, and conversion to inches
     * (since we're using dpi) per measurement sytem.
     * @type Object
     */
    measurementProperties: {
        english: {
            units: ['miles', 'feet', 'inches'],
            abbr: ['mi', 'ft', 'in'],
            inches: [63360, 12, 1]
        },
        metric: {
            units: ['kilometers', 'meters', 'centimeters'],//abrevio la medida:
            abbr: ['km', 'm', 'cm'],
            inches: [39370.07874, 39.370079, 0.393701]
        }
    },

    /**
     * @constructor
     * @param {Object} options Hashtable of options to set on the scale bar
     */
    initialize: function(options) {
        OpenLayers.Control.prototype.initialize.apply(this, [options]);
        // create scalebar DOM elements
        this.element = document.createElement('div');
        /*this.element.style.position = 'relative';
        this.element.style.overflow = 'hidden';
        this.element.className = 'olScaleBarWrapper';*/
        this.labelContainer = document.createElement('div');
        this.labelContainer.className = 'sbUnitsContainer';
        this.labelContainer.style.position = 'absolute';
        this.graphicsContainer = document.createElement('div');
        this.graphicsContainer.style.position = 'absolute';
        this.graphicsContainer.className = 'sbGraphicsContainer';
        this.numbersContainer = document.createElement('div');
        this.numbersContainer.style.position = 'absolute';
        this.numbersContainer.className = 'sbNumbersContainer';
        
        this.labelSpan = document.createElement("div");
        this.labelContainer.className = 'labelSpan';
        this.labelContainer.style.position = 'absolute';
        //this.labelSpan.innerHTML = "Escala 1: " ;
        
       
        
        // put in some markers and bar pieces so style attributes can be grabbed
        // this is a solution for Safari support
        var classArray = ['scaleBarLeft','olScaleBarBar', 'olScaleBarBarAlt',
                          'olScaleBarNumbersBox', 'olScaleBarLabelBox', 'olScaleBarLabelBoxSingleLine'];
        for(classIndex = 0; classIndex < classArray.length; ++classIndex) {
            
            var anElement = document.createElement('div');
            anElement.className = classArray[classIndex];
            this.graphicsContainer.appendChild(anElement);
        }
    },

    /**
     * @type DOMElement
     */    
    draw: function() {
        OpenLayers.Control.prototype.draw.apply(this, arguments);
        // determine offsets for graphic elements
        /*this.xOffsetMarkerMajor = (this.styleValue('.olScaleBarMarkerMajor', 'borderLeftWidth') +
                                   this.styleValue('.olScaleBarMarkerMajor', 'width') +
                                   this.styleValue('.olScaleBarMarkerMajor', 'borderRightWidth')) / 2;
        this.xOffsetMarkerMinor = (this.styleValue('.olScaleBarMarkerMinor', 'borderLeftWidth') +
                                   this.styleValue('.olScaleBarMarkerMinor', 'width') +
                                   this.styleValue('.olScaleBarMarkerMinor', 'borderRightWidth')) / 2;*/
        this.xOffsetBar = (this.styleValue('.olScaleBarBar', 'borderLeftWidth') +
                           this.styleValue('.olScaleBarBar', 'borderRightWidth')) / 2;
        this.xOffsetBarAlt = (this.styleValue('.olScaleBarBarAlt', 'borderLeftWidth') +
                             this.styleValue('.olScaleBarBarAlt', 'borderRightWidth')) / 2;
        this.xOffsetNumbersBox = (this.styleValue('.olScaleBarNumbersBox', 'width')) / 2;
        // support for browsers without the Document.styleSheets property (Opera)
        /*if(!document.styleSheets) {
            // this is a two part hack, one for the offsets here and one for the css below
            this.xOffsetMarkerMajor = 0.5;
            this.xOffsetMarkerMinor = 0.5;
        }*/
        // set scale bar element height
        var classNames = ['.olScaleBarBar', '.olScaleBarBarAlt'];
        if(this.singleLine) {
            classNames.push('.olScaleBarLabelBoxSingleLine');
        }
        else {
            classNames.push('.olScaleBarNumbersBox', '.olScaleBarLabelBox');
        }
        var vertDisp = 0;
        for(classIndex = 0; classIndex < classNames.length; ++classIndex) {
            var aClassName = classNames[classIndex];
            vertDisp = Math.max(vertDisp, this.styleValue(aClassName, 'top') +
                                this.styleValue(aClassName, 'height'));
        }
        this.element.style.height = vertDisp + 'px';
        
        this.xOffsetSingleLine = this.styleValue('.olScaleBarLabelBoxSingleLine', 'width') +
                                 this.styleValue('.olScaleBarLabelBoxSingleLine', 'left');
        
        this.div.className = 'sbContainer';
        /*this.div.style.top="365px";*/
        //alert(navigator.appName);
        if (navigator.appName.contains("Microsoft")) 
        {
            this.div.style.bottom="40px";
        }
        else if (navigator.appName.contains("Opera"))
        {
            this.div.style.bottom="10px";
        }
        else
        {
            this.div.style.bottom="40px";
        }
        this.div.style.left="5px";
        this.div.style.position="absolute";
        this.div.appendChild(this.element);
        
        
       /* var divEscala = document.createElement("div");
        divEscala.style.bottom = "35px";
        divEscala.style.left = "35px";
        
        labelSpan = document.createElement("span");
        labelSpan.innerHTML = "Escala 1: " + Math.round(this.map.baseLayer.getResolution() * OpenLayers.INCHES_PER_UNIT['m'] * OpenLayers.DOTS_PER_INCH)*1000 + ' metros';////porque quiero que me devuelva la escala en metros por eso lo multiplico porque si no me lo da en km
        divEscala.appendChild(labelSpan);
        
        this.element.appendChild(divEscala);*/
        
        this.map.events.register('moveend', this, this.update);
        this.update();
        return this.div;
    },
   
    /**
     * Update the scale bar after modifying properties.
     * @param {Float} scaleDenominator New scale denominator
     */
    update: function() {
        if(this.map.baseLayer == null || !this.map.getResolution()) {
            return;
        }
        /* use getScale after ticket 168 is closed
         * this.scaleDenominator = this.map.getScale();
         */
        this.scaleDenominator = this.map.baseLayer.getResolution() * OpenLayers.INCHES_PER_UNIT[this.map.baseLayer.units] * OpenLayers.DOTS_PER_INCH;
        
        this.labelSpan.innerHTML = "<span>Escala 1: " + Math.round((this.map.baseLayer.getResolution() * OpenLayers.INCHES_PER_UNIT['m'] * OpenLayers.DOTS_PER_INCH)) +"</span>";
        //labelSpan.innerHTML = "Escala 1: " + Math.round((this.map.baseLayer.getResolution() * OpenLayers.INCHES_PER_UNIT['m'] * OpenLayers.DOTS_PER_INCH) * 1000) + ' metros';////porque quiero que me devuelva la escala en metros, si la quisiera en cm lo multiplicaría por 100000
    
        // update the element title and width
        /*alert("resolution ScaleBar: " + this.map.baseLayer.getResolution());
        alert("units ScaleBar: " + OpenLayers.INCHES_PER_UNIT[this.map.baseLayer.units]);
        alert("DOTS_PER_INCH ScaleBar: " + OpenLayers.DOTS_PER_INCH);*/
        
        this.element.title = 'Escala 1:' + this.formatNumber(this.scaleDenominator);
        //this.element.style.width = this.maxWidth + 'px';
        
        /*labelSpan = document.createElement("span");
        labelSpan.innerHTML = "Escala 1: " + Math.round(this.map.baseLayer.getResolution() * OpenLayers.INCHES_PER_UNIT['m'] * OpenLayers.DOTS_PER_INCH)*1000 + ' metros';////porque quiero que me devuelva la escala en metros por eso lo multiplico porque si no me lo da en km
        this.element.appendChild(labelSpan);*/
        
        // check each measurement unit in the display system
        var comparisonArray = new Array();
        for(var unitIndex = 0; unitIndex < this.measurementProperties[this.displaySystem].units.length; ++unitIndex) {
            comparisonArray[unitIndex] = new Object();
            var pixelsPerDisplayUnit = OpenLayers.DOTS_PER_INCH *
                                       this.measurementProperties[this.displaySystem].inches[unitIndex] /
                                       this.scaleDenominator;
            var minSDDisplayLength = ((this.minWidth - this.xOffsetNumbersBox) /
                                       pixelsPerDisplayUnit) /
                                      (this.divisions * this.subdivisions);
            var maxSDDisplayLength = ((this.maxWidth - this.xOffsetNumbersBox) /
                                       pixelsPerDisplayUnit) /
                                      (this.divisions * this.subdivisions);
            // add up scores for each marker (even if numbers aren't displayed)
            for(var valueIndex = 0; valueIndex < (this.divisions * this.subdivisions); ++valueIndex) {
                var minNumber = minSDDisplayLength * (valueIndex + 1);
                var maxNumber = maxSDDisplayLength * (valueIndex + 1);
                var niceNumber = new OpenLayers.Control.ScaleBarLocalgis.HandsomeNumber(minNumber, maxNumber);
                comparisonArray[unitIndex][valueIndex] = {
                    value: (niceNumber.value / (valueIndex + 1)),
                    score: 0,
                    tieBreaker: 0,
                    numDec: 0,
                    displayed: 0
                };
                // now tally up scores for all values given this subdivision length
                for(var valueIndex2 = 0; valueIndex2 < (this.divisions * this.subdivisions); ++valueIndex2) {
                    displayedValuePosition = niceNumber.value *
                                             (valueIndex2 + 1) / (valueIndex + 1);
                    niceNumber2 = new OpenLayers.Control.ScaleBarLocalgis.HandsomeNumber(displayedValuePosition, displayedValuePosition);
                    var isMajorMeasurement = ((valueIndex2 + 1) % this.subdivisions == 0);
                    var isLastMeasurement = ((valueIndex2 + 1) == (this.divisions *
                                                                   this.subdivisions));
                    if((this.singleLine && isLastMeasurement) ||
                       (!this.singleLine && (isMajorMeasurement || this.showMinorMeasures))) {
                        // count scores for displayed marker measurements
                        comparisonArray[unitIndex][valueIndex].score += niceNumber2.score;
                        comparisonArray[unitIndex][valueIndex].tieBreaker += niceNumber2.tieBreaker;
                        comparisonArray[unitIndex][valueIndex].numDec = Math.max(comparisonArray[unitIndex][valueIndex].numDec, niceNumber2.numDec);
                        comparisonArray[unitIndex][valueIndex].displayed += 1;
                    }
                    else {
                        // count scores for non-displayed marker measurements
                        comparisonArray[unitIndex][valueIndex].score += niceNumber2.score / this.subdivisions;
                        comparisonArray[unitIndex][valueIndex].tieBreaker += niceNumber2.tieBreaker / this.subdivisions;
                    }
                }
                // adjust scores so numbers closer to 1 are preferred for display
                var scoreAdjustment = (unitIndex + 1) *
                                      comparisonArray[unitIndex][valueIndex].tieBreaker /
                                      comparisonArray[unitIndex][valueIndex].displayed;
                comparisonArray[unitIndex][valueIndex].score *= scoreAdjustment;
            }
        }
        // get the value (subdivision length) with the lowest cumulative score
        var subdivisionDisplayLength = null;
        var displayUnits = null;
        var displayUnitsAbbr = null;
        var subdivisionPixelLength = null;
        var bestScore = Number.POSITIVE_INFINITY;
        var bestTieBreaker = Number.POSITIVE_INFINITY;
        var numDec = 0;
        for(var unitIndex = 0; unitIndex < comparisonArray.length; ++unitIndex) {
            for(valueIndex in comparisonArray[unitIndex]) {
                if((comparisonArray[unitIndex][valueIndex].score < bestScore) ||
                   ((comparisonArray[unitIndex][valueIndex].score == bestScore) &&
                    (comparisonArray[unitIndex][valueIndex].tieBreaker < bestTieBreaker))) {
                    bestScore = comparisonArray[unitIndex][valueIndex].score;
                    bestTieBreaker = comparisonArray[unitIndex][valueIndex].tieBreaker;
                    subdivisionDisplayLength = comparisonArray[unitIndex][valueIndex].value;
                    numDec = comparisonArray[unitIndex][valueIndex].numDec;
                    displayUnits = this.measurementProperties[this.displaySystem].units[unitIndex];
                    displayUnitsAbbr = this.measurementProperties[this.displaySystem].abbr[unitIndex];
                    pixelsPerDisplayUnit = OpenLayers.DOTS_PER_INCH *
                                           this.measurementProperties[this.displaySystem].inches[unitIndex] /
                                           this.scaleDenominator;
                    subdivisionPixelLength = pixelsPerDisplayUnit * subdivisionDisplayLength; // round before use in style
                }
            }
        }
        // clean out any old content from containers
        while(this.labelContainer.hasChildNodes()) {
            this.labelContainer.removeChild(this.labelContainer.firstChild);
        }
        while(this.graphicsContainer.hasChildNodes()) {
            this.graphicsContainer.removeChild(this.graphicsContainer.firstChild);
        }
        while(this.numbersContainer.hasChildNodes()) {
            this.numbersContainer.removeChild(this.numbersContainer.firstChild);
        }
        // create all divisions
        var aMarker, aBarPiece, numbersBox, xOffset;
        var alignmentOffset = {
            left: 0 + (this.singleLine ? 0 : this.xOffsetNumbersBox),
            center: (this.maxWidth / 2) - (this.divisions * this.subdivisions *
                                           subdivisionPixelLength / 2) -
                                          (this.singleLine ? this.xOffsetSingleLine / 2 : 0),
            right: this.maxWidth - (this.divisions * this.subdivisions *
                                    subdivisionPixelLength) -
                                   (this.singleLine ? this.xOffsetSingleLine : this.xOffsetNumbersBox)
        }
        
        var xPosition = 0 + alignmentOffset[this.align];
        var markerMeasure = 0;
        //Creo los extremos de la barra:
                aBarPiece = document.createElement('div');
                aBarPiece.style.position = 'absolute';
                aBarPiece.style.overflow = 'hidden';
                aBarPiece.style.width = 3 + 'px';
                aBarPiece.className = 'scaleBarLeft';
                aBarPiece.style.left = Math.round(xPosition - this.xOffsetBar - 3) + 'px';//le resto 3 porque es el ancho que tiene la imagen
                aBarPiece.appendChild(document.createTextNode(' '));
                this.graphicsContainer.appendChild(aBarPiece);
                
                
        for(var divisionIndex = 0; divisionIndex < this.divisions; ++divisionIndex) {
            // set xPosition and markerMeasure to start of division
            xPosition = divisionIndex * this.subdivisions * subdivisionPixelLength;
            xPosition += alignmentOffset[this.align];
            markerMeasure = (divisionIndex == 0) ?
                            0 : ((divisionIndex * this.subdivisions) *
                                 subdivisionDisplayLength).toFixed(numDec);
            // add major marker
            /*aMarker = document.createElement('div');
            aMarker.className = 'olScaleBarMarkerMajor';
            aMarker.style.position = 'absolute';
            aMarker.style.overflow = 'hidden';
            aMarker.style.left = Math.round(xPosition - this.xOffsetMarkerMajor) + 'px';
            aMarker.appendChild(document.createTextNode(' '));
            this.graphicsContainer.appendChild(aMarker);*/
            // add major measure
            if(!this.singleLine) {
                numbersBox = document.createElement('div');
                numbersBox.className = 'olScaleBarNumbersBox';
                numbersBox.style.position = 'absolute';
                numbersBox.style.overflow = 'hidden';
                numbersBox.style.textAlign = 'center';
                if(this.showMinorMeasures) {
                    numbersBox.style.left = Math.round(xPosition - this.xOffsetNumbersBox) + 'px';
                }
                else {
                    numbersBox.style.left = Math.round(xPosition - this.xOffsetNumbersBox) + 'px';
                }
                numbersBox.appendChild(document.createTextNode(markerMeasure));
                this.numbersContainer.appendChild(numbersBox);
            }
            // create all subdivisions
            
            for(var subdivisionIndex = 0; subdivisionIndex < this.subdivisions; ++subdivisionIndex) {
                
                aBarPiece = document.createElement('div');
                aBarPiece.style.position = 'absolute';
                aBarPiece.style.overflow = 'hidden';
                aBarPiece.style.width = Math.round(subdivisionPixelLength) + 'px';
                if((subdivisionIndex % 2) == 0) {
                    aBarPiece.className = 'olScaleBarBar';
                    aBarPiece.style.left = Math.round(xPosition - this.xOffsetBar) + 'px';
                }
                else {
                    aBarPiece.className = 'olScaleBarBarAlt';
                    aBarPiece.style.left = Math.round(xPosition - this.xOffsetBarAlt) + 'px';
                }
                //alert("subdivisionIndex:" + subdivisionIndex + " - " + aBarPiece.className);
                
                aBarPiece.appendChild(document.createTextNode(' '));
                this.graphicsContainer.appendChild(aBarPiece);
                // add minor marker if not the last subdivision
                if(subdivisionIndex < (this.subdivisions - 1)) {
                    // set xPosition and markerMeasure to end of subdivision
                    xPosition = ((divisionIndex * this.subdivisions) +
                                 (subdivisionIndex + 1)) * subdivisionPixelLength;
                    xPosition += alignmentOffset[this.align];
                    markerMeasure = (divisionIndex * this.subdivisions +
                                     subdivisionIndex + 1) * subdivisionDisplayLength;
                    /*aMarker = document.createElement('div');
                    aMarker.className = 'olScaleBarMarkerMinor';
                    aMarker.style.position = 'absolute';
                    aMarker.style.overflow = 'hidden';
                    aMarker.style.left = Math.round(xPosition - this.xOffsetMarkerMinor) + 'px';
                    aMarker.appendChild(document.createTextNode(' '));
                    this.graphicsContainer.appendChild(aMarker);*/
                    
                     
                    if(this.showMinorMeasures && !this.singleLine) {
                        // add corresponding measure
                        numbersBox = document.createElement('div');
                        numbersBox.className = 'olScaleBarNumbersBox';
                        numbersBox.style.position = 'absolute';
                        numbersBox.style.overflow = 'hidden';
                        numbersBox.style.textAlign = 'center';
                        numbersBox.style.left = Math.round(xPosition - this.xOffsetNumbersBox) + 'px';
                        numbersBox.appendChild(document.createTextNode(markerMeasure));
                        this.numbersContainer.appendChild(numbersBox);
                    }
                }
            }
        }
        
        
        
                
        // set xPosition and markerMeasure to end of divisions
        xPosition = (this.divisions * this.subdivisions) * subdivisionPixelLength;
        xPosition += alignmentOffset[this.align];
        markerMeasure = ((this.divisions * this.subdivisions) *
                         subdivisionDisplayLength).toFixed(numDec);
        // add the final major marker
       /* aMarker = document.createElement('div');
        aMarker.className = 'olScaleBarMarkerMajor';
        aMarker.style.position = 'absolute';
        aMarker.style.overflow = 'hidden';
        aMarker.style.left = Math.round(xPosition - this.xOffsetMarkerMajor) + 'px';
        aMarker.appendChild(document.createTextNode(' '));
        this.graphicsContainer.appendChild(aMarker);*/
        // add final measure
        if(!this.singleLine) {
            numbersBox = document.createElement('div');
            numbersBox.className = 'olScaleBarNumbersBox';
            numbersBox.style.position = 'absolute';
            numbersBox.style.overflow = 'hidden';
            numbersBox.style.textAlign = 'center';
            if(this.showMinorMeasures) {
                numbersBox.style.left = Math.round(xPosition -
                                                   this.xOffsetNumbersBox) + 'px';
            }
            else {
                numbersBox.style.left = Math.round(xPosition -
                                                   this.xOffsetNumbersBox) + 'px';
            }
            numbersBox.appendChild(document.createTextNode(markerMeasure));
            this.numbersContainer.appendChild(numbersBox);
        }
        // add content to the label element
       /* var labelBox = document.createElement('div');
        labelBox.style.position = 'absolute';
        var labelText;
        if(this.singleLine) {
            labelText = markerMeasure;
            labelBox.className = 'olScaleBarLabelBoxSingleLine';
            labelBox.style.left = (xPosition +
                                   this.styleValue('.olScaleBarLabelBoxSingleLine', 'left')) + 'px';
            
            
        }
        else {
            labelText = '';
            labelBox.className = 'olScaleBarLabelBox';
            labelBox.style.textAlign = 'center';
            labelBox.style.width = Math.round(this.divisions * this.subdivisions *
                                              subdivisionPixelLength) + 'px'
            labelBox.style.left = Math.round(alignmentOffset[this.align]) + 'px';
            labelBox.style.overflow = 'hidden';
        }
        if(this.abbreviateLabel) {
            labelText += ' ' + displayUnitsAbbr;
        }
        else {
            labelText += ' ' + displayUnits;
        }
        labelBox.appendChild(document.createTextNode(labelText));
        this.labelContainer.appendChild(labelBox);*/
        // support for browsers without the Document.styleSheets property (Opera)
        if(!document.styleSheets) {
            // override custom css with default
            var defaultStyle = document.createElement('style');
            defaultStyle.type = 'text/css';
            var styleText = '.olScaleBarBar {top: 12px; background: #666666; height: 1px; border: 0;}';
            styleText += '.olScaleBarBarAlt {top: 12px; background: #666666; height: 1px; border: 0;}';
           /* styleText += '.olScaleBarMarkerMajor {top: 12px; height: 7px; width: 1px; background: #666666; border: 0;}';
            styleText += '.olScaleBarMarkerMinor {top: 12px; height: 5px; width: 1px; background: #666666; border: 0;}';*/
            styleText += '.olScaleBarLabelBox {top: -5px; height: 15px; font-size: 13px; color: #333333; font-variant: small-caps;}';
            styleText += '.olScaleBarNumbersBox {top: 19px; font-size: 11px; width: 40px; height: 15px; color: #333333;}';
            defaultStyle.appendChild(document.createTextNode(styleText));
            document.getElementsByTagName('head').item(0).appendChild(defaultStyle);
        }
                    aBarPiece = document.createElement('div');
                    aBarPiece.style.position = 'absolute';
                    aBarPiece.style.overflow = 'hidden';
                    aBarPiece.style.width = Math.round(subdivisionPixelLength) + 'px';
                    
                    if (aBarPiece.className == 'olScaleBarBar')
                    {
                        aBarPiece.className = 'olScaleBarBarAlt';
                        aBarPiece.style.left = Math.round(xPosition - this.xOffsetBarAlt) + 1 + 'px';
                    }
                    else
                    {
                        aBarPiece.className = 'olScaleBarBar';
                        aBarPiece.style.left = Math.round(xPosition - this.xOffsetBar) + 1 + 'px';
                        
                    }
                    
                    aBarPiece.appendChild(document.createTextNode(' '));
                    this.graphicsContainer.appendChild(aBarPiece);
                    
        //Creo los extremos de la barra:
        //alert(aBarPiece.className);
                aBarPiece = document.createElement('div');
                aBarPiece.style.position = 'absolute';
                aBarPiece.style.overflow = 'hidden';
                aBarPiece.style.width = 3 + 'px';
                aBarPiece.className = 'scaleBarRight';
                if (aBarPiece.className == 'olScaleBarBarAlt')
                {
                    aBarPiece.style.left = Math.round(xPosition - this.xOffsetBarAlt) + Math.round(subdivisionPixelLength)  + 'px';
                }
                else
                {
                    aBarPiece.style.left = Math.round(xPosition - this.xOffsetBar) + Math.round(subdivisionPixelLength)  + 'px';
                }
                aBarPiece.appendChild(document.createTextNode(' '));
                this.graphicsContainer.appendChild(aBarPiece);
                
            
        // append the child containers to the parent element
        
        var labelBox = document.createElement('div');
        labelBox.style.position = 'absolute';
        var labelText;
        if(this.singleLine) {
            labelText = markerMeasure;
            labelBox.className = 'olScaleBarLabelBoxSingleLine';
            labelBox.style.left = (xPosition +
                                   this.styleValue('.olScaleBarLabelBoxSingleLine', 'left')) + 'px';
            
            
        }
        else {
            labelText = '';
            labelBox.className = 'olScaleBarLabelBox';
            labelBox.style.textAlign = 'center';
            //como es abreviado pongo muy poco ancho:
            labelBox.style.width = "30px";
            /*labelBox.style.width = Math.round(this.divisions * this.subdivisions *
                                              subdivisionPixelLength) + 'px'*/
            //labelBox.style.left = Math.round(alignmentOffset[this.align]) + 'px';
            labelBox.style.left = aBarPiece.style.left;
            labelBox.style.overflow = 'hidden';
        }
        if(this.abbreviateLabel) {
            labelText += ' ' + displayUnitsAbbr;
        }
        else {
            labelText += ' ' + displayUnits;
        }
        labelBox.appendChild(document.createTextNode(labelText));
        this.labelContainer.appendChild(labelBox);
        
        this.element.appendChild(this.graphicsContainer);
        this.element.appendChild(this.labelContainer);
        this.element.appendChild(this.numbersContainer);
        this.element.appendChild(this.labelSpan);
    },
    
    /**
     * Get an integer value associated with a particular selector and key.
     * Given a stylesheet with .someSelector {border: 2px solid red},
     * styleValue('.someSelector', 'borderWidth') returns 2
     * @type Int
     * @private
     */
    styleValue: function(aSelector, styleKey) {
        var aValue = 0;
        if(document.styleSheets) {
            for(var sheetIndex = document.styleSheets.length - 1; sheetIndex >= 0; --sheetIndex) {
                var aSheet = document.styleSheets[sheetIndex];
                if(!aSheet.disabled) {
                    var allRules;
                    try {
                        if(typeof(aSheet.cssRules) == 'undefined') {
                            if(typeof(aSheet.rules) == 'undefined') {
                                // can't get rules, assume zero
                                return 0;
                            }
                            else {
                                allRules = aSheet.rules;
                            }
                        }
                        else {
                            allRules = aSheet.cssRules;
                        }
                    } catch(err) {
                        return 0;
                    }
                    for(var ruleIndex = 0; ruleIndex < allRules.length; ++ruleIndex) {
                        var aRule = allRules[ruleIndex];
                        if(aRule.selectorText && (aRule.selectorText.toLowerCase() == aSelector.toLowerCase())) {
                            if(aRule.style[styleKey] != '') {
                                aValue = parseInt(aRule.style[styleKey]);
                            }
                        }
                    }
                }
            }
        }
        // if the styleKey was not found, the equivalent value is zero
        return aValue ? aValue : 0;
    },
    
    /**
     * @type String
     * @param {Float} aNumber
     * @param {Int} numDecimals
     * @private
     */
    formatNumber: function(aNumber, numDecimals) {
        numDecimals = (numDecimals) ? numDecimals : 0;
        var formattedInteger = '' + Math.round(aNumber);
        var thousandsPattern = /(-?[0-9]+)([0-9]{3})/;
        while(thousandsPattern.test(formattedInteger)) {
            formattedInteger = formattedInteger.replace(thousandsPattern, '$1,$2');
        }
        if(numDecimals > 0) {
            var formattedDecimal = Math.floor(Math.pow(10, numDecimals) * (aNumber - Math.round(aNumber)));
            if(formattedDecimal == 0) {
                return formattedInteger;
            }
            else {
                return formattedInteger + '.' + formattedDecimal;
            }
        }
        else {
            return formattedInteger;
        }
    },
    
    /** @final @type String */
    CLASS_NAME: "OpenLayers.Control.ScaleBarLocalgis"
    
});

/**
 * HandsomeNumber is an object representing a nice looking number
 * between two other numbers.
 * @class
 * @private
 */
OpenLayers.Control.ScaleBarLocalgis.HandsomeNumber = OpenLayers.Class.create();
/**
 * @private
 */
OpenLayers.Control.ScaleBarLocalgis.HandsomeNumber.prototype = {
    /**
     * Attempts to generate a nice looking number.
     * @constructor
     * @param {Float} smallUglyNumber
     * @param {Float} bigUglyNumber
     * @param {Int} sigFigs
     * @private
     */
    initialize: function(smallUglyNumber, bigUglyNumber, sigFigs) {
        sigFigs = (sigFigs == null) ? 10 : sigFigs;
        var bestScore = Number.POSITIVE_INFINITY;
        var bestTieBreaker = Number.POSITIVE_INFINITY;
        // if all else fails, return a small ugly number
        var handsomeValue = smallUglyNumber;
        var handsomeNumDec = 3;
        // try the first three comely multiplicands (in order of comliness)
        for(var halvingExp = 0; halvingExp < 3; ++halvingExp) {
            var comelyMultiplicand = Math.pow(2, (-1 * halvingExp));
            var maxTensExp = Math.floor(Math.log(bigUglyNumber /
                                        comelyMultiplicand) / Math.LN10);
            for(var tensExp = maxTensExp;
                tensExp > (maxTensExp - sigFigs + 1); --tensExp) {
                var numDec = Math.max(halvingExp - tensExp, 0);
                var testMultiplicand = comelyMultiplicand *
                                       Math.pow(10, tensExp);
                // check if there is an integer multiple of testMultiplicand
                // between smallUglyNumber and bigUglyNumber
                if((testMultiplicand * Math.floor(bigUglyNumber /
                    testMultiplicand)) >= smallUglyNumber) {
                    // check if smallUglyNumber is an integer multiple of testMultiplicand
                    if(smallUglyNumber % testMultiplicand == 0) {
                        var testMultiplier = smallUglyNumber / testMultiplicand;
                    }
                    // otherwise go for the smallest integer multiple between small and big
                    else {
                        var testMultiplier = Math.floor(smallUglyNumber /
                                                        testMultiplicand) + 1;
                    }
                    // test against the best (lower == better)
                    var testScore = testMultiplier + (2 * halvingExp);
                    var testTieBreaker = (tensExp < 0) ?
                                          (Math.abs(tensExp) + 1) : tensExp;
                    if((testScore < bestScore) || ((testScore == bestScore) &&
                       (testTieBreaker < bestTieBreaker))) {
                        bestScore = testScore;
                        bestTieBreaker = testTieBreaker;
                        handsomeValue = (testMultiplicand *
                                         testMultiplier).toFixed(numDec);
                        handsomeNumDec = numDec;
                    }
                }
            }
        }
        this.value = handsomeValue;
        this.score = bestScore;
        this.tieBreaker = bestTieBreaker;
        this.numDec = handsomeNumDec;
    }
}
/**
 * @private
 */
OpenLayers.Control.ScaleBarLocalgis.HandsomeNumber.prototype.toString = function() {
    return this.value.toString();
}
/**
 * @private
 */
OpenLayers.Control.ScaleBarLocalgis.HandsomeNumber.prototype.valueOf = function() {
    return this.value;
}
