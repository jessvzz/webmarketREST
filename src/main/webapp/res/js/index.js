let setToken = function (token) {
        bearer_token = token;
        if (token) {
            localStorage.setItem("authToken", token);
        } else {
            localStorage.removeItem("authToken"); 
        }
        let tokenf = document.getElementById("token-field");
        if (tokenf) {
            tokenf.value = token;
        }
    };

    let extractTokenFromHeader = function (header) {
        return header.substring("Bearer".length).trim();
    };
    
let sendRestRequest = function (method, url, callback, acceptType = null, payload = null, payloadType = null, token = null, async = true) {
        let xhr = new XMLHttpRequest();
        xhr.open(method, url, async);
        if (token !== null)
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        if (payloadType !== null)
            xhr.setRequestHeader("Content-Type", payloadType);
        if (acceptType !== null)
            xhr.setRequestHeader("Accept", acceptType);
        if (async) {
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    callback(xhr.responseText, xhr.status, xhr.getResponseHeader("Authorization"));
                }
            };
        }
        xhr.send(payload);
        if (!async) {
            callback(xhr.responseText, xhr.status, xhr.getResponseHeader("Authorization"));
    }
    };

let handleLoginButton = function () {
    let u = document.getElementById("username").value;
    let p = document.getElementById("password").value;

    sendRestRequest(
        "post", "rest/auth/login",
        function (callResponse, callStatus, callAuthHeader) {
            if (callStatus === 200) {
                // parso il json
                let jsonResponse = JSON.parse(callResponse);

                // prendo token e ruolo
                let token = jsonResponse.token;
                let role = jsonResponse.role;

                setToken(token);
                alert("Login eseguito con successo!");

                // reindirizzo in base a utente
                if (role === "ORDINANTE") {
                    window.location.href = "/WebMarketREST/ordinantehomepage.html";
                } else if (role === "TECNICO") {
                    window.location.href = "/WebMarketREST/tecnicohomepage.html";
                } 
            } else {
                alert("Login fallito: " + callStatus);
                setToken(null);
            }
        },
        null,
        "username=" + encodeURIComponent(u) + "&password=" + encodeURIComponent(p), 
        "application/x-www-form-urlencoded",
        null
    );
};



document.getElementById("login-button").addEventListener("click", handleLoginButton);
