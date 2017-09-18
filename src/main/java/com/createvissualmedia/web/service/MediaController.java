package com.createvissualmedia.web.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.createvissualmedia.web.service.exception.ServiceException;

@Controller
public class MediaController {
	
	private final MediaService mediaService;
	
	@Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }
	
	@GetMapping("/challenges/{audio:.+}/{video1:.+}/{video2:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> challenges(
    		@PathVariable String audio, @PathVariable String video1, 
    		@PathVariable String video2) {

        Path file = mediaService.challenges(video1,video2, audio);
        
        RandomAccessFile f;
        byte[] b = null;
		try {
			f = new RandomAccessFile(file.toFile(),"r");
			b = new byte[(int)f.length()];
	        f.readFully(b);
	        f.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(file.getFileName());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFileName() + "\"").body(b);
    }
	
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {

        Path file = mediaService.load(filename);
        
        RandomAccessFile f;
        byte[] b = null;
		try {
			f = new RandomAccessFile(file.toFile(),"r");
			b = new byte[(int)f.length()];
	        f.readFully(b);
	        f.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFileName() + "\"")
        		.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
        		.body(b);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
			mediaService.media(file);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}