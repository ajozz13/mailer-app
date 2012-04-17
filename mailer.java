import java.util.ArrayList;
import java.io.File;
import com.ibcinc.development.utilities.email.*;
import com.ibcinc.development.utilities.file.FileUtility;

public class mailer{

	public static final String SMTPServer = "smtp.gmail.com";
	public static final int SMTPPort = 465;
	public static final String email_usern = "ibcmobtxf@gmail.com";
	public static final String pass = "zm498lotu";
	public static String subject = "Incomming Message";
	public static String content = "test";
	//to add multiple addresses the recipient list can be separated by ;
	public static String recipient_list = "";
	public static boolean debug = false;
	public static boolean content_loaded = false;

	

	
	public static void main(String[] args){
		int exit_code = 0;		
		try{
			if(args.length < 2){
				throw new Exception("Usage: mailer -t addresselist [-f FILE_DATA] [-s subject] [-d]\n" 
					+ "Separate multiple email address list with ;");
			}
			int list_index = set(args, "-t");
			recipient_list = args[list_index];

			list_index = set(args, "-f");
			if(list_index > -1){
				content = loadFileData(args[list_index]);
				content_loaded = true;
			}

			list_index = set(args, "-s");
			if(list_index > -1)
				subject = args[list_index];
			
			list_index = set(args, "-d");
			if(list_index > -1)
				debug = true;
			
			if(debug)
				System.out.println("Send Message to " + recipient_list + " with Subject " + subject);
				
			if( !content_loaded ){
				if(debug)
					System.out.println("Load From STDIN");
				StringBuilder email_content = new StringBuilder();
				ArrayList<String> input = FileUtility.readFileFromSTDIN(new ArrayList<String>());
				for(String line : input){
					email_content.append(line);
					email_content.append("\n");
				}
				if(email_content.toString().length() > 0)
					content = email_content.toString();
			}
			if(debug)
				System.out.println("With Content: " + content);
			
			
			System.out.println("Process email to: " + recipient_list);
			processEmail(recipient_list, content);
			
			System.out.println("Done.");
			System.exit(0);
		}catch(Exception t){
			System.out.println(t.getMessage());
			t.printStackTrace(System.out);
			System.exit(1);
		}
	}
	
	public static int set(String[] args, String searchFor){
		int counter = -1;
		for(String s : args){
			counter++;
			if(s.equalsIgnoreCase(searchFor)){
				return counter + 1;
			}

		}
		return -1;
	}

	public static String loadFileData(String path) throws Exception{
		FileUtility fu = new FileUtility( new File(path) );
		return fu.getContentsOfFile();
	}	


	public static void processEmail(String toAddressList, String content) throws Exception{
		SecureTransportEmail email = new SecureTransportEmail(SecureTransportEmail.SSL_TRANSPORT);
		if(debug)
			System.out.println("Server: " + SMTPServer + "\nPort: " + SMTPPort + "\nFrom: "+email_usern);		
		email.setSMTPPort(SMTPPort);
		email.setSMTPServer(SMTPServer);
		email.setFrom(email_usern);
		email.setSubject(subject);
		email.setUserNameAndPass(email_usern, pass);

		email.setRecipients(toAddressList);
		email.setMessage(content);//buildCSVString());
		email.setDebug(debug);
		email.send();
	}
	
	
}
