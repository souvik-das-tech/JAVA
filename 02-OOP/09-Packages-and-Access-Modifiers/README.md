# Packages & Access Modifiers

Study notes only — no single code file for this topic (demonstrating package/access behavior properly needs multiple files across multiple package directories; try it directly in a scratch multi-file setup rather than one `.java` file here).

A **package** is a namespace that groups related classes/interfaces and prevents naming collisions. It also maps to a directory structure — `package com.example.util;` means the file lives under `com/example/util/`.

```java
package com.example.util;

import java.util.List;         // import a single class
import java.util.*;            // import all classes in a package (not sub-packages)

public class StringUtils { ... }
```

- The package statement, if present, must be the **first** non-comment line in the file.
- `import` is purely a compile-time convenience for shorter names — fully-qualified names (`java.util.List`) always work without any import.
- Classes in `java.lang` (`String`, `Integer`, `Math`, ...) are imported automatically — no explicit `import` needed.

## Access modifiers (recap, applied to classes/members)

| Modifier | Same class | Same package | Subclass (diff. package) | Everywhere |
|---|---|---|---|---|
| `private` | ✅ | ❌ | ❌ | ❌ |
| default (no modifier) | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

- A **top-level class** itself can only be `public` or default (package-private) — never `private` or `protected`.
- `protected` grants access to subclasses *even outside the package*, but with a subtlety: a subclass in another package can only access an inherited protected member through a reference of its own type or a subtype, not through an arbitrary superclass-typed reference.
- Default (package-private) access is the "no modifier" case — useful for helper classes/methods meant only for use within the same package, not part of the public API.

## Practice Questions / Exercises

- Create two classes in different packages, and try accessing a package-private (no modifier) field of one from the other — observe the compiler error.
- Create a class in package `a` and a subclass in package `b` (`extends` across packages) — show which members of the parent are and aren't accessible from the subclass.
- Write a small package with one `public` class as the entry point and a package-private helper class used only internally — show the helper can't be accessed from outside the package.
- Import a class two ways — via its fully-qualified name with no `import` statement, and via an `import` — and confirm both compile identically.

## Interview Questions

**Q: What is a package, and why use them?**
A: A package is a namespace for grouping related classes/interfaces, which prevents naming collisions between classes with the same simple name from different libraries, and organizes code by feature/responsibility. It also directly maps to the source directory structure.

**Q: What access level can a top-level class itself have, and why not `private`/`protected`?**
A: Only `public` or default (package-private) — never `private` or `protected`. Those two only make sense for *members* relative to an enclosing class; a top-level class has no enclosing class to be "private to" or to grant "protected" subclass access relative to.

**Q: What's the difference between package-private (default) access and `protected`?**
A: Package-private grants access only within the same package, with no special allowance for subclasses outside it. `protected` grants that same same-package access *plus* access to subclasses even in different packages (with the caveat that access outside the package must go through a reference typed as the subclass or narrower).

**Q: Does `import`ing a class have any runtime cost or effect on the compiled bytecode?**
A: No — `import` is purely a compile-time source-code convenience that lets you use a simple class name instead of writing it fully-qualified every time. It has no effect on the generated `.class` files or runtime behavior at all.

**Q: If class `A` in package `p1` has a `protected` method, and class `B` in package `p2` extends `A`, can an unrelated class `C` in `p2` call that method on a `B` instance?**
A: Only if `C` is itself in the same package as `A` (`p1`), or if `C` is also a subclass of `A`. Plain package membership in `p2` alone doesn't grant `C` access to `A`'s protected member through a `B` instance — `protected` access outside the declaring package is scoped to the subclass hierarchy, not the whole package.

**Q: Can two classes with the same simple name coexist in a program?**
A: Yes, as long as they're in different packages — e.g. `java.util.Date` and `java.sql.Date` are distinct classes with the same simple name, distinguished by their fully-qualified names. This is exactly the naming-collision problem packages are designed to solve.
