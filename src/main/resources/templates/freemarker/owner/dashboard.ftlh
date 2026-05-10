<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Кабинет владельца</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell">
    <aside class="sidebar">
        <div class="brand-card">
            <span class="brand-badge">Owner</span>
            <h1>Кабинет владельца</h1>
            <p>Управление собственными аукционами.</p>
        </div>
        <nav class="side-nav">
            <a href="/freemarker/auctions">Каталог</a>
            <a href="/freemarker/profile">Мой кабинет</a>
            <a class="active" href="/freemarker/owner/dashboard">Кабинет владельца</a>
            <a href="/logout">Выход</a>
        </nav>
    </aside>

    <main class="content">
        <#if successMessage??><div class="alert success">${successMessage}</div></#if>
        <#if errorMessage??><div class="alert error">${errorMessage}</div></#if>
        <section class="hero-card">
            <div>
                <p class="eyebrow">OWNER</p>
                <h2>Ваши аукционы</h2>
                <p>Просмотр ставок и ручное закрытие лотов.</p>
            </div>
        </section>
        <section class="panel-card">
            <div class="table-list">
                <#list ownerAuctions as auction>
                <div class="table-row with-actions">
                    <div><strong>${auction.title}</strong><p>${auction.formattedEndTime}</p></div>
                    <div><strong>${auction.currentPrice}</strong><p>${auction.status}</p></div>
                    <div class="row-actions">
                        <a class="secondary-link" href="/freemarker/owner/auctions/${auction.id}/bids">История ставок</a>
                        <form action="/freemarker/owner/auctions/${auction.id}/close" method="post">
                            <button class="danger-button" type="submit">Закрыть</button>
                        </form>
                    </div>
                </div>
                </#list>
            </div>
        </section>
    </main>
</div>
</body>
</html>
