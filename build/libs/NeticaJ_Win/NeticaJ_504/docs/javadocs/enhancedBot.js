
var ns4 = (document.layers);
var ie4 = (document.all && !document.getElementById);
var ie5 = (document.all && document.getElementById);
var ns6 = (!document.all && document.getElementById);

function loadEnhanced( classNm, item ) {
  var url =  "norsys/netica/" + classNm + ".html#" + item;
	
    if (document.getElementById) {
       //var fm = parent.parent.document.getElementById('classFrm');
       var fm = top.document.getElementById('classFrm');
       //if (fm != null ) fm.location.href = url;
       if (fm != null ) fm.src = url;
    }
    else {
       var fm = top.frames["classFrame"];
       if (fm != null ) {
//test w. IE4
         fm.location.href = "../" + url;
         //fm.src = "../" + url;
       }
    }  
}

function init() {
    //alert( document.location.href );
    //top.leftFrm.leftTopFrm.lastClassURL = document.location.href;
}


