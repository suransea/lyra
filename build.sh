#!/bin/bash

mkdir build

javac -cp .:./lib/args4j-2.0.1.jar -d ./build ./src/com/sea/*.java