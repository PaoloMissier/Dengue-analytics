# classifier

Implementation of the tweets classifier using the weka toolkit

## Installation

Download and add the jar file located under the /lib directory.

## Usage

Instantiate a new Classifier object by calling the getInstance() method. 

```java
Classifier classifier = Classifier.getInstance();
```

The first time you call the getInstance() it might get some time to complete because it will load the models and dictionary for the POS tagger.

Then use it like this:

```java
Category category = classifier.classify(String);
```

or

```java
Category category = Classifier.getInstance().classify(String);
```

The Category is an enum object and you can call:

```java
category.getName();
```

to get a simple name of the category e.g. "Joke/Sarcastic"

or you can also access the enums and compare them with:

```java
Category.INFORMATIVE
Category.JOKE
Category.MOSQUITO_FOCUS
Category.SICKNESS
```
