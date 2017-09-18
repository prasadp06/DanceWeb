package com.createvissualmedia.web.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.createvissualmedia.web.service.MediaService;
import com.createvissualmedia.web.service.exception.ServiceException;

@Service
public class MediaServiceImpl implements MediaService {

	private final Path rootLocation;
	private static final String UPLOAD_PATH = "/mnt/share/mp4/upload/";
	private static final String AUDIO_FILE  = "audio.acc"; 
	
	public MediaServiceImpl(){
		rootLocation = Paths.get(UPLOAD_PATH);
	}
	
	@Override
	public String ping() throws ServiceException {
		return "Hello Service Is Up!!";
	}

	@Override
	public void audio(MultipartFile file) throws ServiceException {
		try {
            if (file.isEmpty()) {
                throw new ServiceException("ERR001","Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        	System.out.println("Faild on IOException");
        	e.printStackTrace();
            throw new ServiceException("ERR002","Failed to store file " + file.getOriginalFilename());
        }catch (Throwable e) {
        	System.out.println("Faild on Throwable");
        	e.printStackTrace();
        	
            throw new ServiceException("ERR003","Failed to store file " + file.getOriginalFilename());
        }
		
	}

	@Override
	public void media(MultipartFile file) throws ServiceException {
		try {
            if (file.isEmpty()) {
                throw new ServiceException("ERR001","Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            
//            String tvideo = UPLOAD_PATH+"v"+file.getOriginalFilename();
//            Path video = this.rootLocation.resolve(file.getOriginalFilename());
//            Path audio = this.rootLocation.resolve(AUDIO_FILE);
            
            
//            String command1 = "MP4Box -rem 2 "+video.toAbsolutePath()+" -out " + tvideo;
//            String command2 = "MP4Box -add "+tvideo+"  -add "+audio.toAbsolutePath()+" " + video.toAbsolutePath();
//            
//            System.out.println(command1);
//            System.out.println(command2);
//            
//            executeCommand(command1);
//            executeCommand(command2);
            
		} catch (IOException e) {
        	System.out.println("Faild on IOException");
        	e.printStackTrace();
            throw new ServiceException("ERR002","Failed to store file " + file.getOriginalFilename());
        } catch (Throwable e) {
        	System.out.println("Faild on Throwable");
        	e.printStackTrace();
            throw new ServiceException("ERR003","Failed to store file " + file.getOriginalFilename());
        }
		
	}
	
	
	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();
		long start = System.currentTimeMillis();
		System.out.println("Executing Commend " + start);
		Process p;
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(rootLocation.toFile());
			p = pb.start();
	        int rc = p.waitFor();
	        System.out.printf("Script executed with exit code %d\n", rc);
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(output.toString());
		System.out.println("Executed Commend took " + (System.currentTimeMillis() - start));
		return output.toString();

	}
    
    private String executeCommand(String[] command) {

		StringBuffer output = new StringBuffer();
		long start = System.currentTimeMillis();
		System.out.println("Executing Commend " + start);
		Process p;
		try {
			
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(rootLocation.toFile());
			p = pb.start();
	        int rc = p.waitFor();
	        System.out.printf("Script executed with exit code %d\n", rc);
			
			//p = Runtime.getRuntime().exec(command);
			//p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(output.toString());
		System.out.println("Executed Commend took " + (System.currentTimeMillis() - start));
		return output.toString();

	}
	
	@Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

	@Override
    public Path challenges(String file1, String file2, String audio) {
		
//		String command1 = "MP4Box -rem 2 "+file1+" -out " + tvideo;
//        String command2 = "MP4Box -add "+file1+"  -add "+audio+" " + video.toAbsolutePath();
//        
		String command1 = "ffmpeg -i "+file1+" -vcodec copy -an n"+file1;
		System.out.println(command1);
		//executeCommand(command1);
		
		String command2 = "ffmpeg -i "+file2+" -vcodec copy -an n"+file2;
		System.out.println(command2);
		//executeCommand(command2);
		
		String command3 = "ffmpeg -i n"+file1+" -i n"+file2+" -filter_complex '[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]' -map [vid] -c:v libx264 -crf 23 -preset veryfast merge"+file1;
		System.out.println(command3);
        //executeCommand(command3);
        
        String filename = "omerge"+file1;
        
        String command4 = "ffmpeg -i "+filename+" -i "+audio+" -c:v copy -c:a aac -strict experimental o"+filename;
        System.out.println(command4);
        //executeCommand(command4);
        //filename = "o"+filename;
        
        String command = "./process.sh "+file1+" "+file2+" " + audio;
        executeCommand(new String[]{"./process.sh",file1,file2,audio});
        /*
        File f = rootLocation.resolve(file1).toFile();
        f.delete();
        f = rootLocation.resolve(file2).toFile();
        f.delete();
        f = rootLocation.resolve("n"+file1).toFile();
        f.delete();
        f = rootLocation.resolve("n"+file2).toFile();
        f.delete();
        */
        
        return rootLocation.resolve(filename);
    }
	
	
}
