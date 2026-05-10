<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body class="auth-body">
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">AUCTION</p>
                <h2>Вход в систему</h2>
                <p>Единый интерфейс аукциона. Выберите движок справа и войдите в систему.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/register">Регистрация</a>
                <select onchange="window.location.href=this.value">
                    <option value="/thymeleaf/login">Thymeleaf</option>
                    <option value="/mustache/login">Mustache</option>
                    <option value="/freemarker/login" selected>FreeMarker</option>
                </select>
            </div>
        </section>

        <#if registeredMessage?? && registeredMessage?has_content><div class="alert success">${registeredMessage}</div></#if>
        <#if logoutMessage?? && logoutMessage?has_content><div class="alert success">${logoutMessage}</div></#if>
        <#if errorMessage?? && errorMessage?has_content><div class="alert error">${errorMessage}</div></#if>

        <section class="panel-card">
            <div class="section-heading">
                <div>
                    <p class="eyebrow">AUTH</p>
                    <h3>Авторизация</h3>
                </div>
            </div>
            <form action="/login" method="post" class="form-grid">
                <label>Логин
                    <input type="text" name="username" required>
                </label>
                <label>Пароль
                    <input type="password" name="password" required>
                </label>
                <button class="primary-button" type="submit">Войти</button>
            </form>
        </section>
    </main>
</div>
</body>
</html>