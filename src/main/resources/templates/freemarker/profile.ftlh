<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page-shell single-engine">
    <main class="content full-width-content">
        <section class="hero-card">
            <div>
                <p class="eyebrow">PROFILE</p>
                <h2>Личный кабинет — ${currentUsername}</h2>
                <p>История ставок, участие в торгах и завершённые аукционы пользователя.</p>
            </div>
            <div class="hero-actions">
                <a class="secondary-link" href="/freemarker/auctions">Каталог</a>
                <#if isOwner><a class="secondary-link" href="/freemarker/owner/dashboard">Кабинет владельца</a></#if>
                <#if isAdmin><a class="secondary-link" href="/freemarker/admin/dashboard">Админка</a></#if>
                <a class="secondary-link" href="/logout">Выход</a>
                <select onchange="window.location.href=this.value">
                    <option value="/thymeleaf/profile">Thymeleaf</option>
                    <option value="/mustache/profile">Mustache</option>
                    <option value="/freemarker/profile" selected>FreeMarker</option>
                </select>
            </div>
        </section>

        <div class="dashboard-grid">
            <section class="panel-card">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">BIDS</p>
                        <h3>Мои ставки</h3>
                    </div>
                </div>
                <div class="table-list">
                    <#if bidHistory?size gt 0>
                        <#list bidHistory as bid>
                            <div class="table-row">
                                <div>
                                    <strong>${bid.auctionTitle}</strong>
                                    <p>${bid.auctionStatus}</p>
                                </div>
                                <div>
                                    <strong>${bid.amount}</strong>
                                    <p>${bid.formattedBidTime}</p>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <div class="empty-state">Ставок пока нет.</div>
                    </#if>
                </div>
            </section>

            <section class="panel-card">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">PARTICIPATION</p>
                        <h3>Участвую</h3>
                    </div>
                </div>
                <div class="table-list">
                    <#if participatingAuctions?size gt 0>
                        <#list participatingAuctions as auction>
                            <div class="table-row">
                                <div>
                                    <strong>${auction.title}</strong>
                                    <p>${auction.status}</p>
                                </div>
                                <div>
                                    <strong>${auction.currentPrice}</strong>
                                    <p>${auction.formattedEndTime}</p>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <div class="empty-state">Нет активных участий.</div>
                    </#if>
                </div>
            </section>

            <section class="panel-card">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">RESULT</p>
                        <h3>Выигранные аукционы</h3>
                    </div>
                </div>
                <div class="table-list">
                    <#if winningAuctions?size gt 0>
                        <#list winningAuctions as auction>
                            <div class="table-row">
                                <div>
                                    <strong>${auction.title}</strong>
                                    <p>${auction.ownerUsername}</p>
                                </div>
                                <div>
                                    <strong>${auction.currentPrice}</strong>
                                    <p>${auction.formattedEndTime}</p>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <div class="empty-state">Побед пока нет.</div>
                    </#if>
                </div>
            </section>
        </div>
    </main>
</div>
</body>
</html>