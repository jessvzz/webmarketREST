$(document).ready(function () {
    var token = localStorage.getItem("authToken"); //mi sono salvata il token nel localstorage che genio
    var uid = localStorage.getItem("utenteId"); 
    
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
        function caricaProposte() {
            $.ajax({
                url: "http://localhost:8080/WebMarketREST/rest/proposte/in_attesa?userId="+uid,
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
                                        <p class="card-row-text">Prodotto: ${proposta.prodotto}</p>
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
                error: function (xhr) {
                    if (xhr.status === 401) {
                        alert("Si prega di effettuare l'accesso.");
                    window.location.href = "index.html";
                    return;   }
        
                    else {  alert("Non ci sono proposte in attesa per questo utente!");
                    }
                }
            });
        }

        caricaProposte();

    });
