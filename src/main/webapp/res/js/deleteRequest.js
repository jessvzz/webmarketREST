$(document).ready(function () {
    // Gestisce il submit del form di eliminazione della richiesta

     $('#deleteRequestForm').submit(function (event) {
        event.preventDefault(); // Evita il refresh della pagina

        // Recupera l'ID della richiesta dal form
        const requestId = $('#requestId').val();

        // Verifica che l'ID della richiesta sia presente
        if (!requestId) {
            alert("Errore: nessuna richiesta selezionata.");
            window.location.href = "tecnicohomepage.html";
            return;
        }

        if (!token) {
            alert("Errore: token non trovato. Per favore, effettua nuovamente il login.");
            window.location.href = "index.html"; // Reindirizza alla pagina di login se il token non è presente
            return;
        }
        
    
        // Invia la richiesta di eliminazione al server
        $.ajax({
            url: `/WebMarketREST/rest/richieste/${requestId}`,
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("authToken")
            },
            success: function () {
                $('#responseMessage').text("Richiesta eliminata con successo!");
            },
            error: function (xhr, status, error) {
                if (xhr.status === 401) {
                    alert("Si prega di effettuare l'accesso.");
                window.location.href = "index.html";
                return;   }
    
                else {  alert("Errore durante l'eliminazione.");
                }
            }
        });
    });
});
