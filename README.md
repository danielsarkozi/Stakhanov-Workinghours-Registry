# Stakhanov-Workinghours-Registry

A projektünk célja, egy munkaidő nyilvántartó alkalmazás kidolgozása és megvalósítása, amely növelné a munkaadó és munkavállaló között az átláthatóságot, illetve a hatékonyságot.

Funkcionális követelmények, szakkifejezések:
 - Munkakörök - mivel online alkalmazást fejlesztünk, így több különböző cég is igénybeveheti a szolgáltatásunkat. Amikor egy cég (admin) regisztrál az oldalra, akkor egy munkakört hoz létre, amelyhez csatlakozhatnak az alkalmazottak. Ehhez tartozik egy naptár, amelyet a felhasználók maguk szerkeszthetnek.
 - Naptár - a munkavállalók megtekinthetik a szabad, illetve már lefoglalt napokat, továbbá megjelölhetik a a számukra megfelelő dátumot a munkaelvégzésére.
 - Bejegyzés - A naptáron bejegyzések tehetők közzé, a munkavállaló nevével, az elvállalt munka dátumával és időtartamával. Fűzhető megjegyzés bármely bejegyzéshez a felhasználók által, illetve lehetőség van időpontcserét igényelni.
 - Lehetőség van a saját profil szerkesztésére, illetve a munkakörön belüli profilok megtekintésére.

Nem funkcionális követelmények:
 - Igényes megjelenés
 - Intuitív kezelőfelület
 - Gyors működés, megbízhatóság
 
Szerepkörök:
 - admin - létrehozhat munkakört; készíthet bejegyzést; felülírhat, törölhet bejegyzéseket; megjegyzést fűzhet; felhasználókat távolíthat el a munkakörből; profilokat tekinthet meg
 - felhasználó - készíthet bejegyzést; megjegyzést fűzhet; profilokat tekinthet meg
 - vendég - regisztrálhat az oldalon
 
Adatbázis model:

![alt text](https://github.com/danielsarkozi/Stakhanov-Workinghours-Registry/blob/master/dbmodel.PNG)

