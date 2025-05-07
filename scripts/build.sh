#!/bin/bash

BLUE='\033[0;34m'
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}Compiling RoboApplication.java...${NC}"
nxjc -sourcepath src -d out src/main/RoboApplication.java

if [ $? -ne 0 ]; then
  echo -e "${RED}Compilation failed. Exiting.${NC}"
  exit 1
fi

echo -e "${GREEN}Compilation successful.${NC}"

if [ "$1" == "--upload" ]; then
  echo -e "${BLUE}Upload flag detected. Linking and running the program on the NXT...${NC}"

  cd out
  nxj -r -o RoboApp.nxj RoboApplication

  if [ $? -ne 0 ]; then
    echo -e "${RED}Linking or running failed.${NC}"
    cd ..
    exit 1
  fi

  cd ..
  echo -e "${GREEN}Program execution finished or started on NXT.${NC}"
else
  echo -e "${GREEN}No --upload flag provided. Only compiled the code.${NC}"
fi

exit 0
