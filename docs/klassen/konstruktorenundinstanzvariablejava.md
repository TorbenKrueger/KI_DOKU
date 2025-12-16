## KonstruktorenUndInstanzVariable

### 1. High-Level Summary  
This file defines an abstract class hierarchy (A → B → C) to demonstrate Java’s constructor chaining and instance variable initialization order. It is neither a DTO nor a service but an educational example highlighting language mechanics.

### 2. Key Business Logic & Algorithms  
This section explains the core mechanisms at work in the class hierarchy.

- Constructors in **A**, **B**, and **C** invoke their superclass before printing a marker.  
- Each constructor calls an abstract method `print()`, resolved at runtime to **C**’s implementation.  
- Java initializes instance fields (`a`, `b`, `c`) before executing the corresponding constructor body.  
- Dynamic dispatch during construction illustrates how overridden methods operate on partially initialized objects.

### 3. Data Flow & State Management  
Key fields and their role in demonstrating initialization order:

| Field | Declared In | Initial Value | Purpose |
| :---- | :---------- | :-----------: | :------ |
| `a`   | class A     |       1       | Illustrates superclass initialization |
| `b`   | class B     |       1       | Illustrates middle‐class initialization |
| `c`   | class C     |       1       | Illustrates subclass initialization |

- **Default state**: All fields start as `0`.  
- **Initializer execution**: Before each constructor runs, its class’s fields are set to the declared value.  
- **State mutation**: No further mutation occurs; values are read in `print()` to show initialization progress.

### 4. API & Interface Description  
Public methods exposed by this file:

| Method               | Purpose                                                                                         | Key Parameters    | Return Value |
| :------------------- | :---------------------------------------------------------------------------------------------- | :---------------- | :----------- |
| `public static main` | Launches the demo by instantiating `C`, triggering the full constructor chain and print output. | `String[] args`   | `void`       |

### 5. Legacy Code Observations (Risks)  
- **Constructor dispatch risk**  
  Calling an overridable method (`print()`) inside a constructor can observe uninitialized subclass fields.  
- **Use of `System.out`**  
  Direct console printing is not configurable like a logging framework.  
- **Hardcoded output**  
  Message text is embedded, reducing flexibility for localization or formatting changes.  
- **Package‐private scope**  
  Abstract classes A and B have default access, limiting reuse outside the package.  
- **No error handling**  
  Any runtime exception will propagate uncaught, which may be undesirable in production.