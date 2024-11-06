$(document).ready(function () {
    var token = localStorage.getItem("authToken"); 
    var uid = localStorage.getItem("utenteId"); 

    console.log('token: '+ token);

    if (!token) {
        alert("Errore: token non trovato. Per favore, effettua nuovamente il login.");
        window.location.href = "index.html"; // Reindirizza alla pagina di login se il token non è presente
        return;
    }

    //formatto data
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('it-IT', options);
    }


    $.ajax({
        url: "/WebMarketREST/rest/richieste/in_corso?ordId="+uid,
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            const container = $("#richieste-container");
            container.empty();

            data.forEach(function (richiesta) {
                const richiestaHTML = `
                    <a href="dettaglio_richiesta.html" class="a-cards" data-id="${richiesta.id}">
                        <div class="richiesta-container card-row-skyblue" data-stato="${richiesta.stato}" data-codice="${richiesta.codiceRichiesta}">
                            <div class="card-row-content">
                                <p class="card-row-text">Codice: ${richiesta.codiceRichiesta}</p>
                            </div>
                            <div class="card-row-content">
                                <p class="card-row-text">Categoria: ${richiesta.categoria ? richiesta.categoria.nome : 'N/A'}</p>
                            </div>
                            <div class="card-row-content">
                                <p class="card-row-text">Data: ${formatDate(richiesta.data)}</p>
                            </div>
                            <div class="card-row-content" data-state="${richiesta.stato}">
                                <p class="card-row-text">${richiesta.stato}</p>
                            </div>
                        </div>
                    </a>
                `;
                container.append(richiestaHTML);
            });
            $(".a-cards").on('click', function (event) {
                    event.preventDefault(); 
                    const richiestaId = $(this).data('id');
                    localStorage.setItem("richiestaId", richiestaId);
                    window.location.href = "dettaglio_richiesta.html";
                });

        },
        error: function (xhr) {
            if (xhr.status === 401) {
                alert("Si prega di effettuare l'accesso.");
            window.location.href = "index.html";
            return;   }

            else {  alert("Non ci sono richieste in attesa per questo utente");
            }             
            
        }


    });



});
