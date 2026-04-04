### issue

Copyright (c) 2025-26 by Fred George  
author: Fred George  fredgeorge@acm.org  
Licensed under the MIT License; see LICENSE file in root.

## Starting this Kotlin project using Gradle

Kotlin is relatively easy to set up with IntelliJ IDEA. 
Gradle is used for building and testing the project, and is a 
prerequisite. Install if necessary.
The following instructions are for installing the code 
in IntelliJ IDEA by JetBrains. 
Adapt as necessary for your environment.

Note: This implementation was set up to use:

- IntelliJ 2026.1 (Ultimate Edition)
- Kotlin 2.3.20 (targeting Java 21 bytecode)
- Java SDK 25 LTS (Oracle)
- Gradle 9.4.1
- JUnit Jupiter 6.0.3 for testing

Open the reference code:

- Download the source code from github.com/fredgeorge
    - Clone, or pull and extract the zip
- Open IntelliJ
- Choose "Open" (it's a Gradle project)
- Navigate to the reference code root, and enter

Source and test directories should already be tagged as such,
with test directories in green.

Confirm that everything builds correctly (and necessary libraries exist).
There is a sample class, Rectangle, with a corresponding
test, RectangleTest. The test should run successfully
from the Gradle __test__ task.

Several settings may need to be manually changed if using IntelliJ IDEA:

- In File - Project Structure - Project Settings - Project, set SDK to 25 (or whatever you earlier SDK)
- In File - Settings - Build, Execution, Deployment - Compiler - Kotlin Compiler, set Target JVM version to 21
- In File - Settings - Build, Execution, Deployment - Build Tools - Gradle, set Gradle JVM to JAVA_HOME or explicitly

Update the following: 

- In settings.gradle.kts, change the rootProject.name
- In both engine and tests, choose your domain name for your code under kotlin directory
- Consider renaming the <engine> project to your domain specific label

## Persistence

Peristence is separated from the domain model (engine).
If imbedded in the model, complexity can compromise the
clarity of the model design. To the maximum extent
possible, peristence should be separated from the model.

Persistence is handled by the Kotlin-serialization library. It
provides a convenient way to serialize and deserialize
Kotlin data classes to and from JSON and Base64 formats.
This ensures that data can be easily stored and transmitted
while maintaining its structure and integrity.

The _Memento Pattern_ (Design Patterns book) is used as the
model for persistence. The pattern suggests an object can
present a binary representation of itself that can only
be reinterpreted by the object's class itself. It can't be
used as an _encapsulation_ bypass.

The example injects _memento_ creation with an extension method
into the Rectangle class. It further injects restoration of the
class into the Companion object of Rectangle.

The base Rectangle class, to support the Memento Pattern,
defines a properly populated DTO in response to toDto(), and
must have a Companion object as a target for the restoration
injection. If JSON serialization is to be supported in creating
the memento, _@Serializable_ must be tagged on the DTO.

The creation of the _memento_ is done in the
RectanglePersistence helper functions in the
persistence package, including the injection of the creation and
restoration functions. This helper class is solely
responsible for the format and content of the _memento_.

The Encoding object allows for gneration and
restoration in either JSON or Base64 formats. Base64
properly _hides_ the content of the memento from prying
eyes. _Polymorphism_ support exists with SerializersModule
parameter on JSON creation.
