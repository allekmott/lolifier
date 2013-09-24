if not exist bin goto nobindir
goto compile

:nobindir
echo "Folder 'bin' doesn't exist, creating"
mkdir bin

:compile
cd src
echo "Compiling source"
javac -d ../bin com/loop404/lolifier/*.java
cd ..

:run
cd bin
echo "Attempting to run..."
java com.loop404.lolifier.Lolifier