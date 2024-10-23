    $(document).ready(function () {
        var token = localStorage.getItem("authToken"); 
        var propostaId = localStorage.getItem("propostaId");

        if (!propostaId) {
            alert("Errore: Nessuna richiesta selezionata.");
            window.location.href = "ordinantehomepage.html";
            return;
        }

        function caricaDettagliRichiesta() {
            $.ajax({
                url: `/WebMarketREST/rest/proposte/${propostaId}`,
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (data) {
                    $('#codice').text("Proposta #" + data.codice);
                    $('#produttore').text(data.produttore);
                    $('#prodotto').text(data.prodotto);
                    $('#codiceProdotto').text(data.codiceProdotto);
                    $('#prezzo').text(data.prezzo);
                    $('#note').text(data.note);
                    var url = data.url;
                    $('#url-link').attr('href', url);
                },
     
               
                error: function (xhr, status, error) {
                    console.error('Errore:', error);
                    alert('Errore nel caricamento dei dettagli della richiesta.');
                }
            });
        }
        
        
        function approvaProposta() {
        $.ajax({
            url: `/WebMarketREST/rest/proposte/${propostaId}/approva`,
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            dataType: "text",
            success: function (response) {
                alert("Proposta approvata con successo.");
                window.location.href = "proposte.html";
            },
            error: function (xhr, status, error) {
                console.error('Errore:', error);
                alert("Errore durante l'approvazione della proposta.");
            }
        });
    }

    $('#accetta-button').on('click', function () {
        approvaProposta();
    });
        


        caricaDettagliRichiesta();
        
        
    });

