<%--
  Created by IntelliJ IDEA.
  User: Vladislav.Klochkov
  Date: 13.06.2018
  Time: 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page trimDirectiveWhitespaces="true" %>
<html>
<head>
    <title>UralTransCom|BuildReports</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="resources/style.css" rel="stylesheet" type="text/css"/>
    <link rel="shortcut icon" href="resources/favicon.ico" type="image/x-icon">
    <script type="text/javascript"
            src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js">
    </script>

    <!-- Копирайт -->
    <script>
        function cop() {
            document.getElementById("copy").innerText = new Date().getFullYear();
        }
    </script>

</head>

<body onload="cop()">

<div class="one">
    <h1>СЕРВИС ОТЧЕТОВ</h1>
    <div class="train">
    		<img class="image" src="resources/train.jpg">
    </div>
</div>

<div>
    <img class="logo" src="resources/logo.jpg">
</div>

<br><br><br><br><br>
<div class="form">
<br><br><br>
    <form enctype="multipart/form-data" method="post" action="report">
        <p>
            Файл вагонов <input type="file" name="wagons" multiple accept="xlsx"><br>
            Файл заявок <input type="file" name="routes" multiple accept="xlsx"><br>
        <p>Факт</p>
            От <input type="date" name="dateFrom"> До <input type="date" name="dateTo"><br>
        <p>Суточная погрузка</p>
            От <input type="date" name="dateFromSpravo4no"> До <input type="date" name="dateToSpravo4no">
        </p>
        <p>
            <input type="submit" value="Выполнить" class="bot1">
        </p>
    </form>
</div>

<br><br><br>

<div align="center" class="footer">
    Create by Vladislav Klochkov. All rights reserved, <span id="copy"></span>
</div>

</body>
</html>