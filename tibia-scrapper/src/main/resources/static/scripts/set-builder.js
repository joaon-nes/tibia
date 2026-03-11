// constantes e config globais
const slotMatrix = [
    ["Amulets", "Helmets", "Backpacks"],
    ["Weapons", "Armors", "Shields"],
    ["Rings", "Legs", "ExtraSlot"],
    [null, "Boots", null]
];

const imagensPadrao = {
    "Amulets": "https://www.tibiawiki.com.br/images/3/3e/Crystal_Necklace.gif",
    "Helmets": "https://www.tibiawiki.com.br/images/2/29/Leather_Helmet.gif",
    "Backpacks": "https://www.tibiawiki.com.br/images/9/9a/Backpack.gif",
    "Weapons": "https://www.tibiawiki.com.br/images/1/1f/Sword.gif",
    "Armors": "https://www.tibiawiki.com.br/images/0/0d/Leather_Armor.gif",
    "Shields": "https://www.tibiawiki.com.br/images/2/2d/Wooden_Shield.gif",
    "Rings": "https://www.tibiawiki.com.br/images/d/db/Axe_Ring.gif",
    "Legs": "https://www.tibiawiki.com.br/images/0/07/Leather_Legs.gif",
    "ExtraSlot": "https://www.tibiawiki.com.br/images/4/46/Moon_Mirror.gif",
    "Boots": "https://www.tibiawiki.com.br/images/9/94/Leather_Boots.gif"
};

const exaltationForgeData = {
    "Weapons": { efeito: "Onslaught", percentuais: [0, 0.50, 1.05, 1.70, 2.45, 3.30, 4.25, 5.30, 6.45, 7.70, 9.05] },
    "Armors": { efeito: "Ruse", percentuais: [0, 0.50, 1.03, 1.62, 2.28, 3.00, 3.78, 4.62, 5.52, 6.48, 7.51] },
    "Helmets": { efeito: "Momentum", percentuais: [0, 2.00, 4.05, 6.20, 8.45, 10.80, 13.25, 15.80, 18.45, 21.20, 24.05] },
    "Legs": { efeito: "Transcendence", percentuais: [0, 0.13, 0.27, 0.44, 0.64, 0.86, 1.11, 1.38, 1.68, 2.00, 2.35] },
    "Boots": { efeito: "Amplification", percentuais: [0, 2.50, 5.40, 9.10, 13.60, 18.90, 25.00, 31.90, 39.60, 48.10, 57.40] }
};

const imbuementsList = {
    "Weapons_Melee": ["Critical Hit (Strike)", "Mana Leech (Void)", "Life Leech (Vampirism)", "Sword Skill (Slash)", "Axe Skill (Chop)", "Club Skill (Bash)", "Fire Damage (Scorch)", "Earth Damage (Venom)", "Ice Damage (Frost)", "Energy Damage (Electrify)", "Death Damage (Reap)"],
    "Weapons_Distance": ["Critical Hit (Strike)", "Mana Leech (Void)", "Life Leech (Vampirism)", "Distance Skill (Precision)", "Fire Damage (Scorch)", "Earth Damage (Venom)", "Ice Damage (Frost)", "Energy Damage (Electrify)", "Death Damage (Reap)"],
    "Weapons_Magic": ["Critical Hit (Strike)", "Mana Leech (Void)", "Life Leech (Vampirism)", "Magic Level (Epiphany)"],
    "Armors": ["Life Leech (Vampirism)", "Fire Protection (Dragon Hide)", "Earth Protection (Lich Shroud)", "Ice Protection (Quagmire)", "Energy Protection (Cloud Fabric)", "Death Protection (Demon Presence)", "Holy Protection (Fairy Wings)"],
    "Helmets": ["Mana Leech (Void)", "Magic Level (Epiphany)", "Distance Skill (Precision)", "Sword Skill (Slash)", "Axe Skill (Chop)", "Club Skill (Bash)", "Shielding (Blockade)"],
    "Shields": ["Shielding (Blockade)", "Fire Protection (Dragon Hide)", "Earth Protection (Lich Shroud)", "Ice Protection (Quagmire)", "Energy Protection (Cloud Fabric)", "Death Protection (Demon Presence)", "Holy Protection (Fairy Wings)"],
    "Boots": ["Speed (Swiftness)", "Paralysis Deflection (Vibrancy)"],
    "Backpacks": ["Capacity (Featherweight)"]
};

const imbuementsStatsData = {
    "Magic Level (Epiphany)": { stat: "magicLevel", bonus: [1, 2, 4], label: " magic level" },
    "Distance Skill (Precision)": { stat: "distanceSkill", bonus: [1, 2, 4], label: " distance" },
    "Sword Skill (Slash)": { stat: "swordSkill", bonus: [1, 2, 4], label: " sword" },
    "Axe Skill (Chop)": { stat: "axeSkill", bonus: [1, 2, 4], label: " axe" },
    "Club Skill (Bash)": { stat: "clubSkill", bonus: [1, 2, 4], label: " club" },
    "Shielding (Blockade)": { stat: "shieldingSkill", bonus: [1, 2, 4], label: " shielding" },
    "Critical Hit (Strike)": { extraStat: "critDamage", bonus: [10, 25, 50], label: "% critical damage" },
    "Mana Leech (Void)": { extraStat: "manaLeech", bonus: [3, 5, 8], label: "% mana leech" },
    "Life Leech (Vampirism)": { extraStat: "lifeLeech", bonus: [5, 10, 25], label: "% life leech" },
    "Speed (Swiftness)": { extraStat: "speed", bonus: [10, 15, 20], label: " speed" },
    "Capacity (Featherweight)": { extraStat: "capacity", bonus: [3, 8, 15], label: "% capacity" },
    "Fire Damage (Scorch)": { extraStat: "fireDmg", bonus: [10, 25, 50], label: "% fire damage" },
    "Earth Damage (Venom)": { extraStat: "earthDmg", bonus: [10, 25, 50], label: "% earth damage" },
    "Ice Damage (Frost)": { extraStat: "iceDmg", bonus: [10, 25, 50], label: "% ice damage" },
    "Energy Damage (Electrify)": { extraStat: "energyDmg", bonus: [10, 25, 50], label: "% energy damage" },
    "Death Damage (Reap)": { extraStat: "deathDmg", bonus: [10, 25, 50], label: "% death damage" },
    "Fire Protection (Dragon Hide)": { protElement: "fire", bonus: [3, 8, 15], label: "% fire protection" },
    "Earth Protection (Lich Shroud)": { protElement: "earth", bonus: [3, 8, 15], label: "% earth protection" },
    "Ice Protection (Quagmire)": { protElement: "ice", bonus: [3, 8, 15], label: "% ice protection" },
    "Energy Protection (Cloud Fabric)": { protElement: "energy", bonus: [3, 8, 15], label: "% energy protection" },
    "Death Protection (Demon Presence)": { protElement: "death", bonus: [2, 5, 10], label: "% death protection" },
    "Holy Protection (Fairy Wings)": { protElement: "holy", bonus: [3, 8, 15], label: "% holy protection" }
};

const limiteClasse = { 0: 0, 1: 1, 2: 2, 3: 3, 4: 10 };
const tierNames = ["Basic", "Intricate", "Powerful"];

// funções de exibição (buscar party e hunts)

function abreviarVocacao(vocacaoStr) {
    if (!vocacaoStr) return '?';
    const v = String(vocacaoStr).toLowerCase();
    if (v.includes('royal paladin')) return 'RP';
    if (v.includes('paladin')) return 'P';
    if (v.includes('elite knight')) return 'EK';
    if (v.includes('knight')) return 'K';
    if (v.includes('master sorcerer')) return 'MS';
    if (v.includes('sorcerer')) return 'S';
    if (v.includes('elder druid')) return 'ED';
    if (v.includes('druid')) return 'D';
    if (v.includes('exalted monk')) return 'EM';
    if (v.includes('monk')) return 'M';
    return '?';
}

function calcularBonusSnapshot(activeData) {
    let bonus = { ml: 0, dist: 0, sword: 0, axe: 0, club: 0, shield: 0, fist: 0 };
    let protecoes = { physical: 1, fire: 1, earth: 1, ice: 1, energy: 1, death: 1, holy: 1, lifeDrain: 1, manaDrain: 1, drowning: 1 };
    let specials = { crit: 0, life: 0, mana: 0 };

    if (!activeData) return { bonus, protecoes, specials };

    for (let slot in activeData) {
        const dados = activeData[slot];
        if (dados && dados.item) {
            const item = dados.item;
            if (item.atributos) {
                let parts = String(item.atributos).toLowerCase().split(',');
                parts.forEach(p => {
                    let match = p.match(/([a-z\s]+)\s*([+-]\s*\d+)/);
                    if (match) {
                        let attr = match[1].trim(); let val = parseInt(match[2].replace(/\s/g, ''));
                        if (attr === 'magic level') bonus.ml += val;
                        else if (attr.includes('sword')) bonus.sword += val;
                        else if (attr.includes('axe')) bonus.axe += val;
                        else if (attr.includes('club')) bonus.club += val;
                        else if (attr.includes('distance')) bonus.dist += val;
                        else if (attr.includes('shielding')) bonus.shield += val;
                        else if (attr.includes('fist')) bonus.fist += val;
                    }
                });
            }
            if (item.protecao) {
                let parts = String(item.protecao).toLowerCase().split(',');
                parts.forEach(p => {
                    let match = p.match(/([a-z\s]+)\s*([+-]\s*\d+)\s*%/);
                    if (match) {
                        let element = match[1].trim(); let factor = 1 - (parseInt(match[2].replace(/\s/g, '')) / 100);
                        if (element.includes('physical')) protecoes.physical *= factor;
                        else if (element.includes('fire')) protecoes.fire *= factor;
                        else if (element.includes('earth')) protecoes.earth *= factor;
                        else if (element.includes('ice')) protecoes.ice *= factor;
                        else if (element.includes('energy')) protecoes.energy *= factor;
                        else if (element.includes('death')) protecoes.death *= factor;
                        else if (element.includes('holy')) protecoes.holy *= factor;
                        else if (element.includes('life drain')) protecoes.lifeDrain *= factor;
                        else if (element.includes('mana drain')) protecoes.manaDrain *= factor;
                        else if (element.includes('drowning')) protecoes.drowning *= factor;
                    }
                });
            }
            if (dados.imbuements) {
                dados.imbuements.forEach(imb => {
                    const details = imbuementsStatsData[imb.tipo];
                    if (details) {
                        if (details.stat) bonus[details.stat] += details.bonus[imb.tier];
                        if (details.protElement) protecoes[details.protElement] *= (1 - (details.bonus[imb.tier] / 100));
                        if (details.extraStat === 'critDamage') specials.crit = Math.max(specials.crit, details.bonus[imb.tier]);
                        else if (details.extraStat) specials[details.extraStat] = (specials[details.extraStat] || 0) + details.bonus[imb.tier];
                    }
                });
            }
        }
    }
    return { bonus, protecoes, specials };
}

function renderSkillsLeft(member, dataBonus) {
    let voc = String(member.vocation).toLowerCase();
    const bonus = dataBonus.bonus;
    let html = '<div class="d-flex flex-column w-100 mt-3" style="font-family: \'Verdana\', sans-serif;"><div><span class="d-block border-bottom border-dark pb-1 mb-2 text-center" style="font-size: 11px; color: #dfdfdf;">Skills</span></div><div class="d-flex flex-column gap-2">';

    const renderBar = (lbl, baseVal, bVal) => {
        baseVal = isNaN(parseInt(baseVal)) ? (lbl === 'ML' ? 0 : 10) : parseInt(baseVal);
        let total = baseVal + bVal;
        let barColor = bVal > 0 ? '#00ff00' : (lbl === 'ML' ? '#0dcaf0' : '#dc3545');
        let textClass = bVal > 0 ? 'text-success' : 'text-white';
        let percentual = total > 0 ? (total * 17) % 100 : 0;
        if (total === 10 && lbl !== 'ML') percentual = 0;
        let tooltipAttr = bVal !== 0 ? `data-bs-toggle="tooltip" data-bs-placement="top" title="${lbl} Base ${baseVal} ${bVal > 0 ? '+' : '-'}${Math.abs(bVal)} = ${total}"` : '';

        return `<div class="mb-1 p-1 rounded" style="background-color: #111; border: 1px solid #222;" ${tooltipAttr}>
                    <div class="d-flex justify-content-between px-1 mb-1"><span style="color: #ccc; font-size: 11px;">${lbl}</span><span class="${textClass}" style="font-size: 11px;">${total}</span></div>
                    <div class="progress" style="height: 4px; background-color: #000; border-radius: 0;"><div class="progress-bar" style="width: ${percentual}%; background-color: ${barColor}; transition: width 1s ease-out;"></div></div>
                </div>`;
    };

    let sTemp = '';
    if (voc.includes('sorcerer') || voc.includes('druid')) sTemp += renderBar('ML', member.magicLevel, bonus.ml);
    else if (voc.includes('paladin')) sTemp += renderBar('ML', member.magicLevel, bonus.ml) + renderBar('Dist', member.distanceSkill, bonus.dist) + renderBar('Shield', member.shieldingSkill, bonus.shield);
    else if (voc.includes('knight')) sTemp += renderBar('ML', member.magicLevel, bonus.ml) + renderBar('Sword', member.swordSkill, bonus.sword) + renderBar('Axe', member.axeSkill, bonus.axe) + renderBar('Club', member.clubSkill, bonus.club) + renderBar('Shield', member.shieldingSkill, bonus.shield);
    else if (voc.includes('monk')) sTemp += renderBar('ML', member.magicLevel, bonus.ml) + renderBar('Fist', member.fistSkill, bonus.fist);

    return html + (sTemp !== '' ? sTemp : '<span class="text-secondary small text-center">Sem skills</span>') + '</div></div>';
}

function renderStatsRight(dataBonus) {
    let html = '<div class="d-flex flex-column gap-3 w-100" style="font-family: \'Verdana\', sans-serif;"><div><span class="d-block border-bottom border-dark pb-1 mb-2" style="font-size: 11px;">Imbuements</span><div class="d-flex flex-column gap-1">';
    const specials = dataBonus.specials;
    let hasSpecial = false;

    const rSpec = (lbl, val) => {
        if (val <= 0) return '';
        hasSpecial = true;
        return `<div class="d-flex justify-content-between px-1 border-secondary pb-1"><span class="text-secondary" style="font-size: 0.85em;">${lbl}</span><span style="font-size: 0.85em;">+${val}%</span></div>`;
    };

    let spTemp = rSpec('Critical hit', specials.crit) + rSpec('Life Leech amount', specials.lifeLeech || specials.life) + rSpec('Mana Leech amount', specials.manaLeech || specials.mana);
    html += (hasSpecial ? spTemp : '<span class="text-secondary small px-1">Nenhum</span>') + '</div></div>';

    html += `<div><span class="d-block border-bottom border-dark pb-1 mb-2" style="font-size: 11px;">Proteções</span><div class="d-flex flex-column gap-1">`;
    let pTemp = '';
    const ICONES = '/tibia-images/Icones';
    const renderProt = (lbl, pVal, icon) => {
        if (pVal === undefined || isNaN(pVal)) return '';
        let val = Math.round((1 - pVal) * 100);
        if (val === 0) return '';
        return `<div class="d-flex justify-content-between px-1 border-secondary pb-1"><span class="text-secondary" style="font-size: 0.85em;">${lbl}</span><div class="d-flex align-items-center gap-1"><span class="${val > 0 ? 'text-success' : 'text-danger'}" style="font-size: 0.85em;">${val > 0 ? '+' : ''}${val}%</span><img src="${ICONES}/${icon}" style="width: 11px; height: 11px; vertical-align: middle;"></div></div>`;
    };

    const p = dataBonus.protecoes;
    pTemp += renderProt('Physical', p.physical, 'Physical_Damage_Icon.gif') + renderProt('Fire', p.fire, 'Burned_Icon.gif') + renderProt('Earth', p.earth, 'Poisoned_Icon.gif') + renderProt('Ice', p.ice, 'Freezing_Icon.gif') + renderProt('Energy', p.energy, 'Electrified_Icon.gif') + renderProt('Death', p.death, 'Cursed_Icon.gif') + renderProt('Holy', p.holy, 'Holy_Damage_Icon.gif') + renderProt('Life Drain', p.lifeDrain, 'Life_Drain_Icon.gif') + renderProt('Mana Drain', p.manaDrain, 'Mana_Drain_Icon.gif') + renderProt('Drowning', p.drowning, 'Drowning_Icon.gif');
    return html + (pTemp !== '' ? pTemp : '<span class="text-secondary small px-1">Nenhuma</span>') + '</div></div></div>';
}

function initPartySnapshotRenderer() {
    document.querySelectorAll('.anuncio-render-area').forEach(area => {
        const d = area.dataset;
        let activeData = null;
        try { if (d.userJson) { const parsedSets = JSON.parse(d.userJson); activeData = parsedSets[1]; } } catch (e) { }

        const mockMember = { vocation: d.voc, magicLevel: d.ml, distanceSkill: d.dist, swordSkill: d.sword, axeSkill: d.axe, clubSkill: d.club, shieldingSkill: d.shield, fistSkill: d.fist };
        const dataBonus = calcularBonusSnapshot(activeData);

        let gridHtml = '<div class="d-flex flex-column align-items-center gap-1">';
        slotMatrix.forEach(row => {
            gridHtml += '<div class="d-flex gap-1">';
            row.forEach(slot => {
                if (!slot) gridHtml += '<div style="width: 34px;"></div>';
                else {
                    let imgUrl = imagensPadrao[slot];
                    let opacityClass = 'opacity-25';
                    let itemName = 'Vazio';

                    if (activeData && activeData[slot] && activeData[slot].item) {
                        imgUrl = activeData[slot].item.imagemUrl;
                        opacityClass = '';
                        itemName = activeData[slot].item.nome;
                    }
                    gridHtml += `<div class="mini-equip-slot" data-bs-toggle="tooltip" title="${itemName}"><img src="${imgUrl}" class="${opacityClass}"></div>`;
                }
            });
            gridHtml += '</div>';
        });
        gridHtml += '</div>';

        area.innerHTML = `<div class="d-flex align-items-start gap-4 flex-grow-1"><div class="flex-shrink-0" style="width: 114px;">${gridHtml}${renderSkillsLeft(mockMember, dataBonus)}</div><div class="flex-grow-1 border-start border-dark ps-3">${renderStatsRight(dataBonus)}</div></div>`;
    });
}
document.addEventListener("DOMContentLoaded", initPartySnapshotRenderer);

// funções set builder (personagem e builder)

let itemSelecionado = null;
let charVocationJs = "None";
let currentSetIndex = 1;
let slotAlvo = "";

function criarSetVazio() {
    return { "Amulets": null, "Helmets": null, "Backpacks": null, "Weapons": null, "Armors": null, "Shields": null, "Rings": null, "Legs": null, "ExtraSlot": null, "Boots": null };
}

let sets = { 1: criarSetVazio(), 2: criarSetVazio(), 3: criarSetVazio() };

function isDuasMaos(item) {
    if (!item) return false;
    const qtdMaos = item.maos || item.mãos || item['Mãos'] || "";
    const valorString = String(qtdMaos).toLowerCase().trim();
    if (valorString === 'duas' || valorString === '2') return true;
    const txt = `${item.nome || ''} ${item.atributos || ''} ${item.propriedades || ''}`.toLowerCase();
    return txt.includes('duas mãos') || txt.includes('two-handed') || txt.includes('duas');
}

function trocarSet(index) {
    currentSetIndex = index;
    atualizarVisualSet();
}

function removerItem(event, categoria) {
    event.preventDefault();
    if (sets[currentSetIndex] && sets[currentSetIndex][categoria]) {
        sets[currentSetIndex][categoria] = null;
        atualizarVisualSet();
    }
}

function verificarVocacaoCompativel(itemVocacao, charVoc) {
    if (!itemVocacao || charVoc === 'None') return true;
    const vocItem = String(itemVocacao).toLowerCase();
    const vocChar = String(charVoc).toLowerCase();
    if (vocItem.includes('all') || vocItem.includes('todas')) return true;

    let baseVoc = "";
    if (vocChar.includes('knight')) baseVoc = 'knight';
    else if (vocChar.includes('paladin')) baseVoc = 'paladin';
    else if (vocChar.includes('sorcerer')) baseVoc = 'sorcerer';
    else if (vocChar.includes('druid')) baseVoc = 'druid';
    else if (vocChar.includes('monk')) baseVoc = 'monk';
    else return true;

    return vocItem.includes(baseVoc);
}

function verificarCategoriaCompativel(categoriaDB, slotAtual) {
    if (!categoriaDB) return false;
    const cat = String(categoriaDB).toLowerCase();
    const vocChar = String(charVocationJs).toLowerCase();

    if (slotAtual === 'Amulets') return cat.includes('amulet') || cat.includes('necklace');
    if (slotAtual === 'Helmets') return cat.includes('helmet');
    if (slotAtual === 'Armors') return cat.includes('armor');
    if (slotAtual === 'Rings') return cat.includes('ring');
    if (slotAtual === 'Legs') return cat.includes('legs');
    if (slotAtual === 'Boots') return cat.includes('boots');
    if (slotAtual === 'Backpacks') return cat.includes('backpack') || cat.includes('container');
    if (slotAtual === 'ExtraSlot') return cat.includes('ammunition') || cat.includes('extra') || cat.includes('light');

    if (slotAtual === 'Weapons') {
        return ['sword', 'axe', 'club', 'distance', 'wand', 'rod', 'bow', 'crossbow', 'weapon', 'fist'].some(t => cat.includes(t));
    }

    if (slotAtual === 'Shields') {
        const wpn = sets[currentSetIndex] && sets[currentSetIndex]['Weapons'] ? sets[currentSetIndex]['Weapons'].item : null;
        const isKnightOrMonk = vocChar.includes('knight') || vocChar.includes('monk');
        const isPaladin = vocChar.includes('paladin');

        if (wpn && isDuasMaos(wpn)) {
            if (isKnightOrMonk) return false;
            if (isPaladin) return cat.includes('quiver');
            return cat.includes('shield') || cat.includes('spellbook');
        }
        if (isPaladin) return cat.includes('shield') || cat.includes('quiver');
        return cat.includes('shield') || cat.includes('spellbook');
    }
    return false;
}

function mapearCategoriaImbuement(categoria) {
    const cat = String(categoria || "").toLowerCase();
    if (cat.includes('sword') || cat.includes('axe') || cat.includes('club') || cat.includes('fist') || cat.includes('weapon')) return "Weapons_Melee";
    if (cat.includes('distance') || cat.includes('bow') || cat.includes('crossbow')) return "Weapons_Distance";
    if (cat.includes('wand') || cat.includes('rod')) return "Weapons_Magic";
    if (cat.includes('armor')) return "Armors";
    if (cat.includes('helmet')) return "Helmets";
    if (cat.includes('shield') || cat.includes('spellbook') || cat.includes('quiver')) return "Shields";
    if (cat.includes('boots')) return "Boots";
    if (cat.includes('backpack')) return "Backpacks";
    return null;
}

function mapearCategoriaForja(categoria) {
    const cat = String(categoria || "").toLowerCase();
    if (['swords', 'axes', 'clubs', 'distance', 'wands', 'rods'].includes(cat)) return "Weapons";
    if (cat === 'armors') return "Armors";
    if (cat === 'helmets') return "Helmets";
    if (cat === 'legs') return "Legs";
    if (cat === 'boots') return "Boots";
    return null;
}

function abrirBusca(categoria) {
    slotAlvo = categoria;
    const vocChar = String(charVocationJs).toLowerCase();

    // verifica se for duas mãos para uso de shield 
    if (slotAlvo === 'Shields' && sets[currentSetIndex] && sets[currentSetIndex]['Weapons']) {
        const wpn = sets[currentSetIndex]['Weapons'].item;
        const isKnightOrMonk = vocChar.includes('knight') || vocChar.includes('monk');
        if (isKnightOrMonk && wpn && isDuasMaos(wpn)) return;
    }

    document.getElementById('inputBuscaItem').value = '';
    document.getElementById('listaResultadosItem').innerHTML = '';
    document.getElementById('areaExaltationForge').classList.add('d-none');
    document.getElementById('footerConfirmarItem').classList.add('d-none');
    itemSelecionado = null;

    const modalEl = document.getElementById('modalAdicionarItem');
    if (modalEl) {
        let modal = bootstrap.Modal.getInstance(modalEl);
        if (!modal) modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

async function buscarItemApi(termo) {
    const lista = document.getElementById('listaResultadosItem');
    if (termo.length < 3) { lista.innerHTML = ''; return; }

    try {
        const response = await fetch(`/api/equipamentos/buscar?termo=${termo}`);
        const itens = await response.json();
        lista.innerHTML = '';

        const itensFiltrados = itens.filter(item => {
            const vocacaoDB = item.vocacao || item.vocation || "";
            return verificarCategoriaCompativel(item.categoria, slotAlvo) && verificarVocacaoCompativel(vocacaoDB, charVocationJs);
        });

        if (itensFiltrados.length === 0) {
            lista.innerHTML = '<div class="text-secondary text-center p-3 small">Nenhum item compatível encontrado.</div>';
            return;
        }

        itensFiltrados.forEach(item => {
            const btn = document.createElement('button');
            btn.className = 'list-group-item list-group-item-action bg-dark text-white border-secondary d-flex align-items-center gap-3 py-2';
            btn.innerHTML = `
                <img src="${item.imagemUrl || 'https://www.tibiawiki.com.br/images/b/b4/Unknown_Item.gif'}" width="32" height="32" style="object-fit: contain;"> 
                <div class="d-flex flex-column text-start">
                    <span class="fw-bold" style="font-size: 0.85em;">${item.nome}</span>
                    <small class="text-secondary" style="font-size: 0.7em;">Level ${item.levelMinimo || 0} | Voc: ${item.vocacao || item.vocation || 'Todas'}</small>
                </div>`;
            btn.onclick = () => selecionarItemParaForja(item);
            lista.appendChild(btn);
        });
    } catch (error) { console.error(error); }
}

function mudarTipoImbuement(index) {
    const typeSelect = document.getElementById(`selectImbuementType${index}`);
    const tierSelect = document.getElementById(`selectImbuementTier${index}`);
    if (typeSelect.value !== "") tierSelect.classList.remove('d-none');
    else { tierSelect.classList.add('d-none'); tierSelect.value = "0"; }

    const todosSelects = document.querySelectorAll('.select-imbuement-tipo');
    const danosElementais = ["Fire Damage (Scorch)", "Earth Damage (Venom)", "Ice Damage (Frost)", "Energy Damage (Electrify)", "Death Damage (Reap)"];

    todosSelects.forEach(select => {
        const outrosValores = Array.from(todosSelects).filter(s => s !== select && s.value !== "").map(s => s.value);
        const algumTemElemental = outrosValores.some(v => danosElementais.includes(v));

        Array.from(select.options).forEach(option => {
            if (option.value === "") return;
            const isElementoConflitante = algumTemElemental && danosElementais.includes(option.value);
            const isJaSelecionado = outrosValores.includes(option.value);

            if (isJaSelecionado || isElementoConflitante) {
                option.disabled = true; option.style.display = 'none';
            } else {
                option.disabled = false; option.style.display = '';
            }
        });
    });
}

function selecionarItemParaForja(item) {
    itemSelecionado = item;
    document.getElementById('listaResultadosItem').innerHTML = '';
    document.getElementById('inputBuscaItem').value = '';

    const area = document.getElementById('areaExaltationForge');
    const footer = document.getElementById('footerConfirmarItem');

    const maxTier = limiteClasse[item.tier || 0] || 0;
    const forjaCat = mapearCategoriaForja(item.categoria);

    let htmlForge = '';
    if (maxTier > 0 && forjaCat) {
        const forjaInfo = exaltationForgeData[forjaCat];
        let htmlSelect = `<select id="selectTierForge" class="form-select bg-black text-white border-secondary mt-1"><option value="0">Tier 0 (Chance 0%)</option>`;
        for (let i = 1; i <= maxTier; i++) htmlSelect += `<option value="${i}">Tier ${i} (Chance ${forjaInfo.percentuais[i]}%)</option>`;
        htmlSelect += `</select>`;
        htmlForge = `<div class="mt-3"><label class="text-warning fw-bold mb-0" style="font-size: 0.85em;"> Tier</label>${htmlSelect}</div>`;
    } else {
        htmlForge = `<input type="hidden" id="selectTierForge" value="0">`;
    }

    const imbuementCat = mapearCategoriaImbuement(item.categoria);
    let htmlImbuements = '';
    let qtdSlots = item.slots !== undefined && item.slots !== null ? item.slots : 0;

    if (qtdSlots > 0 && imbuementCat && imbuementsList[imbuementCat]) {
        htmlImbuements += `<div class="mt-3 border-top border-secondary pt-3"><label class="text-info fw-bold mb-2" style="font-size: 0.85em;"> Imbuement slots: ${qtdSlots}</label>`;
        const isKnightOrPaladin = String(charVocationJs).toLowerCase().includes('knight') || String(charVocationJs).toLowerCase().includes('paladin');

        for (let i = 1; i <= qtdSlots; i++) {
            htmlImbuements += `<div class="d-flex gap-2 mb-2"><select id="selectImbuementType${i}" onchange="mudarTipoImbuement(${i})" class="form-select form-select-sm bg-black text-white border-secondary select-imbuement-tipo" style="flex: 1;"><option value="">-- Slot ${i} Vazio --</option>`;
            imbuementsList[imbuementCat].forEach(imb => {
                if (imbuementCat === 'Helmets' && imb.includes('Epiphany') && isKnightOrPaladin) return;
                htmlImbuements += `<option value="${imb}">${imb}</option>`;
            });
            htmlImbuements += `</select><select id="selectImbuementTier${i}" class="form-select form-select-sm bg-black text-white border-secondary select-imbuement-tier d-none" style="width: 100px;"><option value="0" selected>Basic</option><option value="1">Intricate</option><option value="2">Powerful</option></select></div>`;
        }
        htmlImbuements += `</div>`;
    }

    area.innerHTML = `<div class="d-flex align-items-center gap-3 ${htmlForge !== '' || htmlImbuements !== '' ? 'border-bottom border-secondary pb-3' : ''}">
        <img src="${item.imagemUrl || 'https://www.tibiawiki.com.br/images/b/b4/Unknown_Item.gif'}" width="48" height="48" class="bg-black p-1 rounded border border-secondary">
        <div><h5 class="mb-0 text-info fw-bold">${item.nome}</h5><small class="text-secondary">Categoria: ${item.categoria} | Voc: ${item.vocacao || 'Todas'} | Classe: ${item.tier || 0}</small></div>
    </div>${htmlForge}${htmlImbuements}`;

    area.classList.remove('d-none');
    footer.classList.remove('d-none');
}

function salvarItemNoSet() {
    const selectTier = document.getElementById('selectTierForge');
    const tierEscolhido = selectTier ? parseInt(selectTier.value) : 0;
    const forjaCat = mapearCategoriaForja(itemSelecionado.categoria);

    let bonusAtivo = 0, efeitoAtivo = null;
    if (forjaCat && exaltationForgeData[forjaCat]) {
        efeitoAtivo = exaltationForgeData[forjaCat].efeito;
        if (tierEscolhido > 0) bonusAtivo = exaltationForgeData[forjaCat].percentuais[tierEscolhido];
    }

    let imbuementsSelecionados = [];
    document.querySelectorAll('.select-imbuement-tipo').forEach((box, idx) => {
        if (box.value !== "") {
            const tierBox = document.getElementById(`selectImbuementTier${idx + 1}`);
            imbuementsSelecionados.push({ tipo: box.value, tier: tierBox ? parseInt(tierBox.value) : 0 });
        }
    });

    if (!sets[currentSetIndex]) sets[currentSetIndex] = criarSetVazio();

    sets[currentSetIndex][slotAlvo] = {
        item: itemSelecionado,
        tier: tierEscolhido,
        bonus: bonusAtivo,
        efeito: efeitoAtivo,
        imbuements: imbuementsSelecionados
    };

    if (slotAlvo === 'Weapons' && isDuasMaos(itemSelecionado)) {
        const isKnightOrMonk = String(charVocationJs).toLowerCase().includes('knight') || String(charVocationJs).toLowerCase().includes('monk');
        const isPaladin = String(charVocationJs).toLowerCase().includes('paladin');

        if (isKnightOrMonk) sets[currentSetIndex]['Shields'] = null;
        else if (isPaladin && sets[currentSetIndex]['Shields'] && !String(sets[currentSetIndex]['Shields'].item.categoria).toLowerCase().includes('quiver')) {
            sets[currentSetIndex]['Shields'] = null;
        }
    }

    atualizarVisualSet();
    const modalEl = document.getElementById('modalAdicionarItem');
    if (modalEl) {
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) modal.hide();
    }
}

function recalcularAtributosGlobais() {
    let totalBonusStats = { magicLevel: 0, fistSkill: 0, clubSkill: 0, swordSkill: 0, axeSkill: 0, distanceSkill: 0, shieldingSkill: 0, fishingSkill: 0 };
    let extraStats = { critDamage: 0.0, critChance: 0.0, manaLeechAmount: 0, manaLeechChance: 0, lifeLeechAmount: 0, lifeLeechChance: 0, fireDmg: 0, earthDmg: 0, iceDmg: 0, energyDmg: 0, deathDmg: 0, speed: 0, capacity: 0, mantra: 0, elementalMagicLevels: {} };
    let protMultipliers = { physical: 1, fire: 1, earth: 1, ice: 1, energy: 1, death: 1, holy: 1, lifeDrain: 1, manaDrain: 1, drowning: 1 };
    let totalArmor = 0, weaponAtk = 0, weaponDef = 0, weaponMod = 0, shieldDef = 0, eleDmgText = [];

    if (!sets || !sets[currentSetIndex]) return;

    for (let slot in sets[currentSetIndex]) {
        const dados = sets[currentSetIndex][slot];
        if (dados && dados.item) {
            const item = dados.item;
            if (item.armadura) totalArmor += parseInt(item.armadura) || 0;

            if (slot === 'Weapons') {
                weaponAtk = parseInt(item.ataque) || 0; weaponDef = parseInt(item.defesa) || 0;
                if (item.modDefesa) weaponMod = parseInt(item.modDefesa) || 0;
                if (item.danoElemental && !['nenhum', 'nenhuma', ''].includes(item.danoElemental.toLowerCase().trim())) eleDmgText.push(item.danoElemental);
            }
            if (slot === 'Shields') shieldDef = parseInt(item.defesa) || 0;

            if (item.criticalChance) extraStats.critChance += parseInt(item.criticalChance);
            if (item.criticalDamage) extraStats.critDamage += parseInt(item.criticalDamage);
            if (item.manaLeechChance) extraStats.manaLeechChance += parseInt(item.manaLeechChance);
            if (item.manaLeechAmount) extraStats.manaLeechAmount += parseInt(item.manaLeechAmount);
            if (item.lifeLeechChance) extraStats.lifeLeechChance += parseInt(item.lifeLeechChance);
            if (item.lifeLeechAmount) extraStats.lifeLeechAmount += parseInt(item.lifeLeechAmount);
            if (item.mantra) extraStats.mantra += parseInt(item.mantra);

            if (item.atributos) {
                let parts = String(item.atributos).toLowerCase().split(',');
                parts.forEach(p => {
                    let match = p.match(/([a-z\s]+)\s*([+-]\s*\d+)/);
                    if (match) {
                        let attr = match[1].trim(); let val = parseInt(match[2].replace(/\s/g, ''));
                        if (attr === 'magic level') totalBonusStats.magicLevel += val;
                        else if (attr.includes('magic level')) extraStats.elementalMagicLevels[attr] = (extraStats.elementalMagicLevels[attr] || 0) + val;
                        else if (attr.includes('sword fighting')) totalBonusStats.swordSkill += val;
                        else if (attr.includes('axe fighting')) totalBonusStats.axeSkill += val;
                        else if (attr.includes('club fighting')) totalBonusStats.clubSkill += val;
                        else if (attr.includes('distance fighting')) totalBonusStats.distanceSkill += val;
                        else if (attr.includes('shielding')) totalBonusStats.shieldingSkill += val;
                        else if (attr.includes('fist')) totalBonusStats.fistSkill += val;
                        else if (attr.includes('speed')) extraStats.speed += val;
                    }
                });
            }

            if (item.protecao) {
                let parts = String(item.protecao).toLowerCase().split(',');
                parts.forEach(p => {
                    let match = p.match(/([a-z\s]+)\s*([+-]\s*\d+)\s*%/);
                    if (match) {
                        let element = match[1].trim(); let factor = 1 - (parseInt(match[2].replace(/\s/g, '')) / 100);
                        if (element.includes('physical')) protMultipliers.physical *= factor;
                        else if (element.includes('fire')) protMultipliers.fire *= factor;
                        else if (element.includes('earth')) protMultipliers.earth *= factor;
                        else if (element.includes('ice')) protMultipliers.ice *= factor;
                        else if (element.includes('energy')) protMultipliers.energy *= factor;
                        else if (element.includes('death')) protMultipliers.death *= factor;
                        else if (element.includes('holy')) protMultipliers.holy *= factor;
                        else if (element.includes('life drain')) protMultipliers.lifeDrain *= factor;
                        else if (element.includes('mana drain')) protMultipliers.manaDrain *= factor;
                        else if (element.includes('drowning')) protMultipliers.drowning *= factor;
                    }
                });
            }

            if (dados.imbuements) {
                dados.imbuements.forEach(imb => {
                    const imbDetails = imbuementsStatsData[imb.tipo];
                    if (imbDetails) {
                        const val = imbDetails.bonus[imb.tier];
                        if (imbDetails.stat) totalBonusStats[imbDetails.stat] += val;
                        if (imbDetails.protElement) protMultipliers[imbDetails.protElement] *= (1 - (val / 100));
                        if (imb.tipo.includes("Strike")) { extraStats.critChance += 10.0; extraStats.critDamage += val; }
                        if (imb.tipo.includes("Void")) { extraStats.manaLeechChance += 100; extraStats.manaLeechAmount += val; }
                        if (imb.tipo.includes("Vampirism")) { extraStats.lifeLeechChance += 100; extraStats.lifeLeechAmount += val; }
                        if (imb.tipo.includes("Featherweight")) extraStats.capacity += val;
                        if (imb.tipo.includes("Swiftness")) extraStats.speed += val;
                        if (imb.tipo.includes("Scorch")) extraStats.fireDmg += val;
                        if (imb.tipo.includes("Venom")) extraStats.earthDmg += val;
                        if (imb.tipo.includes("Frost")) extraStats.iceDmg += val;
                        if (imb.tipo.includes("Electrify")) extraStats.energyDmg += val;
                        if (imb.tipo.includes("Reap")) extraStats.deathDmg += val;
                    }
                });
            }
        }
    }

    const skillsAlvo = ['magicLevel', 'fistSkill', 'clubSkill', 'swordSkill', 'axeSkill', 'distanceSkill', 'shieldingSkill', 'fishingSkill'];
    skillsAlvo.forEach(skill => {
        document.querySelectorAll(`.skill-container[data-skill="${skill}"]`).forEach(container => {
            let baseVal = skill === 'magicLevel' ? 0 : 10;
            const inputEl = document.getElementById('base-' + skill);

            if (inputEl) baseVal = parseInt(inputEl.value) || baseVal;
            else if (container.hasAttribute('data-base-value')) baseVal = parseInt(container.getAttribute('data-base-value')) || baseVal;

            const bonusVal = totalBonusStats[skill] || 0;
            const finalVal = baseVal + bonusVal;

            const valElement = container.querySelector('.skill-val-text');
            if (valElement) {
                valElement.innerText = finalVal;
                if (finalVal > baseVal) valElement.style.setProperty('color', '#00ff00', 'important');
                else valElement.style.removeProperty('color');
            }

            const progressBar = container.querySelector('.progress-bar');
            if (progressBar) {
                let percentualVisual = finalVal > 0 ? (finalVal * 17) % 100 : 0;
                if (finalVal === 10 && skill !== 'magicLevel') percentualVisual = 0;
                progressBar.style.width = percentualVisual + '%';
                progressBar.style.backgroundColor = finalVal > baseVal ? '#00ff00' : '';
            }
        });
    });

    const lvlInput = document.getElementById('builder-level');
    if (lvlInput) {
        const lvlVal = document.getElementById('level-val-text');
        if (lvlVal) lvlVal.innerText = parseInt(lvlInput.value) || 100;
    }

    let htmlAtributos = `<div class="mt-2 pt-2 border-top border-secondary"></div>`;
    let atkDisp = weaponAtk > 0 ? weaponAtk.toString() : "0";
    let eleDmgStr = eleDmgText.length > 0 ? ` <span class="text-warning" style="font-size: 0.9em;">(${eleDmgText.join(', ')})</span>` : "";

    htmlAtributos += `<div class="d-flex justify-content-between px-1"><span>Attack Value</span><span class="text-white">${atkDisp}${eleDmgStr}</span></div>`;

    for (let attr in extraStats.elementalMagicLevels) {
        htmlAtributos += `<div class="d-flex justify-content-between px-1"><span class="text-capitalize">${attr}</span><span style="color: #00b900;">+${extraStats.elementalMagicLevels[attr]}</span></div>`;
    }

    if (extraStats.critChance > 100) extraStats.critChance = 100;
    if (extraStats.critChance > 0) {
        htmlAtributos += `<div class="px-1 mt-1 text-white">Critical Hit:</div>
            <div class="d-flex justify-content-between px-1 ms-2"><span class="text-secondary">Chance</span> <span class="text-white">${extraStats.critChance.toFixed(0)}%</span></div>
            <div class="d-flex justify-content-between px-1 ms-2"><span class="text-secondary">Extra Damage</span> <span class="text-white">+${extraStats.critDamage.toFixed(0)}%</span></div>`;
    }
    if (extraStats.lifeLeechChance > 0) {
        htmlAtributos += `<div class="px-1 mt-1 text-white">Life Leech:</div>
            <div class="d-flex justify-content-between px-1 ms-2"><span class="text-secondary">Chance</span> <span class="text-white">${extraStats.lifeLeechChance}%</span></div>
            <div class="d-flex justify-content-between px-1 ms-2"><span class="text-secondary">Amount</span> <span class="text-white">+${extraStats.lifeLeechAmount}%</span></div>`;
    }
    if (extraStats.manaLeechChance > 0) {
        htmlAtributos += `<div class="px-1 mt-1 text-white">Mana Leech:</div>
            <div class="d-flex justify-content-between px-1 ms-2"><span class="text-secondary">Chance</span> <span class="text-white">${extraStats.manaLeechChance}%</span></div>
            <div class="d-flex justify-content-between px-1 ms-2"><span class="text-secondary">Amount</span> <span class="text-white">+${extraStats.manaLeechAmount}%</span></div>`;
    }

    htmlAtributos += `<div class="mt-2 pt-2 border-top border-secondary"></div>`;

    let mapProtections = [
        { key: 'physical', label: 'Physical', icon: 'Physical_Damage_Icon.gif' },
        { key: 'fire', label: 'Fire', icon: 'Burned_Icon.gif' },
        { key: 'earth', label: 'Earth', icon: 'Poisoned_Icon.gif' },
        { key: 'ice', label: 'Ice', icon: 'Freezing_Icon.gif' },
        { key: 'energy', label: 'Energy', icon: 'Electrified_Icon.gif' },
        { key: 'death', label: 'Death', icon: 'Cursed_Icon.gif' },
        { key: 'holy', label: 'Holy', icon: 'Holy_Damage_Icon.gif' },
        { key: 'lifeDrain', label: 'Life Drain', icon: 'Life_Drain_Icon.gif' },
        { key: 'manaDrain', label: 'Mana Drain', icon: 'Mana_Drain_Icon.gif' },
        { key: 'drowning', label: 'Drowning', icon: 'Drowning_Icon.gif' }
    ];

    const ICONES = '/tibia-images/Icones';

    mapProtections.forEach(el => {
        let protMult = protMultipliers[el.key] !== undefined ? protMultipliers[el.key] : 1;
        let protValue = (1 - protMult) * 100;

        if (Math.abs(protValue) > 0.01 && !isNaN(protValue)) {
            let sign = protValue > 0 ? '+' : '';
            let colorAttr = protValue > 0 ? 'style="color: #00b900"' : 'class="text-danger"';
            htmlAtributos += `<div class="d-flex justify-content-between align-items-center px-1 mb-1">
                                <span class="text-secondary">${el.label}</span>
                                <div class="d-flex align-items-center gap-1">
                                    <span ${colorAttr}>${sign}${protValue.toFixed(2)}%</span>
                                    <img src="${ICONES}/${el.icon}" style="width: 11px; height: 11px; vertical-align: middle;">
                                </div>
                              </div>`;
        }
    });

    let defenseValue = Math.max(weaponDef, shieldDef);
    if (weaponMod < 0) defenseValue += weaponMod;
    else if (weaponMod > 0 && shieldDef >= weaponDef) defenseValue += weaponMod;
    else if (weaponMod > 0 && weaponDef > shieldDef) defenseValue += weaponMod;
    if (defenseValue < 0) defenseValue = 0;

    htmlAtributos += `<div class="d-flex justify-content-between px-1"><span>Defense Value</span><span class="text-white">${defenseValue > 0 ? defenseValue : "0"}</span></div>
        <div class="d-flex justify-content-between px-1"><span>Armor Value</span><span class="text-white">${totalArmor > 0 ? totalArmor : "0"}</span></div>`;

    if (extraStats.speed > 0) htmlAtributos += `<div class="d-flex justify-content-between px-1"><span>Speed</span><span class="text-white">+${extraStats.speed}</span></div>`;
    if (extraStats.capacity > 0) htmlAtributos += `<div class="d-flex justify-content-between px-1"><span>Capacity</span><span class="text-white">+${extraStats.capacity}%</span></div>`;

    const masterPainel = document.getElementById('painel-atributos-master');
    if (masterPainel) masterPainel.innerHTML = htmlAtributos;
}

function atualizarVisualSet() {
    const ddlVoc = document.getElementById('builder-vocacao');
    if (ddlVoc) charVocationJs = ddlVoc.value;
    else charVocationJs = document.body.getAttribute('data-vocation') || 'None';

    const painelBonus = document.getElementById('painel-bonus');
    if (painelBonus) painelBonus.innerHTML = '';
    let temAprimoramento = false;

    if (!sets || !sets[currentSetIndex]) {
        sets = { 1: criarSetVazio(), 2: criarSetVazio(), 3: criarSetVazio() };
    }

    for (let slot in sets[currentSetIndex]) {
        const dados = sets[currentSetIndex][slot];
        const slotEl = document.getElementById(`slot-${slot}`);
        if (!slotEl) continue;

        const imgEl = slotEl.querySelector('.slot-img');
        if (!imgEl) continue;

        if (dados && dados.item) {
            imgEl.src = dados.item.imagemUrl || 'https://www.tibiawiki.com.br/images/b/b4/Unknown_Item.gif';
            imgEl.classList.remove('img-grayscale');

            let htmlDetalhesItem = '';

            const tierEl = slotEl.querySelector('.slot-tier');
            if (dados.tier > 0) {
                if (tierEl) {
                    tierEl.innerText = `T${dados.tier}`;
                    tierEl.style.display = 'block';
                }
                htmlDetalhesItem += `<div class="d-flex justify-content-between align-items-center mb-1"><span class="text-info" style="font-size: 0.85em;"> ${dados.efeito}</span><span class="badge bg-info text-dark">+ ${dados.bonus}%</span></div>`;
            } else {
                if (tierEl) tierEl.style.display = 'none';
            }

            if (dados.imbuements && dados.imbuements.length > 0) {
                dados.imbuements.forEach(imb => {
                    const statData = imbuementsStatsData[imb.tipo];
                    const match = imb.tipo.match(/\(([^)]+)\)/);
                    const nomeCurto = match ? match[1] : imb.tipo;
                    const tierNome = tierNames[imb.tier];
                    const valExtra = statData ? `(+${statData.bonus[imb.tier]}${statData.label})` : "";
                    htmlDetalhesItem += `<div class="d-flex align-items-center mb-1"><span class="text-light" style="font-size: 0.8em;"> ${tierNome} ${nomeCurto} <span class="text-secondary">${valExtra}</span></span></div>`;
                });
            }

            if (htmlDetalhesItem !== '') {
                temAprimoramento = true;
                if (painelBonus) painelBonus.innerHTML += `<div class="bg-black p-2 rounded border-start border-3 border-info mb-2 shadow-sm"><span class="text-warning fw-bold d-block small mb-2 border-bottom border-dark pb-1">${dados.item.nome}</span>${htmlDetalhesItem}</div>`;
            }
        } else {
            imgEl.src = imagensPadrao[slot] || '';
            imgEl.classList.add('img-grayscale');
            const tierEl = slotEl.querySelector('.slot-tier');
            if (tierEl) tierEl.style.display = 'none';
        }
    }

    const shieldSlot = document.getElementById('slot-Shields');
    if (shieldSlot) {
        const imgShield = shieldSlot.querySelector('.slot-img');
        const wpn = sets[currentSetIndex] && sets[currentSetIndex]['Weapons'] ? sets[currentSetIndex]['Weapons'].item : null;
        const isKnightOrMonk = String(charVocationJs).toLowerCase().includes('knight') || String(charVocationJs).toLowerCase().includes('monk');
        const isPaladin = String(charVocationJs).toLowerCase().includes('paladin');

        if (wpn && isDuasMaos(wpn)) {
            if (isKnightOrMonk) {
                shieldSlot.classList.add('disabled-slot');
                if (imgShield) {
                    imgShield.src = 'https://www.tibiawiki.com.br/images/2/2d/Wooden_Shield.gif';
                    imgShield.classList.add('img-grayscale');
                }
                const tierEl = shieldSlot.querySelector('.slot-tier');
                if (tierEl) tierEl.style.display = 'none';
            } else if (isPaladin) {
                shieldSlot.classList.remove('disabled-slot');
                if (!sets[currentSetIndex]['Shields'] && imgShield) {
                    imgShield.src = 'https://www.tibiawiki.com.br/images/6/63/Blue_Quiver.gif';
                    imgShield.classList.add('img-grayscale');
                }
            }
        } else {
            shieldSlot.classList.remove('disabled-slot');
            if (!sets[currentSetIndex]['Shields'] && imgShield) {
                imgShield.src = 'https://www.tibiawiki.com.br/images/2/2d/Wooden_Shield.gif';
                imgShield.classList.add('img-grayscale');
            }
        }
    }

    if (!temAprimoramento && painelBonus) {
        painelBonus.innerHTML = '<p class="text-secondary small">Nenhum bônus ou imbuement ativo no momento.</p>';
    }

    recalcularAtributosGlobais();
}