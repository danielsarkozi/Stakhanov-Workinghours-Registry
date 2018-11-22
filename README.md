# Stakhanov-Workinghours-Registry

A projektünk célja, egy munkaidő nyilvántartó alkalmazás kidolgozása és megvalósítása, amely növelné a munkaadó és munkavállaló között az átláthatóságot, illetve a hatékonyságot.

Funkcionális követelmények, szakkifejezések:
 - Munkakörök (Team) - mivel online alkalmazást fejlesztünk, így több különböző cég is igénybeveheti a szolgáltatásunkat. Amikor egy cég (admin) regisztrál az oldalra, akkor egy munkakört hoz létre, amelyhez csatlakozhatnak az alkalmazottak. Ehhez tartozik egy naptár, amelyet a felhasználók maguk szerkeszthetnek.
 - Naptár     (Calendar) - a munkavállalók megtekinthetik a szabad, illetve már lefoglalt napokat, továbbá megjelölhetik a a számukra megfelelő dátumot a munkaelvégzésére.
 - Bejegyzés  (Registry) - A naptáron bejegyzések tehetők közzé, a munkavállaló nevével, az elvállalt munka dátumával és időtartamával. Fűzhető megjegyzés bármely bejegyzéshez a felhasználók által, illetve lehetőség van időpontcserét igényelni.
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

Végpontok:

A végpontokra általánosan igaz, hogy a megtekinthetőségnél egy adott felhasználó, csak a saját érdekeltségébe tartozó adatokat láthatja.
(pl. egy alap felhasználó, csak az saját csapatához tartozó naptárat tudja megtekinteni, de egy admin az összes létrehozottat)

  GET:
    - /calendars - Az összes felhasználói érdekeltségbe tartozó naptár megtekintése
    - /calendars/{id} - Adott id-val rendelkező naptár megtekintése
    - /calendars/{id}/registries - Adott naptárhoz tartozó összes bejegyzés megtekintése
    - /teams - Az összes olyan munkakört kilistázza, amelyben az adott felhasználó tag
    - /teams/{id} - Adott id-val rendelkező munkakört adja meg
    - /teams/{id}/teammates - Visszaadja az adott id-val rendelkező munkakörbe tartozó felhasználó összes publikus adatát
    - /teams/{id}/calendar - Megmutatja az adott id-val rendelkező munkakörhöz tartozó naptárat
    - /registries - Visszaadja az adott felhasználóhoz tartozó összes bejegyzést
    - /registries/{id} - Megmutatja az adott id-val rendelkező bejegyzést
  POST:
    - /login - bejelentkezés
    - /register - regisztálás
    - /calendars - Létrehoz egy naptárat(nem fontos, mert a munkakör létrehozásával, automatikusan generálódik egy)
    - /calendars/{id}/registries - Egy naptárban elhelyez egy bejegyzést
    - /teams - Létrehoz egy munkakört, valamint egy hozzátartozó naptárat
    - /teams/{id}/registry - Közzétesz egy bejegyzést, az adott id-val rendelkező munkakör naptárjában
    - /teams/{id}/calendar - Létrehoz egy naptárat az adott id-hoz tartozó munkakörben(nem fontos, mert a munkakör létrehozásával, automatikusan generálódik egy)
    - /registries - Létrehoz egy bejegyzést (vannak rá kézenfekvőbb végpontok is más kontrollerekben)
  PUT:
    - /calendars/{id} - Adott id-hoz tartozó naptár módosítása
    - /teams/{id} - Adott id-hoz tartozó munkakör módosítása
    - /registries/{id} - Adott id-hoz tartozó bejegyzés módosítása
  DELETE:
    - /calendars/{id} - Naptár törlése amihez az id tartozik. A naptárral együtt törlődnek a bejegyzések is
    - /teams/{id} - Adott munkakör törlése (id). A munkakörrel együtt törlődik a naptár illetve az ahhoz tartozó bejegyzések is
    - /registries/{id0} - Adott id-val rendelkező bejegyzés törlése
