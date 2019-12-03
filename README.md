# Code_Challenge

Willdom Code Challenge by Jhonny Ventiades, This app show a list of stores and the information to contact each store. 

## Prerequisites
* Android SDK v21
* Latest Android Build Tools
* Android Studio v3.5

## Getting started

1. Clone the project git clone https://github.com/jhonny-ventiades/Code_Challenge
2. Open it with Android Studio 3.5(preferably)
3. Sync Gradle libraries and Run 

## How to use
Libraries configurations are in Project.gradle:

```bash
ext {
    buildToolsVersion = "28.0.3"
    androidxLibVersion = "1.1.0"
    coreVersion = "1.0.0"
    roomPersistence = "2.2.2"
    extJUnitVersion = "1.0.0"
    runnerVersion = "1.2.0"
    espressoVersion = "3.2.0"
}
```
if changes are made,  dont forget run instrumental test. 

## Check Style
This project is using Android Material Components, to update the values modify the next files:
```bash
res-> type.xml  // modify style of widgets
res-> color.xml  //modify colors of buttons, surface and widgets
res-> dimens.xml  //modify activity padding, line heights
```


## Test
This projects are using Instrumental test
The class ExampleInstrumentedTest contain 2 test to run:
```bash
validateConnection: Validate the displayed message in snackbar according the connection
validateDataDisplayed: Validate the interaction with recyclerview and activity display
```
No complex bussiness logic was required so there was no need for unit test.



## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
