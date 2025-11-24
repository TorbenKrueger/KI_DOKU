class DnDHandler implements DropTargetListener
  {
    public void dragEnter(DropTargetDragEvent evt) { }
    public void dragOver(DropTargetDragEvent evt) { }
    public void dragExit(DropTargetEvent evt) { }
    public void dragScroll(DropTargetDragEvent evt) { }
    public void dropActionChanged(DropTargetDragEvent evt) { }

    public void drop(DropTargetDropEvent evt)
    {
      DataFlavor[] flavors = evt.getCurrentDataFlavors();
      if (flavors == null)
        return;

      boolean dropCompleted = false;
      for (int i = flavors.length - 1; i >= 0; i--)
      {
        if (flavors[i].isFlavorJavaFileListType())
        {
          evt.acceptDrop(DnDConstants.ACTION_COPY);
          Transferable transferable = evt.getTransferable();
          try
          {
            final Iterator iterator = ((List) transferable.getTransferData(flavors[i])).iterator();

            // what a fix !!!!! (JDK 1.4, JVM hanging on drag and drop if file was already opened)
            SwingUtilities.invokeLater(new Runnable()
            {
              public void run()
              {
                while (iterator.hasNext())
                {
                  open(((File) iterator.next()).getPath());
                }
              }
            });

            dropCompleted = true;

          } catch (Exception e) { }
        }
      }
      evt.dropComplete(dropCompleted);
    }
  }