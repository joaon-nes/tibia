document.addEventListener("DOMContentLoaded", function() {
    if (document.getElementById('notif-badge')) {
        atualizarContadorNotificacoes();
        setInterval(atualizarContadorNotificacoes, 15000);
    }
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
    if(!container) return;
    
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