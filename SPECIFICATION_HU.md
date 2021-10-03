Írj Java programot a MySQL installációban található world mintaadatbázis lekérdezésére és módosítására. A projekt neve World1_JDBC.

A program az adatbázison adott műveleteket végez el. Az elvégezhető műveleteket menüből teszi kiválaszthatóvá a felhasználó számára. A menü legyen rögzített formájú, számozott menü.

A műveletek általában paraméterezhetők. A program a paramétert vagy paramétereket az adatbázishoz fordulás előtt bekéri a standard bemenetről.

Van olyan művelet is, amelynek paraméterét a felhasználó részlegesen, töredékesen is megadhatja, mégpedig a csillag (*) karakter (dzsóker) használatával. A dzsóker egymást közvetlenül követő, tetszőleges karaktereket helyettesíthet, szám szerint nullát, egyet vagy többet. Dzsókeres megadás ott lehetséges csak, ahol azt a művelet leírása explicite megemlíti.

A program a kiválasztott művelet elvégzése után annak eredményét kinyomtatja a standard kimenetre.

A 3. részfeladatban szereplő műveleteket a program az alábbiak szerint bonyolítsa le.

A létrehozási műveletek terjedjenek ki minden adatra (oszlopra). Az új sor (rekord) létrejöttéről a program jelezzen vissza.

A módosítási műveletek végrehajtási rendje a következő. A program bekéri az adott táblázatbeli kulcs adatát vagy adatait, kilistázza az annak megfelelő sort, majd rákérdez, hogy valóban ezt szándékozik-e módosítani a felhasználó. Megerősítés esetén bekéri az új adatokat, és ennek teljesüléséről visszajelez. A bekért adatok magára a kulcsra ne terjedjenek ki, azaz a kulcsot ne lehessen módosítani.

A törlési műveletek végrehajtási rendje a következő. A program bekéri az adott táblázatbeli kulcs adatát vagy adatait, kilistázza az annak megfelelő sort, majd rákérdez, hogy a felhasználó valóban ezt szándékozik-e törölni. Megerősítés esetén törli a rekordot, és ennek teljesüléséről visszajelez.

A programot írd meg úgy, hogy a menüben szereplő műveletek köre (és sorrendje) legyen könnyen megválasztható egy adott bővebb halmazból, és maga a teljes halmaz is legyen könnyen bővíthető.

1. részfeladat
Készíts mentést (dump SQL script fájlt) a teljes world adatbázis sémáról és azt helyezd biztonságba. Erre szükség lesz, mert a sémát vissza kell majd állítanod a 3. részfeladatot követően.

2. részfeladat
Írd meg a fent vázolt programot. A menüben az alábbi lekérdező műveletek szerepeljenek.

Adott ország (adatbázisban szereplő) városainak listája: a városok neve és lélekszáma, az utóbbi szerinti csökkenő sorrendben.

Dzsókeresen adott kontinens országainak listája: kontinens, ország, főváros neve. (A dzsókeres megadásnak több kontinens is megfelelhet.) Elsődlegesen a kontinensek, másodlagosan az országok neve szerinti ábécé sorrendben.

Információ dzsókeresen adott városról: a város neve, országának neve, lakosságának részaránya az országéhoz képest, valamint annak jelzése, hogy főváros-e. (A dzsókeres megadásnak több város is megfelelhet, így az eredmény általában itt is egy lista.) A városok neve szerinti ábécé sorrendben.

Adott nyelvet beszélő országok listája: ország neve, a nyelvet beszélők száma, valamint annak jelzése, hogy a nyelv ott hivatalos-e. Elsődlegesen az utóbbi szerint, másodlagosan a beszélők száma szerint csökkenő sorrendbe rendezve.

Adott számot meghaladó beszélővel rendelkező nyelvek listája: a nyelv neve, az országok száma, a beszélők száma. Az utóbbi szerinti csökkenő sorrendbe rendezve.

A kontinensek listája az átlagos egy főre eső GNP adattal. A listán ne szerepeljen a lényegében lakatlan (ill. nagyon alacsony teljes GNP-vel rendelkező) Antarktisz. A sorrend legyen a listázott GNP per capita számok csökkenő sorrendje. A lista után a program nyomtassa ki a világátlagot is.

3. részfeladat
Bővítsd a menüt a következő műveletekkel.

Város létrehozása

Város módosítása (ID alapján): lélekszám és közigazgatási terület neve

Város törlése (ID alapján)

Beszélt nyelv létrehozása egy országban

Beszélt nyelv módosítása egy országban (nyelv és országkód alapján): beszélői számarány és hivatalos jelleg

Beszélt nyelv törlése egy országban (nyelv és országkód alapján)
