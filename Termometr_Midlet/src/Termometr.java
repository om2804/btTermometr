import javax.microedition.lcdui.*; 
import javax.microedition.midlet.*; 
import javax.bluetooth.*;
import java.io.*;
import javax.microedition.io.*;     // для работы с потоками

public  class Termometr extends MIDlet implements CommandListener
{
	private Command exitCommand = new Command ("Exit", Command.EXIT, 2);
	private Command connectCmd = new Command("Connect", Command.ITEM, 1);
	private Display mydisplay;
	private MyCanvas mycanvas; 

	private String URL = "btspp://001111160227:1;authenticate=false;encrypt=false;master=false";
    private StreamConnection con;
    private OutputStream outs;
    private InputStream ins;
	
	boolean running = false;

	public Termometr ()
	{
		mydisplay = Display.getDisplay(this);   		
	}	
  
	public void startApp()
	{
		BtInit();
		mycanvas = new MyCanvas();
		mycanvas.addCommand(exitCommand);
		mycanvas.addCommand(connectCmd);
		mycanvas.setCommandListener(this);
		mydisplay.setCurrent(mycanvas);  	
	}
  
	public void Alarm(Exception e) 
	{
        Alert alert = new Alert("Exception", e.getClass().getName(), null, AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        mydisplay.setCurrent(alert);
    }
  
	public void pauseApp() {}
  
	public void destroyApp(boolean uncondition) 
	{
		BtDisconnect();
	}
  
	public void commandAction (Command c, Displayable s)
	{
	   if (c == exitCommand)
	   {
			destroyApp(false);
			notifyDestroyed ();
	   }
	   else
	   {
			BtConnect();
	   }
	}
	
	
	
	// получаем ссылку на локальное устройство и делаем его доступным для поиска
	public void BtInit()
	{
		try
		{		
			LocalDevice local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);
		} 
		catch (Exception e)
		{
			Alarm(e);
		}            
	}
	
	// подключение к bluetooth
	public void BtConnect()
	{
		try
		{
			if (con != null) BtDisconnect();
			con = (StreamConnection) Connector.open(URL, Connector.READ_WRITE);
			outs = con.openOutputStream();
			ins = con.openInputStream();
			running = true;
            new Thread(){public void run(){BtReceiver();}}.start(); // запуск чтения в отдельном потоке
		} 
		catch (Exception e)
		{
			Alarm(e);
		}  
	}

	// отключение от bluetooth
	public void BtDisconnect()
	{
		try
		{
			running = false;
			if(con != null) con.close();
			if(outs != null) outs.close();
			if(ins != null) ins.close();
		} 
		catch (Exception e)
		{
			Alarm(e);
		}  
	}
  
   // чтение данных из блютуз
	public void BtReceiver()
    {	
        while(running)
        {
            if(con != null && outs !=null && ins != null)
            {
                try
                {			
					if (ins.read() == 0xDE && ins.read() == 0xAD) // проверяем начало пакета
					{
						short temp = (short)(ins.read() << 8 | ins.read());
						mycanvas.setTemp(temp);
					}
                }
                catch(Exception e)
                {
                    running = false;
					Alarm(e);                    
                }
            }
        }
    }

}