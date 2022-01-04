[18:54, 04.01.2022] Gaye Nur Yankın: package Socket1;
import java.io.*;
import java.net.*;
import java.util.*;

public class TcpRouter
{
    private static ServerSocket serverSocket;
    private static InetAddress host;
    private static final int PORT = 1241;
    private static final int PORT2 = 1240;
    private static Socket link2 = null;
    private static int counter = 0;
    private static int dropPacketSize = 0;

    public static void main(String[] args)
    {
        System.out.println("Port Aciliyor");
        {
            try
            {

                Random roundInteger = new Random();
//			dropPacketSize = (int) Integer.valueOf(args[0])*14/100;
                dropPacketSize = (int) Integer.valueOf(10)*14/100;
                dropPacketSize += roundInteger.nextInt(2);

                int dropPacket[] = new int[dropPacketSize];
                for(int i = 0; i < dropPacket.length; i++)
                {
                    if(i==0)
                        dropPacket[i] = roundInteger.nextInt(Integer.valueOf(10)) + 1;
                }

                host = InetAddress.getLocalHost();
            }
            catch(UnknownHostException uhEx)
            {
                System.out.println("Host ID not found!");
                System.exit(1);
            }
        }
        try
        {

            serverSocket = new ServerSocket(PORT);  
            link2 = new Socket(host,PORT2);
        }
        catch(IOException ioEx)
        {
            System.out.println(
                    "Unable to attach to port for router!");
            System.exit(1);
        }
        handleClient();
    }
    private static String handleClient()
    {
        Socket link = null;                        //Step 2.  

        try
        {
            link = serverSocket.accept();           //Step 2. 

            Scanner input =
                    new Scanner(link.getInputStream());  //Step 3. 
            PrintWriter output =
                    new PrintWriter(
                            link.getOutputStream(),true);   //Step 3.    
            
            String message = input.nextLine(); 
            

            Scanner input2 =
                    new Scanner(link2.getInputStream());
//Step 2.    
            PrintWriter output2 =
                    new PrintWriter(
                            link2.getOutputStream(),true);     //Step 2.  
            while (!message.equals("*CLOSE*")){
                System.out.println("Gönderici mesaji"+message);
                output2.println(message);

                String str=input2.nextLine();
                System.out.println("Alicidan gelen mesaj "+str);
                output.println(str);
                message = input.nextLine();
                
            }
            

        }

        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }
        finally
        {
            try
            {
                System.out.println(
                        "\n* Baglanti Kapaniyor *");
                link.close();                
            }
            catch(IOException ioEx)
            {
                System.out.println(
                        "Bağlanti Kesilemedi!");
                System.exit(1);
            }
        }
        return null;
    }