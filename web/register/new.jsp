<jsp:useBean id="helper" class="core.helpers.Bootstrap3Helper" />
<% helper.init(request,response); %>
 <div class="container">
    <form action="/register/save" method="post">
        <%=helper.formGroupText("firstName", "First Name :")%>
        <%=helper.formGroupText("lastName", "Last Name :")%>
        <%=helper.formGroupText("email", "Email :")%>
        <%=helper.formGroupPassword("password", "Password:")%>
        <img src="/images/photo.jpg.png" />
        <%=helper.csrfField()%>
        <%=helper.submit("Register")%>
    </form>
</div> <!-- /container -->
