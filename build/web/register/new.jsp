<jsp:useBean id="helper" class="core.helpers.BootstrapHelper" />
<% helper.init(request,response); %>
<form action="/register/save" method="post">
    <%=helper.inputText("name", "Your name:")%>
    <%=helper.inputText("email", "emaile:")%>
    <%=helper.inputPassword("password", "password:")%>
    <%=helper.csrfField()%>
    <input type="submit" value="Save" />
</form>