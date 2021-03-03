# TrafficSimulator
This program simulates different types of intersection. One car is represented as a thread trying to access a resource which is the intersection.

## Each type of intersection described in Romanian

  
**simple_semaphore**  
Masina afiseaza ca a ajuns la semaphore, asteapta cu sleep numarul de secunde
dat in fisier, apoi afiseaza ca a inceput sa conduca.


**simple_n_roundabout**    
Masina afiseaza ca a ajuns la rondou, incearca sa intre prin a da acquire unui
semaphore. Acesta o va opri daca deja sunt N masini in rondou si ii va permite
sa intre doar cand a iesit o masina. In final masina da release semaphore-ului
pentru a permite si altor masini sa intre, si afiseaza ca a iesit din rondou.


**simple_strict_1_car_roundabout**    
Masina incearca sa intre in rondou prin a da synchronize pe banda sa. Aceasta
banda este reprezentata de un string intr-un array si este gasita la indicele
egal cu numarul benzii masinii. Doar o masina poat da syncronized pe stringul
benzii. Avem si o bariera inainte de intrarea in rondou pentru a preveni
masinile din a intra singure in rondou, acestea fiind nevoite sa intre cate una
din fiecare banda in acelasi timp.


**simple_strict_x_car_roundabout**  
Pentru a permite ca mai multe masini sa acceseze o banda am facut un array de
semafoare, fiecare acceptand un maxim de X masini. Astfel doar X masini pot
intra in rondou pe fiecare linie. De asemenea am folosit o bariera care
asteapta un numar de (numar de benzi * numar de masini permise in rondou pe o
singura banda) masini. Astfel toate masinile vor intra in acelasi timp in
rondou. Am folosit aceasta bariera inainte de selectie pentru a nu avea o runda
noua de selectii inainte ca toate masinile anterioare sa iasa din rondou.


**simple_max_x_car_roundabout**  
Aceasta cerinta este asemanatoare cu cea anterioara in care am un array de
semafoare fiecare reprezentand o banda. Fiecare semafor permite unui numar de X
masini sa il acceseze la un moment dat. Astfel este garantat ca in rondou vor
fi maxim X masini de pe fiecare banda.


**priority_intersection**  
Pentru aceasta intersectie folosesc un contor care numara cate masini cu
prioritate sunt in momentul curent in intersectie. Masinile fara prioritate
apoi verifica pe rand, in mod continuu (busy waiting) daca nu mai sunt masini cu
prioritate in intersectie. Asigur faptul ca masinile fara prioritate intra in
aceeasi ordine in care au ajuns folosind un semafor cu fairness pornit.


**crosswalk**  
Fiecare masina intra intr-un loop while in care verifica daca au terminat de
trecut pietonii. In caz negativ intra inapoi in loop si verifica daca pietonii
sunt in trecere. Daca sunt in trecere isi updateaza mesajul curent cu o lumina
rosie, iar daca nu, cu o lumina verde. Dupa aceasta verificare, in caz ca
mesajul curent difera de cel anterior, il va afisa.
Daca pietonii au terminat de traversat atunci verifica daca avea lumina rosie,
caz in care se face verde si afiseaza acest lucru. Mesajele anterioare ale
fiecarei masini sunt tinute intr-un array, accesa de fiecare masina prin id-ul
sau.


**simple_maintenance**  
Masinine trec printr-un loop while chiar daca deja au trecut, pana cand au
trecut toate celelalte masini.
Ideea principala a rezolvarii este ca fiecare masina verifica daca au trecut
deja numarul maxim de masini permis, si in caz negativ, va trece. In caz
pozitiv va astepta pana la urmatoarea iteratie pentru a trece. Cand o masina
trece prin intersectie aceasta incrementeaza numarul total de masini trecute,
dar si numarul de masini care au trecut in aceasta iteratie, pentru a
instiinta celelalte masini. Pentru a asigura ca masinile de pe banda 0 trec
primele folosesc o bariera la care trebuie sa ajunga toate masinile inainte de a
trece la cele de pe linia 1.


**complex_maintenance**  
Asemamator cu punctul precedent fiecare masina trece doar daca nu s-a atins
numarul maxim de masini, doar ca acest lucru se calculeaza pe mai multe benzi.
De asemenea toate masinile trec printr-un loop pana cand au trecut toate
masinile. Verificarea daca benzile vechi se vor elimina sau trece la capatul
cozii se face doar in cazul in care au trecut numarul maxim de masini sau daca
nu mai sunt masini pe liniile vechi.


**railroad**  
Ordinea este mentinuta cu ajutorul unor semafoare cu fairness activat. Toate
masinile afiseaza faptul ca au ajuns la trecere si incrementeaza un contor.
Prima masina care a ajuns asteapta pana cand toate celelalte masini au ajuns,
dupa care schimba valorea unei variabile pentru a le instiinta pe celelalte
masini sa nu mai afiseze measjul ca trenul a trecut, dupa care afiseaza ea acest
mesaj si apoi mesajul ca a pornit.
