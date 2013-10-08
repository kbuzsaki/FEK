#!/bin/sh
# recursive/countlines.sh
total=0
numFiles=0
for currfile in `find . -name "*.java" -print`
do
		lines=(`wc -l $currfile| awk '{print $1}'`)
        total=$[total+lines]
		numFiles=$[numFiles+1]
        echo 'Total: ' $total 'Lines: ' $lines 'File: ' $currfile 'Files: ' $numFiles
done
read -p "Press Enter to Continue..."