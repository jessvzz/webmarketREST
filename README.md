# TO DO LIST

## Gea

- [x]   Login con username e password.
- [x]   Associazione di una richiesta di acquisto a un tecnico incaricato
- [x]   Approvazione (da parte dell'ordinante) di una  _proposta di acquisto_
- [x]   Estrazione lista delle richieste di acquisto  _in corso_  (non chiuse) di un determinato ordinante
- [x]  Estrazione di tutti i dettagli di una richiesta di acquisto (richiesta iniziale, eventuale prodotto candidato, approvazione/rifiuto dell'ordinante con relativa motivazione)
- [x]  logout

## Samanta
- [-] Inserimento di una  _richiesta di acquisto_  (comprensiva di categoria di prodotto, di tutte le caratteristiche richieste per quel tipo di prodotto e delle eventuali note)
- [-]  Inserimento e modifica (da parte del tecnico incaricato) di una  _proposta di acquisto_  associata a una richiesta
- [-] Eliminazione di una  _richiesta di acquisto_  dal sistema
- [-]  Estrazione lista delle richieste di acquisto non ancora assegnate ad alcun tecnico
- [-] Estrazione lista richieste di acquisto gestite da un determinato tecnico

- [ ]: Per l'INSERIMENTO delle RICHIESTE introdurre il modo di far inserire anche le caratteristiche, pensavo ad un array di caratteristiche da introdurre dentro richieste
    per far sì che dopo l'inserimento della richiesta si prende l'id e per ogni caratteristica specificata dall'utente
    si inserisce un record nella tabella `caratteristica_richiesta`.

- [ ]: INSERIMENTO/MODIFICA di una PROPOSTA, devo configurare il sistema in modo che gestisca correttamente i campi facoltativi e obbligatori, sia durante l'inserimento che durante la modifica!! (idea della query string dinamica con i campi coinvolti nell'aggiornamento, lasciando invariati gli altri)

- [ ]: nell'ESTRAZIONE delle LISTE, pensare a quali campi restituire in OUTPUT. ad esempio per come è ora mi restituisce tutti i campi coinvolti, tra cui i dettagli del tecnico (password compresa ecc) 

-[ ]: implementare metodo per verificare se un utente è autorizzato ( isUserInRole(String role) ? )

## ALTRO
- [ ] Contrassegnare tutto come "protected path" con @Context e @Logged
- [x] trova modo per far vedere categoria, utente e tecnico in richieste
- [ ] Rivedere Deserializer richiesta
- [ ] Rivedere path richieste 'gestite_da'

