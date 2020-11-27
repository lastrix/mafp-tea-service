#!/bin/bash

mvn install -DskipTests && docker build -t mafp/app-tea-service .
