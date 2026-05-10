<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Кабинет владельца</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">OWNER</p>
                <h2>Кабинет владельца — ${currentUsername}</h2>
                <p>Создание лотов, просмотр собственных аукционов и история ставок по каждому лоту.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/auctions">Каталог</a>
                <a class="secondary-link" href="/freemarker/profile">Профиль</a>
                <#if isAdmin><a class="secondary-link" href="/freemarker/admin/dashboard">Админка</a></#if>
                <a class="secondary-link" href="/logout">Выход</a>
                <select onchange="window.location.href=this.value">
                    <option value="/thymeleaf/owner/dashboard">Thymeleaf</option>
                    <option value="/mustache/owner/dashboard">Mustache</option>
                    <option value="/freemarker/owner/dashboard" selected>FreeMarker</option>
                </select>
            </div>
        </section>

        <#if successMessage?? && successMessage?has_content><div class="alert success">${successMessage}</div></#if>
        <#if errorMessage?? && errorMessage?has_content><div class="alert error">${errorMessage}</div></#if>

        <section class="panel-card">
            <div class="section-heading">
                <div>
                    <p class="eyebrow">CREATE</p>
                    <h3>Новый аукцион</h3>
                </div>
            </div>
            <form action="/owner/auctions" method="post" class="form-grid two-columns">
                <label>Название
                    <input type="text" name="title" required>
                </label>
                <label>Начальная цена
                    <input type="number" step="0.01" name="startPrice" required>
                </label>
                <label class="full-width">Описание
                    <textarea name="description" rows="4" required></textarea>
                </label>
                <label>Дата начала
                    <input type="datetime-local" name="startTime" required>
                </label>
                <label>Дата окончания
                    <input type="datetime-local" name="endTime" required>
                </label>
                <button class="primary-button full-width" type="submit">Создать лот</button>
            </form>
        </section>

        <section class="auction-grid">
            <#list ownerAuctions as auction>
                <article class="auction-card">
                    <span class="tag">${auction.status}</span>
                    <h4>${auction.title}</h4>

                    <div class="detail-row">
                        <span>Текущая цена</span>
                        <strong>${auction.currentPrice}</strong>
                    </div>
                    <div class="detail-row">
                        <span>Окончание</span>
                        <strong>${auction.formattedEndTime}</strong>
                    </div>

                    <div class="row-actions compact-form">
                        <a class="secondary-link" href="/freemarker/owner/auctions/${auction.id}/bids">История ставок</a>
                        <form action="/owner/auctions/${auction.id}/close" method="post">
                            <button class="danger-button" type="submit">Закрыть</button>
                        </form>
                    </div>
                </article>
            </#list>
        </section>
    </main>
</div>
</body>
</html>