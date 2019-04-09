function init() {
  var h = document.location.href;
  var p2 = h.indexOf(".html");
  var p1 = h.lastIndexOf("/");
  if (p>=0) {
     h = h.substr(p+6);
     var p2 = h.indexOf(".");
     h = h.substring(0,p2);
  }
  //var date = new Date();
  //var twoWeeksFromNow = date.getTime() + (56*24*3600*1000);
  //date.setTime( twoWeeksFromNow ); 
  document.cookie = "lastClass=" + h + "; expires="+ date.toGMTString();
if (top.leftFrm && top.leftFrm.leftTopFrm && top.leftFrm.leftTopFrm.storeLastClass ) {
   top.leftFrm.leftTopFrm.storeLastClass( h );
}

	//alert( "A=" + document.cookie );
    //if (top.leftFrm && top.leftFrm.leftTopFrm && top.leftFrm.leftTopFrm.lastClassURL ) {
    //top.leftFrm.leftTopFrm.lastClassURL = 
    //}
}