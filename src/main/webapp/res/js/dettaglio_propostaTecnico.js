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
                $('#produttore').text(data.produttore);
                $('#prodotto').text(data.prodotto);
                $('#codice_prodotto').text(data.codiceProdotto);
                $('#prezzo').text(`${data.prezzo}`);
                $('#url_link').attr('href', data.url);
                $('#note').text(data.note || "Nessuna nota inserita");

            },
            error: function(xhr, status, error) {
                console.error('Errore:', error);
                alert('Errore nel caricamento dei dettagli della proposta.');
            }
        });
    }

    caricaDettagliProposta();
});