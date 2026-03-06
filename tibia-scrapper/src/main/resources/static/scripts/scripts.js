let globalHuntsData = [];

document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById('notif-badge')) {
        atualizarContadorNotificacoes();
        setInterval(atualizarContadorNotificacoes, 15000);
    }

    fetch('/json/hunts.json')
        .then(response => response.json())
        .then(data => {
            globalHuntsData = data;

            if (document.getElementById('inputObjetivoEspecifico')) {
                initHuntAutocomplete('inputObjetivoEspecifico', 'sugestoesParty', 'infoHuntParty');
            }
            if (document.getElementById('nomeHunt')) {
                initHuntAutocomplete('nomeHunt', 'sugestoesHunt', 'infoHuntDetalhe');
            }

            if (document.getElementById('hunt-descriptions-container') && document.getElementById('huntNameForDesc')) {
                populateHuntDescriptions();
            }
        })
        .catch(err => console.warn('Hunts JSON não encontrado. Funcionalidade de autocomplete desativada.', err));
});

function atualizarContadorNotificacoes() {
    fetch('/notificacoes/api/count')
        .then(response => response.json())
        .then(count => {
            const badge = document.getElementById('notif-badge');
            if (badge) {
                if (count > 0) {
                    badge.innerText = count > 20 ? '20+' : count;
                    badge.classList.remove('d-none');
                } else {
                    badge.classList.add('d-none');
                }
            }
        })
        .catch(err => console.error("Erro ao buscar notificações", err));
}

function carregarNotificacoes() {
    const container = document.getElementById('lista-notificacoes-preview');
    if (!container) return;

    container.innerHTML = '<li class="text-center p-3 text-secondary"><i class="fas fa-spinner fa-spin"></i> Carregando...</li>';

    fetch('/notificacoes/api/resumo')
        .then(response => response.json())
        .then(data => {
            container.innerHTML = '';

            if (data.length === 0) {
                container.innerHTML = '<li class="dropdown-item text-secondary small text-center py-3">Nenhuma notificação recente.</li>';
                return;
            }

            data.forEach(notif => {
                const lidaClass = notif.lida ? 'text-secondary opacity-75' : 'text-white fw-bold bg-secondary bg-opacity-10';

                const item = `
                    <li>
                        <a class="dropdown-item d-flex align-items-start gap-2 py-2 border-bottom border-secondary ${lidaClass}" 
                           href="${notif.link}" style="white-space: normal;">
                            <div class="mt-1"><i class="fa-solid ${notif.icone}"></i></div>
                            <div style="width: 100%;">
                                <div class="small">
                                    <span class="text-warning">${notif.remetente}</span> ${notif.mensagem}
                                </div>
                                <div class="text-secondary text-end" style="font-size: 0.65em;">${notif.dataFormatada}</div>
                            </div>
                        </a>
                    </li>
                `;
                container.innerHTML += item;
            });
        })
        .catch(err => {
            container.innerHTML = '<li class="dropdown-item text-danger small text-center">Erro ao carregar notificações.</li>';
        });
}

function initHuntAutocomplete(inputId, suggestionsId, infoCardId) {
    const input = document.getElementById(inputId);
    const suggestionsBox = document.getElementById(suggestionsId);
    const infoCard = document.getElementById(infoCardId);

    if (!input || !suggestionsBox) return;

    input.addEventListener('input', function () {
        const val = this.value.toLowerCase();
        suggestionsBox.innerHTML = '';

        if (!val || val.length < 2) {
            suggestionsBox.style.display = 'none';
            if (infoCard) infoCard.classList.add('d-none');
            return;
        }

        const matches = globalHuntsData
            .filter(h => h.name && h.name.toLowerCase().includes(val))
            .slice(0, 8);

        if (matches.length > 0) {
            suggestionsBox.style.display = 'block';
            matches.forEach(match => {
                const a = document.createElement('a');
                a.href = '#';
                a.className = 'list-group-item list-group-item-action bg-dark text-white border-secondary p-2 d-flex align-items-center gap-2';
                a.style.cursor = 'pointer';

                a.innerHTML = `
                    <i class="fa-solid fa-map-location-dot text-info"></i>
                    <div class="d-flex flex-column">
                        <span class="fw-bold text-warning" style="font-size: 0.85em;">${match.name}</span>
                        <span class="text-secondary" style="font-size: 0.7em;"><i class="fa-solid fa-city"></i> ${match.city || 'Desconhecida'}</span>
                    </div>
                `;

                a.addEventListener('click', function (e) {
                    e.preventDefault();
                    input.value = match.name;
                    suggestionsBox.style.display = 'none';
                    showHuntInfoCard(match, infoCard);
                });

                suggestionsBox.appendChild(a);
            });
        } else {
            suggestionsBox.style.display = 'none';
        }
    });

    document.addEventListener('click', function (e) {
        if (e.target !== input && !suggestionsBox.contains(e.target)) {
            suggestionsBox.style.display = 'none';
        }
    });
}

function showHuntInfoCard(hunt, infoCard) {
    if (!infoCard) return;
    infoCard.classList.remove('d-none');

    const minLvl = hunt.lvlknights || hunt.lvlmages || hunt.lvlpaladins || '?';

    infoCard.innerHTML = `
        <div class="d-flex align-items-center gap-3">
            <div class="bg-black rounded border border-secondary p-2 text-center d-flex align-items-center justify-content-center" style="width: 48px; height: 48px; flex-shrink: 0;">
                <i class="fa-solid fa-map text-warning fs-4"></i>
            </div>
            <div class="flex-grow-1">
                <h6 class="text-info mb-1" style="font-size: 0.85rem;">${hunt.name}</h6>
                <div class="d-flex flex-wrap gap-2 text-secondary" style="font-size: 0.75rem;">
                    <span title="Cidade"><i class="fa-solid fa-city text-light"></i> ${hunt.city || '-'}</span>
                    <span title="Level Mínimo"><i class="fa-solid fa-turn-up text-light"></i> Lvl ${minLvl}+</span>
                    <span title="Exp Média"><i class="fa-solid fa-star text-light"></i> Exp: ${hunt.exp || '?'}</span>
                    <span title="Loot Médio"><i class="fa-solid fa-coins text-light"></i> Loot: ${hunt.loot || '?'}</span>
                </div>
            </div>
        </div>
    `;
}

function populateHuntDescriptions() {
    if (!globalHuntsData || globalHuntsData.length === 0) return;

    const container = document.getElementById('hunt-descriptions-container');
    const huntNameEl = document.getElementById('huntNameForDesc');

    if (!container || !huntNameEl) return;

    const huntName = huntNameEl.innerText.trim().toLowerCase();

    const hunt = globalHuntsData.find(h => h.name && h.name.toLowerCase() === huntName);

    if (!hunt) {
        container.innerHTML = '<span class="text-secondary small">Nenhuma descrição oficial encontrada no wiki para esta hunt.</span>';
        return;
    }

    const loc = hunt.location || 'Desconhecida';
    const cleanLoc = loc.replace(/\{\{[^}]+\}\}/g, '').trim();

    const vocations = hunt.vocation || 'Todas';

    const lvlP = hunt.lvlpaladins ? `Level ${hunt.lvlpaladins}` : '-';
    const skP = hunt.skpaladins ? `Distance ${hunt.skpaladins}` : '';
    const paladinText = lvlP !== '-' ? `Paladin: <span class="text-white">${lvlP} ${skP ? '(' + skP + ')' : ''}</span>` : '';

    const lvlK = hunt.lvlknights ? `Level ${hunt.lvlknights}` : '-';
    const skK = hunt.skknights ? `Skills ${hunt.skknights}` : '';
    const defK = hunt.defknights ? `Shield ${hunt.defknights}` : '';
    const knightText = lvlK !== '-' ? `Knight: <span class="text-white">${lvlK} ${skK || defK ? '(' + skK + (skK && defK ? ' / ' : '') + defK + ')' : ''}</span>` : '';

    const lvlM = hunt.lvlmages ? `Level ${hunt.lvlmages}` : '-';
    const mageText = lvlM !== '-' ? `Mages: <span class="text-white">${lvlM}</span>` : '';

    const reqsArr = [paladinText, knightText, mageText].filter(t => t !== '');
    const reqsHtml = reqsArr.length > 0 ? reqsArr.join(' <span class="text-secondary mx-1">|</span> ') : '<span class="text-white">Sem requisitos específicos</span>';

    const lootStar = hunt.lootstar || '?';
    const loot = hunt.loot || '?';
    const expStar = hunt.expstar || '?';
    const exp = hunt.exp || '?';

    container.innerHTML = `
        <div class="row g-3" style="font-family: 'Verdana', sans-serif; font-size: 0.85em;">
            <div class="col-12">
                <span class="text-secondary d-block mb-1">Localização:</span>
                <span class="text-white"><i class="fa-solid fa-earth-americas text-info me-1"></i> ${cleanLoc || loc}</span>
            </div>
            <div class="col-md-12">
                <span class="text-secondary d-block mb-1">Vocações:</span>
                <span class="text-white"><i class="fa-solid fa-users text-primary me-1"></i> ${vocations}</span>
            </div>
            <div class="col-md-12">
                <span class="text-secondary d-block mb-1">Recomendações:</span>
                <span class="text-warning"><i class="fa-solid fa-turn-up text-success me-1"></i> ${reqsHtml}</span>
            </div>
            <div class="col-md-6 border-top border-secondary pt-2 mt-3">
                <span class="text-secondary d-block mb-1">Loot:</span>
                <span class="text-white"><i class="fa-solid fa-coins text-warning me-1"></i> ${loot} <span class="text-secondary ms-1">(Star: ${lootStar})</span></span>
            </div>
            <div class="col-md-6 border-top border-secondary pt-2 mt-3">
                <span class="text-secondary d-block mb-1">Exp:</span>
                <span class="text-white"><i class="fa-solid fa-star text-info me-1"></i> ${exp} <span class="text-secondary ms-1">(Star: ${expStar})</span></span>
            </div>
        </div>
    `;
}