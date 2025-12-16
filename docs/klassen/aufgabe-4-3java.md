## Aufgabe_4_3

### High-Level Summary
This class provides a generic utility for counting occurrences of a specified element in an array.  
It offers no instance state or mutable fields.  
It also includes a main method to demonstrate functionality.

### Key Business Logic & Algorithms
The core logic resides in the generic static method `count`.  
It iterates over the input array and increments a counter.  
It handles null item searches with a dedicated loop branch.  
It uses `equals` for non-null comparisons.  
It returns the total count of matching elements.

### Data Flow & State Management
- No instance or class fields are declared.  
- Local variable `count` holds the tally of matches.  
- Method parameter `array` supplies the sequence of elements.  
- Method parameter `item` defines the target to compare.  
- The `count` variable increments inside the chosen loop.  
- The `main` method defines sample arrays and invokes `count`.  
- The `main` method prints results to standard output.

### API & Interface Description
| Method          | Purpose                                               | Key Parameters           | Return Value |
| :-------------- | :---------------------------------------------------- | :----------------------- | :----------- |
| `count`         | Count how many times a given item appears in an array | `T[] array`, `T item`    | `int`        |
| `main`          | Demonstrate the `count` method with sample arrays     | `String[] args`          | `void`       |

### Legacy Code Observations (Risks)
- The method does not check for null array input, causing a potential NullPointerException.  
- Using explicit `new Integer` and `new String` constructors in main is outdated.  
- `System.out.println` usage hinders logging consistency in larger apps.  
- Generic method itself is thread-safe but `main` relies on console I/O.  
- No exception handling or validation guards against invalid inputs.  
- Class name follows assignment naming but lacks domain clarity.