<%--
  Created by IntelliJ IDEA.
  User: Vladislav.Klochkov
  Date: 15.01.2018
  Time: 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>UralTransCom</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="resources/style.css" rel="stylesheet" type="text/css"/>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,600,700&subset=latin,cyrillic" rel="stylesheet"
          type="text/css">
    <script type="text/javascript"
            src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js">
    </script>

    <!-- Вызов лоадера -->
    <script>
        jQuery(function ($) {
            $('#startProcess').on('click', function (e) {
                $('.content').toggleClass('hide');
            });
        });
    </script>

    <!-- Блокировка экрана -->
    <script type="text/javascript">
        function lockScreen()
        {
            var lock = document.getElementById('lockPane');
            if (lock)
                lock.className = 'lockScreenOn';
        }
    </script>

    <style>
        body {
            font: 14px/1 "Open Sans", sans-serif;
        }

        /* Настрйоки вкладок*/
        /* Стили секций с содержанием */
        .tabs > section {
            display: none;
            max-width: 100%;
            padding: 15px;
            background: #fff;
            border: 1px solid #ddd;
        }

        .tabs > section > p {
            margin: 0 0 5px;
            line-height: 1.5;
            color: #383838;
            /* прикрутим анимацию */
            -webkit-animation-duration: 1s;
            animation-duration: 1s;
            -webkit-animation-fill-mode: both;
            animation-fill-mode: both;
            -webkit-animation-name: fadeIn;
            animation-name: fadeIn;
        }

        /* Описываем анимацию свойства opacity */
        @-webkit-keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        /* Прячем чекбоксы */
        .tabs > input {
            display: none;
            position: absolute;
        }

        /* Стили переключателей вкладок (табов) */
        .tabs > label {
            display: inline-block;
            margin: 0 0 -1px;
            padding: 15px 25px;
            font-weight: 600;
            text-align: center;
            color: #aaa;
            border: 0px solid #ddd;
            border-width: 1px 1px 1px 1px;
            background: #f1f1f1;
            border-radius: 3px 3px 0 0;
        }

        /* Шрифт-иконки от Font Awesome в формате Unicode */
        .tabs > label:before {
            font-family: fontawesome;
            font-weight: normal;
            margin-right: 10px;
        }

        /* Изменения стиля переключателей вкладок при наведении */
        .tabs > label:hover {
            color: #888;
            cursor: pointer;
        }

        /* Стили для активной вкладки */
        .tabs > input:checked + label {
            color: #555;
            border-top: 1px solid #4B79BB;
            border-bottom: 1px solid #fff;
            background: #fff;
        }

        /* Активация секций с помощью псевдокласса :checked */
        #tab1:checked ~ #content-tab1,
        #tab2:checked ~ #content-tab2,
        #tab3:checked ~ #content-tab3,
        #tab4:checked ~ #content-tab4 {
            display: block;
        }

        /* Убираем текст с переключателей и оставляем иконки на малых экранах*/
        @media screen and (max-width: 680px) {
            .tabs > label {
                font-size: 0;
            }

            .tabs > label:before {
                margin: 0;
                font-size: 18px;
            }
        }

        /* Изменяем внутренние отступы переключателей для малых экранов */
        @media screen and (max-width: 400px) {
            .tabs > label {
                padding: 15px;
            }
        }


        /* Стили лоадера */
        .hide {
            display: none;
        }
        .content {
            padding: 15px;
            margin:0 auto;
            left:50%;
            top:50%;

        }

        p {
            margin: 0;
            padding: 10px 0;
            color: #777;
        }

        .load-wrapp {
            float: left;
            width: 100px;
            height: 100px;
            margin: 0 10px 10px 0;
            padding: 20px 20px 20px;
            border-radius: 5px;
            text-align: center;
            background-color: #d8d8d8;
        }

        .load-wrapp p {padding: 0 0 20px;}
        .load-wrapp:last-child {margin-right: 0;}

        .line {
            display: inline-block;
            width: 15px;
            height: 15px;
            border-radius: 15px;
            background-color: #4b9cdb;
        }

        .load-3 .line:nth-last-child(1) {animation: loadingC .6s .1s linear infinite;}
        .load-3 .line:nth-last-child(2) {animation: loadingC .6s .2s linear infinite;}
        .load-3 .line:nth-last-child(3) {animation: loadingC .6s .3s linear infinite;}

        @keyframes loadingC {
            0 {transform: translate(0,0);}
            50% {transform: translate(0,15px);}
            100% {transform: translate(0,0);}
        }

        /* Блокировка экрана */
        .lockScreenOff {
            display: none;
            visibility: hidden;
        }
        .lockScreenOn {
            display: block;
            visibility: visible;
            position: absolute;
            z-index: 999;
            top: 0px;
            left: 0px;
            width: 100%;
            height: 100%;
            background-color: #ccc;
            text-align: center;
            filter: alpha(opacity=10);
            opacity: 0.10;
        }
    </style>
</head>
<div id="lockPane" class="lockScreenOff"></div>
<script>
    function cop() {
        document.getElementById("copy").innerText = new Date().getFullYear();
    }
</script>
<body onload="cop()">
<div class="one">
    <h1>Сервис распределения вагонов</h1>
    <img src="resources/logo.jpg">
</div>

<script type="text/javascript">
    $(document).ready(function () {

        $(popup_bg).click(function () {
            $(popup).fadeOut(800);
        });

    });

    function showPopup() {
        $(popup).fadeIn(800);
    }
</script>
<input type="button" value="Создать отчет" onclick="showPopup()"
       class="bot1">
<form action="/" method="get">
    <input type="submit" value="Очистить форму"
           class="bot1">
</form>
<table class="table_report">
    <tr>
        <td class="td_report">
            <div id="popup"
                 style="position: absolute; height: 100%; width: 100%; top: 0; left: 0; display: none;">
                <div id="popup_bg"
                     style="background: rgba(0, 0, 0, 0.2); position: absolute; z-index: 1; height: 100%; width: 100%;">
                </div>
                <div class="form">
                    <form enctype="multipart/form-data" method="post" action="/reports">
                        <p>Файл заявок</p>
                        <input type="file" name="routes" multiple accept="xlsx">
                        <p>Файл вагонов</p>
                        <input type="file" name="wagons" multiple accept="xlsx">
                        <p>
                            <input type="submit" value="Start" class="bot2" id="startProcess" onclick="lockScreen();">
                        </p>
                    </form>
                </div>
            </div>
        </td>
    </tr>
</table>

<div class="content hide">
    <div class="load-wrapp">
        <div class="load-3">
            <p>Обработка</p>
            <div class="line"></div>
            <div class="line"></div>
            <div class="line"></div>
        </div>
    </div>
</div>

<div class="container">
    <div class="tabs">
        <input id="tab1" type="radio" name="tabs" checked>
        <label for="tab1" title="Распределенные рейсы">Распределенные рейсы</label>

        <input id="tab2" type="radio" name="tabs">
        <label for="tab2" title="Нераспределенные рейсы">Нераспределенные рейсы</label>

        <input id="tab3" type="radio" name="tabs">
        <label for="tab3" title="Нераспределенные вагоны">Нераспределенные вагоны</label>

        <input id="tab4" type="radio" name="tabs">
        <label for="tab4" title="Ошибки">Ошибки</label>

        <section id="content-tab1">
            <p>
                <c:if test="${!empty reportListOfDistributedRoutesAndWagons}">
            <table>
                <c:forEach items="${reportListOfDistributedRoutesAndWagons}" var="reportList">
                    <tr>
                        <td>${reportList}</td>
                    </tr>
                </c:forEach>
            </table>
            </c:if>
            </p>
        </section>
        <section id="content-tab2">
            <p>
                <c:if test="${!empty reportListOfDistributedRoutes}">
            <table>
                <c:forEach items="${reportListOfDistributedRoutes}" var="reportListNo">
                    <tr>
                        <td>${reportListNo}</td>
                    </tr>
                </c:forEach>
            </table>
            </c:if>
            </p>
        </section>
        <section id="content-tab3">
            <p>
                <c:if test="${!empty reportListOfDistributedWagons}">
            <table>
                <c:forEach items="${reportListOfDistributedWagons}" var="reportListNoWagon">
                    <tr>
                        <td>${reportListNoWagon}</td>
                    </tr>
                </c:forEach>
            </table>
            </c:if>
            </p>
        </section>
        <section id="content-tab4">
            <p>
                <c:if test="${!empty reportListOfError}">
            <table>
                <c:forEach items="${reportListOfError}" var="Error">
                    <tr>
                        <td>${Error}</td>
                    </tr>
                </c:forEach>
            </table>
            </c:if>
            </p>
        </section>
    </div>

    <div align="center" id="footer">
        Copyright &copy; ООО "Уральская транспортная компания" <span id="copy"></span>.
        Create by Vladislav Klochkov. All rights reserved
    </div>
</div>
</body>
</html>
