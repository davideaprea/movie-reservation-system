# WAL
- creazione booking e order in una transaction
- pagamento completo verso gateway
- salvataggio pagamento
- schedule che controlla perdiodicamente gli ordini. Se non c'è nessun pagamento, chiama prima il gateway per chiederne lo stato.
dopodichè, viene registrato il pagamento o eliminato l'ordine se il pagamento non è andato a buon fine