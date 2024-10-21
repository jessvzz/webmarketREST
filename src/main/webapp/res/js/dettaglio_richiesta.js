    $(document).ready(function () {
        var token = localStorage.getItem("authToken"); 
        var richiestaId = localStorage.getItem("richiestaId");

        if (!richiestaId) {
            alert("Errore: Nessuna richiesta selezionata.");
            window.location.href = "ordinantehomepage.html";
            return;
        }

        function caricaDettagliRichiesta() {
            $.ajax({
                url: `/WebMarketREST/rest/richieste/${richiestaId}/dettagli`,
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (data) {
                    $('#codice_richiesta').text(data.codice_richiesta);
                    $('#codice_richiesta_title').text(data.codice_richiesta);
                    $('#stato_richiesta').text(data.stato_richiesta);
                    $('#categoria').text(data.categoria);
                    $('#data_richiesta').text(data.data_richiesta);
                    $('#note_richiesta').text(data.note_richiesta);

                    if (data.proposta) {
                        var propostaHtml = `
                            <div class="proposta-card">
                                <div><strong>Produttore:</strong> ${data.proposta.produttore}</div>
                                <div><strong>Prodotto:</strong> ${data.proposta.prodotto}</div>
                                <div><strong>Codice Prodotto:</strong> ${data.proposta.codice_prodotto}</div>
                                <div><strong>Prezzo:</strong> €${data.proposta.prezzo}</div>
                                <div><strong>Stato Proposta:</strong> ${data.proposta.stato_proposta}</div>
                                <div><strong>URL:</strong> <a href="${data.proposta.url_prodotto}" target="_blank">Vai al prodotto</a></div>`;

                        if (data.proposta.motivazione_proposta) {
                            propostaHtml += `<div><strong>Motivazione:</strong> ${data.proposta.motivazione_proposta}</div>`;
                        }

                        if (data.proposta.note_proposta) {
                            propostaHtml += `<div><strong>Note:</strong> ${data.proposta.note_proposta}</div>`;
                        }

                        propostaHtml += `</div>`;

                        $('#proposte').html(propostaHtml);
                    } else {
                        $('#proposte').html('<div class="proposta-card empty">Nessuna proposta disponibile.</div>');
                    }
                },
                error: function (xhr, status, error) {
                    console.error('Errore:', error);
                    alert('Errore nel caricamento dei dettagli della richiesta.');
                }
            });
        }

        caricaDettagliRichiesta();
    });
