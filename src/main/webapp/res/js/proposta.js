    $(document).ready(function () {
        var token = localStorage.getItem("authToken"); 
        console.log('token: '+ token);
        var propostaId = localStorage.getItem("propostaId");

        if (!propostaId) {
            alert("Errore: Nessuna richiesta selezionata.");
            window.location.href = "ordinantehomepage.html";
            return;
        }

        if (!token) {
            alert("Errore: token non trovato. Per favore, effettua nuovamente il login.");
            window.location.href = "index.html"; // Reindirizza alla pagina di login se il token non è presente
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
                    $('#codiceRichiesta').text("Richiesta #"+data.richiestaOrdine.codiceRichiesta);

                    var url = data.url;
                    $('#url-link').attr('href', url);
                },
     
               
                error: function (xhr) {
                    if (xhr.status === 401) {
                        alert("Si prega di effettuare l'accesso.");
                    window.location.href = "index.html";
                    return;   }
        
                    else {  alert("Errore durante il caricamento delle richieste.");
                    }
                }
            });
        }
        
        
        function approvaProposta() {
        $.ajax({
            url: `/WebMarketREST/rest/proposte/${propostaId}/approva`,
            method: "PATCH",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            },
            dataType: "text",
            success: function (response) {
                alert("Proposta approvata con successo.");
                window.location.href = "proposte.html";
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert("Si prega di effettuare l'accesso.");
                window.location.href = "index.html";
                return;   }
    
                else {  alert("Errore durante l'approvazione della proposta.");
                }
            }
        });
    }

    $('#accetta-button').on('click', function () {
        approvaProposta();
    });
    
    $('#rifiuta-btn').on('click', function () {
        var motivazione = prompt("Inserisci una motivazione per il rifiuto:");

        if (!motivazione) {
            alert("Devi inserire una motivazione per rifiutare la proposta.");
            return; 
        }

        $.ajax({
            url: `/WebMarketREST/rest/proposte/${propostaId}/rifiuta`,
            method: "PATCH",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/x-www-form-urlencoded"
            },
            dataType: "text",

            data: { 
                motivazione: motivazione
            },
            success: function (response) {
                alert("Proposta rifiutata con successo.");
                window.location.href = "proposte.html";
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    alert("Si prega di effettuare l'accesso.");
                window.location.href = "index.html";
                return;   }
    
                else {  alert("Errore durante il rifiuto della proposta.");
                }
            }
        });
    });
        


        caricaDettagliRichiesta();
        
        
    });

