CLASS_DIR=classes
SRC_DIRS=.:lib:lib/*
SRC_FILES=project_rpg/*.java project_rpg/enums/*.java
TEST_DIRS=../tests/lib/*:../tests/project_rpg_tests
TEST_FILES=../tests/project_rpg_tests/*.java

all:
	javac -cp $(SRC_DIRS) -d $(CLASS_DIR) $(SRC_FILES)

.PHONY: clean run style test

clean:
	find . -name "*.class" -type f -delete

run:
	java -cp $(CLASS_DIR):$(SRC_DIRS) project_rpg/Main

style:
	java -jar lib/checkstyle-6.19-all.jar -c/google_checks.xml $(SRC_FILES) $(TEST_FILES) | grep -v PackageName

test:
	javac -cp $(SRC_DIRS):$(TEST_DIRS) -d $(CLASS_DIR) $(TEST_FILES)
	java -cp $(CLASS_DIR):$(SRC_DIRS):$(TEST_DIRS) TestRunner

