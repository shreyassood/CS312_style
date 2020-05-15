# CS312_style
Style Checker for CS312 at UT.

#### Production: https://cs312style.herokuapp.com/
#### Stage: https://cs312style-stage.herokuapp.com/ (Should be in sync with master)

### Status
![Build CI](https://github.com/shreyassood/CS312_style/workflows/Build%20CI/badge.svg)


## Local Run Instructions:
`mvn clean install && heroku local`

See https://github.com/shreyassood/CS312_style/tree/master/web on how to run React frontend.

## Deployment:

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/shreyassood/CS312_style)

Uses multiple buildpacks (Java-Maven and Node) to setup Spring Backend and Build React Frontend
