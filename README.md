# Themenforum API

Hochschule Osnabrück, Prüfungsleistung im Modul SWA, WS22/23

Das Themenforum bietet eine Plattform, auf der Nutzer Beiträge posten und kommentieren können.
Posts und Kommentare können mit Up- und Downvotes von den Nutzern bewertet werden. Die
geposteten Beiträge sind immer einem Thema zugeordnet. Diese Oberthemen können ebenfalls von
den Nutzern erstellt werden, und sind dann für alle anderen zugänglich. Als nicht registrierter Nutzer kann man alle Beiträge und Kommentare sehen. Möchte man selbst
einen Post erstellen oder kommentieren, benötigt man einen Account.


## Starten der Anwendung im Devmode

Die Anwendung kann mit folgendem Befehl gestartet werden
```shell script
./mvnw quarkus:dev
```

> Die Swagger-Dokumentation ist verfügbar unter:  http://localhost:8080/q/swagger-ui.
              
### Insomnia
Nach dem Start lässt sich die Anwendung über das bereitgestellte Insomnia-Dokument manuell testen. 

           
### Webfrontend
> Das Webfrontend ist verfügbar unter:  http://localhost:8080/ui/public.

Hier kann man zunächst die Funktionen als nicht-registrierter Nutzer testen und die Posts und Themen ansehen (Browse Topics, Browse Posts).

Registriert man sich auf dem Forum, kann man Posts und Themen erstellen und Kommentare und Bewertungen (Up-/Downvotes) abgeben.
Bewertungen werden über die Buttons links neben dem Post oder Kommentar abgegeben und können dort über das "X" wieder entfernt werden.
Kommentare können über das Formular unter einem Post erstellt werden. 
Über die Schaltfläche "reply" kann auf ein Kommentar geantwortet werden (hier gerne die Sortierung über verschiedene Kommentar-Ebenen testen). 

Posts und Kommentare können über die Buttons "Popular" und "New" sortiert werden.

Selbst erstellte Inhalte können über die "delete"-Schaltfläche gelöscht werden.
Auf der privaten Profilübersicht (Schaltfläche oben rechts) werden die eigenen Inhalte im Forum verwaltet.
Diese sind entsprechend verlinkt.

> Die Admin-Übersicht ist verfügbar unter:  http://localhost:8080/ui/admin.
> Hier können alle Inhalte des Forums verwaltet werden.

#### Bereitgestellte Accounts
Folgende Accounts sind bereits im Themenforum vorhanden und mit Inhalten gefüllt (Passwort entspricht Namen).
- oschluet
- lbattist
- admin (Admin-UI nur mit diesem Nutzer erreichbar)

## Testen der Anwendung 
Die Rest-Assured Tests werden mit folgendem Befehl ausgeführt
```shell script
./mvnw test
```

## Autoren

- Oliver Schlüter, 914726 - oliver.schlueter@hs-osnabrueck.de

- Lorenzo Battiston, 919355 - lorenzo.battiston@hs-osnabrueck.de

