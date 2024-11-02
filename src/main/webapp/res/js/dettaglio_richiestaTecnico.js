$(document).ready(function() {
    var token = localStorage.getItem("authToken"); 
    var utenteId = localStorage.getItem("utenteId");
    var richiestaId = localStorage.getItem("richiestaId");


    if (!richiestaId) {
        alert("Errore: Nessuna richiesta selezionata.");
        window.location.href = "tecnicohomepage.html";
        return;
    }

    if (!token) {
        alert("Errore: token non trovato. Per favore, effettua nuovamente il login.");
        window.location.href = "index.html"; // Reindirizza alla pagina di login se il token non è presente
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

                // Caratteristiche della richiesta
                if (data.caratteristiche) {
                    var caratteristicheHtml = '';
                    Object.keys(data.caratteristiche).forEach(function (key) {
                        caratteristicheHtml += `<div><strong>${key}:</strong> ${data.caratteristiche[key]}</div>`;
                    });
                    $('#caratteristiche').html(caratteristicheHtml);
                } else {
                    $('#caratteristiche').html('<div class="caratteristica-card empty">Nessuna caratteristica disponibile.</div>');
                }

                // Proposte      

                const statoPropostaMapping = {
                ACCETTATO: "Accettato",
                RIFIUTATO: "Rifiutato",
                IN_ATTESA: "In Attesa",
                ORDINATO: "Ordinato"
            };


      },
           
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert("Si prega di effettuare l'accesso.");
                window.location.href = "index.html";
                return;   }
    
                else {  alert("Errore durante il caricamento dettaglio richieste.");
                }
            }
        });
    }
        $('#prendiInCarico').click(function () {
            $.ajax({
                url: `/WebMarketREST/rest/richieste/${richiestaId}/presa_in_carico?idtecnico=${utenteId}`,
                method: "PATCH",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (response) {
                    window.location.href = "richiesteInAttesa.html";
                },
                error: function (xhr) {
                    if (xhr.status === 401) {
                        alert("Si prega di effettuare l'accesso.");
                    window.location.href = "index.html";
                    return;   }
        
                    else {  alert("Errore durante l'operazione");
                    }
                }
            });
        
    });
    caricaDettagliRichiesta();

});