/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 5. 1.
 */
package com.realsnake.sample.service.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.realsnake.sample.constants.CommonConstants;
import com.realsnake.sample.mapper.common.CommonMapper;
import com.realsnake.sample.model.common.AddressVo;
import com.realsnake.sample.model.common.AttachFileVo;
import com.realsnake.sample.model.common.BuildingInfoVo;
import com.realsnake.sample.model.common.CommonCodeVo;
import com.realsnake.sample.model.common.CommonDto;
import com.realsnake.sample.model.common.JibunVo;
import com.realsnake.sample.model.common.RoadNameCodeVo;
import com.realsnake.sample.util.Locations;
import com.realsnake.sample.util.Locations.LatLng;

/**
 * <pre>
 * Class Name : CommonServiceImpl.java
 * Description : Description
 * </pre>
 *
 * @author 전강욱(realsnake1975@gmail.com)
 * @since 2017. 5. 1.
 * @version 1.0
 */
@Service
public class CommonServiceImpl implements CommonService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jusodb.work.dir}")
    private String jusodbWorkDir;

    @Value("${file.path.temp}")
    private String tempFilePath;

    @Value("${file.path}")
    private String filePath;

    @Autowired
    private CommonMapper commonMapper;

    // @Override
    // @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    // public void saveAttachFile(AttachFileVo param) throws Exception {
    // this.commonMapper.insertAttachFile(param);
    // }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeAttachFile(Integer attachFileSeq, CommonDto param) throws Exception {
        AttachFileVo attachFile = this.commonMapper.selectAttachFile(attachFileSeq);
        File file = new File(attachFile.getPath(), attachFile.getName());

        if (file.delete()) {
            attachFile.setDelUserSeq(param.getLoginUser().getSeq());
            this.commonMapper.deleteAttachFile(attachFile);
        }
    }

    @Override
    public AttachFileVo findAttachFile(Integer param) throws Exception {
        return this.commonMapper.selectAttachFile(param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachFileVo> findAttachFileList(AttachFileVo param) throws Exception {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regCommonCode(CommonCodeVo param) throws Exception {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyCommonCode(CommonCodeVo param) throws Exception {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeCommonCode(CommonCodeVo param) throws Exception {

    }

    @Override
    @Transactional(readOnly = true)
    public CommonCodeVo findCommonCode(String param) throws Exception {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public int findCommonCodeListCount(CommonDto param) throws Exception {
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommonCodeVo> findCommonCodeList(CommonDto param) throws Exception {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regBuildingInfo(BuildingInfoVo param) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regJibun(JibunVo param) throws Exception {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regRoadNameCode(RoadNameCodeVo param) throws Exception {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regJuso(String filterParam) throws Exception {
        // road, jibun, build
        // String filterParam = "build";
        Collection<File> files = FileUtils.listFiles(new File(this.jusodbWorkDir), FileFilterUtils.prefixFileFilter(filterParam), null);

        for (File file : files) {
            List<String> lines = FileUtils.readLines(file, "MS949");

            for (String line : lines) {
                String[] temps = line.split("[|]", -1);

                int count = 0;
                RoadNameCodeVo roadNameCode = null;
                JibunVo jibun = null;
                BuildingInfoVo buildingInfo = null;

                if ("road".equalsIgnoreCase(filterParam)) {
                    roadNameCode = new RoadNameCodeVo();
                } else if ("jibun".equalsIgnoreCase(filterParam)) {
                    jibun = new JibunVo();
                } else if ("build".equalsIgnoreCase(filterParam)) {
                    buildingInfo = new BuildingInfoVo();
                }

                for (String temp : temps) {
                    if ("road".equalsIgnoreCase(filterParam)) {
                        /* @formatter:off */
                        if (count == 0) roadNameCode.setSigunguCode(temp);
                        if (count == 1) roadNameCode.setRoadNameNo(temp);
                        if (count == 2) roadNameCode.setRoadName(temp);
                        if (count == 3) roadNameCode.setRoadNameRome(temp);
                        if (count == 4) roadNameCode.setEopmyendongSeq(temp);
                        if (count == 5) roadNameCode.setSidoName(temp);
                        if (count == 6) roadNameCode.setSigunguName(temp);
                        if (count == 7) roadNameCode.setEopmyendongGubun(temp);
                        if (count == 8) roadNameCode.setEopmyendongCode(temp);
                        if (count == 9) roadNameCode.setEopmyendongName(temp);
                        if (count == 10) roadNameCode.setUpperRoadNameNo(temp);
                        if (count == 11) roadNameCode.setUpperRoadName(temp);
                        if (count == 12) roadNameCode.setUseYn(temp);
                        if (count == 13) roadNameCode.setUpdateReason(temp);
                        if (count == 14) roadNameCode.setUpdateHistInfo(temp);
                        if (count == 15) roadNameCode.setSidoNameRome(temp);
                        if (count == 16) roadNameCode.setSigunguNameRome(temp);
                        if (count == 17) roadNameCode.setEopmyendongNameRome(temp);
                        if (count == 18) roadNameCode.setDispDate(temp);
                        if (count == 19) roadNameCode.setEndDate(temp);
                        /* @formatter:on */
                    } else if ("jibun".equalsIgnoreCase(filterParam)) {
                        /* @formatter:off */
                        if (count == 0) jibun.setBeopjungdongCode(temp);
                        if (count == 1) jibun.setSidoName(temp);
                        if (count == 2) jibun.setSigunguName(temp);
                        if (count == 3) jibun.setBeopjungeopmyendongName(temp);
                        if (count == 4) jibun.setBeopjungriName(temp);
                        if (count == 5) jibun.setSanYn(temp);
                        if (count == 6) jibun.setBunji(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 7) jibun.setHo(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 8) jibun.setRoadNameCode(temp);
                        if (count == 9) jibun.setJihaYn(temp);
                        if (count == 10) jibun.setBuildingBonbun(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 11) jibun.setBuildingBubun(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 12) jibun.setJibunSeq(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 13) jibun.setMoveReasonCode(temp);
                        /* @formatter:on */
                    } else if ("build".equalsIgnoreCase(filterParam)) {
                        /* @formatter:off */
                        if (count == 0) buildingInfo.setBeopjungdongCode(temp);
                        if (count == 1) buildingInfo.setSidoName(temp);
                        if (count == 2) buildingInfo.setSigunguName(temp);
                        if (count == 3) buildingInfo.setBeopjungeopmyendongName(temp);
                        if (count == 4) buildingInfo.setBeopjungriName(temp);
                        if (count == 5) buildingInfo.setSanYn(temp);
                        if (count == 6) buildingInfo.setBunji(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 7) buildingInfo.setHo(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 8) buildingInfo.setRoadNameCode(temp);
                        if (count == 9) buildingInfo.setRoadName(temp);
                        if (count == 10) buildingInfo.setJihaYn(temp);
                        if (count == 11) buildingInfo.setBuildingBonbun(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 12) buildingInfo.setBuildingBubun(StringUtils.isNumeric(temp)?Integer.valueOf(temp):null);
                        if (count == 13) buildingInfo.setBuildingName(temp);
                        if (count == 14) buildingInfo.setDetailBuildingName(temp);
                        if (count == 15) buildingInfo.setBuildingMgtNo(temp);
                        if (count == 16) buildingInfo.setEopmyendongSeq(temp);
                        if (count == 17) buildingInfo.setHaengjungdongCode(temp);
                        if (count == 18) buildingInfo.setHaengjungdongName(temp);
                        if (count == 19) buildingInfo.setZipCode(temp);
                        if (count == 20) buildingInfo.setZipSeq(temp);
                        if (count == 21) buildingInfo.setMassDeliveryName(temp);
                        if (count == 22) buildingInfo.setMoveReasonCode(temp);
                        if (count == 23) buildingInfo.setDispDate(temp);
                        if (count == 24) buildingInfo.setBeforeUpdateRoadName(temp);
                        if (count == 25) buildingInfo.setBuildingNamePerSigungu(temp);
                        if (count == 26) buildingInfo.setUnionHousingYn(temp);
                        if (count == 27) buildingInfo.setNewZipCode(temp);
                        if (count == 28) buildingInfo.setDetailAddrYn(temp);
                        if (count == 29) buildingInfo.setBigo1(temp);
                        if (count == 30) buildingInfo.setBigo2(temp);
                        /* @formatter:on */
                    }

                    count++;
                }

                // System.out.println(roadNameCode.toString());
                // System.out.println(jibun.toString());
                // break;

                if ("road".equalsIgnoreCase(filterParam)) {
                    this.commonMapper.insertRoadNameCode(roadNameCode);
                } else if ("jibun".equalsIgnoreCase(filterParam)) {
                    this.commonMapper.insertJibun(jibun);
                } else if ("build".equalsIgnoreCase(filterParam)) {
                    this.commonMapper.insertBuildingInfo(buildingInfo);
                }

            }

            logger.info("<<파일명 {}>> DB 등록 완료", file.getName());
            Thread.sleep(3000);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void regAddress(AddressVo param) throws Exception {
        LatLng latLng = Locations.getLocationFromAddress(param.getAddr1());
        if (latLng != null) {
            param.setLat(String.valueOf(latLng.lat));
            param.setLng(String.valueOf(latLng.lng));
        }
        // 주소저장
        this.commonMapper.insertAddress(param);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void modifyAddress(AddressVo param) throws Exception {
        AddressVo oldAddress = this.commonMapper.selectAddress(param.getAddrSeq());

        if (oldAddress.getZipCode().equals(param.getZipCode()) && oldAddress.getAddr1().equals(param.getAddr1()) && oldAddress.getAddr2().equals(param.getAddr2())) {
            // 주소가 동일하므로 실행 중단
            return;
        }

        this.commonMapper.deleteAddress(param);
        this.regAddress(param);
    }

    /**
     * 임시 폴더의 파일을 실재 저장 폴더로 옮긴다.
     *
     * @param uploadedFiles
     * @param groupSeq
     * @param gubun
     * @return
     * @throws Exception
     */
    @Override
    public List<AttachFileVo> moveUploadedFile(String[] uploadedFiles, Integer groupSeq, String gubun) throws Exception {
        if (uploadedFiles == null || uploadedFiles.length == 0) {
            return null;
        }

        // 파일저장 규칙
        String targetFolder = this.filePath + File.separator + gubun + File.separator + CommonConstants.YYYYMM_SDF.format(new Date()) + File.separator + groupSeq;

        File destFolder = new File(targetFolder);
        if (!destFolder.exists()) {
            destFolder.mkdirs();
            destFolder.setReadable(true, false);
            destFolder.setWritable(true, false);
        }

        List<AttachFileVo> attachFileList = new ArrayList<AttachFileVo>();

        for (String tempFilePath : uploadedFiles) {
            File tempUploadedFile = new File(tempFilePath);
            String fileName = tempUploadedFile.getName();

            if (tempUploadedFile.exists()) {
                File destFile = new File(destFolder, fileName);

                FileUtils.moveFile(tempUploadedFile, destFile);
                logger.debug("임시파일 {}, 저장파일 {}", tempUploadedFile.getCanonicalPath(), destFile.getCanonicalPath());

                if (destFile.exists()) {
                    String ext = FilenameUtils.getExtension(fileName);
                    String orgName = FilenameUtils.getBaseName(fileName);
                    orgName = orgName.substring(0, orgName.lastIndexOf("-")) + "." + ext;

                    AttachFileVo attachFile = new AttachFileVo();
                    attachFile.setGubun(gubun);
                    attachFile.setPath(targetFolder);
                    attachFile.setName(fileName);
                    attachFile.setOrgName(orgName);
                    attachFile.setExt(ext);
                    attachFile.setSize((int) (destFile.length() / 1024)); // kb 단위로 저장

                    // this.saveAttachFile(attachFile);
                    attachFileList.add(attachFile);
                }
            }
        }

        return attachFileList;
    }

}
