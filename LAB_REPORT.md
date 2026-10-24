# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/witthayut/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | Temurin 25.0.4.1+1 |
| Maven | 3.10.0 |
| IDE | Visual Studio Code 1.143.1 |
| SonarQube for IDE | 6.2.0 |
| AI tool and exact model | ChatGPT, GPT-6.1 Sol (prompts A, B and C, and a second opinion on drafts); Claude, claude-opus-5-5 (guidance and suggestions) |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 9bb1f01

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S2143 | ParcelService.java:1 | Use the "java.time" API for date and time. |
| 2 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 3 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 4 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 5 | java:S4973 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 6 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 7 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 8 | java:S2095 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 9 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |
| 10 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 11 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 12 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 13 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 2026-10-24 |
| AI tool and exact model | ChatGPT, GPT-6.1 Sol |
| Commit reviewed | 9bb1f01 |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | opened the folder |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | ParcelService.java:42 | Strings compared with == instead of equals() | both | java:S4973 | yes, line 42 uses == on two String objects, and when I ran the program pickup P1 5 printed Fee: 0.0 and P1 stayed STORED |
| 2 | ParcelService.java:76 | FileWriter is never closed | both | java:S2095 | yes, there is no close() or try-with-resources, and parcels.csv was empty after my run |
| 3 | ParcelService.java:80 | Empty catch block hides file errors | both | java:S108 | yes, catch (IOException e) { } on lines 80-81 contains no statement, so a failed save is silent |
| 4 | ParcelService.java:6 | Unused import java.util.Date | both | java:S1128 | yes, nothing in ParcelService uses Date |
| 5 | ParcelService.java:12 | Unused private field maxParcels | both | java:S1068 | yes, maxParcels is set on line 12 but never read anywhere |
| 6 | ParcelService.java:1 | Use the java.time API instead of java.util.Date | SonarQube | java:S2143 | partly, Date appears only in the unused import on line 6 and the code never uses it, so there is no date code to change |
| 7 | ParcelService.java:30 | System.out used instead of a logger (SMS message) | SonarQube | java:S106 | yes, the service class prints directly to the console |
| 8 | ParcelService.java:67 | System.out used instead of a logger (staff report) | SonarQube | java:S106 | yes, the service class prints directly to the console |
| 9 | ParcelService.java:69 | System.out used instead of a logger (waiting list) | SonarQube | java:S106 | yes, the service class prints directly to the console |
| 10 | Main.java:9 | System.out used instead of a logger (command list) | SonarQube | java:S106 | yes, but Main is a command-line program, so printing is its normal output |
| 11 | Main.java:15 | System.out used instead of a logger (locker number) | SonarQube | java:S106 | yes, but Main is a command-line program, so printing is its normal output |
| 12 | Main.java:17 | System.out used instead of a logger (fee) | SonarQube | java:S106 | yes, but Main is a command-line program, so printing is its normal output |
| 13 | Main.java:22 | System.out used instead of a logger (free lockers) | SonarQube | java:S106 | yes, but Main is a command-line program, so printing is its normal output |
| 14 | Main.java:11 | No input checks: a short command or a non-number crashes the program | AI | probable bug | yes, typing only store makes words[1] throw ArrayIndexOutOfBoundsException, and pickup P1 abc makes Integer.parseInt throw NumberFormatException |
| 15 | Main.java:11 | split(" ") on one space breaks commands typed with two spaces | AI | probable bug | yes, store  P1 6501 2 typed with two spaces prints No free locker, because the empty word shifts the values and 6501 is read as the size |
| 16 | Main.java:8 | Scanner is never closed | AI | resource leak | yes, but vague, it also says closing it would close System.in and only suggests a fix if Main owns the input, so it does not say clearly whether to fix it; the Java extension also warns about it, SonarQube does not |
| 17 | Main.java:13 | Parcel size is never checked to be 1 to 3 | AI | probable bug | yes, size 0 is stored in locker 1, size 4 gives No free locker, and fee() charges every size other than 1 or 2 at the large rate |
| 18 | Main.java:6 | main reads input and runs store, pickup, list and staff, chosen by the command word | AI | logical cohesion | yes, one if-else chain picks one of several different actions |
| 19 | ParcelService.java:64 | printParcels prints two different reports chosen by a flag | AI | logical cohesion | yes, the forStaff flag decides which of two reports runs |
| 20 | ParcelService.java:9 | ParcelService does locker allocation, pickup, fees, SMS, printing and CSV saving | AI | weak class cohesion | yes, the class has six different reasons to change |
| 21 | Main.java:19 | Main passes a boolean flag that decides what printParcels does | AI | control coupling | yes, printParcels(false) and printParcels(true) at lines 19 and 21 control the branch inside the method |
| 22 | ParcelService.java:46 | fee() receives the whole Parcel but uses only its size | AI | stamp coupling | yes, fee() reads only parcel.getSize() on lines 56 and 58 |
| 23 | Parcel.java:7 | status is public and ParcelService writes it directly | AI | content coupling | yes, ParcelService sets parcel.status on lines 28 and 44 and reads it on lines 67, 68 and 78 |
| 24 | ParcelService.java:20 | getLockers() returns the internal list, and Main reaches through it | AI | exposed internal collection, Law of Demeter | yes, Main.java:22 calls service.getLockers().stream() and could also add or remove lockers |
| 25 | Parcel.java:6 | Size codes 1, 2 and 3 must mean the same thing in Parcel, Locker, ParcelService and Main | AI | connascence of convention | yes, the meaning is only in comments (Parcel.java:6, Locker.java:5) and in fee() lines 56 to 61 |
| 26 | Parcel.java:13 | Status values NEW, STORED and PICKED_UP are plain strings shared by two classes | AI | connascence of convention | yes, a typo such as STORE would compile and silently break the check on ParcelService.java:68 |
| 27 | Parcel.java:9 | Constructor takes code and studentId as two Strings in a row | AI | connascence of position | yes, swapping words[1] and words[2] on Main.java:13 would compile without an error |

2.1 Name two problems that only the AI review found.

- Control coupling: Main passes a boolean flag to printParcels (Main.java:19 and 21, ParcelService.java:64), and the flag decides which report is printed (row 21).
- Connascence of convention: Parcel, Locker, ParcelService and Main must all read the size codes 1, 2 and 3 the same way (row 25).

2.2 Name one problem that only SonarQube found, or write none.

- java:S106: System.out is used instead of a logger, in seven places such as Main.java:9 and ParcelService.java:30 (rows 7 to 13). The AI review did not mention it.

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

Row 16 (Main.java:8, Scanner never closed) was too vague. The AI said to close it but also warned that closing it closes System.in, and it never said whether that matters here. I checked Main.java myself. Only main reads input and the program ends after the loop, so closing it is safe.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Fixed | 182450c |
| 2 | Fixed | 182450c |
| 3 | Fixed | 0ee290c |
| 4 | Kept | Printing the SMS message to the console is how this program simulates sending an SMS, and there is no real SMS service. |
| 5 | Fixed | 0fdf727 |
| 6 | Kept | printParcels prints the staff report that the staff command asks for, so console output is the intended result. |
| 7 | Kept | printParcels prints the waiting list that the list command asks for, so console output is the intended result. |
| 8 | Fixed | 8074078 |
| 9 | Fixed | be03407 |
| 10 | Kept | Printing in Main is the output of a command-line program. |
| 11 | Kept | Printing in Main is the output of a command-line program. |
| 12 | Kept | Printing in Main is the output of a command-line program. |
| 13 | Kept | Printing in Main is the output of a command-line program. |

Number of SonarQube issues that remain after your fixes: 7

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Control coupling: Main passed a boolean flag to printParcels(boolean forStaff) to choose the report (Table 2 row 21) | Split printParcels into printWaitingParcels() and printStaffReport(), and Main now calls the one it needs | 197a5bc |
| Connascence of convention: Parcel, Locker, ParcelService and Main all had to read the size codes 1, 2 and 3 the same way (Table 2 row 25) | Added a Size enum (SMALL, MEDIUM, LARGE) that holds each daily rate; Parcel and Locker store a Size, fee() uses the rate from the enum, and Main converts the typed number once with Size.fromNumber() | 1043455 |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
store P1 6501 2
SMS to 6501: parcel P1 is in locker 2
Locker 2
pickup P1 5
Fee: 30.0
staff
P1 6501 PICKED_UP
Free lockers: 3
quit

parcels.csv:
P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 2026-10-25 | 2026-10-25 |
| AI tool and exact model | ChatGPT, GPT-6.1 Sol | ChatGPT, GPT-6.1 Sol |
| Prompt (B or C as given, or your changed prompt in full) | B as given | C as given |
| Builds with mvn package (yes or no) | yes | yes |
| Number of classes, interfaces, enums and records | 1 class | 5 classes, 1 interface, 2 enums, 2 records (Rental is a private record inside BikeShareService) |
| SonarQube issues, total | 16 | 2 |
| SonarQube issues, by rule | java:S106 ×7, java:S2386 ×5, java:S3776 ×2, java:S135 ×1, java:S9358 ×1 | java:S106 ×1, java:S135 ×1 |
| AI review findings in Check 1 | 1 | 0 |
| AI review findings in Check 2 | 8 | 4 |
| Review output file in the reviews folder | reviews/bikeshare-poor-review.md | reviews/bikeshare-good-review.md |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

Not directly. In my results no SonarQube rule was named after cohesion or coupling. The closest were java:S2386 "Make this member "protected"" on the public static arrays, which are global shared data that any class could change, and java:S3776 "Refactor this method to reduce its Cognitive Complexity from 28 to the 15 allowed", which comes from process() doing too many jobs. In this lab SonarQube pointed at these symptoms but did not use the course terms.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

Yes. It gave 8 design findings for the poor version and 4 for the good version. For example, only the poor review reports weak class cohesion: "Main represents the command-line interface, bike inventory, active rentals, pricing rules, and CSV storage in one class" (Main.java:11–161).

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

bikeshare-good. I would add VISITOR(4) to the MemberType enum and update the help text in ConsoleMenu, and parsing, fees and the CSV would work without other changes. In bikeshare-poor I would have to add a new int code 3 and change the type parsing (Main.java:104–111), the fee line (Main.java:141) and the help text, and the fee line treats every type except 1 as staff, so a missed change would silently charge visitors 3 baht.

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

SonarQube found small code mistakes, like using == for Strings, a file that was never closed and unused code. In this lab it did not find the design problems, like the boolean flag or the size numbers 1, 2 and 3.

5.2 When should you not trust the AI review? Give an example from this lab.

I should not trust it when the answer depends on something it did not check. For the Scanner, it said "close it if Main owns the input" but did not say if that was true, so I had to check the code myself.

5.3 What will you do differently in Project Phase 2 after this lab?

I will run SonarQube and the AI review while we build, not only at the end. I will use enums instead of number codes and separate methods instead of boolean flags. I will also check any AI code before we commit it.

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| ChatGPT, GPT-6.1 Sol | Prompt A reviews (Tasks 2 and 4), Prompts B and C (Task 4); second opinion on my draft answers | The 3 review files in reviews/; all files in bikeshare-poor and bikeshare-good | Opened the code at every line it cited; built and ran both BikeShare programs and compared their fees |
| Claude, claude-opus-5-5 | Guidance through the lab steps; suggested wording and code that I reviewed | Suggested wording for Table 2, Table 3 and Tasks 4-5, which I shortened and edited; the Size enum code for design fix 2, which I chose; saved the ChatGPT answers into files | Compared Table 2 with the code and SonarQube; ran the program after every fix and checked the output and parcels.csv; checked that SonarQube still showed 7 issues |
