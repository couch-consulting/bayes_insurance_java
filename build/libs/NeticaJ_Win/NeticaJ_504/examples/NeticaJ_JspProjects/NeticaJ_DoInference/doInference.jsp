<!--
  Copyright © 2006  Norsys Software Corp.
-->
<jsp:useBean id="doInf" scope="session" class="norsys.netica.jspprojects.doinference.DoInferenceBean"/>

<% doInf.processRequest(request); // upon each invocation, reprocess the request %>


<html>
  <head>
    <title>DoInference.jsp: sample JSP project calling Netica on server</title>
  </head>

<body bgcolor="white" style="color:navy; font-size: 24px">

<form type=post action="/Netica/doInference.jsp">

<center><u>Norsys Netica Do Inference JSP Example</u><br><%= doInf.getDate() %></center>
<br>  
<br>  Select the predispositions and symptoms that apply for your patient
<br>  
<br>  Predispositions: 
<br>  
<table width=700 border=0 cellspacing=0 cellpadding=4 style="margin:5; color:navy; font-size: 22px">
  <tr bgcolor="EEFFEE" > 
    <td align=right width=350>Did he/she visit Asia?</td>
    <td><input type=radio name=visitAsiaRadio value="no"  <%= "no" .equals(doInf.visitAsiaRadio)? "CHECKED":"" %>> No </td>
    <td><input type=radio name=visitAsiaRadio value="yes" <%= "yes".equals(doInf.visitAsiaRadio)? "CHECKED":"" %>> Yes </td>
    <td><input type=radio name=visitAsiaRadio value="unk" <%= "unk".equals(doInf.visitAsiaRadio)? "CHECKED":"" %>> Don't Know </td>
  </tr> 
  <tr bgcolor="EEEEFF"> 
    <td align=right>Does he/she smoke?</td>
    <td><input type=radio name=smokingRadio   value="no"  <%= "no" .equals(doInf.smokingRadio)?   "CHECKED":"" %>> No </td>
    <td><input type=radio name=smokingRadio   value="yes" <%= "yes".equals(doInf.smokingRadio)?   "CHECKED":"" %>> Yes </td>
    <td><input type=radio name=smokingRadio   value="unk" <%= "unk".equals(doInf.smokingRadio)?   "CHECKED":"" %>> Don't Know </td>
  </tr> 
</table>
<br>  Symptoms:
<br>  
<table width=700 border=0 cellspacing=0 cellpadding=4 style="margin:5; color:navy; font-size: 22px">
  <tr bgcolor="FFEEEE" > 
    <td align=right width=350>Does he/she have an abnormal X-Ray?</td>
    <td><input type=radio name=xRayRadio      value="no"  <%= "no" .equals(doInf.xRayRadio)?      "CHECKED":"" %>> No </td>
    <td><input type=radio name=xRayRadio      value="yes" <%= "yes".equals(doInf.xRayRadio)?      "CHECKED":"" %>> Yes </td>
    <td><input type=radio name=xRayRadio      value="unk" <%= "unk".equals(doInf.xRayRadio)?      "CHECKED":"" %>> Don't Know </td>
  </tr> 
  <tr bgcolor="FFFFEE"> 
    <td align=right>Does he/she have Dyspnea?</td>
    <td><input type=radio name=dyspneaRadio   value="no"  <%= "no" .equals(doInf.dyspneaRadio)?   "CHECKED":"" %>> No </td>
    <td><input type=radio name=dyspneaRadio   value="yes" <%= "yes".equals(doInf.dyspneaRadio)?   "CHECKED":"" %>> Yes </td>
    <td><input type=radio name=dyspneaRadio   value="unk" <%= "unk".equals(doInf.dyspneaRadio)?   "CHECKED":"" %>> Don't Know </td>
  </tr> 
</table>
<br>  
<br>  <INPUT type=submit name=submit Value="Submit"  style="background-color:#AAFFAA" >
      <INPUT type=submit name=newPatient Value="New Patient Reset" ALIGN=RIGHT style="background-color:#AACCFF" >
<br>
<br>  <% java.text.DecimalFormat fmt = new java.text.DecimalFormat( "###.#%" ); 
         fmt.setMinimumFractionDigits(1);
         String pct[] = new String[3];
         pct[0] = fmt.format( doInf.tuberculosis.getBelief ("Present"));
         pct[1] = fmt.format( doInf.cancer.getBelief ("Present"));
         pct[2] = fmt.format( doInf.bronchitis.getBelief ("Present"));

      %>

<table width=700 border=0 cellspacing=0 cellpadding=4 style="margin:5; color:navy; font-size: 22px">
  <tr bgcolor="FFEEDD"> 
    <td align=right width=350>The probability of tuberculosis is:</td>
    <td align=right><%= pct[0] %></td>
    <td   width=250><img src="clear.gif" width="<%= pct[0] %>" height=10 style="background-color:navy"></td>
  </tr>
  <tr bgcolor="EEDDCC"> 
    <td align=right>The probability of lung cancer is:</td>
    <td align=right><%= fmt.format( doInf.cancer.getBelief ("Present")) %></td>
    <td   width=250><img src="clear.gif" width="<%= pct[1] %>" height=10 style="background-color:navy"></td>
  </tr>
  <tr bgcolor="DDCCBB"> 
    <td align=right>The probability of bronchitis is:</td>
    <td align=right><%= fmt.format( doInf.bronchitis.getBelief ("Present")) %></td>
    <td   width=250><img src="clear.gif" width="<%= pct[2] %>" height=10 style="background-color:navy"></td>
  </tr> 
</table>
 
<hr>  
   <%= doInf.getLog() %>
</form>

</body>

</html>

