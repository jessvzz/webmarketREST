$(document).ready(function () {
    var token = localStorage.getItem("authToken"); //mi sono salvata il token nel localstorage che genio
    console.log('token: '+ token);

    //formatto data
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('it-IT', options);
    }


    $.ajax({
        url: "/WebMarketREST/rest/richieste/in_corso",
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (data) {
            const container = $("#richieste-container");
            container.empty();

            data.forEach(function (richiesta) {
                const richiestaHTML = `
                    <a href="dettaglio_richiesta_ord?n=${richiesta.id}" class="a-cards">
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
        },
        error: function (xhr) {
            if (xhr.status === 401) {
                alert("Si prega di effettuare l'accesso.");
            } else {
                alert("Errore durante il caricamento delle richieste.");
            }
        }
    });

    // filtro stato richieste
    $('#status').on('change', function () {
        const selectedStatus = $(this).val();
        $(".richiesta-container").each(function () {
            const richiestaStatus = $(this).data('stato');
            if (selectedStatus === 'tutti' || richiestaStatus === selectedStatus) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
});