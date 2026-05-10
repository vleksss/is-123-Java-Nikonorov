<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ставки по аукциону</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">OWNER</p>
                <h2>Ставки по аукциону #${auctionId}</h2>
                <p>Полная история ставок по выбранному лоту владельца.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/owner/dashboard">Назад</a>
                <a class="secondary-link" href="/freemarker/auctions">Каталог</a>
                <a class="secondary-link" href="/logout">Выход</a>
                <select onchange="window.location.href=this.value">
                    <option value="/thymeleaf/owner/auctions/${auctionId}/bids">Thymeleaf</option>
                    <option value="/mustache/owner/auctions/${auctionId}/bids">Mustache</option>
                    <option value="/freemarker/owner/auctions/${auctionId}/bids" selected>FreeMarker</option>
                </select>
            </div>
        </section>

        <#if successMessage?? && successMessage?has_content><div class="alert success">${successMessage}</div></#if>
        <#if errorMessage?? && errorMessage?has_content><div class="alert error">${errorMessage}</div></#if>

        <section class="panel-card">
            <div class="table-list">
                <#if auctionBids?size gt 0>
                    <#list auctionBids as bid>
                        <div class="table-row">
                            <div>
                                <strong>${bid.bidderUsername}</strong>
                                <p>${bid.formattedBidTime}</p>
                            </div>
                            <div>
                                <strong>${bid.amount}</strong>
                                <p>${bid.auctionStatus}</p>
                            </div>
                        </div>
                    </#list>
                <#else>
                    <div class="empty-state">По этому аукциону ставок пока нет.</div>
                </#if>
            </div>
        </section>
    </main>
</div>
</body>
</html>