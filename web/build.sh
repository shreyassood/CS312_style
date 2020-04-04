#!/bin/bash

set -euo pipefail
current=$(dirname $(realpath ${BASH_SOURCE[0]}))

run () {
    npm run build
    rm -rf ../src/main/resources/static
    cp -r build ../src/main/resources/static
    (cd .. && mvn package)
}

(cd "${current}" && run)