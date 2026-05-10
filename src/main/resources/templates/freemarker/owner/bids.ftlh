<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ставки по аукциону</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell">
    <aside class="sidebar">
        <div class="brand-card">
            <span class="brand-badge">Owner</span>
            <h1>История ставок</h1>
            <p>Полный список ставок по выбранному аукциону владельца.</p>
        </div>
        <nav class="side-nav">
            <a href="/freemarker/auctions">Каталог</a>
            <a href="/freemarker/owner/dashboard">Кабинет владельца</a>
            <a href="/logout">Выход</a>
        </nav>
    </aside>

    <main class="content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">Аукцион</p>
                <h2>История ставок по лоту #${auctionId}</h2>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/owner/dashboard">Назад</a>
            </div>
        </section>
        <section class="panel-card">
            <div class="table-list">
                <#list auctionBids as bid>
                <div class="table-row">
                    <div><strong>${bid.bidderUsername}</strong><p>${bid.formattedBidTime}</p></div>
                    <div><strong>${bid.amount}</strong><p>${bid.auctionStatus}</p></div>
                </div>
                </#list>
            </div>
        </section>
    </main>
</div>
</body>
</html>
