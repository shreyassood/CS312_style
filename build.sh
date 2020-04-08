#!/bin/bash

(cd web &&
	npm install &&
	npm run build &&
	cd .. &&
	rm -rf ./src/main/resources/static &&
	cp -r web/build ./src/main/resources/static)
