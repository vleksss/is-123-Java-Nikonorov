<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Админка</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">ADMIN</p>
                <h2>Панель администратора — ${currentUsername}</h2>
                <p>Управление пользователями и всеми аукционами системы.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/auctions">Каталог</a>
                <a class="secondary-link" href="/freemarker/profile">Профиль</a>
                <#if isOwner><a class="secondary-link" href="/freemarker/owner/dashboard">Кабинет владельца</a></#if>
                <a class="secondary-link" href="/logout">Выход</a>
                <select onchange="window.location.href=this.value">
                    <option value="/thymeleaf/admin/dashboard">Thymeleaf</option>
                    <option value="/mustache/admin/dashboard">Mustache</option>
                    <option value="/freemarker/admin/dashboard" selected>FreeMarker</option>
                </select>
            </div>
        </section>

        <#if successMessage?? && successMessage?has_content><div class="alert success">${successMessage}</div></#if>
        <#if errorMessage?? && errorMessage?has_content><div class="alert error">${errorMessage}</div></#if>

        <div class="dashboard-grid wide-grid">
            <section class="panel-card">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">USERS</p>
                        <h3>Пользователи</h3>
                    </div>
                </div>

                <div class="table-list">
                    <#list users as user>
                        <div class="table-row with-actions">
                            <div>
                                <strong>${user.username}</strong>
                                <p>${user.email}</p>
                            </div>
                            <div>
                                <strong>${user.role}</strong>
                                <p>${user.enabled?string('Активен', 'Заблокирован')}</p>
                            </div>
                            <div class="row-actions stacked-actions">
                                <#if user.protectedUser>
                                    <div class="tag">Недоступно</div>
                                <#else>
                                    <form action="/admin/users/${user.id}/status" method="post">
                                        <input type="hidden" name="enabled" value="${user.nextEnabledValue?string('true','false')}">
                                        <button class="danger-button" type="submit">${user.statusActionLabel}</button>
                                    </form>
                                    <form action="/admin/users/${user.id}/role" method="post">
                                        <input type="hidden" name="role" value="USER">
                                        <button class="secondary-button" type="submit">Сделать USER</button>
                                    </form>
                                    <form action="/admin/users/${user.id}/role" method="post">
                                        <input type="hidden" name="role" value="OWNER">
                                        <button class="secondary-button" type="submit">Сделать OWNER</button>
                                    </form>
                                    <form action="/admin/users/${user.id}/role" method="post">
                                        <input type="hidden" name="role" value="ADMIN">
                                        <button class="secondary-button" type="submit">Сделать ADMIN</button>
                                    </form>
                                </#if>
                            </div>
                        </div>
                    </#list>
                </div>
            </section>

            <section class="panel-card">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">AUCTIONS</p>
                        <h3>Все аукционы</h3>
                    </div>
                </div>

                <div class="table-list">
                    <#list allAuctions as auction>
                        <div class="table-row with-actions">
                            <div>
                                <strong>${auction.title}</strong>
                                <p>${auction.ownerUsername}</p>
                            </div>
                            <div>
                                <strong>${auction.status}</strong>
                                <p>${auction.currentPrice}</p>
                            </div>
                            <div class="row-actions">
                                <form action="/admin/auctions/${auction.id}/close" method="post">
                                    <button class="danger-button" type="submit">Закрыть аукцион</button>
                                </form>
                            </div>
                        </div>
                    </#list>
                </div>
            </section>
        </div>
    </main>
</div>
</body>
</html>