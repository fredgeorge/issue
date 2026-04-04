# issue

Copyright (c) 2025-26 by Fred George  
author: Fred George  fredgeorge@acm.org  
Licensed under the MIT License; see LICENSE file in root.

### Concepts

The _Issue_ model represents aberrations in a process. Issues have
three states:

- _open_: An issue that has not been addressed
- _resolved_: An issue that has been addressed
- _dismissed_: An issue that is closed but may be raised again

Both resolved and dismissed issues are closure states. _Resolved_
issues generally mean that the aberration still exists but is
acceptable for now, and should not be raised again unless
circumstances change. _Dismissed_ issues generally mean that some 
other action is to be taken to address the aberration, but the
check for the aberration may result in a new issue raised.

_IssueSet_ aggregates all the Issues for ready access with
a suite of methods for accessing Issues by state and/or type. 
Internally, each type is segregated into a separate _set_ (so
duplicate Issues are automatically collapsed).

Persistence is supported in the persistence package 
through Encoding helper functions. By default, both
JSON strings and Base64 strings are supported. To the 
maximum extent possible, persistence implementation should
reside in the persistence package.

### Using the Issue model

Specific Issues for your domain implement the _Issue_
interface. Unique values for a new Issue can be defined.
To support serialization with the kotlinx-serialization
plugin, the new Issue must:

- Provide a DTO for serialization with all fields necessary
 for deserialization
- Must define a toDto() method returning the DTO and tag the 
 DTO with @Serializable.
- They must define a constructor that accepts all 
 the fields necessary for the DTO to restore the Issue.
 The constructor can be private since we prefer to invoke 
 it with reflection to maintain encapsulation.
- Define (preferably in the persistence module) an extension 
 method toIssue() that restores the Issue from the DTO 
 (again, preferably through reflection)
- Since polymorphism is involved, create a Kotlin Json object 
 with the appropriate serializer module registering 
 each IssueDto.
- Adust the issueFrom(dto: IssueDto<*>) method in
 IssueSetPersistence to support the new Issue type.

Examples are provided in the persistence package for two
sample Issues: TestIssue1 and TestIssue2.

### Persistence Support

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
used as an _encapsulation_ bypass. For transmission purpose,
mementos should be Strings rather than just binary data.

In the example, IssueSetPersistence injects _memento_
creation with an extension method into the IssueSet class.
It further injects restoration of the class into the
Companion object of IssueSet.

The base IssueSet class, to support the Memento Pattern,
defines a properly populated DTO in response to toDto(), and
must have a Companion object as a target for the restoration
injection. If JSON serialization is to be supported in creating
the memento, _@Serializable_ must be tagged on IssueDto subclasses.

The creation of the _memento_ is also done with the
IssueSetPersistence helper functions in the
persistence package, including the injection of the creation and
restoration functions. This helper class is solely
responsible for the format and content of the _memento_.

The Encoding object allows for generation and
restoration in either JSON or Base64 formats. Base64
properly _hides_ the content of the memento from prying
eyes. _Polymorphism_ support exists through
the SerializersModule parameter on Json creation.

## Starting this Kotlin project using Gradle

Kotlin is relatively easy to set up with IntelliJ IDEA. 
Gradle is used for building and testing the project and is a 
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
- Kotlin Serialization 1.8.1

Open the reference code:

- Download the source code from github.com/fredgeorge
    - Clone, or pull and extract the zip
- Open IntelliJ
- Choose "Open" (it's a Gradle project)
- Navigate to the reference code root, and hit Enter

Source and test directories should already be tagged as such,
with test directories in green.

Confirm that everything builds correctly (and the 
necessary libraries exist).
There are sample tests, including serialization. 
The test should run successfully from the Gradle __test__ task.

Several settings may need to be manually changed if using IntelliJ IDEA:

- In File - Project Structure - Project Settings - Project, set SDK to 25 (or whatever earlier SDK you're using)
- In File - Settings - Build, Execution, Deployment - Compiler - Kotlin Compiler, set "Target JVM version" to 25
- In File - Settings - Build, Execution, Deployment - Build Tools - Gradle, set Gradle JVM to JAVA_HOME or explicitly
