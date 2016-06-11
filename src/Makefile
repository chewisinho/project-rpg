all:
	javac -cp ".:lib/*" project_rpg/*.java project_rpg/behaviors/*.java

.PHONY: clean run

clean:
	find . -name "*.class" -type f -delete

run:
	java -cp ".:lib/*" project_rpg/Main

