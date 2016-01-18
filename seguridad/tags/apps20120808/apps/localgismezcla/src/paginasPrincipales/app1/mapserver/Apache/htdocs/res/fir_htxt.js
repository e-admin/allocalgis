// <![CDATA[

/******************************************************************************
 This is the flash detection script.
 ******************************************************************************/
var required = 6;
var hasFlash = false;
if(navigator.appVersion.indexOf("MSIE") != -1 && navigator.appVersion.indexOf("Windows") != -1){
	document.write('<SCR' + 'IPT LANGUAGE=VBScript\> \n');
	document.write('on error resume next \n');
	// AFAIK creating an instance of an older version of the Flash object 
	// will return succeed even if the actual installed version is newer.
	document.write('hasFlash = (IsObject(CreateObject("ShockwaveFlash.ShockwaveFlash." & required))) \n');  
	document.write('<'+'/scr' + 'ipt\> \n');
	}
else {
	var plugin = (navigator.mimeTypes && navigator.mimeTypes["application/x-shockwave-flash"])?navigator.mimeTypes["application/x-shockwave-flash"].enabledPlugin:0;
	if (plugin) {
		var isVersion2 = navigator.plugins["Shockwave Flash 2.0"] ? " 2.0" : "";
		var flashDescription = navigator.plugins["Shockwave Flash" + isVersion2].description;
		var flashVersion = parseInt(flashDescription.charAt(flashDescription.indexOf(".") - 1));
		hasFlash = flashVersion >= required;
		}
	}


/******************************************************************************
 This classes the <html> element as `hasFlash` if flash is found. This style
 hook can be used to hide our to-be-replaced content before it even comes down
 the datapipe and eliminate the FOPSC ("Flash of Partially Styled Content")
 
 I've determined that the lighter the page weight the greater chance of a FOPSC.
 ******************************************************************************/
if (hasFlash && document.getElementsByTagName && document.getElementsByTagName('html')[0]) {
	document.getElementsByTagName('html')[0].className += (document.getElementsByTagName('html')[0].className=='')?'hasFlash':' hasFlash';
	}
	
	
/******************************************************************************
 Some utility functions. Look at them--aren't they useful?
 ******************************************************************************/
 
function SI_normalizeWhiteSpace(txt) {
	var rE = /\s+/gi;
	return txt.replace(rE,' ');
	}
	
function SI_replaceHeadlines() {
	var d = document;
	if (!hasFlash || !d.getElementsByTagName) return;
	var swfDir = "http://www.justwatchthesky.com/v13/f/"; 
	
	var h1s = d.getElementsByTagName('h1');
	for (var i=(h1s.length-1); i>=0; i--) {
		h1 = h1s[i];
		if (h1.parentNode.nodeName=='DIV') {
			div = h1.parentNode;
			if (div.className=='copy') {
			
				var h1,h2a,h2b;
				for (var j=(div.childNodes.length-1); j>=0; j--) {
					child = div.childNodes[j];
					if (child.nodeName == 'H2') {
						h2Node = child;
						for (var k=0; k<h2Node.childNodes.length; k++) {
							grandChild=h2Node.childNodes[k];
							if (grandChild.nodeName == 'SPAN') {
								h2a = grandChild.innerHTML;
								}
							else if (grandChild.nodeName == 'EM') {		
								h2b = grandChild.innerHTML;
								}
							}
						}
					else if (child.nodeName == 'H1') {
						h1Node = child;
						h1 = h1Node.innerHTML;
						}
					}
				if (h1 && h2b) {
					var c = d.createElement('div'); c.className = 'h1';
					h2Node.parentNode.removeChild(h2Node);
					h1Node.parentNode.replaceChild(c,h1Node);
					
					var swfHTML,w=445,h=30;
				
					var fv	= 'h1='+escape(h1)+'&h2='+escape(h2a)+'&h3='+escape(h2b);
					var swf	= 'htxt.swf';
					
					swfHTML  = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+w+'" height="'+h+'">';
					swfHTML += '	<param name="movie" value="'+swfDir+swf+'" />';
					swfHTML += '	<param name="flashvars" value="'+fv+'" />';
					swfHTML += '	<embed src="'+swfDir+swf+'" flashvars="'+fv+'" width="'+w+'" height="'+h+'" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer" />';
					swfHTML += '<'+'/object>';
					
					c.innerHTML = swfHTML;
					h1='',h2a='',h2b='';
					}
				}
			}
		}
	
	if (d.body && d.body.style) {
		d.body.style.height = "110px";
		d.body.style.height = "auto";
		}
	}

function SI_replaceHearts() {
	var d = document;
	if (!hasFlash || !d.getElementsByTagName) return;
	var swfDir = "http://www.justwatchthesky.com/v13/f/"; 
	
	var h1s = d.getElementsByTagName('h1');
	for (var i=(h1s.length-1); i>=0; i--) {
		h1 = h1s[i];
		if (h1.parentNode.nodeName=='DIV') {
			div = h1.parentNode;
			if (div.id=='heart') {
			
				var h1a,h1b,h2a,h2b,h2c;
				for (var j=(div.childNodes.length-1); j>=0; j--) {
					child = div.childNodes[j];
					if (child.nodeName == 'H1') {
						h1Node = child;
						for (var k=0; k<h1Node.childNodes.length; k++) {
							grandChild=h1Node.childNodes[k];
							if (grandChild.nodeName == 'B') {
								h1a = grandChild.innerHTML;
								}
							else if (grandChild.nodeName == 'SPAN') {		
								h1b = grandChild.innerHTML;
								}
							}
						}
					else if (child.nodeName == 'H2') {
						h2Node = child;
						for (var k=0; k<h2Node.childNodes.length; k++) {
							grandChild=h2Node.childNodes[k];
							if (grandChild.nodeName == 'B') {
								h2a = grandChild.innerHTML;
								}
							else if (grandChild.nodeName == 'EM') {		
								h2b = grandChild.innerHTML;
								}
							else if (grandChild.nodeName == 'SPAN') {		
								h2c = grandChild.innerHTML;
								}
							}
						}
					}
				if (h1a && h2a) {
					var c = d.createElement('div'); c.className = 'h1';
					h2Node.parentNode.removeChild(h2Node);
					h1Node.parentNode.replaceChild(c,h1Node);
					
					var swfHTML,w=714,h=163;
				
					var fv	= 'hI='+escape(h1a)+'&IhrtURL='+escape(h1b)+'&hU='+escape(h2a)+'&hA='+escape(h2b)+'&hrtURL='+escape(h2c);
					var swf	= 'heart.swf';
					
					swfHTML  = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="'+w+'" height="'+h+'">';
					swfHTML += '	<param name="movie" value="'+swfDir+swf+'" />';
					swfHTML += '	<param name="flashvars" value="'+fv+'" />';
					swfHTML += '	<embed src="'+swfDir+swf+'" flashvars="'+fv+'" width="'+w+'" height="'+h+'" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer" />';
					swfHTML += '<'+'/object>';
					
					c.innerHTML = swfHTML;
					h1a='',h1b='',h2a='',h2b='',h2c='';
					}
				}
			}
		}
	
	if (d.body && d.body.style) {
		d.body.style.height = "163px";
		d.body.style.height = "auto";
		}
	}
	

function SI_init() {
	SI_replaceHeadlines();
	SI_replaceHearts();
	} window.onload = SI_init;


function comma(number) {
	number = number.toString();
	if (number.length > 3) {
		var mod = number.length % 3;
		var output = (mod > 0 ? (number.substring(0,mod)) : '');
		for (i=0 ; i < Math.floor(number.length / 3); i++) {
			if ((mod == 0) && (i == 0))
				output += number.substring(mod+ 3 * i, mod + 3 * i + 3);
			else
				output+= ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);
		}
		return (output);
	} else return number;
}

// ]]>