<jsp:useBean id="helper" class="core.helpers.Bootstrap3Helper" />
<% helper.init(request,response); %>
 <div class="container">
    <form action="/register/save" method="post">
        <%=helper.formGroupText("name", "Name :")%>
        <%=helper.formGroupText("email", "Email :")%>
        <%=helper.formGroupPassword("password", "Password:")%>
        <%=helper.csrfField()%>
        <%=helper.submit("Register")%>
    </form>
</div> <!-- /container -->
