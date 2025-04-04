# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Phase 2:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco4LpOzz6K8SwgX0ALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhy3K8gagrCjAr5iI6zoEgRLIerqmSxv604hq60qRhy0YwLxDqsYmyawSW6E8tmuaYP+ILwSUVwDGRoxQTWQazs246ttB7YqYU2Q9jA-aDr0GkjlpBnVlOumNvpE5Gcuq7eH4gReCg6B7gevjMMe6SZJgZkXkU1DXtIACiu4xfUMXNC0D6qE+3TTnpaDGWyynqZlznzvZbbKWyuFIQFvpoZVYCYRiOFynh7GUYRRgoNwPFBgGTlzhRTJutRpTSO1FKGPx2hCmEBVzpJeHSUCsk1QpCB5jJJmRWppYwN+V7reFYB9gOQ7bWYnAeeugSQrau7QjAADio6ssFp5heezCqded0Jcl9ijhlPXoDl82pvlANFa5MELVABTlTAyCxGh0J1dhs0FIJpIwOSYC8d1daFX1ZoRoUlowLRtrjfIk0wNNgO4WjHEY-DYAPaMqiwgTVHCeUkQWKgNDU8qmOPaYuGdnle5I2oimlUml5RWm-S-azLYVIro4AJLSC2ACMvYAMwACxPCemQGhWxVfDoCCgA2ZvARbTxKygABy9sQ40O3y52+2HVZavKwZqt9E7ms6-rRtTCb+qaWOrlPFbNt23ZcdTE7rvJ4Zewe6dK6eJ5G7YD4UDYNw8DcYYLOGM9oX7WVu3lDeDQ-X9wRg0Oaejp7xzA8CoN43O2mp6O6ex5nSlrap4IwJ6eqwnA5fI1iqP5Oj7qYxSOM02gHMDVzpM8uTQYMVNYPL6v5Qz5klewk7AkM0T7JWjaQujBJouy2cUPlPPXqZMtq1Q07D+dSIctYGV1obPYMAganB9hZI61lQFh0gTnc6XkAiWHashZIMAABSEAeT3VHIEBOIAGyvRyO9OWm1qiUjvC0J2-1+7oCHCXYAmCoBwAgMhKAsxQFdw7D3ACzxrYcK4TwhcAB1Fg6tEotAAEK7gUHAAA0l8JB4Dw6Q1TJPeUMAABWBC0CwnwfJFAaJ6pn3vkRbGXUt47yEsTDk+9eQU2AFTLeViWqcXXszUcN8NbSAceoAoJMyYv0MIxUBXj+o+L8FoK+-jb7aGCaoUJzjKTYASYYJ2VNknyFmmLNa5RTFoH-uPQBH9gFbQEXtN6vtjpLjOnnC6AQvDsK7F6WAwBsAl0IPERIKQQpnkoXXeWDdYrxUSslYwxkCjix6CdZSujELT24HgdmMTCYX3WVAEiqT0nf12ayYAgsbgaDpivaxayuk4wOfkEmiAuknMFtOC5jV6beIxiAXZ197mPOOQLBAET3kISKV-TpeBykyw+grE6P5vb1PgX7E67kgA