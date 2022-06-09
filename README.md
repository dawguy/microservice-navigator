# Microservice Navigator
The goal of this project is to make navigating around gitlab microservices easier. The workflow that I'm looking for is something along the lines of.
1. Pull up terminal
2. Navigate to either directory containing multiple microservices or a particular microservice
3. Run a shell script which starts up this tool
   1. Microservice navigator will give a small list of options confirming where you want to go
   2. Then will call `open -a "Google Chrome" http://{path-to-service}/{selected-sub-path}` which opens the path in google chrome

## Why
Mostly to speed everything up a bit, I'm moving into a position where more gitlab navigation wil be necessary. So having an extensible CLI tool which saves ~15 seconds per repo will be a lifesaver.

## Future ideas
Access an API to automate tasks like confirming what is deployed in each environment for a set of microservices

## What I'm not planning
No plans on skipping any authentication requests on the first repo opened.

No plans on trying to turn microservices in a navigable graph.