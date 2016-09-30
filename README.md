### Reality Mining Datensatz aufbereiten ###
1. Zunächst muss der Reality Mining Datensatz mit dem Skript ***reality_mining_to_csv.m*** aus MATLAB in den Ordner "data_directory/reality_mining/csv" exportiert werden (der Ordner "data_directory" muss sich im Wurzelverzeichnis des Repositories befinden).
2. Anschließend werden mit dem Programm ***src/reality_mining/DatasetPreparationStep1.java*** die Benutzerprofile im JSON-Format erstellt.
3. Nun kann die Datenbank für die Mobilfunkzellen und ihre GPS-Positionen erstellt werden. Dafür muss das Programm ***src/google/GoogleMobileCellDB.java*** ausgeführt werden.
4. Anschließend werden mit dem Programm ***src/reality_mining/DatasetPreparationStep2.java*** die Standortverläufe in den Benutzerprofilen mit den soeben gewonnenen GPS-Positionen versehen.
5. In diesem Schritt wird die Foursquare Datenbank erstellt, welche GPS-Koordinaten und die zugehörigen Lokalitäten enthält. Dafür muss das Programm ***src/foursquare/venue/VenueDB.java*** ausgeführt werden.
6. Die letzte Datenbank welche erstellt werden muss, ist die der Foursquare Kategorien. Dies geschieht mit dem Programm ***src/foursquare/venue/category/CategoryDB.java***.
7. Der letzte schritt werden die Foursquare Kategorien den Aufenthaltsorten zugeordnet und die Tagesprofile erstellt. Dafür muss das Programm ***src/reality_mining/DatasetPreparationStep3.java*** ausgeführt werden.

### Evaluation ###
Die Evaluation geschieht mit den beiden Programmen:
* ***src/location_prediction/geographic/Evaluation.java***
* ***src/location_prediction/semantic/Evaluation.java***
