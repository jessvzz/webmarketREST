$(document).ready(function () {
    var token = localStorage.getItem("authToken"); //mi sono salvata il token nel localstorage che genio
    console.log('token: '+ token);

    //formatto data
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('it-IT', options);
    }


    $.ajax({
        url: "/WebMarketREST/rest/proposte/in_attesa",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            const container = $("#richieste-container");
            container.empty();

            data.forEach(function (proposta) {
                const propostaHTML = `
                    <a href="dettaglio_propostaTecnico.html" class="a-cards" data-id="${proposta.id}">
                        <div class="richiesta-container card-row-skyblue" data-stato="${proposta.stato}" data-codice="${proposta.codice}">
                            <div class="card-row-content">
                                <p class="card-row-text">Codice: ${proposta.codice}</p>
                            </div>
                            <div class="card-row-content">
                                <p class="card-row-text">Prodotto: ${proposta.prodotto}</p>
                            </div>
                            <div class="card-row-content">
                                <p class="card-row-text">Data: ${formatDate(proposta.data)}</p>
                            </div>
                            <div class="card-row-content" data-state="${proposta.stato}">
                                <p class="card-row-text">${proposta.stato}</p>
                            </div>
                        </div>
                    </a>
                `;
                container.append(propostaHTML);
            });
            $(".a-cards").on('click', function (event) {
                    event.preventDefault(); 
                    const propostaId = $(this).data('id');
                    localStorage.setItem("propostaId", propostaId);
                    window.location.href = "dettaglio_propostaTecnico.html";
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
