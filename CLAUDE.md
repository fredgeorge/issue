# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
./gradlew build          # Build all modules
./gradlew test           # Run all tests
./gradlew :tests:test    # Run tests in the tests module
./gradlew :persistence:test  # Run tests in the persistence module

# Run a single test class
./gradlew :tests:test --tests "com.nrkei.project.issue.unit.IssueTest"

# Run a single test method (use quotes around names with spaces)
./gradlew :tests:test --tests "com.nrkei.project.issue.unit.IssueTest.can create and find Issues"

./gradlew publish        # Publish to local Maven repository
```

## Architecture Overview

This is a domain-driven Issue tracking framework. An _Issue_ represents an aberration in a process, with a strict state machine: `OPEN → RESOLVED` or `OPEN → DISMISSED` (no re-opening).

### Module Structure

- **engine** — Core domain model (`Issue`, `IssueSet`, `IssueVisitor`, `PrettyPrint`). No persistence concerns. Published as `issue-engine`.
- **test-support** — Concrete `Issue` implementations (`TestIssue1`, `TestIssue2`) used for testing the framework.
- **persistence** — Serialization logic isolated from the domain via the Memento Pattern. Published as `issue-persistence`.
- **tests** — Integration/unit tests for the engine and test-support modules.

### Key Design Patterns

**Issue<I>** uses a self-referencing generic (`abstract class Issue<I : Issue<I>>`) for type-safe subclasses. Equality is based only on `raisedBy` + `issueType` (immutable fields); `closedBy` and `state` are excluded from `equals`/`hashCode`.

**IssueSet** segregates issues by type into internal `Bucket<*>` sets (a `MutableMap<IssueType<*>, Bucket<*>>`), so duplicate issues collapse automatically. Query methods filter by state and/or type.

**Visitor Pattern** — `IssueVisitor` traverses the issue graph. `PrettyPrint` is the reference implementation using `StringBuilder`.

**Memento Pattern** — Persistence is entirely in the `persistence` module. Extension functions inject `toMemento()` into `IssueSet` and `fromMemento()` into `IssueSet.Companion`, keeping the domain model clean. `Encoding` supports both JSON and Base64 output.

**Adding a new Issue type** requires:
1. Extend `Issue<I>` and implement `issueType: IssueType<I>`
2. Provide a `@Serializable` DTO implementing `IssueDto<I>` with all fields needed for restoration
3. Implement `toDto()` returning the DTO
4. Add a constructor (can be `private`) accepting all DTO fields — used via reflection for restoration
5. Define a `toIssue()` extension on the DTO in the persistence module (typically using reflection)
6. Register the DTO in the `Json` serializers module and update `issueFrom(dto: IssueDto<*>)` in `IssueSetPersistence`

See `TestIssue1` / `TestIssue2` in `test-support` and `TestIssuePersistence` in `persistence` as reference implementations.
