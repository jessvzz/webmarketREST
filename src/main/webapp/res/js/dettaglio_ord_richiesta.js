$(document).ready(function () {
    // Recupera il token di autenticazione dal localStorage
    var token = localStorage.getItem("authToken");
    console.log('token:', token);

    // Funzione per estrarre il parametro 'n' dall'URL
    function getQueryParam(param) {
        var urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(param);
    }

    // Estrae l'ID della richiesta dall'URL
    var richiestaId = getQueryParam('n');

    // Funzione per formattare la data in un formato leggibile
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('it-IT', options);
    }


    // Chiamata AJAX per ottenere i dettagli della richiesta
    $.ajax({
        url: "/WebMarketREST/rest/richieste/" + richiestaId,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (richiesta) {
            // Popola i campi con i dati ricevuti
            $('#richiesta-titolo').text("> Richiesta #" + richiesta.codiceRichiesta);
            $('#richiesta-codice').text("Richiesta #" + richiesta.codiceRichiesta);
            $('#richiesta-stato').text("Stato #" + richiesta.stato);
            $('#richiesta-categoria').text(richiesta.categoria ? richiesta.categoria.nome : 'N/A');
            $('#richiesta-data').text(formatDate(richiesta.data));
            $('#richiesta-note').text(richiesta.note || 'Nessuna nota inserita');

            // Gestione delle proposte
            const proposteContainer = $("#proposte-container");
            if (richiesta.proposte && richiesta.proposte.length > 0) {
                richiesta.proposte.forEach(function (proposta) {
                    const propostaHTML = `
                        <div class="proposta-card">
                            <a href="dettaglio_proposta_ord?n=${proposta.key}">${proposta.codice}</a>
                        </div>
                    `;
                    proposteContainer.append(propostaHTML);
                });
            } else {
                proposteContainer.append('<div class="proposta-card empty">Nessuna proposta disponibile.</div>');
            }
        },
        error: function (xhr) {
            if (xhr.status === 401) {
                alert("Si prega di effettuare l'accesso.");
            } else {
                alert("Errore durante il caricamento del dettaglio della richiesta.");
            }
        }
    });
});
