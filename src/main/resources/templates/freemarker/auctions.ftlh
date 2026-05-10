<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Аукционы</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">CATALOG</p>
                <h2>Каталог аукционов — ${currentUsername}</h2>
                <p>Полный список лотов, ставки и управление страницами через единый интерфейс.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/profile">Профиль</a>
                <#if isOwner><a class="secondary-link" href="/freemarker/owner/dashboard">Кабинет владельца</a></#if>
                <#if isAdmin><a class="secondary-link" href="/freemarker/admin/dashboard">Админка</a></#if>
                <a class="secondary-link" href="/logout">Выход</a>
                <select onchange="window.location.href=this.value">
                    <option value="/thymeleaf/auctions">Thymeleaf</option>
                    <option value="/mustache/auctions">Mustache</option>
                    <option value="/freemarker/auctions" selected>FreeMarker</option>
                </select>
            </div>
        </section>

        <#if successMessage?? && successMessage?has_content><div class="alert success">${successMessage}</div></#if>
        <#if errorMessage?? && errorMessage?has_content><div class="alert error">${errorMessage}</div></#if>

        <#if canCreateAuction>
            <section class="panel-card">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">OWNER</p>
                        <h3>Создать аукцион</h3>
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
        </#if>

        <section class="auction-grid">
            <#list auctions as auction>
                <article class="auction-card">
                    <span class="tag">${auction.status}</span>
                    <h4>${auction.title}</h4>
                    <p class="auction-description">${auction.description}</p>

                    <div class="detail-row">
                        <span>Владелец</span>
                        <strong>${auction.ownerUsername}</strong>
                    </div>
                    <div class="detail-row">
                        <span>Стартовая цена</span>
                        <strong>${auction.startPrice}</strong>
                    </div>
                    <div class="detail-row">
                        <span>Текущая цена</span>
                        <strong>${auction.currentPrice}</strong>
                    </div>
                    <div class="detail-row">
                        <span>Окончание</span>
                        <strong>${auction.formattedEndTime}</strong>
                    </div>

                    <form class="form-grid compact-form" action="/bids" method="post">
                        <input type="hidden" name="auctionId" value="${auction.id}">
                        <label>Сумма ставки
                            <input type="number" step="0.01" name="amount" required>
                        </label>
                        <button class="primary-button" type="submit">Сделать ставку</button>
                    </form>
                </article>
            </#list>
        </section>
    </main>
</div>
</body>
</html>