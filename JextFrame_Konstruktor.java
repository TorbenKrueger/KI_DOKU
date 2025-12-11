  /**
   * Create a new GUI.
   * @param args Arguments (to open a file directly)
   */

  public JextFrame(String args[])
  {
    this(args, true);
  }

  JextFrame(String args[], boolean toShow)
  {
    super("Jext - Java Text Editor");
    getContentPane().setLayout(new BorderLayout());

    Jext.setSplashProgress(10);
    Jext.setSplashText(Jext.getProperty("startup.gui"));

    defaultProjectMgmt = new DefaultProjectManagement(this);
    addProjectManagement(defaultProjectMgmt);
    registerPlugins();
    setIconImage(GUIUtilities.getJextIconImage());
    XMenuReader.read(this, Jext.class.getResourceAsStream("jext.menu.xml"), "jext.menu.xml");
    getJMenuBar().putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

    inputHandler = new DefaultInputHandler(Jext.getInputHandler());

    Jext.setSplashProgress(20);
    Jext.setSplashText(Jext.getProperty("startup.toolbar.build"));
    XBarReader.read(this, Jext.class.getResourceAsStream("jext.toolbar.xml"), "jext.toolbar.xml");

    // create the text areas pane
    splittedTextArea = createTextArea();
    textAreasPane = new JextTabbedPane(this);
    textAreasPane.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
    textAreasPane.putClientProperty(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);

    Jext.setSplashProgress(30);
    Jext.setSplashText(Jext.getProperty("startup.files"));

    workspaces = new Workspaces(this);
    workspaces.load();

    textAreaSplitter.setContinuousLayout(true);
    textAreaSplitter.setTopComponent(textAreasPane);
    textAreaSplitter.setBottomComponent(splittedTextArea);
    textAreaSplitter.setBorder(null);
    // textAreaSplitter.setResizeWeight(1.0);

    Jext.setSplashProgress(50);
    Jext.setSplashText(Jext.getProperty("startup.xinsert"));

    Jext.setSplashText(Jext.getProperty("startup.xinsert.build"));

    rightFrame = new SimpleInternalFrame(null, null, textAreaSplitter);

    vTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
    vTabbedPane.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
    vTabbedPane.putClientProperty(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);
    GUIUtilities.setScrollableTabbedPane(vTabbedPane);
    virtualFolders = new VirtualFolders(this);
    selectProjectManagement(Jext.getProperty("projectManagement.current",
     defaultProjectMgmt.getLabel()));
//    vTabbedPane.add(Jext.getProperty("vTabbedPane.project"), new ProjectPanel(this));

    if (Jext.getBooleanProperty("xtree.enabled"))
    {
      xtree = new XTree(this, "jext.insert.xml");
      vTabbedPane.add(Jext.getProperty("vTabbedPane.xinsert"), xtree);
    }
    //if (vTabbedPane.getTabCount() == 0)
    //  Jext.setProperty("leftPanel.show", "off");
    leftFrame = new SimpleInternalFrame("Tools");
    leftFrame.setContent(vTabbedPane);

    split = Factory.createStrippedSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftFrame, rightFrame, 0.00f);
    split.setContinuousLayout(true);
    _dividerSize = split.getDividerSize();

    Jext.setSplashProgress(60);

    hTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
    hTabbedPane.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
    hTabbedPane.putClientProperty(Options.NO_CONTENT_BORDER_KEY, Boolean.TRUE);
    GUIUtilities.setScrollableTabbedPane(hTabbedPane);
    if (Jext.getBooleanProperty("console.enabled"))
    {
      // creates console
      console = new Console(this);
      console.setPromptPattern(Jext.getProperty("console.prompt"));
      console.displayPrompt();

      hTabbedPane.add(Jext.getProperty("hTabbedPane.console"), console);
      hTabbedPane.setPreferredSize(console.getPreferredSize());
    }

    consolesFrame = new SimpleInternalFrame("Consoles");
    consolesFrame.setContent(hTabbedPane);
    splitter = Factory.createStrippedSplitPane(JSplitPane.VERTICAL_SPLIT, consolesFrame, split, 0.00f);
    splitter.setContinuousLayout(true);

    centerPane = new JPanel(new BorderLayout());
    centerPane.add(BorderLayout.CENTER, splitter);

    Jext.setSplashProgress(70);
    Jext.setSplashText(Jext.getProperty("startup.gui"));
    status.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent me) {
        //TODO: check if getNSTextArea() is the right thing.
        JextTextArea jta = getNSTextArea(), jtaSplitted = getTextArea();
        jta.rotateLineTerm();
        if (jtaSplitted != jta)
          jtaSplitted.rotateLineTerm();
        updateStatus(jta);//for the left status bar message
        //about isDirty: the JextFrame.setChanged/setSaved assume if the dirty flag
        //is in the wrong state(false for setChanged/true for setSaved), then the GUI hasn't been updated.
        //This is false here-rotateLineTerm changes actually the dirty flag but doesn't update the GUI.
        if (jta.isDirty())
          textAreasPane.setDirtyIcon(jta);
        else
          textAreasPane.setCleanIcon(jta);
        setStatus(jta);//for the right status bar message
      }
    });

    // we finally add the labels, used to display informations, and the toolbar
    JPanel pane = new JPanel(new BorderLayout());
    pane.add(BorderLayout.WEST, status);
    pane.add(BorderLayout.EAST, message);
    centerPane.add(BorderLayout.SOUTH, pane);
    centerPane.setBorder(new EmptyBorder(6, 0, 0, 0));
    getContentPane().add(BorderLayout.CENTER, centerPane);
    getContentPane().add(BorderLayout.NORTH, toolBar);

    // we load the user geometry
    Jext.setSplashProgress(80);
    Jext.setSplashText(Jext.getProperty("startup.props"));

    pack();
    GUIUtilities.loadGeometry(this, "jext");
    loadProperties(false);

    // here is the window listener which call exit on window closing
    addWindowListener(new WindowHandler());

    Jext.setSplashProgress(90);
    Jext.setSplashText(Jext.getProperty("startup.files"));

    if (args != null)
    {
      workspaces.selectWorkspaceOfNameOrCreate(Jext.getProperty("ws.default"));
      setBatchMode(true);

      for (int i = 0; i < args.length; i++)
      {
        if (args[i] != null)
          open(Utilities.constructPath(args[i]));
      }

      setBatchMode(false);
    }

    updateSplittedTextArea(getTextArea());
    Jext.setSplashProgress(95);
    Jext.setSplashText(Jext.getProperty("startup.plugins"));

    Jext.executeScripts(this);
    JARClassLoader.executeScripts(this);
    updatePluginsMenu();
    toolBar.addMisc(this);
    triggerTabbedPanes();

    Jext.setSplashProgress(100);

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addJextListener(new PluginHandler());
    addJextListener(new ModeHandler());
    // we notify listeners that a new Jext window is opened
    fireJextEvent(JextEvent.OPENING_WINDOW);

    getTextArea().setParentTitle();

    Jext.killSplashScreen();
    setVisible(toShow);

    getTextArea().grabFocus();
    getTextArea().requestFocus();
  }