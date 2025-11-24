
  /**
   * Waits for a connexion request and handle it. The client should provide a special line
   * containing a message, the arguments for the loading and the authorization key. This
   * key is used to avoid security holes in your system.
   */

  public void run()
  {
    while (tServer != null)
    {
      try
      {
        Socket client = server.accept();
        if (client == null)
          continue;

        if (!"127.0.0.1".equals(client.getLocalAddress().getHostAddress()))
        {
          client.close();
          Jext.stopServer();
          intrusion();
          return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String givenKey = reader.readLine();
        reader.close();

        if (givenKey != null)//when backgrounding connects to check existance of server but sends
          //nothing.
          if (givenKey.startsWith("load_jext:") && givenKey.endsWith(":" + key))
          {
            Vector args = new Vector(1);
            StringTokenizer st = new StringTokenizer(givenKey.substring(10,
        					     givenKey.length() - (key.length() + 1)), "?");
            while (st.hasMoreTokens())
              args.addElement(st.nextToken());

            if (args.size() > 0)
            {
              String arguments[] = new String[args.size()];
              args.copyInto(arguments);
              args = null;

              if (Jext.getBooleanProperty("jextLoader.newWindow"))
              {
        	Jext.newWindow(arguments);
              } else if (!Jext.isRunningBg()) {
        	ArrayList instances = Jext.getInstances();
                synchronized(instances) {
                  if (instances.size() != 0) {//can be 0 when backgrounding.??? No more!
                    JextFrame parent = (JextFrame) instances.get(0);
                    for (int i = 0; i < arguments.length; i++)
                      parent.open(arguments[i]);
                    //parent.setVisible(true);//this code is not good when running background server,
                    //since Jext keeps builtTextArea set(and doesn't open a new one until it isn't unset).
                    //And so setVisible is not needed.
                  } else {
                    Jext.newWindow(arguments);
                    System.err.println("DEBUG - instances.size() == 0 in JextLoader.java!");
                  }
                }

              } else                    //when Jext.isRunningBg()
                Jext.newWindow(arguments);
            } else
              Jext.newWindow();

            client.close();
          } else if (givenKey.equals("kill:" + key)) {
            if (Jext.isRunningBg())// && Jext.getWindowsCount() <= 1 )
            {
              ArrayList instances = Jext.getInstances();
              synchronized (instances)
              {
                //normally at least one window is always open, even if hidden, but in some moments
                //this isn't true(when the user exits jext and it has not still started a new window).
                //If one window is open, we must check it is not shown.
                JextFrame lastInstance = null;
                if (instances.size() == 0 || instances.size() == 1 && 
                    ! ( lastInstance = (JextFrame)instances.get(0) ) .isVisible())
                {
                  if (instances.size() != 0)
                  {
                    Jext.closeToQuit(lastInstance, true);
                    //since the window has not been shown this could be useless, but maybe not, especially to
                    //dispatch JextEvent's.
                  }
                  //I've commented out the above code since it causes bugs with the ProjectManagement,
                  //that is NullPointerEx. I'm trying if this doesn't happen without cleanMemory.
                  Jext.finalCleanupAndExit();//check well this! TODO
                }
              }
            }
          } else {
            client.close();
            Jext.stopServer();
            intrusion();
            return;
          }
      } catch (IOException ioe) { }
    }
  }