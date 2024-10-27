// Per gestire la sessione. Questo script è incluso in ogni pagina che richiede l'autenticazione.
// Se il token non è presente o è scaduto(il server risponde con un errore 401, non autorizzato), 
//l'utente viene reindirizzato alla pagina di login.
// Inoltre, ogni chiamata AJAX effettuata con jQuery imposterà l'header di autorizzazione con il token.

$(document).ready(function () {

    const token = localStorage.getItem("authToken");

    // Imposta un header di autorizzazione per ogni chiamata AJAX
    $.ajaxSetup({
        headers: {
            "Authorization": "Bearer " + token
        },
        error: function (xhr) {
            // Controlla se la risposta è un errore 401 (sessione scaduta o token non valido)
            if (xhr.status === 401) {
                alert("!!!!!Sessione scaduta. Verrai reindirizzato alla pagina di login.");
                localStorage.removeItem("authToken"); // Rimuovi il token scaduto
                window.location.href = "index.html"; // Reindirizza alla pagina di login
            }
        }
    });
});

