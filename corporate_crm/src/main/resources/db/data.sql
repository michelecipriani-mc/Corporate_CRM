-- Inserimento automatico ruoli nel DB
INSERT INTO "public"."ruolo" ("id", "nome") 
VALUES 
    (1, 'ADMIN'),
    (2, 'HR'),
    (3, 'BACKOFFICE'),
    (4, 'VENDITORE'),
    (5, 'CANDIDATO'),
    (6, 'UTENTE')
ON CONFLICT (id) DO NOTHING;