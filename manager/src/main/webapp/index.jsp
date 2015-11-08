<%@ include file="templates/header.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%@ include file="templates/sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h1 class="page-header">Dashboard</h1>

            <form action="api/admin/start" method="post">
                <button type="submit">Start</button>
            </form>

        </div>
    </div>
</div>
<%@ include file="templates/footer.jsp" %>
