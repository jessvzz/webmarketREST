$(document).ready(function () {
    var token = localStorage.getItem("authToken");

    $('#logout-btn').on('click', function (e) {
        e.preventDefault(); 

        if (!token) {
            alert("Errore: Nessun token trovato. Sarai reindirizzato alla pagina di login.");
            window.location.href = "index.html";
            return;
        }

        $.ajax({
            url: "/WebMarketREST/rest/auth/logout",
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function () {
                localStorage.removeItem("authToken");

                alert("Logout effettuato con successo.");
                window.location.href = "index.html";
            },
            error: function (xhr, status, error) {
                console.error("Errore nel logout:", error);
                alert("Errore durante il logout");
            }
        });
    });
});
