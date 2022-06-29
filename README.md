# Quiz-app Backend

## Settings
In the resources folder you can find an `application.properties.example` file. Duplicate this file and remove 
`.example`from the filename. In this file you will be able to set some variables that will define the workings 
of the quiz application.

### Quiz Settings

- `quiz.intervalKnown` &rarr; defines the interval at which you *'know'* a student. 
- `quiz.usergradeifwrong` &rarr; default value of 0 and defines by how much the ef value decreases when an answer is wrong. [0 - 4]

### OAuth Settings

- `oauth2.client-id` &rarr;
- `oauth2.client-secret` &rarr;
- `oauth2.token-uri` &rarr;
- `oauth2.user-info-uri` &rarr;
- `oauth2.authorization-uri` &rarr;
- `oauth2.user-name-attribute` &rarr;

## Running the application
