package com.createvissualmedia.web.service;

import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.createvissualmedia.web.service.exception.ServiceException;

public interface MediaService {
	
	public String ping() throws ServiceException;
	
	public void audio(MultipartFile file) throws ServiceException;
	
	public void media(MultipartFile file) throws ServiceException;
	
	public Path load(String filename);
	
	public Path challenges(String file1, String file2, String audio);
}
