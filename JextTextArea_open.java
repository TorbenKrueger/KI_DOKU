/**
   * Opens a file in a new tabbed pane. In case it is already opened, we ask user if
   * he wants to reload it or open it in a new pane.
   * @param addToRecentList If false, the file name is not added to recent list
   */

  public JextTextArea open(String file, boolean addToRecentList)
  {
    if (file == null)
      return null;

    if (!(new File(file)).exists())
    {
      String[] args = { file };
      Utilities.showError(Jext.getProperty("textarea.file.notfound", args));
      return null;
    }

    String _file;
    JextTextArea textArea;
    JextTextArea[] areas = getTextAreas();

out:  for (int i = 0; i < areas.length; i++)
    {
      textArea = areas[i];
      if (textArea.isNew())
        continue;

      _file = textArea.getCurrentFile();
      if (_file != null && _file.equals(file))
      {
        int response = JOptionPane.showConfirmDialog(this,
                       Jext.getProperty("textarea.file.opened.msg", new Object[] { _file }),
                       Jext.getProperty("textarea.file.opened.title"),
                       JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (response)
        {
          case 0:
            textArea.open(_file, addToRecentList);
            textAreasPane.setSelectedComponent(textArea);
            return textArea;
          case 1:
            break out;
          default:
            return null;
        }
      }
    }

    textArea = createTextArea();
    textArea.open(file, addToRecentList);
    addTextAreaInTabbedPane(textArea);
    JextTextArea firstTextArea = (JextTextArea) textAreasPane.getComponentAt(0);
    if (textAreasPane.getTabCount() == 2 && firstTextArea.isNew() && firstTextArea.getLength() == 0)
      close(firstTextArea);

    return textArea;
  }