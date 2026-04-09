<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <div class="topbar">
        <div>
            <h1>${pageTitle}</h1>
            <p>Текущий движок: FreeMarker</p>
        </div>
        <div class="nav-links">
            <a href="/thymeleaf/auctions">Thymeleaf</a>
            <a href="/mustache/auctions">Mustache</a>
            <a href="/logout">Выход</a>
        </div>
    </div>

    <#if successMessage?? && successMessage?has_content>
        <div class="success">${successMessage}</div>
    </#if>

    <#if errorMessage?? && errorMessage?has_content>
        <div class="error">${errorMessage}</div>
    </#if>

    <#if canCreateAuction?? && canCreateAuction>
        <div class="card" style="margin-bottom: 24px;">
            <h2>Создать аукцион</h2>
            <form action="/owner/auctions" method="post" class="form-grid">
                <label>Название <input type="text" name="title" required></label>
                <label>Описание <input type="text" name="description" required></label>
                <label>Начальная цена <input type="number" step="0.01" name="startPrice" required></label>
                <label>Дата начала <input type="datetime-local" name="startTime" required></label>
                <label>Дата окончания <input type="datetime-local" name="endTime" required></label>
                <button type="submit">Создать</button>
            </form>
        </div>
    </#if>

    <div class="auction-grid">
        <#list auctions as auction>
            <div class="card">
                <h3>${auction.title}</h3>
                <p>${auction.description}</p>
                <p><strong>Владелец:</strong> ${auction.ownerUsername}</p>
                <p><strong>Статус:</strong> ${auction.status}</p>
                <p><strong>Текущая цена:</strong> ${auction.currentPrice}</p>
                <p><strong>Окончание:</strong> ${auction.endTime}</p>

                <form action="/bids" method="post" class="form-grid">
                    <input type="hidden" name="auctionId" value="${auction.id}">
                    <label>Ставка <input type="number" step="0.01" name="amount" required></label>
                    <button type="submit">Сделать ставку</button>
                </form>
            </div>
        </#list>
    </div>
</div>
</body>
</html>