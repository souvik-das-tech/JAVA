# AI Instructions for this Repo

This file applies no matter which AI tool is being used (Claude Code, Antigravity,
Cursor, Windsurf, or any other AI IDE/CLI). Read this before doing anything in
this repository.

## Purpose of this repo

This is a personal **Core Java study repo** (Basics → Advanced), tracked via
[ROADMAP.md](ROADMAP.md). The owner is a fresher preparing for Java developer
jobs. The goal is to actually *learn* the material — not to have an AI dump
finished code/notes without the owner engaging with it.

## Folder structure convention

- Each topic from `ROADMAP.md` gets its **own folder**, numbered under its
  section, e.g.:
  ```
  01-Basics/
    01-JDK-JRE-JVM-and-IDE-Setup/
      README.md
    02-Variables-and-Data-Types/
      README.md
      *.java   (only if the topic has a code component)
  02-OOP/
    01-Classes-and-Objects/
      README.md
      *.java
  ```
- Inside each topic folder:
  - **If the topic is conceptual/study-only** (no code to write — e.g. JDK vs
    JRE vs JVM, installation steps, theory-only topics): create **only a
    `README.md`** with notes and self-check questions. Do **not** create any
    `.java` file for such topics.
  - **If the topic has a code component** (e.g. loops, OOP, collections,
    streams): create a `README.md` for study notes/questions **and** the
    relevant `.java` file(s), pre-filled only with the question as a comment
    (no solution) — see the hard rule below for the exact format.

## Workflow (important — follow this order)

1. The user selects a topic (from `ROADMAP.md` or by naming it).
2. The AI creates the topic's folder, a `README.md`, and — if the topic has a
   code component — the `.java` file(s) with the correct file name(s), each
   containing only the question as a comment at the top (no solution). The
   `README.md` contains:
   - a concise explanation of the concept,
   - practice questions / exercises for that topic, and
   - an **"Interview Questions"** subsection: 5-10 questions on this topic as
     actually asked in Microsoft/MNC Java developer interviews (conceptual
     "explain X" / "difference between X and Y" / "what happens if" style,
     not coding problems), **each with its answer included**.
   - Do **not** add a "Key takeaways / self-check" checklist section — skip it.
3. The user attempts to answer the questions / write the solution
   **themselves**, directly inside the pre-created `.java` file (under the
   question comment) or in the `README.md`.
4. Only if the user gets stuck and explicitly asks (e.g. "generate it",
   "solve this", "give me the code") does the AI then fill in the solution
   in that `.java` file or fill in the explanation.

## Hard rule: do not write solution code preemptively

**Do NOT write the actual solution/implementation inside a `.java` file
until the user explicitly asks for it.**

- Creating the topic folder and the `README.md` (explanation + questions) is
  always fine.
- For a code-related topic, also create the `.java` file itself with a
  proper file name (matching the public class name, e.g. `Loops.java` for a
  `public class Loops`) — but leave it **empty of solution code**. Put the
  exercise/question as a comment block at the top of the file instead, so
  the user can write their own solution directly underneath it in the same
  file. Example:
  ```java
  // Q: Write a program that prints all even numbers from 1 to 50
  // using a for loop.

  public class Loops {
      public static void main(String[] args) {
          // write your solution here
      }
  }
  ```
- Writing actual solution code, even for a "simple"/"obvious" exercise, is
  **not** fine unless the user has asked for the code to be generated for
  that specific topic/question.
- If unsure whether the user is asking for code or just asking for the folder,
  notes, and empty `.java` file (with the question as a comment) to be set
  up, ask.

## Verifying generated solution code

- Whenever the AI writes actual solution code into a `.java` file (per the
  rule above — i.e. the user explicitly asked for it), it must `javac` and
  `java` (or otherwise run) that file before considering the task finished,
  and confirm the output actually matches what the code is supposed to
  demonstrate.
- If compilation fails or the output is wrong, fix it and re-verify — don't
  hand back code that hasn't actually been run.
- `.class` files produced while verifying are build artifacts, not part of
  the repo — delete them after verifying (they're also gitignored).

## Updating progress

- `README.md` at the repo root tracks overall progress with checkboxes.
- Whenever a topic is covered in a session (discussed, notes/questions
  created, or the user works through it), mark that topic done in
  `README.md`: change its `- [ ]` to `- [x]` and bump the progress count at
  the top. Do this automatically as part of covering the topic — no need to
  wait for the user to explicitly say "mark it done".
