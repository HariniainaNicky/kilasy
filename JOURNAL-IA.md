# Journal IA — Kilasy, pénalité de retard

Outil utilisé : OpenCode (agent IA en terminal), modèle DeepSeek V4.1 Flash Free.

## Changement : pénalité de retard sur les notes

### Échange 1 — planification
Demande : « −10 % de la note par jour de retard entamé, plafond −50 % »,
exemples 16 → 16 / 14,4 / 11,2 / 8.
Proposition : une classe `Grades` (lateDays, penaltyPercent, gradeOutOf20,
finalGrade) branchée aux quatre endroits qui calculent une note :
GET /submissions/{id}, PUT /submissions/{id}/grade, bulletin, export CSV.
Champs `lateDays`, `penaltyPercent`, `rawGrade` ajoutés aux réponses JSON,
pénalité expliquée dans l'email, README mis à jour.
Décision : gardé.
Pourquoi : la note /20 était recalculée à quatre endroits ; une seule fonction
évite toute divergence. La pénalité se déduit de `deadline` + `submitted_at`,
donc aucune colonne en base.
Vérification : les quatre exemples du cahier des charges sont couverts par la
formule, plan relu avant d'écrire du code.

### Échange 2 — implémentation
Demande : implémenter, commit, push, pull request.
Proposition : `Grades.java` et branchement dans `SubmissionController`,
`GradeReport`, `CsvExport`, README ; commit `6de7f16` ; PR #1.
Décision : gardé.
Vérification : `mvn compile` passe.

### Échange 3 — contrôle
Demande : vérifier la règle sur les données de démo.
Proposition : appels manuels sur le serveur relancé.
Résultat :
- GET /submissions/1..4 → 16,0 / 14,4 / 11,2 / 8,0
- bulletin : notes et moyenne pénalisées
- CSV : colonne `jours_de_retard` correcte, note pénalisée
- PUT /submissions/7/grade : email « 15,3/20 — note brute 17,0/20 »
Décision : correction demandée.
Pourquoi : le CSV recalculait les jours de retard en SQL en plus de
`Grades.lateDays`, et les commentaires ajoutés étaient en français alors que le
reste du code est en anglais.

### Échange 4 — correction
Demande : refactorer uniquement le code ajouté, sans toucher au reste.
Proposition : calcul unique via `Grades.lateDays` (expression SQL supprimée),
commentaires en anglais, `french()` renommé `formatGrade()`, `max_points` lu
une seule fois, README ramené à une puce ajoutée. Commit `3e35d53`.
Décision : gardé.
Vérification : recompilation, mêmes valeurs qu'au contrôle précédent, données
de démo restaurées après le test de notation.

## Bilan

- Fichiers créés : `src/main/java/hei/school/Grades.java`, `JOURNAL-IA.md`
- Fichiers modifiés : `SubmissionController.java`, `GradeReport.java`,
  `CsvExport.java`, `README.md`
