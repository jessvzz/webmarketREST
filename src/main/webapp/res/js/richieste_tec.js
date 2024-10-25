$(document).ready(function () {
    var token = localStorage.getItem("authToken"); //mi sono salvata il token nel localstorage che genio
    console.log('token: '+ token);

    //formatto data
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('it-IT', options);
    }


    $.ajax({
        url: "/WebMarketREST/rest/richieste/in_attesa",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            const container = $("#richieste-container");
            container.empty();

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