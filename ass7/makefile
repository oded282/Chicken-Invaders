# 308338219
# bennooo

compile:
	mkdir bin
	javac -d bin -cp biuoop-1.4.jar:. src/*/*.java
check:
	java -jar checkstyle-5.7-all.jar -c biuoop.xml src/*/*.java
run:
	java -jar space-invaders.jar

jar:
	jar -cfm space-invaders.jar Manifest.mf -C bin . -C resources .
