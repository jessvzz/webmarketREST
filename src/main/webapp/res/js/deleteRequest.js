document.getElementById("deleteRequestForm").addEventListener("submit", function (event) {
    event.preventDefault(); 
    const requestId = document.getElementById("requestId").value;
    const token = localStorage.getItem("token"); // Assicurati di recuperare il token dal local storage

    if (requestId && token) {
        sendDeleteRequest(requestId, token);
    } else {
        document.getElementById("responseMessage").innerText = "ID richiesta o token mancante.";
    }
});

function sendDeleteRequest(requestId, token) {
    const url = `rest/richieste/${requestId}`; 

    let xhr = new XMLHttpRequest();
    xhr.open("DELETE", url, true);
    xhr.setRequestHeader("Authorization", "Bearer " + token);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                document.getElementById("responseMessage").innerText = "Richiesta eliminata con successo!";
            } else {
                document.getElementById("responseMessage").innerText = "Errore nell'eliminazione: " + xhr.status;
            }
        }
    };
    xhr.send();
}