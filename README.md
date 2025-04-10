# 👾 Event-Driven-Systems-Homework 👾

## Membri
1. Balan Florin
2. Ciudin Stefana
3. Jitca Diana 

## Cerinta
Scrieti un program care sa genereze aleator seturi echilibrate de subscriptii si publicatii cu posibilitatea de fixare a: numarului total de mesaje (publicatii, respectiv subscriptii), ponderii pe frecventa campurilor din subscriptii si ponderii operatorilor de egalitate din subscriptii pentru cel putin un camp. Publicatiile vor avea o structura fixa de campuri. Implementarea temei va include o posibilitate de paralelizare pentru eficientizarea generarii subscriptiilor si publicatiilor, si o evaluare a timpilor obtinuti.

## Exemplu
Publicatie: `{(stationid,1);(city,"Bucharest");(temp,15);(rain,0.5);(wind,12);(direction,"NE");(date,2.02.2023)}` - Structura fixa a campurilor publicatiei e: stationid-integer, city-string, temp-integer, rain-double, wind-integer, direction-string, date-data; pentru anumite campuri (stationid, city, direction, date), se pot folosi seturi de valori prestabilite de unde se va alege una la intamplare; pentru celelalte campuri se pot stabili limite inferioare si superioare intre care se va alege una la intamplare.

Subscriptie: `{(city,=,"Bucharest");(temp,>=,10);(wind,<,11)}` - Unele campuri pot lipsi; frecventa campurilor prezente trebuie sa fie configurabila (ex. 90% city - exact 90% din subscriptiile generate, cu eventuala rotunjire la valoarea cea mai apropiata de procentul respectiv, trebuie sa includa campul "city"); pentru cel putin un camp (exemplu - city) trebui sa se poate configura un minim de frecventa pentru operatorul "=" (ex. macar 70% din subscriptiile generate sa aiba ca operator pe acest camp egalitatea).

## Explicatii
1. Introducere
   
   Aplicatia dezvoltata reuseste sa implementeze cerintele din enunt, folosindu-se de structura prezentata mai jos.
   Pentru realizarea cerintei referitoare la paralelizare, am utilizat thread-uri, asupra carora am realizat si diferite teste.
   Metricile obtinute sunt prezentate in tabelul final.
2. Structura
   - `SubscriptionData` defineste un custom type ce reprezinta cum arata fiecare tupla din generari. Contine *tipul*, *operatorul* si *valoarea* asignata.
   - `Subscription` reprezinta o subscriptie care contine mai multe SubscriptionData.
   - `Publication` reprezinta o publicatie care include mai multi parametrii : stationId, city, temp, rain, wind, direction si date.
   - `PublisherSpout` este responsabila cu generarea de obiecte Publication cu valori random pentru fiecare field.
   - `SubscriptionSpout` este responsabile de generarea de obiecte Subscription pe baza frecventelor de camp predefinite si a frecventelor de egalitate. Se asigura ca subscriptiile generate respecta proportiile specificate pentru fiecare camp si operator.
       - variabila `totalSubscriptions` defineste numarul total de subscriptii pe care vrei sa le genereze
       - fiecare apel la nextTuple():
          1. verifica daca generatedSubscriptions a ajuns la totalSubscriptions
          2. daca da, returneaza null, deci nu mai genereaza nimic
          3. altfel, genereaza o noua subscriptie si incrementeaza generatedSubscriptions
   - `BasicBolt` este folosita pentru a simula o topologie adevarata, care proceseaza tuplele prin a le afisa direct in consola si le scrie intr-un fisier.
   - `ThreadedTask` este conceputa pentru a executa o sarcina Runnable data de un anumit numar de ori, utilizand un numar fix de fire de executie. Acesta masoara si afiseaza timpul necesar pentru finalizarea tuturor sarcinilor.
      - subscriptionSpout.nextTuple() va genera o singura subscriptie per apel, pana cand atinge totalSubscriptions
      - dupa aceea, va returna null, deci nu mai adauga nimic nou
   - `App`, punctul de intrare al aplicatiei Java, demonstreaza utilizarea claselor ThreadedTask, PublisherSpout si SubscriptionSpout. Aceasta creeaza instante ale acestor clase si afiseaza rezultatele in consola.

3. Generarea subsctiptiilor
  - Distributia normala (Gaussiana) pentru generarea valorilor pentru campurile `temp` si `rain`. Aceasta este utilizata pentru a genera valori care urmeaza o distributie normala cu o anumita medie si deviatie standard.
  - Distributia uniforma pentru generarea valorilor pentru campurile `city`, `wind`, `direction`, `stationid` si `date`. Aceasta este utilizata pentru a selecta valori aleatorii dintr-o lista predefinita sau pentru a genera valori intregi in intervale specifice.
    
4. Evaluarea timpilor

| Name                                   | Number Of Cores | Number Of Logical Processors | Number Of Threads | Time  | Sequential |
|----------------------------------------|-----------------|------------------------------|-------------------|-------|------------|
| AMD Ryzen 5 4500U with Radeon Graphics | 6               | 6                            | 4                 | 423ms | 1572ms     |
| 13th Gen Intel(R) Core(TM) i7-13620H   | 10              | 16                           | 4                 | 401ms | 1603ms     |
| 14th Gen Intel(R) Core(TM) i9-14900HX  | 24              | 32                           | 4                 | 400ms | 1214ms     |

- Evaluarea timpilor este manipulata, intrucat s-a folosit un Thread.Sleep pentru a 'simula' crearea unor date mai complicate. Motivul: programul nostru genereaza date foarte rapid, thread-urile suferind de un cost de creare (overhead), care ingreuneaza timpul de executie cu cat avem mai multe thread-uri. Metoda aleasa de noi, dupa cum se observa, nu face o diferenta mare intre puterea procesoarelor, intrucat toate asteapta acelasi timp de sleep. (10ms)
- Testand pe ultimul procesor mentionat, fara acest Sleep, se obtin rezultate inconsistente: cateodata ruleaza mai bine cu un singur thread, cateodata mai rau. In general, 10.000 de publicatii si subscriptii se genereaza in ~250ms, pe cand cu acel Sleep a reusit 100 de astfel de date in ~400, respectiv ~1200ms.