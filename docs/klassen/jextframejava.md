## JextFrame

### 1. High-Level Summary
JextFrame is the main window controller for Jext, a pure-Java text editor.  
It extends `JFrame` and orchestrates UI components (menus, toolbars, split panes), file operations, event dispatching, plugin management, and project integrations.  
This class is neither a simple DTO nor a pure utility; it combines controller logic with view setup and state management.

### 2. Key Business Logic & Algorithms
JextFrame’s core responsibilities revolve around managing editor tabs, file I/O, UI layout, and event propagation.  
- It handles opening, creating, saving, and closing text areas with dirty-check prompts.  
- It dynamically shows or hides side/top panels based on user properties.  
- Batch mode toggling defers expensive operations during bulk file processing.  
- Drag-and-drop support enqueues file opens via the AWT DnD API and `SwingUtilities.invokeLater`.  

### 3. Data Flow & State Management
JextFrame maintains significant internal state through member variables and collections.  
- GUI components (`toolBar`, `xtree`, `console`, `textAreasPane`, split panes) are initialized at construction and mutated via property loaders.  
- `batchMode` integer and `waitCount` track reentrant state for bulk operations and cursor changes.  
- Listener lists (`jextListeners`) and transient UI items (`transientItems`) record dynamic extensions by plugins or modes.  
- Project integrations are registered in `projectMgmts` and switched at runtime via `selectProjectManagement`.

Key Fields:
- `JextToolBar toolBar`: Main toolbar  
- `JextTabbedPane textAreasPane`: Central editor tabs  
- `JSplitPane split, splitter, textAreaSplitter`: Layout managers for panels and split views  
- `ArrayList jextListeners`: Registered `JextListener` observers  
- `HashMap projectMgmts`: Available project management modules  
- `AutoSave auto`: Background auto-save thread  

### 4. API & Interface Description
Public methods expose UI accessors, file/workspace operations, event handling, layout management, and lifecycle controls.  

#### 4.1 UI Component Accessors  
Provides direct access to embedded GUI panels and docks.  

| Method                         | Purpose                                      | Key Parameters | Return Value           |
| :----------------------------- | :------------------------------------------- | :------------- | :----------------------|
| `getTabbedPane()`              | Returns main text editor tabbed pane         | –              | `JextTabbedPane`       |
| `getVerticalTabbedPane()`      | Returns left-side tools tabbed pane          | –              | `JTabbedPane`          |
| `getHorizontalTabbedPane()`    | Returns top/console tabbed pane              | –              | `JTabbedPane`          |
| `getXTree()`                   | Returns file-insert tree component           | –              | `XTree`                |
| `getVirtualFolders()`          | Returns virtual folders panel                | –              | `VirtualFolders`       |
| `getWorkspaces()`              | Returns workspace panel                      | –              | `Workspaces`           |
| `getConsole()`                 | Returns or lazily creates the console        | –              | `Console`              |
| `getDawnLogWindow()`           | Returns Dawn scripting log window            | –              | `AbstractLogWindow`    |
| `getPythonLogWindow()`         | Returns Python scripting log window          | –              | `AbstractLogWindow`    |
| `getDawnDock()`                | Returns dock for Dawn log window             | –              | `Dockable`             |
| `getPythonDock()`              | Returns dock for Python log window           | –              | `Dockable`             |
| `getFileChooser(mode)`         | Returns a shared `JFileChooser` in given mode| `int mode`     | `JFileChooser`         |
| `getInputHandler()`            | Returns current input handler                | –              | `InputHandler`         |
| `setInputHandler(handler)`     | Sets a new input handler                     | `InputHandler` | `void`                 |

#### 4.2 File and Workspace Operations  
Methods for tab management, file opening, saving, and the recent-files menu.  

| Method                                | Purpose                                               | Key Parameters                        | Return Value       |
| :------------------------------------ | :---------------------------------------------------- | :------------------------------------ | :----------------- |
| `open(file)`                          | Opens a file; prompts reload if already open          | `String file`                         | `JextTextArea`     |
| `open(file, addToRecent)`             | Opens a file with control over recent-list addition   | `String`, `boolean`                   | `JextTextArea`     |
| `openForLoading(file)`                | Opens file without adding to recent list              | `String file`                         | `JextTextArea`     |
| `createFile()`                        | Creates a new empty text area tab                     | –                                     | `JextTextArea`     |
| `close(textArea)`                     | Closes tab with dirty-check prompt                    | `JextTextArea`                        | `void`             |
| `close(textArea, checkContent)`       | Closes tab; optional save prompt                      | `JextTextArea`, `boolean`             | `void`             |
| `closeAll()`                          | Closes all open text areas                            | –                                     | `void`             |
| `checkContent(textArea)`              | Prompts user to save if area is dirty                 | `JextTextArea`                        | `boolean`          |
| `setRecentMenu(menu)`                 | Assigns the recent-files menu component               | `JextRecentMenu`                      | `void`             |
| `reloadRecent()`                      | Rebuilds the recent-files menu                        | –                                     | `void`             |
| `removeRecent()`                      | Clears all entries from the recent menu               | –                                     | `void`             |
| `saveRecent(file)`                    | Adds a file path to the recent menu                   | `String file`                         | `void`             |

#### 4.3 Event Handling  
Observer pattern for internal `JextEvent` propagation.  

| Method                           | Purpose                                | Key Parameters                     | Return Value |
| :--------------------------------| :--------------------------------------| :--------------------------------- | :----------- |
| `addJextListener(l)`             | Registers an event listener            | `JextListener l`                   | `void`       |
| `removeJextListener(l)`          | Unregisters an event listener          | `JextListener l`                   | `void`       |
| `removeAllJextListeners()`       | Clears all registered listeners        | –                                   | `void`       |
| `fireJextEvent(type)`            | Fires a global event                   | `int type`                          | `void`       |
| `fireJextEvent(textArea, type)`  | Fires an event tied to a text area     | `JextTextArea`, `int type`          | `void`       |

#### 4.4 Properties and Layout Management  
Loads user preferences, applies UI changes, and toggles split panes.  

| Method                         | Purpose                                                         | Key Parameters  | Return Value |
| :----------------------------- | :-------------------------------------------------------------- | :-------------- | :----------- |
| `loadProperties()`             | Loads all configurable properties and triggers UI update        | –               | `void`       |
| `loadProperties(triggerPanes)` | Loads properties; optionally toggles side/top panels            | `boolean`       | `void`       |
| `loadButtonsProperties()`      | Applies toolbar visibility and button highlight colors          | –               | `void`       |
| `loadConsoleProperties()`      | Applies console color and prompt properties                     | –               | `void`       |
| `loadTextAreaProperties()`     | Applies settings to all text areas                              | –               | `void`       |
| `triggerTabbedPanes()`         | Shows or hides left and top tabbed panes based on flags         | –               | `void`       |
| `splitEditor()`                | Splits or merges the editor view according to user preferences  | –               | `void`       |

#### 4.5 Project Management  
Integrates different project management modules and switches among them.  

| Method                       | Purpose                                         | Key Parameters     | Return Value |
| :--------------------------- | :---------------------------------------------- | :----------------- | :----------- |
| `selectProjectManagement(name)` | Switches the active project manager UI         | `String name`      | `boolean`    |
| `getProjectManager()`        | Retrieves the currently selected project manager | –                 | `ProjectManager` |

#### 4.6 Text Area and Editor State  
Controls batch mode, cursor changes, and status indicators for text areas.  

| Method                              | Purpose                                         | Key Parameters   | Return Value |
| :---------------------------------- | :---------------------------------------------- | :----------------| :----------- |
| `setBatchMode(on)`                  | Enables/disables batch mode                      | `boolean on`     | `void`       |
| `getBatchMode()`                    | Checks if batch mode is currently active         | –                | `boolean`    |
| `getKeyEventInterceptor()`          | Retrieves custom key event interceptor           | –                | `KeyListener`|
| `setKeyEventInterceptor(listener)`  | Sets custom key event interceptor                | `KeyListener`    | `void`       |
| `freeze()`                          | Freezes GUI for transient component addition     | –                | `void`       |
| `itemAdded(comp)`                   | Records a transient GUI component                | `Component`      | `void`       |
| `reset()`                           | Restores GUI to baseline (removes transient items)| –               | `void`       |
| `setPluginsMenu(menu)`              | Assigns the plugins menu                          | `JMenu`          | `void`       |
| `updatePluginsMenu()`               | Rebuilds plugins menu entries                     | –                | `void`       |
| `startAutoSave()`                   | Launches the auto-save background thread          | –                | `void`       |
| `stopAutoSave()`                    | Stops the auto-save thread                        | –                | `void`       |
| `showWaitCursor()`                  | Switches UI to wait cursor                        | –                | `void`       |
| `hideWaitCursor()`                  | Restores default cursor                           | –                | `void`       |
| `updateStatus(textArea)`            | Updates caret position in status bar              | `JextTextArea`   | `void`       |
| `setStatus(textArea)`               | Updates edit/readonly indicator in message bar    | `JextTextArea`   | `void`       |
| `resetStatus(textArea)`             | Clears dirty flag and resets icons                | `JextTextArea`   | `void`       |
| `setNew(textArea)`                  | Prepares labels and icons for a new file tab      | `JextTextArea`   | `void`       |
| `setChanged(textArea)`              | Flags a text area as modified                     | `JextTextArea`   | `void`       |
| `setSaved(textArea)`                | Clears modification flag after save               | `JextTextArea`   | `void`       |

#### 4.7 Constructors and Lifecycle  
Handles window creation, splash screen progression, plugin execution, and shutdown.  

| Method                                | Purpose                                          | Key Parameters             | Return Value |
| :------------------------------------ | :----------------------------------------------- | :------------------------- | :----------- |
| `JextFrame()`                         | Initializes the editor UI with default settings  | –                          | –            |
| `JextFrame(args[])`                   | Initializes UI and opens specified files         | `String[] args`            | –            |
| `JextFrame(args[], toShow)`           | Core constructor with visibility control         | `String[] args, boolean`   | –            |
| `closeToQuit()`                       | Checks dirty files, saves projects, but not JVM  | –                          | `void`       |
| `closeWindow()`                       | Disposes window and may terminate JVM if last    | –                          | `void`       |
| `closeWindow(jvm)`                    | Conditional disposal based on last window status | `boolean jvm`              | `void`       |
| `saveConsole()`                       | Persists console content if configured           | –                          | `void`       |
| `cleanMemory()`                       | Nulls references to aid garbage collection       | –                          | `void`       |

#### 4.8 Input Handling  
Overrides key dispatch to route events through the custom input handler.  

| Method                  | Purpose                                          | Key Parameters         | Return Value |
| :---------------------- | :----------------------------------------------- | :--------------------- | :----------- |
| `processKeyEvent(evt)`  | Delegates raw key events to `InputHandler` or interceptor | `KeyEvent evt` | `void` |

### 5. Legacy Code Observations (Risks)
Though feature-rich, JextFrame exhibits several maintainability and safety concerns:  

- **Monolithic Design**: Over 2 000 lines in one class violate Single Responsibility Principle.  
- **Thread Safety**: Lists and maps (`jextListeners`, `projectMgmts`) are unsynchronized; concurrent plugin registration or event firing may race.  
- **Swallowed Exceptions**: Plugin/menu builders catch broad `Throwable` without reporting to user; debugging is hampered.  
- **AWT/Swing EDT**: Some UI actions occur outside the Event Dispatch Thread, risking race conditions.  
- **Hardcoded Property Keys**: Strings littered throughout; typos in key names may go unnoticed.  
- **Use of `System.gc()`**: Forced garbage collection is discouraged and non-deterministic.  
- **Deprecated Libraries**: Relies on older JGoodies “Plastic” themes and pre-Java 5 raw types (`Vector`, `HashMap`).  
- **Cursor Counting Logic**: `waitCount` increments/decrements may desynchronize under exceptions.  
- **AutoSave Thread**: Unchecked interruptions and no shutdown hook; potential resource leakage.