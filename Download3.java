// hassan saud
// 0089-bscs-19
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.nio.charset.StandardCharsets.*;
import javax.swing.*;
public class Download3{
	private int indexOfHd=-1;
	public static int BUFFER_SIZE=8192;
	private int fileSize=0;
	public void download(URL url, File fileName)throws IOException{
		String hostName=url.getHost().toLowerCase();
		String urlFoundHD="",urlFoundSD="";
		Scanner input=new Scanner(System.in);
			binaryFileDownload(url,fileName);
		if(hostName.equalsIgnoreCase("fb.watch") || hostName.contains("Facebook".toLowerCase())){
			try{
			
			urlFoundHD=getFacebookUrl(fileName, "hd_src");
			urlFoundSD=getFacebookUrl(fileName, "sd_src");
			//System.out.println("\n\nRetrieved URL-hd: "+urlFoundHD+"\n\n");
			//System.out.println("\n\nRetrieved URL-hd: "+urlFoundSD+"\n\n");
			fileName.delete();
			
			}
			catch(Exception e)
			{System.out.println("Error in Facebook");}
			System.out.println("\n\nPlease Select Quality: (1=Standard, 2=HD): ");
			short choice=input.nextShort();
			if(choice==1)
			binaryFileDownload(new URL(urlFoundSD), fileName);
			else binaryFileDownload(new URL(urlFoundHD), fileName);
		}
		else if(hostName.equalsIgnoreCase("www.instagram.com")){
			try{
			String instaUrl=getInstagramUrl(fileName);
			fileName.delete();
			binaryFileDownload(new URL(instaUrl), fileName);
			}catch(Exception e){System.out.println("Issue in instagram.");}
			
		}
		else if(hostName.contains("pinterest".toLowerCase())){
			try{
			String pinterestUrl=getPinterestUrl(fileName);
			fileName.delete();
			binaryFileDownload(new URL(pinterestUrl), fileName);
			}catch(Exception e){System.out.println("Issue in Pinterest.");}
		}
		
		//http.disconnect();
		
	}
	public void binaryFileDownload(URL url, File fileName)throws IOException{
		
		HttpURLConnection http=(HttpURLConnection)url.openConnection();
		http.setInstanceFollowRedirects(true);
		InputStream in=http.getInputStream();
		OutputStream out=new FileOutputStream(fileName);
		
		
		byte[] buffer=new byte[BUFFER_SIZE];
		int size=0;
		do{
			size=in.read(buffer);
			if(size !=- 1){
				fileSize+=size;
				out.write(buffer,0,size);}
		}while(size != -1);
		
		out.flush();
		out.close();
		in.close();
		http.disconnect();
	}
	
	
	private String getFacebookUrl(File fileName, String quality)throws Exception{
		//static int indexOfHd=-1;
		FileReader searchFile=new FileReader(fileName);
		BufferedReader bufferRead=new BufferedReader(searchFile);
		String line="";
		int indexOfQuality  = -1;
		while((line=bufferRead.readLine()) != null){
			if(quality.equalsIgnoreCase("sd_src")) indexOfQuality=line.indexOf(quality,indexOfHd);
			else
			indexOfQuality=line.indexOf(quality);
			if(indexOfQuality != -1) break;
		}
		if(quality.equalsIgnoreCase("hd_src")) indexOfHd=indexOfQuality;
		if(indexOfQuality != -1){
			String newURL=line.substring(indexOfQuality+7 , line.indexOf(",",indexOfQuality));
			bufferRead.close();
			searchFile.close();
			return newURL.substring(1,newURL.length()-1);
		}
		
		else{
			System.out.println("File not Parsed !");
			bufferRead.close();
			searchFile.close();
			return "";
		}
		
	}
	
	private String getInstagramUrl(File fileName)throws Exception{
		FileReader searchFile=new FileReader(fileName);
		BufferedReader bufferRead=new BufferedReader(searchFile);
		String line="";
		String newUrl="";
		int indexOfUrl= -1;
		while((line=bufferRead.readLine()) != null){
			indexOfUrl=line.indexOf("secure_url");
			if(indexOfUrl != -1) break;
		}
		if(indexOfUrl != -1){
			newUrl=line.substring(indexOfUrl+21, line.length()-4);
			bufferRead.close();
			searchFile.close();
			return newUrl;
		}
		else{
			/*newUrl=getInstaImageUrl(fileName);
			System.out.println("File not Parsed !");
			bufferRead.close();
			searchFile.close();*/
			return getInstaImageUrl(fileName);
		}
	}
	private String getInstaImageUrl(File fileName)throws Exception{
		FileReader searchFile=new FileReader(fileName);
		BufferedReader bufferRead=new BufferedReader(searchFile);
		String line="";
		String newUrl="";
		int indexOfUrl= -1;
		while((line=bufferRead.readLine()) != null){
			indexOfUrl=line.indexOf("og:image");
			if(indexOfUrl != -1) break;
		}
		if(indexOfUrl != -1){
			newUrl=line.substring(indexOfUrl+19, line.length()-4);
			bufferRead.close();
			searchFile.close();
			return newUrl;
		}
		else return "";
	}
	private String getPinterestUrl(File fileName) throws Exception{
		FileReader searchFile=new FileReader(fileName);
		BufferedReader bufferRead=new BufferedReader(searchFile);
		String line="";
		String newUrl="";
		int indexOfUrl= -1, count=0;
		while(count++ <=9200){
			line += (char)searchFile.read();
		}
		
		
			indexOfUrl=line.indexOf("twitter:image:src");
			if(indexOfUrl != -1){ 
				indexOfUrl=line.indexOf("content",indexOfUrl);
				
			}
		
		if(indexOfUrl != -1){
			newUrl=line.substring(indexOfUrl+9, line.indexOf("data", indexOfUrl));
			System.out.println("PINTEREST URL: "+newUrl.substring(0,newUrl.length()-2));
			bufferRead.close();
			searchFile.close();
			return newUrl.substring(0,newUrl.length()-2);
		}
		else return "";
	}
	
	public static void main(String[] args){
		Scanner input=new Scanner(System.in);
		String url,fileName;
		/*System.out.println("\n\nSir, This is just a command line utility. There could be bugs and mal-functioning\nPlease bear with me. I shall sort all this out with time.\nAudios are still downloading correctly but the only issue is videos.\nText files are not supported for now. I am still working on the other types of files.");
		System.out.println("It will be good if you copy and paste the url instead of self-writing.\n");
		System.out.print("\nEnter URL: (like http//:www.xyz.com): ");*/
		System.out.print("ENter URL: ");
		url=input.nextLine();
		
		System.out.print("Enter File Name: (Please provide file name with proper extension): ");
		fileName=input.nextLine();
		try{
			Download3 down=new Download3();
			down.download(new URL(url), new File(fileName));
		}catch(IOException e) {e.printStackTrace();}
		//catch(MalformedURLException e) {e.printStackTrace();}
		catch(Exception e)    {e.printStackTrace();}
		
	}
}
