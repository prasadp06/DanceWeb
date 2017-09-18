package com.createvissualmedia.web.service;


import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.createvissualmedia.web.service.exception.ServiceException;
import com.createvissualmedia.web.service.impl.MediaServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

public class MediaServiceTest {
	private MediaService service;

    @Before
    public void init() {
        service = new MediaServiceImpl();
    }

    @Test
    public void loadNonExistent() {
        assertThat(service.load("bar.txt")).doesNotExist();
    }

    @Test
    public void saveAndLoad() throws ServiceException {
        service.audio(new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes()));
        assertThat(service.load("foo.txt")).exists();
    }

    
    @Test(expected = ServiceException.class)
    public void savePermitted() throws ServiceException {
        service.audio(new MockMultipartFile("foo", "bar/../foo.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
    }
}
