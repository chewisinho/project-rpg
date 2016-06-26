all:
	javac -cp ".:lib/*" -d classes project_rpg/*.java project_rpg/audio/*.java project_rpg/behaviors/*.java

.PHONY: clean run style test

clean:
	find . -name "*.class" -type f -delete

run:
	java -cp "classes:lib/*" project_rpg/Main

style:
	java -jar lib/checkstyle-6.19-all.jar -c/google_checks.xml project_rpg/*.java project_rpg/audio/*.java project_rpg/behaviors/*.java ../tests/project_rpg_tests/*.java | grep -v PackageName

test:
	javac -cp ".:lib/*:../tests/project_rpg_tests:../tests/lib/*" -d classes ../tests/project_rpg_tests/*.java
	java -cp "classes:lib/*:../tests/lib/*" TestRunner

