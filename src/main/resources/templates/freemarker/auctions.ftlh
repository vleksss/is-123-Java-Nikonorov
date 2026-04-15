<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">Шаблонизатор</p>
                <h2>${pageTitle}</h2>
                <p>FreeMarker-версия каталога аукционов с теми же возможностями участия.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/thymeleaf/auctions">Thymeleaf</a>
                <a class="secondary-link" href="/mustache/auctions">Mustache</a>
                <a class="secondary-link" href="/profile">Личный кабинет</a>
                <a class="secondary-link" href="/logout">Выход</a>
            </div>
        </section>

        <#if successMessage?? && successMessage?has_content>
            <div class="alert success">${successMessage}</div>
        </#if>

        <#if errorMessage?? && errorMessage?has_content>
            <div class="alert error">${errorMessage}</div>
        </#if>

        <#if canCreateAuction?? && canCreateAuction>
            <section class="panel-card">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">Роль OWNER</p>
                        <h3>Создание аукциона</h3>
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
                    <button class="primary-button full-width" type="submit">Создать аукцион</button>
                </form>
            </section>
        </#if>

        <section>
            <div class="section-heading">
                <div>
                    <p class="eyebrow">Аукционы</p>
                    <h3>Каталог лотов</h3>
                </div>
            </div>
            <div class="auction-grid">
                <#list auctions as auction>
                    <article class="auction-card">
                        <div class="auction-meta">
                            <span class="tag">${auction.status}</span>
                            <span class="muted">До ${auction.endTime}</span>
                        </div>
                        <h4>${auction.title}</h4>
                        <p class="auction-description">${auction.description}</p>
                        <div class="detail-row"><span>Владелец</span><strong>${auction.ownerUsername}</strong></div>
                        <div class="detail-row"><span>Текущая цена</span><strong>${auction.currentPrice}</strong></div>
                        <form action="/bids" method="post" class="form-grid compact-form">
                            <input type="hidden" name="auctionId" value="${auction.id}">
                            <label>Ваша ставка
                                <input type="number" step="0.01" name="amount" required>
                            </label>
                            <button class="primary-button" type="submit">Сделать ставку</button>
                        </form>
                    </article>
                </#list>
            </div>
        </section>
    </main>
</div>
</body>
</html>
