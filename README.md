Aufsetzen des Servers:

Entweder das beigefügte Docker-image über Docker starten oder die WAR-Datei die beim Maven-Build erzeugt wurde starten.
Beim start der WAR-Datei muss ein Argument angegeben werden, welches auf das Stammverzeichnis des Servers zeigt.
In diesem Stammverzeichnis (der Ort kann frei gewählt werden, es müssen nur Lese- und Schreibzugriffe bestehen) werden alle Logs und Konfigurationen gespeichert.


Beispielverzeichnis: /home/user/chillimport-files

So wird der Server gestartet mit: "java -jar server.war /home/user/chillimport-files"


In dem Ordner muss eine Datei "server-url.cfg" angelegt und die URL des FROST-Servers dort formlos in die erste Zeile hineinkopiert werden.
Wird eine HTTP-Authentifizierung benötigt so muss der Nutzername und das Passwort in der Datei "username.cfg" im selben Ordner abgespeichert werden.
Das Format für den Nutzer "fabi" mit Passwort "password" ist "fabi:password" (ohne Anführungszeichen) und wird so in die erste Zeile der username.cfg geschrieben.

Ohne angegebene Server-URL wird der Server stoppen, ohne angebenen Username läuft er trotzdem.

Nach dem Start des Servers (circa 10sek) steht er unter Port 8000 zur Verfügung.


DOCKER :

NOTE : Current compose  version only works on Linux. If you wish to use docker compose on MacOS remove the network_mode argument from the docker-compose file. (Note that localhost FROST-Servers can not be used this way)

Compose does not work on Windows due to Path problems.

Docker-Compose File can be found within the repository, replace any <basepath> by an absolute path you wish to use for files created/used by the server and <serverurl> by the used FROST-Server's URL

If you wish to start the docker image from the terminal instead, pass the basepath and serverurl environment variables via command line (sudo docker run -e envName=<...> server)