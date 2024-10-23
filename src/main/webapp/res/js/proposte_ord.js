$(document).ready(function () {
    var token = localStorage.getItem("authToken"); //mi sono salvata il token nel localstorage che genio
    console.log('token: '+ token);

    //formatto data
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('it-IT', options);
    }
        function caricaProposte() {
            $.ajax({
                url: "http://localhost:8080/WebMarketREST/rest/proposte/in_attesa",
                method: "GET",
                 headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (data) {
                    var proposteContainer = $('#proposte-container');
                    proposteContainer.empty(); 

                    data.forEach(function (proposta) {
                        var cardHtml = `
                                <a href="proposta.html" class="a-cards" data-id="${proposta.id}">                                
                                    <div class="card-row-salm proposta-container" data-stato="${proposta.stato}" data-codice="${proposta.codice}">
                                    <div class="card-row-content mb-2">
                                        <p class="card-row-text">Codice: ${proposta.codice}</p>
                                    </div>
                                    <div class="card-row-content mb-2">
                                        <p class="card-row-text">Categoria: ${proposta.categoria}</p>
                                    </div>
                                    <div class="card-row-content mb-2">
                                        <p class="card-row-text">Data: ${new Date(proposta.data).toLocaleDateString()}</p>
                                    </div>
                                    
                                </div>
                            </a>
                        `;

                        proposteContainer.append(cardHtml);
                    });
                    $(".a-cards").on('click', function (event) {
                    event.preventDefault(); 
                    const propostaId = $(this).data('id');
                    localStorage.setItem("propostaId", propostaId);
                    window.location.href = "proposta.html";
                });
                },
                error: function (xhr, status, error) {
                    console.error('Errore nel caricamento delle proposte:', error);
                    alert('Errore nel caricamento delle proposte.');
                }
            });
        }

        caricaProposte();

    });
