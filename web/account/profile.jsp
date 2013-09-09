<jsp:useBean id="helper" class="core.helpers.Bootstrap3Helper" />
<% helper.init(request,response); %>
 <div class="container">
    <%=helper.flashText()%> 
    <form action="/userprofile/save" method="post">
        <%=helper.formGroupText("firstName", "First Name :")%>
        <%=helper.formGroupText("lastName", "Last Name :")%>
        <%=helper.formGroupText("email", "Email :")%>
        <%=helper.formGroupPassword("password", "Password:")%>        
        <%=helper.csrfField()%>
        <%=helper.submit("Save")%>
    </form>
</div> <!-- /container -->
