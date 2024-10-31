# TO DO LIST
- [x] aggiungere caratteristiche dove servono e cambiare in caso il file openapi
- [x] inserire l'errore 401 in tutte le responses del file openapi
- [ ] gestire correttamente nel codice le varie eccezioni aggiunte (tipo 401 non è gestita in  richieste/in_attesa)
- [ ] cambiare schema liste openapi --> non lo so in realta
- [ ] controlla tutte le responses (soprattutto quelle che rispondono con html)
- [x] togliere amministratore db 
- [x] Togliere presa in carico ecc da client tecnico ("prendi in carico" da fixare).
- [ ] inserire tutti gli esempi necessari nel file yaml
- [?] Nel dettaglio richiesta client lato ordinante mostrare anche gli altri campi tipo motivazione, stato proposta --> necessario?
- [x] Contrassegnare tutto come "protected path" con @Logged
- [x] semplice client tecnico
- [x] dettagli richiesta --> da metterci anche le caratteristiche
- [x] nei GET delle proposte la richiesta è sempre null
- [x] rivedere POST della proposta
- [x] ENDPOINT richieste/gestite_da da rivedere
- [x] Gestire sessione scaduta --> deve rimandare al login

- [ ] per Gea --> rileggiti tutto file yaml e capisci bene cosa mettere sui campi required lol
- [ ] non so se mi piace uno schema per uid
- [x] inserimento e modifica DA PARTE DEL TECNICO INCARICATO penso di voler fare una roba
- [x] fai stessa roba per rifiuto proposta
- [x] modifica presaInCarico

- Non mi funziona richieste/in_corso su postman, su client sì.. mah
- devo finire di controllare se sono presenti tutte le eccezioni e se gestite correttamente nel codice.
- nel file yaml mi da warning su RichiestaOrdineList, perchè mai utilizzato.
- in richieste/in_attesa manca il controllo del id utente per il 401. va lasciato così com'è il metodo ed eventualemnte togliere tra le responses il 401 o è giusto gestirlo in tutti gli endpoint il caso non fosse autorizzato l'utente?
- Ma l'errore 404 not found va messo a ogni endpoint e gestito? per capire se modificare lo yaml togliendo '404' per gli endpoint che non lo riguardano.
- Gestire il codice 500 in getRichiesta, non so dove è meglio posizionare il catch.

