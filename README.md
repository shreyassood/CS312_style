# CS312_style
Style Checker for CS312 at UT: https://cs312style.herokuapp.com/

### Status
![Build CI](https://github.com/shreyassood/CS312_style/workflows/Build%20CI/badge.svg)


## Local Run Instructions:
`mvn clean install && heroku local`

See https://github.com/shreyassood/CS312_style/tree/master/web on how to run React frontend.

## Deployment:

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/shreyassood/CS312_style)

Add node support on heroku (to build React UI):
`heroku buildpacks:add --index 1 heroku/nodejs`
