
var ns4 = (document.layers);
var ie4 = (document.all && !document.getElementById);
var ie5 = (document.all && document.getElementById);
var ns6 = (!document.all && document.getElementById);

function loadClFm( classNm ) {
  var url =  "norsys/netica/" + classNm + ".html";
	
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
         //fm.location.href = "../" + url;
alert(url);
         fm.location.href = url;
       }
    }  
}

function loadEnBot( classNm ) {
  var url =  "classItems/items_" + classNm + ".html";
  if ( classNm.indexOf( "gui/" ) == 0 ) {
    url = "classItems/gui/items_" + classNm.substring(4)+ ".html";
  }
//alert( "2 : " + url );
	
    if (document.getElementById) {
       //var fm = parent.parent.document.getElementById('enhancedBotFrm');
       var fm = parent.document.getElementById('enhancedBotFrm');
       //if (fm != null ) fm.location.href = url;
       if (fm != null ) fm.src = url; else alert ( "null" );
    }
    else {
       var fm = parent.frames["enhancedBotFrame"];
       if (fm != null ) {
//test w. IE4
         //fm.location.href = "../" + url;
         fm.location.href = url;
       }
    }  
}

function load2( classNm ){
  loadClFm( classNm );
  loadEnBot( classNm );
}
