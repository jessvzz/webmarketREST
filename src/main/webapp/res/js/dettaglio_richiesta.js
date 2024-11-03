    $(document).ready(function () {
        var token = localStorage.getItem("authToken"); 
        var richiestaId = localStorage.getItem("richiestaId");

        if (!richiestaId) {
            alert("Errore: Nessuna richiesta selezionata.");
            window.location.href = "ordinantehomepage.html";
            return;
        }

        if (!token) {
            alert("Errore: token non trovato. Per favore, effettua nuovamente il login.");
            window.location.href = "index.html"; 
            return;
        }

        function caricaDettagliRichiesta() {
            $.ajax({
                url: `/WebMarketREST/rest/richieste/${richiestaId}/dettagli`,
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (data) {
                    $('#codice_richiesta').text(data.codiceRichiesta);
                    $('#codice_richiesta_title').text(data.codiceRichiesta);
                    $('#stato_richiesta').text(data.stato);
                    $('#categoria').text(data.categoria.nome);
                    $('#data_richiesta').text(data.data);
                    $('#note_richiesta').text(data.note);
                    
                    const statoPropostaMapping = {
                    ACCETTATO: "Accettato",
                    RIFIUTATO: "Rifiutato",
                    IN_ATTESA: "In Attesa",
                    ORDINATO: "Ordinato"
};

                    if (data.proposte && data.proposte.length > 0) {
                    var proposteHtml = '';
                    data.proposte.forEach(function (proposta) {
                        var readableStatoProposta = statoPropostaMapping[proposta.stato] || proposta.statoProposta;
                        

                        var propostaHtml = `
                            <div class="proposta-card">
                                <div><strong>Produttore:</strong> ${proposta.produttore}</div>
                                <div><strong>Prodotto:</strong> ${proposta.prodotto}</div>
                                <div><strong>Codice Prodotto:</strong> ${proposta.codice}</div>
                                <div><strong>Prezzo:</strong> ${proposta.prezzo}</div>
                                <div><strong>URL:</strong> <a href="${proposta.url}" target="_blank">Vai al prodotto</a></div>
                                <div><strong>Stato Proposta:</strong> ${readableStatoProposta}</div>`;

                        if (proposta.motivazione) {
                            propostaHtml += `<div><strong>Motivazione:</strong> ${proposta.motivazione}</div>`;
                        }

                        if (proposta.note) {
                            propostaHtml += `<div><strong>Note:</strong> ${proposta.note}</div>`;
                        }

                        propostaHtml += `</div>`;
                        proposteHtml += propostaHtml;
                    });
                    
                    $('#proposte').html(proposteHtml);
                } else {
                    $('#proposte').html('<div class="proposta-card empty">Nessuna proposta disponibile.</div>');
                }
                },
               
                error: function (xhr) {
                    if (xhr.status === 401) {
                        alert("Si prega di effettuare l'accesso.");
                    window.location.href = "index.html";
                    return;   }
        
                    else {  alert("Errore durante il caricamento dettagli delle richieste.");
                    }
                }
            });
        }
        
        $('#eliminaRichiesta').click(function () {
        if (confirm("Sei sicuro di voler eliminare questa richiesta?")) {
            $.ajax({
                url: `/WebMarketREST/rest/richieste/${richiestaId}`,
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (response) {
                    window.location.href = "richieste.html";
                },
                error: function (xhr) {
                    if (xhr.status === 401) {
                        alert("Si prega di effettuare l'accesso.");
                    window.location.href = "index.html";
                    return;   }
        
                    else {  alert("Errore durante il l'eliminazione delle richieste.");
                    }
                }
            });
        }
    });


        caricaDettagliRichiesta();
        
        
    });
