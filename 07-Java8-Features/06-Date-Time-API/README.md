# Date & Time API (`LocalDate`, `LocalDateTime`, `Duration`)

`java.time` (Java 8+) replaced the old, notoriously flawed `java.util.Date`/`Calendar` API. The new types are **immutable** and **thread-safe** — every "modifying" method returns a new instance instead of mutating.

## Core types

```java
LocalDate date = LocalDate.of(2026, 8, 30);         // date only — no time, no timezone
LocalDate today = LocalDate.now();

LocalTime time = LocalTime.of(14, 30);               // time only — no date
LocalDateTime dt = LocalDateTime.of(date, time);      // date + time, no timezone
LocalDateTime now = LocalDateTime.now();

ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));   // date + time + timezone
```

- All immutable: `date.plusDays(1)` returns a **new** `LocalDate` — the original `date` is unchanged (just like `String`).

## Manipulating dates

```java
LocalDate tomorrow = today.plusDays(1);
LocalDate lastMonth = today.minusMonths(1);
LocalDate nextYear = today.plusYears(1);

today.getDayOfWeek();        // e.g. SUNDAY
today.isBefore(tomorrow);    // true
today.isLeapYear();
```

## `Duration` vs `Period`

- **`Duration`** — a time-based amount (hours, minutes, seconds, nanos) — for measuring elapsed time between two `LocalTime`/`LocalDateTime`/`Instant` values.
- **`Period`** — a date-based amount (years, months, days) — for measuring elapsed time between two `LocalDate` values.

```java
Duration duration = Duration.between(LocalTime.of(9, 0), LocalTime.of(17, 30));
duration.toHours();           // 8
duration.toMinutes();         // 510

Period period = Period.between(LocalDate.of(2020, 1, 1), LocalDate.of(2026, 8, 30));
period.getYears();            // 6
period.getMonths();           // 7
period.getDays();             // 29
```

## Formatting & parsing

```java
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
String formatted = date.format(fmt);              // "30-08-2026"
LocalDate parsed = LocalDate.parse("30-08-2026", fmt);
```

## Practice Questions / Exercises

- Create a `LocalDate` for your birthday, and compute your age in years using `Period.between(birthday, LocalDate.now()).getYears()`.
- Compute the `Duration` between two `LocalTime`s representing a work day's start/end, printing total hours and minutes.
- Format a `LocalDate` using a custom pattern (`DateTimeFormatter.ofPattern(...)`) and parse it back.
- Demonstrate immutability: call `.plusDays(5)` on a `LocalDate` without reassigning the result, and show the original variable is unchanged.

## Interview Questions

**Q: Why was `java.util.Date`/`Calendar` replaced with the `java.time` API in Java 8?**
A: The old API had deep design flaws: `Date` was mutable (unsafe to share across threads or hand out from a getter without defensive copying), months were confusingly 0-indexed, `Calendar` was clumsy and inconsistent to use, and neither had a clean, explicit way to represent date-only, time-only, or timezone-aware values distinctly. `java.time` (based on the well-regarded Joda-Time library) fixes all of this with immutable, clearly-scoped types.

**Q: What's the difference between `LocalDate`, `LocalDateTime`, and `ZonedDateTime`?**
A: `LocalDate` represents a date only (no time, no timezone) — e.g. a birthday. `LocalDateTime` represents a date and time together, still with no timezone — e.g. "this event starts at 2pm on March 5th," ambiguous about which timezone that 2pm is in. `ZonedDateTime` adds a specific timezone/offset, representing an actual, unambiguous point in time as experienced in that zone.

**Q: Why are `java.time` types immutable, and what does that mean for methods like `plusDays()`?**
A: Immutability makes them inherently thread-safe (no risk of one thread mutating a date another thread is reading) and prevents accidental aliasing bugs. Every "modifying" method like `plusDays(1)` returns a **brand-new** instance with the new value rather than mutating the original — if you don't capture and use the return value, the original object is completely unchanged, a common source of beginner bugs (`date.plusDays(1);` alone does nothing useful).

**Q: What's the difference between `Duration` and `Period`?**
A: `Duration` measures a time-based amount (hours/minutes/seconds/nanos) — used for elapsed time between two time-inclusive values like `LocalDateTime` or `Instant`. `Period` measures a date-based amount (years/months/days) — used for elapsed time between two `LocalDate` values, correctly accounting for varying month lengths and leap years, which `Duration` (built on fixed-length seconds) cannot do.

**Q: How would you get the current moment in time as a timezone-independent instant, versus the current date/time in a specific timezone?**
A: `Instant.now()` gives a timezone-independent point on the timeline (essentially, epoch time) — good for timestamps and machine-to-machine comparisons. `ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))` (or similar) gives the current date/time as it would read on a clock in that specific zone, useful for anything user-facing where local wall-clock time matters.

**Q: Are `java.time` types compatible with the older `Date`/`Calendar` API if you're working in a codebase that still uses both?**
A: Yes — `java.time` provides conversion methods for interop, e.g. `Date.from(instant)` / `date.toInstant()`, and `GregorianCalendar.from(zonedDateTime)` / `calendar.toZonedDateTime()`, letting you bridge legacy APIs (some older libraries still expect `java.util.Date`) with the modern API in the rest of the codebase.
