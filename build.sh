#!/bin/bash

run () {
    (cd web && npm install && npm run build)
    rm -rf ./src/main/resources/static
    cp -r web/build ./src/main/resources/static
}

run
