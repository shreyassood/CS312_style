#!/bin/bash

set -euo pipefail
current=$(dirname $(realpath ${BASH_SOURCE[0]}))

run () {
    (cd web && npm run build)
    rm -rf ./src/main/resources/static
    cp -r web/build ./src/main/resources/static
    mvn package
}

(cd "${current}" && run)