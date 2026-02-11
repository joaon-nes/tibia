let slotAtual = null;

let setEquipado = {
    "Helmets": null,
    "Armors": null,
    "Legs": null,
    "Boots": null,
    "Shields": null,
    "Spellbooks": null, 
    "Weapons": null, 
    "Amulets": null,
    "Rings": null,
    "Backpacks": null,
    "Extra Slot": null
};

function getSlotId(categoria) {
    if (categoria === 'Extra Slot') return 'slot-Extra';
    return `slot-${categoria}`;
}

function limparTextoParaBuild(texto) {
    if (!texto || texto === 'Nenhum' || texto === 'Nenhuma' || texto === '0' || texto === '') return '';
    return texto;
}

function abrirModal(categoriaSlot) {
    slotAtual = categoriaSlot;
    
    let categoriaApi = categoriaSlot;
    const vocElement = document.getElementById('char-vocacao');
    const voc = vocElement ? vocElement.value : "";

    if (categoriaSlot === 'Weapons') {
        if (voc === 'Knight') {
            categoriaApi = "Swords,Axes,Clubs";
        } else if (voc === 'Paladin') {
            categoriaApi = "Distance";
        } else if (voc === 'Sorcerer' || voc === 'Druid') {
            categoriaApi = "Wands,Rods";
        } else if (voc === 'Monk') {
            categoriaApi = "Fist";
        } else {
            categoriaApi = "Swords,Axes,Clubs,Wands,Rods,Distance,Fist";
        }
    } 
    else if (categoriaSlot === 'Shields') {
        if (voc === 'Sorcerer' || voc === 'Druid') {
            categoriaApi = "Shields,Spellbooks";
        } else if (voc === 'Paladin') {
            categoriaApi = "Shields,Quivers";
        } else if (voc === 'Knight' || voc === 'Monk') {
            categoriaApi = "Shields";
        } else {
            categoriaApi = "Shields,Spellbooks,Quivers";
        }
    }

    const modalTitulo = document.getElementById('modal-titulo');
    modalTitulo.innerText = `Selecionar ${categoriaSlot}`;
    modalTitulo.setAttribute('data-categoria-api', categoriaApi); 

    carregarItens(categoriaApi);
    const modal = new bootstrap.Modal(document.getElementById('modalSelecao'));
    modal.show();
}

function carregarItens(categoriaApi) {
    const listaDiv = document.getElementById('lista-itens-modal');
    listaDiv.innerHTML = '<div class="text-center w-100 text-white py-5"><i class="fas fa-spinner fa-spin fa-2x"></i><br>Carregando itens...</div>';

    const levelChar = document.getElementById('char-level').value || 9999;
    const vocacaoChar = document.getElementById('char-vocacao').value || "";
    const sortOrder = document.getElementById('modal-sort-order').value;

    let url = `/api/enciclopedia?categoria=${categoriaApi}&level=${levelChar}`;
    if (vocacaoChar) {
        url += `&vocacao=${vocacaoChar}`;
    }

    fetch(url)
        .then(response => response.json())
        .then(data => {
            listaDiv.innerHTML = '';

            if (data.length === 0) {
                listaDiv.innerHTML = '<div class="text-center w-100 text-muted py-5">Nenhum item encontrado para sua vocação/nível.</div>';
                return;
            }

            data.sort((a, b) => {
                const lvlA = a.levelMinimo || 0;
                const lvlB = b.levelMinimo || 0;
                return sortOrder === 'asc' ? lvlA - lvlB : lvlB - lvlA;
            });

            data.forEach(item => {
                const div = document.createElement('div');
                div.className = 'col-md-4 col-6 text-center p-2 border border-secondary item-selector-card';
                div.style.cursor = 'pointer';
                div.style.backgroundColor = '#111';

                let stats = [];
                if (item.danoElemental != null) {
                    const temElemento = item.danoElemental &&
                        item.danoElemental.toLowerCase() !== 'nenhum' &&
                        item.danoElemental.toLowerCase() !== 'nenhuma' &&
                        item.danoElemental.toLowerCase() !== '';
                    if (item.ataque > 0) {
                        stats.push(`Atk: ${item.ataque}${temElemento ? ' + ' + item.danoElemental : ''}`);
                    }
                } else if (item.ataque > 0) {
                     stats.push(`Atk: ${item.ataque}`);
                }

                if (item.volume > 0) stats.push(`Vol: ${item.volume}`);
                if (item.defesa > 0) stats.push(`Def: ${item.defesa}`);
                if (item.armadura > 0) stats.push(`Arm: ${item.armadura}`);
                if (item.hitPercent && item.hitPercent !== '0') stats.push(`Hit: ${item.hitPercent}%`);
                if (item.alcance > 1) stats.push(`Range: ${item.alcance}`);
                
                if (item.elementalBond != null) {
                    const bond = item.elementalBond &&
                        item.elementalBond.toLowerCase() !== 'nenhum' &&
                        item.elementalBond.toLowerCase() !== 'nenhuma' &&
                        item.elementalBond.toLowerCase() !== '';
                    if(bond) stats.push(`Bond: ${item.elementalBond}`);
                }

                const bSkill = limparTextoParaBuild(item.bonusSkill);
                const bProt = limparTextoParaBuild(item.protecao);
                let extras = (bSkill && bProt) ? `${bSkill}, ${bProt}` : (bSkill || bProt || "");

                div.innerHTML = `
                    <div class="p-1 h-100 d-flex flex-column align-items-center justify-content-between">
                        <img src="${item.imagemUrl || 'https://www.tibiawiki.com.br/images/b/b4/Unknown_Item.gif'}" width="32" class="mb-1" onerror="this.src='https://www.tibiawiki.com.br/images/b/b4/Unknown_Item.gif'">
                        <div class="item-name-modal" style="font-size: 10px; color: #fff; font-weight: bold; line-height: 1.1; margin-bottom: 2px;">
                            ${item.nome}
                        </div>
                        <div class="text-white-50" style="font-size: 11px; line-height: 1.2;">${stats.join(' | ')}</div>
                        <div class="text-warning" style="font-size: 10px; font-weight: bold;">Lvl: ${item.levelMinimo > 0 ? item.levelMinimo : '-'}</div>
                        <div class="text-info" style="font-size: 10px; font-style: italic;">${extras}</div>
                    </div>
                `;

                div.onclick = () => selecionarItem(item);
                listaDiv.appendChild(div);
            });
        })
        .catch(error => {
            console.error('Erro:', error);
            listaDiv.innerHTML = '<div class="text-danger text-center w-100 py-4">Erro ao carregar itens. Tente novamente.</div>';
        });
}

function renderizarItensNoModal() {
    const categoriaApi = document.getElementById('modal-titulo').getAttribute('data-categoria-api');
    if (categoriaApi) {
        carregarItens(categoriaApi);
    }
}

function selecionarItem(item) {
    if (!slotAtual) return;

    const idSlot = getSlotId(slotAtual);
    const slotDiv = document.getElementById(idSlot);
    
    if (!slotDiv) {
        console.error("Slot não encontrado no HTML:", idSlot);
        return;
    }

    const imgUrl = item.imagemUrl || 'https://www.tibiawiki.com.br/images/b/b4/Unknown_Item.gif';
    
    if (!slotDiv.getAttribute('data-empty-img')) {
        const imgTag = slotDiv.querySelector('img');
        if (imgTag) slotDiv.setAttribute('data-empty-img', imgTag.src);
    }

    const imgTag = slotDiv.querySelector('img');
    if (imgTag) imgTag.src = imgUrl;
    
    slotDiv.classList.add('border-primary'); 
    slotDiv.style.boxShadow = "0 0 15px rgba(0, 183, 255, 0.6)";

    setEquipado[slotAtual] = item;

    const modalEl = document.getElementById('modalSelecao');
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();

    calcularStatus();
}

function removerItem(event, slot) {
    event.preventDefault(); 
    
    const idSlot = getSlotId(slot);
    const slotDiv = document.getElementById(idSlot);
    
    if (!slotDiv) return;

    const emptyImg = slotDiv.getAttribute('data-empty-img');
    
    if (emptyImg) {
        const imgTag = slotDiv.querySelector('img');
        if (imgTag) imgTag.src = emptyImg;
    }
    slotDiv.classList.remove('border-primary');
    slotDiv.style.boxShadow = "none";
    
    setEquipado[slot] = null;
    calcularStatus();
}

function limparSet() {
    Object.keys(setEquipado).forEach(slot => {
        const idSlot = getSlotId(slot);
        const slotDiv = document.getElementById(idSlot);
        
        if (slotDiv) {
            const emptyImg = slotDiv.getAttribute('data-empty-img');
            if (emptyImg) {
                const imgTag = slotDiv.querySelector('img');
                if (imgTag) imgTag.src = emptyImg;
            }
            slotDiv.classList.remove('border-primary');
            slotDiv.style.boxShadow = "none";
        }
        setEquipado[slot] = null;
    });
    calcularStatus();
}

function calcularStatus() {
    let totalArm = 0;
    let totalDef = 0; 
    
    let skillsMap = {};
    let specialBonuses = [];
    
    let protecoes = {
        'Physical': 0, 'Earth': 0, 'Fire': 0, 
        'Ice': 0, 'Energy': 0, 'Death': 0, 'Holy': 0
    };

    Object.values(setEquipado).forEach(item => {
        if (item) {
            if (item.armadura) totalArm += item.armadura;
            if (item.defesa) totalDef += item.defesa;
            if (item.modDefesa && !isNaN(parseInt(item.modDefesa))) totalDef += parseInt(item.modDefesa);

            if (item.bonusSkill && item.bonusSkill !== 'Nenhum' && item.bonusSkill !== 'Nenhuma') {
                const regexSkill = /([a-zA-Z\s]+)\s([+-]?\d+)/g;
                let match;
                let foundAny = false;

                while ((match = regexSkill.exec(item.bonusSkill)) !== null) {
                    foundAny = true;
                    let nomeSkill = match[1].trim();
                    let valorSkill = parseInt(match[2]);

                    if (skillsMap[nomeSkill]) {
                        skillsMap[nomeSkill] += valorSkill;
                    } else {
                        skillsMap[nomeSkill] = valorSkill;
                    }
                }

                if (!foundAny) {
                    if (!specialBonuses.includes(item.bonusSkill)) {
                        specialBonuses.push(item.bonusSkill);
                    }
                }
            }

            if (item.protecao && item.protecao !== 'Nenhum' && item.protecao !== 'Nenhuma') {
                const regexProt = /([a-zA-Z]+)\s*([+-]?\d+)%/g;
                let match;
                while ((match = regexProt.exec(item.protecao)) !== null) {
                    const tipo = match[1];
                    const valor = parseInt(match[2]);
                    const chave = tipo.charAt(0).toUpperCase() + tipo.slice(1).toLowerCase();
                    if (protecoes.hasOwnProperty(chave)) {
                        protecoes[chave] += valor;
                    }
                }
            }
        }
    });

    const elArm = document.getElementById('total-arm');
    const elDef = document.getElementById('total-def');
    if(elArm) elArm.innerText = totalArm;
    if(elDef) elDef.innerText = totalDef;

    const bonusDiv = document.getElementById('total-bonus');
    if (bonusDiv) {
        bonusDiv.innerHTML = '';
        let temBonus = false;

        for (const [nome, valor] of Object.entries(skillsMap)) {
            const row = document.createElement('div');
            row.className = 'skill-row text-warning small';
            const sinal = valor >= 0 ? '+' : '';
            row.innerHTML = `<i class="fa-solid fa-star me-1" style="font-size: 0.6rem;"></i> ${nome} ${sinal}${valor}`; 
            bonusDiv.appendChild(row);
            temBonus = true;
        }

        specialBonuses.forEach(b => {
            const row = document.createElement('div');
            row.className = 'skill-row text-warning small';
            row.innerHTML = `<i class="fa-solid fa-star me-1" style="font-size: 0.6rem;"></i> ${b}`; 
            bonusDiv.appendChild(row);
            temBonus = true;
        });

        for (const [elemento, valor] of Object.entries(protecoes)) {
            if (valor !== 0) {
                const row = document.createElement('div');
                row.className = 'skill-row small d-flex justify-content-between align-items-center mb-1';
                
                const corValor = valor > 0 ? 'text-success' : 'text-danger';
                const sinal = valor > 0 ? '+' : '';
                
                let icone = '';
                switch(elemento) {
                    case 'Fire': icone = '<i class="fa-solid fa-fire text-danger"></i>'; break;
                    case 'Ice': icone = '<i class="fa-regular fa-snowflake text-info"></i>'; break;
                    case 'Energy': icone = '<i class="fa-solid fa-bolt text-primary"></i>'; break;
                    case 'Earth': icone = '<i class="fa-solid fa-leaf text-success"></i>'; break;
                    case 'Death': icone = '<i class="fa-solid fa-skull text-secondary"></i>'; break;
                    case 'Holy': icone = '<i class="fa-solid fa-cross text-warning"></i>'; break;
                    default: icone = '<i class="fa-solid fa-shield-halved"></i>';
                }

                row.innerHTML = `
                    <span class="text-secondary">${icone} ${elemento}</span>
                    <span class="fw-bold ${corValor}">${sinal}${valor}%</span>
                `;
                bonusDiv.appendChild(row);
                temBonus = true;
            }
        }

        if (!temBonus) {
            bonusDiv.innerHTML = '<div class="text-center text-muted small py-4"><i class="fa-solid fa-ghost fs-4 mb-2 d-block opacity-25"></i>Nenhum bônus ativo.</div>';
        }
    }

    atualizarGrafico(protecoes);
}

function atualizarGrafico(protecoes) {
    if (window.myRadarChart) {
        const dataArray = [
            protecoes['Physical'],
            protecoes['Earth'],
            protecoes['Fire'],
            protecoes['Ice'],
            protecoes['Energy'],
            protecoes['Death'],
            protecoes['Holy']
        ];
        window.myRadarChart.data.datasets[0].data = dataArray;
        window.myRadarChart.update();
    }
}