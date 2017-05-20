package com.realsnake.sample.controller.common;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realsnake.sample.constants.ApiResultCode;
import com.realsnake.sample.exception.CommonApiException;
import com.realsnake.sample.model.common.AttachFileVo;
import com.realsnake.sample.model.common.CommonDto;
import com.realsnake.sample.service.common.CommonService;
import com.realsnake.sample.util.RandomKeys;
import com.realsnake.sample.util.Responses;

/**
 * Created by doring on 15. 4. 2..
 */
@Controller
public class CommonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    @Value("${file.path.temp}")
    private String tempFilePath;

    @Value("${file.path.editor.image}")
    private String editorImagePath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommonService commonService;

    /**
     * @author shavrani 2016.05.31
     */
    @RequestMapping(value = "/common/editor/image/temp/upload", method = RequestMethod.POST)
    public @ResponseBody String editorImageUpload(@RequestParam(value = "upload", required = false) MultipartFile img) {
        deleteOldTempFiles();

        String originalFileName = img.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originalFileName);

        try {
            File dir = new File(tempFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
                dir.setReadable(true, false);
                dir.setWritable(true, false);
            }

            File dest = new File(dir, String.format("%s.%s", RandomKeys.make(16), ext));
            while (dest.exists()) {
                dest = new File(dir, String.format("%s.%s", RandomKeys.make(16), ext));
            }
            dest.createNewFile();
            dest.setReadable(true, false);
            dest.setWritable(true, false);

            img.transferTo(dest);

            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("fileName", dest.getName());
            obj.put("uploaded", 1);
            obj.put("url", "/api/common/temp/image/" + dest.getName());

            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return "error";
    }

    /**
     * @author shavrani 2016.05.31
     */
    private void deleteOldTempFiles() {
        File dir = new File(tempFilePath);
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return System.currentTimeMillis() - pathname.lastModified() > 24 * 60 * 60 * 1000;
            }
        });
        if (files != null && files.length != 0) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     * @author shavrani 2016.05.31
     */
    @RequestMapping(value = "/common/temp/image/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> editorTempImageView(@PathVariable("fileName") String fileName) {
        File toServeUp = new File(tempFilePath, fileName);

        return Responses.getFileEntity(toServeUp, fileName);
    }

    /**
     * @author shavrani 2016.05.31
     */
    @RequestMapping(value = "/common/editor/image/{middlePath}/{id}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> editorImageView(@PathVariable("middlePath") String middlePath, @PathVariable("id") String id, @PathVariable("fileName") String fileName) {
        File toServeUp = new File(String.format(editorImagePath + "/%s/%s", middlePath, id), fileName);

        return Responses.getFileEntity(toServeUp, fileName);
    }

    /**
     * CKEditor 다이알로그 창에서 이미지 업로드
     *
     * @param request
     * @param response
     * @param upload
     */
    @RequestMapping(value = "/common/editor/image/temp/dialog-upload", method = RequestMethod.POST)
    public void editorImageUploadForCk(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "upload", required = false) MultipartFile upload) {
        PrintWriter printWriter = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        this.deleteOldTempFiles();

        String originalFileName = upload.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originalFileName);

        try {
            File dir = new File(tempFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
                dir.setReadable(true, false);
                dir.setWritable(true, false);
            }

            File dest = new File(dir, String.format("%s.%s", RandomKeys.make(16), ext));
            while (dest.exists()) {
                dest = new File(dir, String.format("%s.%s", RandomKeys.make(16), ext));
            }
            dest.createNewFile();
            dest.setReadable(true, false);
            dest.setWritable(true, false);

            upload.transferTo(dest);

            String callback = request.getParameter("CKEditorFuncNum");
            printWriter = response.getWriter();
            String fileUrl = "/api/common/temp/image/" + FilenameUtils.getName(dest.getCanonicalPath());

            LOGGER.debug("<<이미지 저장 경로>> {}", dest.getCanonicalPath());
            LOGGER.debug("<<이미지 URL 경로>> {}", "/api/common/temp/image/" + FilenameUtils.getName(dest.getCanonicalPath()));

            printWriter.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + callback + ", '" + fileUrl + "', ' 이미지를 업로드하였습니다. '" + ")</script>");
            printWriter.flush();
        } catch (Exception e) {
            LOGGER.error("<<CK에디터 이미지 파일 업로드 중 오류>> {}", e.getMessage());
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * 파일을 임시 저장 폴더에 업로드한다.<br />
     *
     * @param attachFile
     * @return
     * @throws JsonProcessingException
     */
    // @RequestMapping(value = "/common/file/temp/upload", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8") // org.springframework.web.HttpMediaTypeNotAcceptableException: Could not
    // find acceptable representation 오류 발생
    @PostMapping(value = "/common/file/temp/upload")
    @ResponseBody
    public AttachFileVo uploadBoardTempFile(@RequestParam(value = "attachFile", required = false) MultipartFile attachFile) throws JsonProcessingException {
        this.deleteOldTempFiles();

        String originalFileName = attachFile.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originalFileName);

        AttachFileVo attachFileVo = null;

        try {
            File dir = new File(this.tempFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
                dir.setReadable(true, false);
                dir.setWritable(true, false);
            }

            String onlyFileName = FilenameUtils.getName(originalFileName);
            String fileName = FilenameUtils.removeExtension(onlyFileName) + "-" + new SimpleDateFormat("HHmmssSSS").format(new Date()) + "." + ext;
            File dest = new File(dir, fileName);

            while (dest.exists()) {
                dest = new File(dir, fileName);
            }

            dest.createNewFile();
            dest.setReadable(true, false);
            dest.setWritable(true, false);

            attachFile.transferTo(dest);

            attachFileVo = new AttachFileVo();
            attachFileVo.setPath(this.tempFilePath);
            attachFileVo.setName(dest.getName()); // 저장된 파일명
            attachFileVo.setOrgName(originalFileName);
            attachFileVo.setExt(ext);
            int fileSize = (int) (attachFile.getSize() / 1024);
            attachFileVo.setSize(fileSize); // kb 단위로 저장
        } catch (Exception e) {
            LOGGER.error("<<파일업로드 오류>> {}", e.getMessage());
        }

        return attachFileVo;
    }

    @RequestMapping(value = "/common/file/{seq}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("seq") Integer seq) throws Exception {
        AttachFileVo attachFile = this.commonService.findAttachFile(seq);

        if (attachFile == null) {
            return null;
        }

        // 4 윈도우
        String path = attachFile.getPath().replaceAll("\\\\", "/");

        File toServeUp = new File(path, attachFile.getName());
        return Responses.getFileEntity(toServeUp, attachFile.getOrgName());
    }

    /**
     * 파일 및 파일정보 삭제
     *
     * @param seq
     * @return
     */
    @RequestMapping(value = "/common/file/remove/{seq}")
    @ResponseBody
    public String removeFile(@PathVariable("seq") Integer seq, CommonDto param) {
        try {
            this.commonService.removeAttachFile(seq, param);
            return "OK";
        } catch (Exception e) {
            return "NOK";
        }
    }

    /**
     * 이미지 파일을 보여준다.
     *
     * @param path
     * @param name
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private void viewImage(String path, String name, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        OutputStream os = null;
        FileInputStream fis = null;

        try {
            if (StringUtils.isBlank(path) || StringUtils.isBlank(name)) {
                return;
            }

            String ext = StringUtils.upperCase(FilenameUtils.getExtension(name));
            String mime = null;

            if ("JPG".equals(ext) || "JPEG".equals(ext)) {
                mime = "jpeg";
            } else if ("PNG".equals(ext)) {
                mime = "png";
            } else if ("GIF".equals(ext)) {
                mime = "gif";
            }

            if (mime == null) {
                return;
            }

            response.setHeader("Cache-Control", "no-cache"); // http 1.1
            response.setHeader("Pragma", "no-cache"); // http 1.0
            response.setDateHeader("Expires", -1); // proxy server에 cache방지

            response.setContentType("image/" + mime);
            // response.setHeader("Content-Disposition", "inline; filename=\""+ fileName +"\""); // 파일명 지정

            fis = new FileInputStream(new File(path, name));
            os = response.getOutputStream();

            int bytesRead = 0;
            byte[] buffer = new byte[4096];

            while ((bytesRead = fis.read(buffer, 0, 4096)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (UnsupportedEncodingException uee) {
            throw uee;
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    @Deprecated
    private static final String JUSO_API_URL = "http://www.juso.go.kr/addrlink/addrLinkApi.do?currentPage=%s&countPerPage=10&keyword=%s&confmKey=%s&resultType=json";

    @Deprecated
    @RequestMapping(value = "/common/search/addr", method = RequestMethod.POST)
    @ResponseBody
    public String searchAddr(HttpServletRequest request) throws CommonApiException {
        String currentPage = request.getParameter("currentPage");
        String confmKey = request.getParameter("confirmKey");
        String keyword = request.getParameter("keyword");

        try {
            String apiUrl = String.format(JUSO_API_URL, currentPage, URLEncoder.encode(keyword, "UTF-8"), confmKey);
            LOGGER.debug("<<주소 API URL>> {}", apiUrl);
            // 응답 샘플
            // https://www.juso.go.kr/CommonPageLink.do?link=/addrlink/devAddrLinkRequestSample
            // 우편번호 서비스
            // https://spi.maps.daum.net/postcode/guidessl#info
            return this.restTemplate.getForObject(new URI(apiUrl), String.class);
        } catch (UnsupportedEncodingException | RestClientException | URISyntaxException e) {
            throw new CommonApiException(ApiResultCode.COMMON_FAIL, e);
        }
    }

}
