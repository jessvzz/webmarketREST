# TO DO LIST
- [x] aggiungere caratteristiche dove servono e cambiare in caso il file openapi
- [x] inserire l'errore 401 in tutte le responses del file openapi
- [ ] gestire correttamente nel codice le varie eccezioni aggiunte (tipo 401 non è gestita in  richieste/in_attesa)
- [ ] cambiare schema liste openapi --> non lo so in realta
- [ ] controlla tutte le responses (soprattutto quelle che rispondono con html)
- [x] togliere amministratore db 
- [x] Togliere presa in carico ecc da client tecnico ("prendi in carico" da fixare).
- [ ] inserire tutti gli esempi necessari nel file yaml
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


-Capire perchè dentro ProposteService:   int modificaProposta(PropostaAcquisto prop, int idProposta, int techId); gli passo sia la proposta che l'id della proposta..?

-Tolgo getAllRequest, tutti i metodi commentati tipo getAll(), la post che avevo fatto bruttissima addItem(), getRichiesteNonAssegnate(), eliminato file openapi (ho lasciato la versione attuale openapi2.yaml), eliminato file json schemas che tanto non ci servono, faccio il controlo sulla motivazione proposte che se null non lo restituisco in output,getAllRichieste().

