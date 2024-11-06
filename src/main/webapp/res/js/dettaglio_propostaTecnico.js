$(document).ready(function() {
    var token = localStorage.getItem("authToken"); 
    var propostaId = localStorage.getItem("propostaId"); 

    console.log('propostaId: ' + propostaId);
    
    if (!propostaId) {
        alert("Errore: Nessuna proposta selezionata.");
        window.location.href = "proposte_tec.html";
        return;
    }

    if (!token) {
        alert("Errore: token non trovato. Per favore, effettua nuovamente il login.");
        window.location.href = "index.html";
        return;
    }

    function caricaDettagliProposta() {
        $.ajax({
            url: `/WebMarketREST/rest/proposte/${propostaId}`,
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function(data) {
                $('#codice').text(data.codice);
                $('#produttore').val(data.produttore);
                $('#prodotto').val(data.prodotto);
                $('#codice_prodotto').val(data.codiceProdotto);
                $('#prezzo').val(`${data.prezzo}`);
                $('#url_link').attr('href', data.url);
                $('#url_input').val(data.url);
                $('#note').val(data.note || "Nessuna nota inserita");

            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert("Si prega di effettuare l'accesso.");
                window.location.href = "index.html";
                return;   }
    
                else {  alert("Errore durante il caricamento dei dettagli della proposta.");
                }
            }
        });
    }

    caricaDettagliProposta();

    $('#modifica').click(function() {
        $('#url_link').hide();
        $('#url_input').show();
        
        $('input').prop('disabled', false);
        $('#prendiInCarico').hide();
        $('#salvaModifiche').show();
    });

    $('#salvaModifiche').click(function() {
        const propostaAggiornata = {
            produttore: $('#produttore').val(),
            prodotto: $('#prodotto').val(),
            codiceProdotto: $('#codice_prodotto').val(),
            prezzo: parseFloat($('#prezzo').val()),
            url: $('#url_input').val(),
            note: $('#note').val()
        };

        $.ajax({
            url: `/WebMarketREST/rest/proposte/${propostaId}`,
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            data: JSON.stringify(propostaAggiornata),
             dataType: "text", 
            success: function(response) {
                alert("Modifiche salvate con successo.");
                $('input').prop('disabled', true);
                
                $('#url_link').attr('href', $('#url_input').val()).show();
                $('#url_input').hide();
                $('#prendiInCarico').show();
                $('#salvaModifiche').hide();
                caricaDettagliProposta();
            },
            error: function(xhr, status, error) {
                console.error('Errore:', error);
                alert('Errore nel salvataggio delle modifiche.');
            }
        });
    });
});