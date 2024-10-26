$(document).ready(function () {
    var token = localStorage.getItem("authToken"); //mi sono salvata il token nel localstorage che genio
    var idTecnico = localStorage.getItem("idTecnico");

    if (!idTecnico) {
        alert("Errore.");
        window.location.href = "tecnicohomepage.html";
        return;
    }
    
    console.log('token: '+ token);
    console.log('ID Tecnico:', idTecnico);
    //formatto data
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('it-IT', options);
    }

 // Chiamata AJAX per ottenere le richieste gestite dal tecnico loggato
 $.ajax({
    url: `/WebMarketREST/rest/richieste/gestite_da_tecnico/${idTecnico}`, // Modificato per l'endpoint giusto
    method: "GET",
    headers: {
        "Authorization": "Bearer " + token // Passa il token nell'header per autenticazione
    },
    success: function (data) {
        const container = $("#richieste-container");
        container.empty(); // Svuota il container prima di riempirlo con nuove richieste

            data.forEach(function (richiesta) {
                const richiestaHTML = `
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
            } else {
                alert("Errore durante il caricamento delle richieste.");
            }
        }


    });



});
