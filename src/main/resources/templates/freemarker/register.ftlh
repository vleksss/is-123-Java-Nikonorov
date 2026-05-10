<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body class="auth-body">
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">AUCTION</p>
                <h2>Регистрация</h2>
                <p>Создайте обычного пользователя системы. Роль OWNER и ADMIN через регистрацию не выдаётся.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/login">Вход</a>
                <select onchange="window.location.href=this.value">
                    <option value="/thymeleaf/register">Thymeleaf</option>
                    <option value="/mustache/register">Mustache</option>
                    <option value="/freemarker/register" selected>FreeMarker</option>
                </select>
            </div>
        </section>

        <#if errorMessage?? && errorMessage?has_content><div class="alert error">${errorMessage}</div></#if>

        <section class="panel-card">
            <div class="section-heading">
                <div>
                    <p class="eyebrow">AUTH</p>
                    <h3>Данные пользователя</h3>
                </div>
            </div>
            <form action="/register" method="post" class="form-grid">
                <input type="hidden" name="engine" value="freemarker">
                <label>Логин
                    <input type="text" name="username" value="${(registerRequest.username)!''}" required>
                </label>
                <label>Email
                    <input type="email" name="email" value="${(registerRequest.email)!''}" required>
                </label>
                <label>Пароль
                    <input type="password" name="password" required>
                </label>
                <button class="primary-button" type="submit">Зарегистрироваться</button>
            </form>
        </section>
    </main>
</div>
</body>
</html>