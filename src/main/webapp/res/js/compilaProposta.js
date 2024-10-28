// $(document).ready(function () {
//     // Recupera il token e l'ID della richiesta salvati in localStorage
//     var token = localStorage.getItem("authToken");
//     var richiestaId = localStorage.getItem("richiestaId");

//     // Assicurati che ci sia un ID di richiesta
//     if (!richiestaId) {
//         alert("Errore: nessuna richiesta selezionata.");
//         window.location.href = "tecnicohomepage.html";
//         return;
//     }

//     if (!token) {
//         alert("Errore: autenticazione non valida. Per favore effettua nuovamente il login.");
//         window.location.href = "index.html";
//         return;
//     }

//     // Gestisci il submit della form
//     $('#propostaForm').submit(function (event) {
//         event.preventDefault(); // Evita il refresh della pagina

//         // Recupera i dati dalla form
//         const propostaData = {
//             produttore: $('#produttore').val(),
//             prodotto: $('#prodotto').val(),
//             codiceProdotto: $('#codice').val(),
//             prezzo: parseFloat($('#prezzo').val()),
//             url: $('#url').val(),
//             note: $('#note').val(),
//             stato: "IN_ATTESA", // Stato predefinito per la proposta
//             data: new Date().toISOString().split("T")[0], // Data di oggi
//             richiestaOrdine: {
//                 id: parseInt(richiestaId)
//             }
//         };

//         // Invia la proposta al server
//         $.ajax({
//             url: "/WebMarketREST/rest/proposte",
//             method: "POST",
//             headers: {
//                 "Authorization": "Bearer " + token
//             },
//             contentType: "application/json",
//             data: JSON.stringify(propostaData),
//             success: function (response) {
//                 alert("Proposta inviata con successo!");
//                 window.location.href = "tecnicohomepage.html"; // Reindirizza alla homepage del tecnico
//             },
//             error: function (xhr, status, error) {
//                 console.error("Errore:", error);
//                 const errorMessage = xhr.responseJSON ? xhr.responseJSON.error : 'Errore nell\'invio della proposta.';
//                 alert(errorMessage);
//             }
//         });
//     });
// });
