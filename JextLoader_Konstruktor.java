final class JextLoader implements Runnable
{
  private int port;
  private File auth;
  private String key;
  private Thread tServer;
  private ServerSocket server;

  JextLoader()
  {
    auth = new File(Jext.SETTINGS_DIRECTORY + ".auth-key");
    // creates the authorization key
    try
    {
      BufferedWriter writer = new BufferedWriter(new FileWriter(auth));
      // 16 383 = unreserved range of ports
      port = Math.abs(new Random().nextInt()) % (16383);
      String portStr = Integer.toString(port);
      key = Integer.toString(Math.abs(new Random().nextInt()) % (int) Math.pow(2, 30));
      writer.write(portStr, 0, portStr.length());
      writer.newLine();
      writer.write(key, 0, key.length());
      writer.flush();
      writer.close();

      // creates the server
      server = new ServerSocket(Jext.JEXT_SERVER_PORT + port);

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    // server is necessarily threaded
    tServer = new Thread(this);
    tServer.start();
  }