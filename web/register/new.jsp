<form action="/register/save" method="post">
    <input type="text" name="name" value="" />
    <input type="text" name="email" value="" />
    <input type="text" name="password" value="" />
    <input type="text" name="ctoken" value=<%=request.getSession(false).getAttribute("ctoken") %> />
    <input type="submit" value="Save" />
</form>